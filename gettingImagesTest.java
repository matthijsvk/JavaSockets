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
			    DataOutputStream outToServer = new DataOutputStream(new DataOutputStream(clientSocket.getOutputStream())); 
			    
			    // Create an inputstream (convenient data reader) to this host
			    DataInputStream in = new DataInputStream(clientSocket.getInputStream());				    
					    			 	
			 	outToServer.writeBytes("GET "+imageURL+" HTTP/1.1\r\n");	// write commands to server
				outToServer.writeBytes("host: "+shortHost+":80\r\n\r\n");
				outToServer.flush();	    

				
				// ##### create directory structure and file #####
				String toDelete = imageURL.substring(imageURL.indexOf("http://"),nthOccurrence(imageURL, "/", 3)+1);
				System.out.println(nthOccurrence(imageURL, "/", 1));
				System.out.println(imageURL.indexOf("http://"));
				System.out.println("TO DELETE: " + toDelete);
				String imagePath = imageURL.replace(toDelete,"");//"http://"+shortHost+"/","");	// get the image path by removing http etc
				
				System.out.println("requestedImage: "+ imageURL);		//TODO remove debug 
			    System.out.println("DEBUG: " + imagePath); 				//TODO remove debug
			    
			    
				// get the name of the image itself by removing the directory names
				String shorter = imagePath.substring(imagePath.indexOf("/"), imagePath.length());
				while (shorter.contains("/")){
					shorter= shorter.substring(shorter.indexOf("/")+1, shorter.length());
				}
				
				String dirNames = imagePath.replace(shorter,"");		// get the directory name = url - imageName
				dirNames = dirNames.substring(0, dirNames.length()-1); 	//remove trailing slash
				
				//System.out.println(shorter);
				System.out.println("DIRNAMES: "+ dirNames);
				File dir = new File("./" + dirNames);
			    // create directory structure
			    boolean successful = dir.mkdirs();
			    if (successful)
			      System.out.println("directories were created successfully");
			    else
			      System.out.println("failed trying to create the directories");
				
			    // create the file to which received data will be written
			    System.out.println(imagePath);
			    File file = new File(imagePath); 
			    
			    successful = file.createNewFile();
			    if (successful)
			      System.out.println("file was created successfully");
			    else
			      System.out.println("failed trying to create the file");
			    
			    
			    
			    // #####  receive data and write to the file #####
			    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			    
			    // receive the data from the server using a buffer
			    int count;
			    byte[] buffer = new byte[8192];
			    while ((count = in.read(buffer)) > 0)
			    {
			      dos.write(buffer, 0, count);
			      dos.flush();
			    }
			    dos.close();
			    System.out.println("image transfer done");

			    clientSocket.close();     
			}
		 public static int nthOccurrence(String str, String c, int n) {
			 n-=1;
			    int pos = str.indexOf(c, 0);
			    while (n-- > 0 && pos != -1)
			        pos = str.indexOf(c, pos+1);
			    return pos;
			}
}
