package Server;

//Import necessary IO and NETwork libraries
import java.io.*;
import java.net.*;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

/*
 * A simple example TCP Server application
 *
 * Computer Networks, KU Leuven.
 *
 */
public class TCPServer
{
	/*
	 * Everything is included in the main method.
	 */
	public static void main(String argv[]) throws Exception
	{
		// Create server (incoming) socket on port 6789.
		ServerSocket welcomeSocket = new ServerSocket(7000);

		// Wait for a connection to be made to the server socket. 
		while(true)
		{
			// Create	 a 'real' socket from the Server socket.
			Socket connectionSocket = welcomeSocket.accept();

			// Create an inputstream (convenient data reader) to this host
			BufferedInputStream inFromClient = new BufferedInputStream(connectionSocket.getInputStream());

			// Create outputstream (convenient data writer) to this host.
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			// Read text from the client, make it uppercase and write it back.
//			String clientSentence = inFromClient.read();
//			System.out.println("Received: " + clientSentence);
//			String capsSentence = clientSentence.toUpperCase() + '\n';
//			outToClient.writeBytes(capsSentence);
//			System.out.println("Sent: "+ capsSentence);
//			
			
			
			// Read data from the server and write it to the screen.
			int a = 0;int b = 0;
			boolean requestEnded = false;
			String request = "";
			int inputFromClient = inFromClient.read();
			while (inputFromClient != -1 && !requestEnded){
				if (a == 10 && b == 13){
					requestEnded = true;
				}
				if (!requestEnded){
					request = request +(char)inputFromClient;
					inputFromClient = inFromClient.read();
					b = a; a = inputFromClient;
				}
			}
			
			Respond query = null;
			if(request.substring(0, 3).equals("GET")){
				query = new GetRespond(request, outToClient, inFromClient);
			}
			System.out.println(query);
			if(request.substring(0, 4).equals("POST")){
				query = new PostRespond(request, outToClient, inFromClient);
			}
			if(request.substring(0, 3).equals("PUT")){
				query = new PutRespond(request, outToClient, inFromClient);
			}
			if(request.substring(0, 4).equals("HEAD")){
				query = new HeadRespond(request, outToClient, inFromClient);
			}
			
			query.execute();
			
		}
		//welcomeSocket.close();  //never reached because the server has to stay online to accept clients

	} // End of main method.

} // End of class TCPServer