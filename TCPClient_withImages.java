// Import necessary IO and NETwork libraries
import java.io.*;
import java.net.*;

import getImages;

/*
 * A simple example TCP Client application
 *
 * Computer Networks, KU Leuven.
 *
 */
class TCPClient
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
	
	

    // Create a socket to localhost (this machine, port 6789).
    Socket clientSocket = new Socket(shortHost, Integer.parseInt(port));

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
    String htmlFromServer;
    String outputFromServer = inFromServer.readLine();
    while (outputFromServer != null){
    	if (outputFromServer.contains("<html")==true){
    		htmlPartStarted = true;
    	}
    	System.out.println(outputFromServer);
    	if (htmlPartStarted == true){
    		htmlFromServer.concat(outputFromServer); //add server output to html string
    		out.println(outputFromServer);
    	}
    	outputFromServer = inFromServer.readLine();
    }
  //close the stream to text (html) file
    out.close();
    
    
    
    // ###### Getting the images #######
    
    Document doc = JSoup.parse(htmlFromServer);

    // Then select images inside it:
    Elements images = doc.select("img");

    // Iterate
	 for (Element el : images) {
	     String imageUrl = el.attr("src");
	     
	     System.out.println(imageUrl); //DEBUG
	     
	     getImages.saveImageToFile(imageUrl, shortHost, port);
	 }
    
    
    
    
    // Close the socket.
    clientSocket.close();

    } // End of main method

  } // End of class TCPClient
