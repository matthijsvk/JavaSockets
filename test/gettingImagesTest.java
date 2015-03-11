package test;
import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class gettingImagesTest {

	public static void main(String[] args) throws IOException {
		String host = "www.javatpoint.com";
		String shortHost;
		if (host.contains("/")) {
			shortHost = host.substring(0,host.indexOf("/"));
		}
		else{
			shortHost = host;
		}
		
		 Document doc = Jsoup.connect("http://"+host).get(); 
		 //System.out.println(doc2);
		 
		 Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
		 // Iterate
		 for (Element el : images) {
		     String imageURL = el.attr("src");    
		     readImage(imageURL, "www.javatpoint.com", 80);
		 }
	}

		 public static void readImage(String imageURL, String shortHost, int port) throws IOException
			{	    
			    Socket clientSocket = new Socket(shortHost, port);

			     // Create outputstream (convenient data writer) to this host. 
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

				// Create an inputstream (convenient data reader) to this host
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					    			 	
				
			    // sometimes the parsing fails a bit and does not include the "http://shortHost/" part
			    if (!imageURL.contains("http://") && !imageURL.contains(shortHost)){
			    	imageURL = "http://" + shortHost + "/" + imageURL;
			    }
			    
			    System.out.println("requestedImage: "+ imageURL);		//TODO remove debug 
			    System.out.println("askedHost: "+ shortHost);			//TODO remove debug 
			   
			 	outToServer.writeBytes("GET "+imageURL+" HTTP/1.1\r\n");// write commands to server
				outToServer.writeBytes("host: "+shortHost+":80\r\n\r\n");
				outToServer.flush();	    

				
				// ##### create directory structure and file #####
				
				// get PATH this image, so without http etc
				String imagePath;	
				imagePath = imageURL.replace("http://","");				//filter out "http://" chars if they exist
				imagePath = imagePath.replace(shortHost, "");			// get rid of hostname
				if (imagePath.indexOf("/") == 0)						//get rid of leading slash if exists
					imagePath = imagePath.substring(1, imagePath.length());
								
			    System.out.println("ImagePath: " + imagePath); 			//TODO remove debug
			   
				// get IMAGE NAME itself by removing directory names
			    String imageName;
				if (imagePath.contains("/")){	// file is stored in a dir
					imageName = imagePath.substring(imagePath.indexOf("/"), imagePath.length()); //first directory doesn't have a leading slash
					while (imageName.contains("/")){
						imageName= imageName.substring(imageName.indexOf("/")+1, imageName.length());
					}
				}
				else{	// not stored in dir, but immediately under host
					imageName = imagePath; 
				}
				
				System.out.println(imageName);
				
				// get file EXTENSION (loop in reverse over chars until '.' found
				int placeInUrl = imagePath.length() -1;
				String extension = ".html";
				while (imagePath.charAt(placeInUrl) != ("/").charAt(0) && extension == ".html"){
					if (imagePath.charAt(placeInUrl) == (".").charAt(0)){
						extension = imagePath.substring(placeInUrl);

					}
					placeInUrl = placeInUrl -1;
				}
				
				System.out.println("EXTENSION: "+ extension);
				
				
				// get the DIRECTORY TREE and create it locally : imagePath - imageName
				String dirNames = imagePath.replace(imageName,"");		
				dirNames = dirNames.substring(0, dirNames.length()-1); 	// remove trailing slash
				
				System.out.println("DIRNAMES: "+ dirNames);
				
				File dir = new File("./" + dirNames);
			    // create directory structure
			    boolean successful = dir.mkdirs();
			    if (successful)
			      System.out.println("directories were created successfully");
			    else
			      System.out.println("failed trying to create the directories");
				
			    
			    
			    // create THE FILE to which received data will be written
			    // OLD matthijs' Stackoverflow code  TODO remove
	//			    System.out.println(imagePath);
	//			    File file = new File(imagePath); 
	//			    
	//			    successful = file.createNewFile();
	//			    if (successful)
	//			      System.out.println("file was created successfully");
	//			    else
	//			      System.out.println("failed trying to create the file");
			 
				PrintWriter textWriter = new PrintWriter(imagePath); 				// for ascii type files (eg html)
				FileOutputStream binWriter = new FileOutputStream(dirNames + "/" + "BIN" + imageName);		// for binary files (eg images)

				System.out.println("a little further even");

				
				 // #####  receive data and write to the file  #####
				boolean entityPartStarted = false;	// entity = stuff after header
				
				String outputFromServer = inFromServer.readLine();
				
				System.out.println(outputFromServer);
				
				while (outputFromServer != null){
							// ###################
							// #######	TODO	waarom zo'n vreemde structuur? ik heb het hieronder herschreven, is dat niet beter? of moet het toch zo om een of andere reden?
					
//					    	if (outputFromServer.length() < 3){  		// < 3  because that marks the newlines at the end of the header
//					    		entityPartStarted = true;
//					    	}
//					    	if (!entityPartStarted){
//					    		System.out.println(outputFromServer);	// just print 
//					    	}
//					    	outputFromServer = inFromServer.readLine();
//					    	if (entityPartStarted == true && outputFromServer != null){
//					    		textWriter.println(outputFromServer);
//					    		byte[] outputFromServerBin = outputFromServer.getBytes();
//					    		binWriter.write(outputFromServerBin);
//					    	}
					    	if (outputFromServer.length() < 3){  		// < 3  because that marks the newlines at the end of the header
					    		entityPartStarted = true;
					    	}
					    	if (!entityPartStarted){
					    		System.out.println(outputFromServer);	// just print 
					    	}
					    	else {
					    		//System.out.println(outputFromServer);
					    		textWriter.println(outputFromServer);						// for ascii type files (eg html)
					    		byte[] outputFromServerBin = outputFromServer.getBytes();	// for binary files (eg images)
					    		binWriter.write(outputFromServerBin);
					    	}
					    	outputFromServer = inFromServer.readLine();
				}
				System.out.println("and we are done!");
				System.out.println();

				//close the stream to text file
				textWriter.close();
				binWriter.close();

				// Close the socket.
				clientSocket.close();
			    
				
				// OLD matthijs' Stackoverflow code TODO remove
//			    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
//			    
//			    // receive the data from the server using a buffer
//			    int count;
//			    byte[] buffer = new byte[8192];
//			    while ((count = inFromServer.read(buffer)) > 0)
//			    {
//			      dos.write(buffer, 0, count);
//			      dos.flush();
//			    }
//			    dos.close();
//			    System.out.println("image transfer done");
//
//			    clientSocket.close();     
			}
		 public static int nthOccurrence(String str, String c, int n) {
			 n-=1;
			    int pos = str.indexOf(c, 0);
			    while (n-- > 0 && pos != -1)
			        pos = str.indexOf(c, pos+1);
			    return pos;
			}
}
