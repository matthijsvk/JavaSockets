
	
	
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
		if(command.equals("GET")){
				Get getCommand = new Get(shortHost,hostExtension, args[3],command, outToServer,inFromServer);
				getCommand.execute();
		}
		if(command == "POST"){
				Post postCommand = new Post(shortHost, hostExtension, args[3], command, outToServer, inFromServer);
		}
		if(command == "PUT"){
				Put putCommand = new Put(command, command, command, command, outToServer, inFromServer);
		}
		if(command == "HEAD"){
		}
	    
	    // Close the socket.
	    clientSocket.close();

	    } // End of main method

	  } // End of class TCPClient


