package Client;

// Import necessary libraries
import java.net.*;

/*
 * A simple example TCP Client application
 *
 * Computer Networks, KU Leuven.
 *
 */
public class TCPClient {

	public static void main(String[] args) throws Exception{  // args[] = [command, host, port, HTTPVersion]
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

		Command query = null;

		String command = args[0];
		if(command.equals("GET")){
			query = new Get(shortHost,hostExtension, args[3],command, clientSocket);
		}
		if(command.equals("POST")){
			query = new Post(shortHost, hostExtension, args[3], command, clientSocket);
		}
		if(command.equals("PUT")){
			query = new Put(shortHost,hostExtension, args[3],command, clientSocket);
		}
		if(command.equals("HEAD")){
			query = new Head(shortHost,hostExtension, args[3],command, clientSocket);
		}

		query.execute();

		// Close the socket.
		clientSocket.close();

	} // End of main method

} // End of class TCPClient


