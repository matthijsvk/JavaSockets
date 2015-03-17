package Server;

//Import necessary IO and NETwork libraries
import java.io.*;
import java.net.*;
import java.util.Arrays;

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
			
		int serverPort = 7094; 
		
		// Create server (incoming) socket on port 6789.
		ServerSocket serverSocket = new ServerSocket(serverPort);
		
		while (true){
		// Wait for a connection to be made to the server socket. 
		// Create	 a 'real' socket from the Server socket.
		Socket clientSocket = serverSocket.accept();
		
		// Create an inputstream (convenient data reader) to this host
		BufferedInputStream inFromClient = new BufferedInputStream(clientSocket.getInputStream());

		// Create outputstream (convenient data writer) to this host.
		DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

		// Read text from the client, make it uppercase and write it back.
		//	String clientSentence = inFromClient.read();
		//	System.out.println("Received: " + clientSentence);
		//	String capsSentence = clientSentence.toUpperCase() + '\n';
		//	outToClient.writeBytes(capsSentence);
		//	System.out.println("Sent: "+ capsSentence);
		//			
		int inputFromClient;
		while (true){
			System.out.println("The next step is a read that seems to take forever!");
			inputFromClient = inFromClient.read();
			//Matthijs is supercool!!!!
			while(inputFromClient != -1 && inputFromClient != 10 && inputFromClient != 13 && inputFromClient != 10 && inputFromClient !=32){
				System.out.println("Ready for new request!");
			// Read data from the server and write it to the screen.
			int a = 0;int b = 0;int c = 0;int d=0;
			boolean requestEnded = false;
			String[] request = new String[100];
			request[0] = "";
			int placeInArray = 0;
			while (!requestEnded && inputFromClient != -1){
				d=c;c =b;b = a; a = inputFromClient;
				request[placeInArray] = request[placeInArray] +(char)inputFromClient;
				if (a == 10 && b == 13 && c == 10 && d == 13){
					requestEnded = true;
				}
				if (a == 10 && b == 13){
					placeInArray += 1;
					request[placeInArray] = "";
				}
				if(!requestEnded){inputFromClient = inFromClient.read();}
			}
			System.out.println("here comes the request");
			System.out.println(Arrays.toString(request));
			Respond query = null;
			if(request[0].substring(0, 3).equals("GET")){
				query = new GetRespond(request, outToClient, inFromClient, serverPort);}
			if(request[0].substring(0, 4).equals("POST")){
				query = new PostRespond(request, outToClient, inFromClient, serverPort);}
			if(request[0].substring(0, 3).equals("PUT")){
				query = new PutRespond(request, outToClient, inFromClient, serverPort);}
			if(request[0].substring(0, 4).equals("HEAD")){
				query = new HeadRespond(request, outToClient, inFromClient, serverPort);}
			
			try{
				query.execute();
			System.out.println("we executed the Query");
			}catch (FileNotFoundException fileNotFoundException){
				System.out.println("ERROR 404 NOT FOUND");
				String headerForClient = "HTTP/1.1 404 Not Found\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 24" + "\r\n\r\n" + "<html> not found </html>";
				System.out.println(headerForClient);
				outToClient.writeBytes(headerForClient);
			}catch (IOException anyOtherException){
				System.out.println("ERROR 500 SERVER ERROR");
				String headerForClient = "HTTP/1.1 500 Server Error\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 27" + "\r\n\r\n" + "<html> server error </html>";
				System.out.println(headerForClient);
				outToClient.writeBytes(headerForClient);
			}catch (NotModifiedSinceException notModifiedSinceException){
				System.out.println("ERROR 304 NOT MODIFIED");
				String headerForClient = "HTTP/1.1 304 Not Modified\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 27" + "\r\n\r\n" + "<html> server error </html>";
				System.out.println(headerForClient);
				outToClient.writeBytes(headerForClient);}
			}
			}
		} //never reached because the server has to stay online to accept clients
		
	} // End of main method.

} // End of class TCPServer