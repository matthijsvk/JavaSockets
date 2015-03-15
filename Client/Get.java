package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Get extends RetrieveDataCommand {

	protected String extension;
	protected int length;
	protected Date lastModified;
	protected File file;	// the file where the data from this GET are saved
	
	public Get(String shortHost,String hostExtension,String HTTPVersion, String command, Socket clientSocket) throws UnknownHostException, IOException {
		super(shortHost,hostExtension,HTTPVersion, command, clientSocket);
		this.file = this.getFile();
		
	}

	/**
	 * This method executes the needed commands to GET a file or webpage.
	 */
	public void execute() throws IOException{
		
		super.execute();
		this.parseHeader();
		this.pullEntity();
		if (this.HTTPVersion.equals("1.0")){
			this.terminate();	// terminate the connection created by this Command
			//must be here instead of in superclass because maybe you want to do other stuff after the super.execute, but before the this.terminate
		}
	}
	
	/**
	 * This method creates the message to be sent as the GET command. It adds an If-Modified-Since line to the header if the GET object already exxists on the local system.
	 */
	@Override
	public void createDataToBeSent(String command){
		
		//System.out.println("CreateDATA in GET");

		if (HTTPVersion.equals("1.0")){
			toBeSent = command + " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n";
		}
		else if (HTTPVersion.equals("1.1")){
			toBeSent = command + " " + hostExtension + " HTTP/1.1" + "\r\n" + "host:" + shortHost + "\r\n\r\n";
		}
		else{
			//throw error
		}
		
		//if (this.file != null)
		//	System.out.println("THE FILES HESRE: " +this.file+ this.file.exists());
		
		// if the file already exists on the file system, check the last time it was modified and send that in the GET header
		if (this.file != null && this.file.exists()){
			
			System.out.println("******* ALREADY HAVE THIS FILE **********");
			
		    String lastModified = formatDate(new Date(file.lastModified()));
		    //System.out.println(lastModified);
		    toBeSent = toBeSent + "\r\n" +"If-Modified-Since: " + lastModified;
		}
		
		System.out.println("TO BE SENT: "+toBeSent);
	}

	

	/**
	 * This method reads data from the server and interprets it. If it is a website (html), it is parsed and all embedded images are GET'ed too.
	 * 															If if is another file, it is downloaded and saved to the appropriate location on the local system
	 * @throws IOException
	 */
	private void pullEntity() throws IOException {

		// creating DIR structures
		String path; 
		path = getPath(this.shortHost, this.hostExtension);
		createDirStructure(path);

		// create file to write to
		FileOutputStream binWriter = new FileOutputStream(path);

		// Read data from the server
		System.out.println("START getting "+this.extension+ " type file");	//TODO

		int dataInFromServer = 0; 			//bytes you receive
		if (extension.equals("html") && getExtensionFromPath(path).equals(".html")){	// if html, you have to pare it and GET all embedded images	
			//sometimes, the extension in the header and in the filename are not the same. if in doubt, assume it is a normal file

			String htmlFromServer = ""; 	//get the html to be parsed
			int nbBytesRead = 0;			// needed for HTML 1.1 b/c persisten connection: http://stackoverflow.com/questions/183409/http-1-1-persistent-connections-using-sockets-in-java
			while ( (nbBytesRead < this.length) && (dataInFromServer != -1) ){
				dataInFromServer = inFromServer.read();
				htmlFromServer = htmlFromServer + (char)dataInFromServer;
				binWriter.write(dataInFromServer);
				nbBytesRead += 1;
			}

			//System.out.println(htmlFromServer);

			Document parsed = Jsoup.parse(htmlFromServer);

			// get all images from the parsed document
			Elements images = parsed.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
			Elements links = parsed.select("a[href]");//new Elements();

			// get all the parsed objects: images and links
			getLinksAndImages(images, links);
		}
		else { //normal file (leaf)
			int nbBytesRead = 0;			// needed for HTML 1.1 b/c persistent connection: http://stackoverflow.com/questions/183409/http-1-1-persistent-connections-using-sockets-in-java
			while ((nbBytesRead < this.length) && (dataInFromServer != -1) ){
				dataInFromServer = inFromServer.read();
				binWriter.write(dataInFromServer);
				nbBytesRead += 1;
			}
		}

		System.out.println("GOT the file " + path);
		System.out.println("-------------------------------------------------");
		//close the stream to file
		binWriter.close();
	}

	/**
	 * This method GETs all images and links that are passed as its arguments. These arguments are generally the results of the parsing of a html file.
	 * @param images
	 * @param links
	 */
	private void getLinksAndImages(Elements images, Elements links) {
		
		if (images.size() > 0){
			for (Element el : images) {						// iterate, we've gotta GET them all!!
				String imageURL = el.attr("src");			// some images are relative (/images/image.jpg), some are absolute (http://www.test.com/images/image.jpg)
				imageURL = imageURL.replace("http://","");	// remove the absolute adressing stuff
				String imageExtension = imageURL.replace(shortHost, "");
				if (imageExtension.indexOf("/") != 0)		//add leading slash if not exists
					imageExtension = "/" + imageExtension;

				// try catch to not crash on 404 not found
				try{
					System.out.println("getting image: "+imageExtension); //TODO
					Command query = new Get(this.shortHost,imageExtension,this.HTTPVersion, "GET", clientSocket);
					query.execute();
				}
				catch (Exception e){;}
			}
		}
		
		if (links.size() > 0){
			for (Element link : links) {						// iterate, we've gotta GET them all!!		
				String URL = link.attr("href");
				URL = URL.replace("http://","");	// remove the absolute adressing stuff

				String[] results = parseShort_ExtensionURL(URL);
				String shortURL = results[0]; String URLExtension = results[1];


				// try catch to not crash on 404 not found
				try{
					System.out.println("getting URL: "+URLExtension); //TODO
					Command query = new Get(shortURL,URLExtension,this.HTTPVersion, "GET", clientSocket);
					query.execute();
				}
				catch (Exception e){;}
			}
		}
	}

	/**
	 * This method parses the header you received from the server and stores the file extension and the length of the file GOT
	 */
	private void parseHeader() {

		//Parse for file extension
		int indexOfType = header.indexOf("Content-Type:");
		boolean extensionNotFound = true;
		while (extensionNotFound ){
			if( (header.substring(indexOfType,indexOfType+1)).equals("/")){
				extensionNotFound = false;
			}
			indexOfType += 1;
		}
		boolean endOfExtensionNotFound = true;
		int indexOfEndOfType = indexOfType;
		while (endOfExtensionNotFound ){
			indexOfEndOfType += 1;
			if( (header.charAt(indexOfEndOfType)==13) || (header.substring(indexOfEndOfType,indexOfEndOfType+1)).equals(" ") 
					|| header.substring(indexOfEndOfType,indexOfEndOfType+1).equals(";")) {
				endOfExtensionNotFound = false;
			}
		}
		extension = header.substring(indexOfType, indexOfEndOfType);


		//parse for content length
		int indexOfLength = header.indexOf("Content-Length:");
		if (indexOfLength >0){
			boolean lengthNotFound = true;
			while (lengthNotFound ){
				if( (header.substring(indexOfLength,indexOfLength+1)).equals(" ")){
					lengthNotFound = false;
				}
				indexOfLength += 1;
			}
			boolean endOfLengthNotFound = true;
			int indexOfEndOfLength = indexOfLength;
			while (endOfLengthNotFound ){
				indexOfEndOfLength += 1;
				if( (header.charAt(indexOfEndOfLength)==13) || (header.substring(indexOfEndOfLength,indexOfEndOfLength+1)).equals(" ")) {
					endOfLengthNotFound = false;
				}
			}
			length = Integer.parseInt(header.substring(indexOfLength, indexOfEndOfLength));
		}
		else{
			length = -1;
		}
	}
	
	/**
	 * This method extracts the host URL and the extension from a href="url" url, so it can be GET'ed.
	 * @param URL
	 * @return
	 */
	public String[] parseShort_ExtensionURL(String URL){
		//for files on a different web server
		String shortURL; String URLExtension;
		if (URL.contains("www.")){	
			if (URL.contains("/")){
				shortURL = URL.substring(0, URL.indexOf("/"));
				URLExtension = URL.substring(URL.indexOf("/"), URL.length());
			}
			else{
				shortURL = URL;
				URLExtension = "/";
			}
		}
		else{
			shortURL = shortHost;
			URLExtension = "/"+URL;
		}

		//add leading slash if not exists
		if (URLExtension.indexOf("/") != 0)		
			URLExtension = "/" + URLExtension;
		
		return new String[] {shortURL, URLExtension};
	}

	/**
	 * This method returns the path to a file. If the file is a website (www.test.com), is is stored in test/test.html
	 * 											 If the file is a normal file (www.test.com/dir/file), it is stored in test/dir/file
	 * @param shortHost
	 * @param hostExtension
	 * @return
	 */
	public String getPath(String shortHost, String hostExtension){
		String path;
		String hostDirName = this.shortHost.replace("www.","");int puntIndex=hostDirName.indexOf(".");hostDirName = hostDirName.substring(0, puntIndex);

		if (hostExtension.equals("/")){							//if you're requesting 'www.test.com' or 'www.test.com/',
			path = hostDirName+ "/" + hostDirName + ".html";	// it is stored under 'test/test.html'
		}
		else{
			if (hostExtension.indexOf("/") == 0)	//for normal files
				path = hostDirName + hostExtension;
			else
				path= hostDirName + "/" + "somethingWentWrong.error";
		}
		
		System.out.println("GETTING Path: " + path + " ... was "+shortHost+ " "+hostExtension);
		
		return path;
		
		
	}


	/**
	 * This method creates the directory structure so that files can be saved to the location specified in their path
	 * @param path
	 * @param name
	 */
	public void createDirStructure(String path){

		// get the DIRECTORY TREE and create it locally : imagePath - imageName
		String name = getFileName(path);
		String dirNames = path.replace(name,"");	

		// remove trailing slash
		dirNames = dirNames.substring(0, dirNames.length()-1); 	

		// create directory structure
		File dir = new File("./" + dirNames);
		boolean successful = dir.mkdirs();
//		if (successful)
//			System.out.println("directories were created successfully" + dirNames);
//		else
//			System.out.println("failed trying to create the directories");
	}


	/**
	 * This method gets the name of a file based on its path. Basically, this returns all the chars before the last slash
	 * @param path
	 * @return
	 */
	public String getFileName(String path){
		String name;
		// get IMAGE NAME itself by removing directory names
		if (path.contains("/")){	// file is stored in a dir
			name = path.substring(path.indexOf("/"), path.length()); //first directory doesn't have a leading slash
			while (name.contains("/")){
				name= name.substring(name.indexOf("/")+1, name.length());
			}
		}
		else{	// not stored in dir, but immediately under host
			name = path; 
		}
		return name;
	}

	/**
	 * This method returns the file extension of a file based on its path (filename works too). Basically, it returns all the chars before the last "."
	 * @param path
	 * @return
	 */
	public static String getExtensionFromPath(String path){
		// get file EXTENSION (loop in reverse over chars until '.' found
		int placeInUrl = path.length() -1;
		String extension = ".html";
		while (path.charAt(placeInUrl) != ("/").charAt(0) && extension == ".html"){
			if (path.charAt(placeInUrl) == (".").charAt(0)){
				extension = path.substring(placeInUrl);
			}
			placeInUrl = placeInUrl -1;
		}
		return extension;
	}
	
	/**
	 * This method returns the given date in the HTTP standard format.
	 * @param date
	 * @return
	 */
	private String formatDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
	}
	
	/**
	 * This method returns the path this GET object has on the local system.
	 * @return
	 */
	private File getFile() {
		//String hostDirName = shortHost.replace("www.","");int puntIndex=hostDirName.indexOf(".");hostDirName = hostDirName.substring(0, puntIndex);
	    String relativePath = getPath(this.shortHost, this.hostExtension);
	    String workingDirPath = System.getProperty("user.dir") + System.getProperty("file.separator"); //hostDirName is already appended in getPath()

	    File file = new File(workingDirPath + System.getProperty("file.separator") + relativePath);
		return file;
	}
}
