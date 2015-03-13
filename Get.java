

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Get extends RetrieveDataCommand {

	protected String extension;
	protected int length;



	public Get(String shortHost,String hostExtension,String HTTPVersion, String command, DataOutputStream outToServer, BufferedInputStream inFromServer) {
		super(shortHost,hostExtension,HTTPVersion, command, outToServer, inFromServer);
	}


	public void execute() throws IOException{

		super.execute();
		this.parseHeader();
		this.pullEntity();
	}


	private void pullEntity() throws IOException {
		
		String URL;
		String[] results; String path; String name;
		if (!this.hostExtension.equals("/")){		//file, not homepage
			URL = this.shortHost + this.hostExtension;
			results = getNamePath(URL, this.shortHost);
			path = results[0]; name = results[1];
			createDirStructure(path,name);
		}
		else{		//homepage, no directory needed
			path = this.shortHost; name = this.shortHost.replace("www.","");int puntIndex=shortHost.indexOf(".");
			name = name.substring(0, puntIndex);path=name+".html";
		}
		
		// make file to write to
		FileOutputStream binWriter = new FileOutputStream(path);

		// Read data from the server and write it to the screen.

		int dataInFromServer = 0; 			//bytes you receive
		if (extension == "html"){
			String htmlFromServer = ""; 	//get the html to be parsed
			while (dataInFromServer != -1){
				dataInFromServer = inFromServer.read();
				if (extension == "html")
					htmlFromServer = htmlFromServer + (char)dataInFromServer;
				binWriter.write(dataInFromServer);
			}

			System.out.println("PARSING....."); //TODO remove debug
			System.out.println(htmlFromServer);
			Document parsed = Jsoup.parse(htmlFromServer);

			Elements images = parsed.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
			// Iterate
			if (images.size() > 0){
				for (Element el : images) {
					String imageURL = el.attr("src");   
					results = getNamePath(imageURL, this.shortHost);
					String imagePath = results[0]; String imageName = results[1];
					
					//createDirStructure(imagePath, imageName);  //already done at beginning of pullEntity
					Command query = new Get(this.shortHost,imagePath,this.HTTPVersion, "GET", outToServer, inFromServer);
					query.execute();
				}
			}
		}
		else {
			while (dataInFromServer != -1){
				dataInFromServer = inFromServer.read();
				binWriter.write(dataInFromServer);
			}
		}
		//close the stream to text file
		binWriter.close();
	}

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
			if( (header.charAt(indexOfEndOfType)==13) || (header.substring(indexOfEndOfType,indexOfEndOfType+1)).equals(" ")) {
				endOfExtensionNotFound = false;
			}
		}
		extension = header.substring(indexOfType, indexOfEndOfType);


		//parse for content length
		int indexOfLength = header.indexOf("Content-Length:");
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
	
	public String[] getNamePath(String URL, String shortHost){
		if (!URL.contains(shortHost)){
			URL = shortHost + "/" + URL;
		}
		// ##### create directory structure and file #####

		// get PATH this image, so without http etc
		String path; String name;
		path = URL.replace("http://","");				//filter out "http://" chars if they exist
		if (path == shortHost){			//if you're requesting 'www.test.com'
			// path = shortHost
			name = shortHost;
		}
		else{
			path = path.replace(shortHost, "");				//get rid of hostname
			if (path.indexOf("/") == 0)						//get rid of leading slash if exists
				path = path.substring(1, path.length());

			System.out.println("path: " + path); 			//TODO remove debug

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
		}

		String[] results = new String[] {path, name};
		return results;
		
	}
	
	/**
	 * this function creates the directories to which the image will be saved
	 * @param path
	 * @param name
	 */
	public static void createDirStructure(String path, String name){
		// sometimes image url's don't include the "http://shortHost/" part
		

		// get the DIRECTORY TREE and create it locally : imagePath - imageName
		String dirNames = path.replace(name,"");		
		dirNames = dirNames.substring(0, dirNames.length()-1); 	// remove trailing slash

		System.out.println("DIRNAMES: "+ dirNames);

		File dir = new File("./" + dirNames);
		// create directory structureFile
		boolean successful = dir.mkdirs();
		if (successful)
			System.out.println("directories were created successfully");
		else
			System.out.println("failed trying to create the directories");

	}
	 public static int nthOccurrence(String str, String c, int n) {
		 n-=1;
		    int pos = str.indexOf(c, 0);
		    while (n-- > 0 && pos != -1)
		        pos = str.indexOf(c, pos+1);
		    return pos;
		}
}
