package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Get extends RetrieveDataCommand {

	protected String extension;
	protected int length;


	public Get(String shortHost,String hostExtension,String HTTPVersion, String command, Socket clientSocket) throws UnknownHostException, IOException {
		super(shortHost,hostExtension,HTTPVersion, command, clientSocket);
	}

	/**
	 * This function executes the needed commands to GET a file or webpage.
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
	 * This function reads data from the server and interprets it. If it is a website (html), it is parsed and all embedded images are GET'ed too.
	 * 																If if is another file, it is downloaded and saved to the appropriate location on the local system
	 * @throws IOException
	 */
	private void pullEntity() throws IOException {

		System.out.println("creating DIR structures...");	//TODO
		
		String path; 
		path = getPath(this.shortHost, this.hostExtension);
		createDirStructure(path);
		
		// create file to write to
		FileOutputStream binWriter = new FileOutputStream(path);

		// Read data from the server and write it to the screen.
		System.out.println("starting getting "+this.extension+ " type file");	//TODO
		
		int dataInFromServer = 0; 			//bytes you receive
		if (extension.equals("html") && getExtensionFromPath(path).equals(".html")){	//sometimes, the extension in the header and in the filename are not the same. if in doubt, assume it is a normal file
			
			System.out.println("##### HTML file ######");
			
			String htmlFromServer = ""; 	//get the html to be parsed
			int nbBytesRead = 0;			// needed for HTML 1.1 b/c persisten connection: http://stackoverflow.com/questions/183409/http-1-1-persistent-connections-using-sockets-in-java
			while ( (nbBytesRead < this.length) && (dataInFromServer != -1) ){
				dataInFromServer = inFromServer.read();
				htmlFromServer = htmlFromServer + (char)dataInFromServer;
				binWriter.write(dataInFromServer);
				nbBytesRead += 1;
			}

			//System.out.println("PARSING..."); //TODO remove debug
			//System.out.println(htmlFromServer);
			Document parsed = Jsoup.parse(htmlFromServer);

			Elements images = parsed.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
			// Iterate
			if (images.size() > 0){
				for (Element el : images) {
					String imageURL = el.attr("src");			// some images are relative (/images/image.jpg), some are absolute (http://www.test.com/images/image.jpg)
					imageURL = imageURL.replace("http://","");	// remove the absolute adressing stuff
					String imageExtension = imageURL.replace(shortHost, "");
					if (imageExtension.indexOf("/") != 0)				//add leading slash if not exists
						imageExtension = "/" + imageExtension;
					
					System.out.println("getting image: "+imageExtension); //TODO

					//createDirStructure(imagePath, imageName);  //already done at beginning of pullEntity
					//try{
						Command query = new Get(this.shortHost,imageExtension,this.HTTPVersion, "GET", clientSocket);
						query.execute();
					//}
					//catch (Exception e){System.out.println(e);}
				}
			}
		}
		else {
			System.out.println("#####  not HTML file ######");
			int nbBytesRead = 0;			// needed for HTML 1.1 b/c persisten connection: http://stackoverflow.com/questions/183409/http-1-1-persistent-connections-using-sockets-in-java
			while ((nbBytesRead < this.length) && (dataInFromServer != -1) ){
				dataInFromServer = inFromServer.read();
				binWriter.write(dataInFromServer);
				nbBytesRead += 1;
			}
		}
		
		System.out.println("GOT the file " + path);
		System.out.println("-------------------------------------------------");System.out.println();
		//close the stream to text file
		
		binWriter.close();
	}

	/**
	 * This function parses the header you received from the server and stores the file extension and the length of the file GOT
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
	 * this function returns the path to a file. If the file is a website (www.test.com), is is stored in test/test.html
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
		return path;
	}


	/**
	 * this function creates the directory structure so that files can be saved to the location specified in their path
	 * @param path
	 * @param name
	 */
	public void createDirStructure(String path){

		// get the DIRECTORY TREE and create it locally : imagePath - imageName
		String name = getFileName(path);
		String dirNames = path.replace(name,"");	
		
		// remove trailing slash
		dirNames = dirNames.substring(0, dirNames.length()-1); 	

		//System.out.println("DIRNAMES: "+ dirNames);

		// create directory structure
		File dir = new File("./" + dirNames);
		boolean successful = dir.mkdirs();
				if (successful)
					System.out.println("directories were created successfully");
				else
					System.out.println("failed trying to create the directories");
	}
	

	/**
	 * this function gets the name of a file based on its path. Basically, this returns all the chars before the last slash
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
	 * this function returns the file extension of a file based on its path (filename works too). Basically, it returns all the chars before the last "."
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
}
