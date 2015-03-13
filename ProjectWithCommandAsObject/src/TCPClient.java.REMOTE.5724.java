
	
	
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



	public static void main(String[] args) throws Exception{
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
				

		// Create a socket to host (URI, port).
		Socket clientSocket = new Socket(shortHost, Integer.parseInt(args[2]));

		// Create outputstream (convenient data writer) to this host. 
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			    
			    
		// Create an inputstream (convenient data reader) to this host
		BufferedInputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
		
		Command query = null;
		
		String command = args[0];
		if(command.equals("GET")){
				query = new Get(shortHost,hostExtension, args[3],command, outToServer,inFromServer);
		}
		if(command.equals("POST")){
				query = new Post(shortHost, hostExtension, args[3], command, outToServer, inFromServer);
		}
		if(command.equals("PUT")){
				query = new Put(shortHost,hostExtension, args[3],command, outToServer,inFromServer);
		}
		if(command.equals("HEAD")){
				query = new Head(shortHost,hostExtension, args[3],command, outToServer,inFromServer);
		}
		
		query.execute();
	    
	    // Close the socket.
	    clientSocket.close();

	    } // End of main method
	  
	  

	  } // End of class TCPClient


