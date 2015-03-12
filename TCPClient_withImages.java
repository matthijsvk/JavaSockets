// Import necessary IO and NETwork libraries
import java.io.*;
import java.net.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/*
 * A simple example TCP Client application
 *
 * Computer Networks, KU Leuven.
 *
 */
class TCPClient_withImages
  {

  /*
   * Everything is included in the main method.
   */
  public static void main(String[] args) throws Exception
    {
	
	String command = args[0];
	String host = args[1];
	String shortHost;
	String requestedFile;
	if (host.contains("/")) {
		shortHost = host.substring(0,host.indexOf("/"));
		System.out.println(shortHost);
		System.out.println(host.indexOf("/"));
		System.out.println(host.length());
		requestedFile = host.substring(host.indexOf("/"), host.length());
		System.out.println("requestedFile: "+ requestedFile);
	}
	else{
		shortHost = host;
		requestedFile = "/";
	}
	
	String port = args[2];
	String httpVersion = args[3];
	
	for (int i=0;i<=3;i++){  //TODO remove debug info
		System.out.println(args[i]);
	}

    // Create a socket to localhost (this machine, port 6789).
    Socket clientSocket = new Socket(shortHost, Integer.parseInt(port));
   
    System.out.println("test");

    // Create outputstream (convenient data writer) to this host. 
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    
    // Create an inputstream (convenient data reader) to this host
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    
    if (httpVersion.equals("1.0")){
    	System.out.println("1.0 entered");
        outToServer.writeBytes(command + " "  + requestedFile + " HTTP/1.0" + "\r\n\r\n");
        System.out.println(command +  " "  + requestedFile + " HTTP/1.0" + "\r\n\r\n");
        }
    else if (httpVersion.equals("1.1")){
    	System.out.println("1.1 entered");
    	outToServer.writeBytes(command + " "+ requestedFile + " HTTP/1.1" + "\r\n" + "host:" + shortHost + "\r\n\r\n");
    }
    else{
    	//throw error
    }
    
    // make file to write to
    PrintWriter out = new PrintWriter("receivedStuff.html");
    
    boolean htmlPartStarted = false;
    
    // Read text from the server and write it to the screen.
    String htmlFromServer = "";
    String outputFromServer = inFromServer.readLine();
    while (outputFromServer != null){
    	if (outputFromServer.contains("<html")==true){
    		htmlPartStarted = true;
    	}
    	System.out.println(outputFromServer);
    	if (htmlPartStarted == true){
    		htmlFromServer = htmlFromServer + outputFromServer; //add server output to html string
    		out.println(outputFromServer);
    	}
    	outputFromServer = inFromServer.readLine();
    }
  //close the stream to text (html) file
    out.close();
    
    
    // ###### Get the images #######
    
    System.out.println("PARSING....."); //TODO remove debug
    System.out.println(htmlFromServer);
    Document doc = Jsoup.parse(htmlFromServer);

    System.out.println("parsing.....");
    
    // Then select images inside it:
    Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
    
    System.out.println(images.size());
    for (Object el:images) {
    	System.out.println("one");
    }

    // Iterate
	 for (Element el : images) {
	     String imageUrl = el.attr("src");
	     
	     System.out.println("DEBUG" + imageUrl); //TODO remove debug
	     
	     readImage(imageUrl, shortHost, Integer.parseInt(port));
	 }
    
	 
	 // using the JSoup GET methode
	 Document doc2 = Jsoup.connect("http://www.javatpoint.com").get(); 
	 System.out.println(doc2);
	 
	 Elements images2 = doc2.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
	    
	    System.out.println(images2.size());
	    int i=0;
	    for (Object el:images2) {
	    	System.out.println(i);
	    	i++;
	    }

	    // Iterate
		 for (Element el : images2) {
		     String imageUrl = el.attr("src");
		     
		     System.out.println("DEBUG" + imageUrl); //TODO remove debug
		     
		     readImage(imageUrl, shortHost, Integer.parseInt(port));
		 }
	 
    // Close the socket.
    clientSocket.close();

    } // End of main methodgetImages.saveImageToFile
  
  public static void readImage(String imageURL, String shortHost, int port) throws IOException
	{	    
	    Socket clientSocket = new Socket(shortHost, port);

	    // Create outputstream (convenient data writer) to this host. 
	    //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());	//old
	    DataOutputStream outToServer = new DataOutputStream(new DataOutputStream(clientSocket.getOutputStream())); 	//new
	    
	    // Create an inputstream (convenient data reader) to this host
	    //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	//old
	    DataInputStream in = new DataInputStream(clientSocket.getInputStream());	//new
	    
		
		// Parse image name 
	    // TODO check if images is server url or local adress (eg /images/image.jpg)
	 	System.out.println("requestedImage: "+ imageURL);
	 	
	 	// TODO http 1.0 or 1.1
	
	 	// write commands to server
	    outToServer.writeBytes("GET "+imageURL+" HTTP/1.1\r\n");
		outToServer.writeBytes("host: "+shortHost+":80\r\n\r\n");
		outToServer.flush();	    

		// make the file to which data will be written
	    File file = new File(imageURL); 	//TODO put here a proper name (with folder slashes)
	    file.createNewFile();
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

  } // End of class TCPClient
