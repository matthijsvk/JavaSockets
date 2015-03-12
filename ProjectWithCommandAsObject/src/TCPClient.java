
	
	
	// Import necessary IO and NETwork libraries
	import java.io.*;
	import java.net.*;

	/*
	 * A simple example TCP Client application
	 *
	 * Computer Networks, KU Leuven.
	 *
	 */
	public class TCPClient {

	  public static void main(String[] args) throws Exception
	    {
		  String host = args[1];
			String shortHost; // only www.abc.def
			String hostExtension; // everything after the shorthost
			host = host.replace("http://","");	//filter out http:// chars if they exist
				if (host.contains("/")) {
					shortHost = host.substring(0,host.indexOf("/"));
					hostExtension = host.substring(host.indexOf("/"), host.length());
				}
				else{
					shortHost = host;
					hostExtension = "/";
				}
					  

				
				
				
				
				
				
		String port = args[2];
		String httpVersion = args[3];
				
				
		// Create a socket to localhost (this machine, port 6789).
		Socket clientSocket = new Socket(shortHost, Integer.parseInt(port));

		// Create outputstream (convenient data writer) to this host. 
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			    
			    
		// Create an inputstream (convenient data reader) to this host
		BufferedInputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
				
				
				
		String command = args[0];
		if(command == "GET"){
				Get getCommand = new Get(shortHost,hostExtension, args[3],command);
		}
		if(command == "POST"){
				Post postCommand = new Post();
		}
		if(command == "PUT"){
				Put putCommand = new Put();
		}
		if(command == "HEAD"){
		}
		

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
	    FileOutputStream binWriter = new FileOutputStream("receivedStuff"+extension);
	    
	    
	    boolean entityPartStarted = false; // entity = stuff after header
	    
	    
	    System.out.println("a little further even");
	    
	    int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
	    
	    // Read data from the server and write it to the screen.
	    int outputFromServer = inFromServer.read();
	    System.out.print((char)outputFromServer);
	    while (outputFromServer != -1){
	    if (a == 10 && b == 13 && c == 10 && d == 13){
	    	entityPartStarted = true;
	    }
	    if (!entityPartStarted){
	    	System.out.print((char)outputFromServer);
	    }
	    outputFromServer = inFromServer.read();
	    d = c;
	    c = b;
	    b = a;
	    a = outputFromServer;
	   if (entityPartStarted == true && outputFromServer != -1){
	        binWriter.write(outputFromServer);
	        }
	    }
	    System.out.println("and we are done!");
	    
	    //close the stream to text file
	    binWriter.close();
	    
	    
	    // Close the socket.
	    clientSocket.close();

	    } // End of main method

	  } // End of class TCPClient


