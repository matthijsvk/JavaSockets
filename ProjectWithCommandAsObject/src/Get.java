package ProjectWithCommandAsObject.src;

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

	public void execute() throws IOException{

		super.execute();
		this.parseHeader();
		this.pullEntity();
		this.terminate();	//must be here instead of in superclass because maybe you want to do other stuff after the super.execute, but before the this.terminate
	}

	private void pullEntity() throws IOException {

		System.out.println("creating DIR structures...");	//TODO
		
		String URL;
		String[] results; String path; String name;
		if (!this.hostExtension.equals("/")){		//file, not homepage
			URL = this.shortHost + this.hostExtension;
			results = getNamePath(URL, this.shortHost);
			path = results[0]; name = results[1];
			createDirStructure(path,name);
		}
		else{		//homepage, no directory needed
			path = this.shortHost; name = this.shortHost.replace("www.","");int puntIndex=name.indexOf(".");
			name = name.substring(0, puntIndex);path=name+".html";
		}

		// make file to write to
		FileOutputStream binWriter = new FileOutputStream(path);

		// Read data from the server and write it to the screen.

		System.out.println("starting getting data...");	//TODO
		System.out.println("file extension:" + this.extension);
		
		int dataInFromServer = 0; 			//bytes you receive
		if (extension.equals("html")){
			
			System.out.println(" HTML file ######");
			
			String htmlFromServer = ""; 	//get the html to be parsed
			while (dataInFromServer != -1){
				dataInFromServer = inFromServer.read();
				htmlFromServer = htmlFromServer + (char)dataInFromServer;
				binWriter.write(dataInFromServer);
			}

			System.out.println("PARSING..."); //TODO remove debug
			System.out.println(htmlFromServer);
			Document parsed = Jsoup.parse(htmlFromServer);

			Elements images = parsed.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
			// Iterate
			if (images.size() > 0){
				for (Element el : images) {
					String imageURL = el.attr("src");
					if (!imageURL.contains(shortHost)){
				    	imageURL = shortHost + "/" + imageURL;
				    }
					results = getNamePath(imageURL, this.shortHost);
					String imagePath = results[0]; //String imageName = results[1];
					
					System.out.println("getting image: "+imageURL); //TODO

					//createDirStructure(imagePath, imageName);  //already done at beginning of pullEntity
					Command query = new Get(this.shortHost,"/"+imagePath,this.HTTPVersion, "GET", clientSocket);
					query.execute();
				}
			}
		}
		else {
			System.out.println("#####  not HTML file ######");
			while (dataInFromServer != -1){
				dataInFromServer = inFromServer.read();
				binWriter.write(dataInFromServer);
			}
		}
		
		System.out.println("GOT the file " + path);
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

	public String[] getNamePath(String URL, String shortHost){
		if (!URL.contains(shortHost)){
			URL = shortHost + "/" + URL;
		}

		// get PATH this image, so without http etc
		String path; String name;
		path = URL.replace("http://","");				//filter out "http://" chars if they exist
		if (path.equals(shortHost)){			//if you're requesting 'www.test.com'
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
