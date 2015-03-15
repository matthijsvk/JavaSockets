package Server;

//Import necessary IO and NETwork libraries
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

		int port = 7036;

		// Create server (incoming) socket on port 6789.
		ServerSocket welcomeSocket = new ServerSocket(port);

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

			int inputFromClient;
			while (true){
				System.out.println("The next step is a read that seems to take forever!");
				inputFromClient = inFromClient.read();		// 10 = \r; 13 = \n; 32 = space
				while(inputFromClient != -1 && inputFromClient != 10 && inputFromClient != 13 && inputFromClient !=32){
					System.out.println("Ready for new request!");
					
					String[] request = getRequest(inFromClient, inputFromClient);
					
					System.out.println("here comes the request");
					System.out.println(Arrays.toString(request));
					
					Respond query = null;
					if(request[0].substring(0, 3).equals("GET")){
						query = new GetRespond(request, outToClient, inFromClient, port);}
					if(request[0].substring(0, 4).equals("POST")){
						query = new PostRespond(request, outToClient, inFromClient, port);}
					if(request[0].substring(0, 3).equals("PUT")){
						query = new PutRespond(request, outToClient, inFromClient, port);}
					if(request[0].substring(0, 4).equals("HEAD")){
						query = new HeadRespond(request, outToClient, inFromClient, port);}

					try{
						query.execute();
						System.out.println("we executed the Query");
					}catch (FileNotFoundException fileNotFoundException){
						System.out.println("ERROR 404 NOT FOUND");
						String headerForClient = "HTTP/1.1 404 NOT FOUND\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23" + "\r\n\r\n" + "<html> this file is not found <html>";
						System.out.println(headerForClient);
						outToClient.writeBytes(headerForClient);
					}catch (IOException anyOtherException){
						System.out.println("ERROR 500 SERVER ERROR");
						String headerForClient = "HTTP/1.1 500 SERVER ERROR\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 26" + "\r\n\r\n" + "<html> server error <html>";
						System.out.println(headerForClient);
						outToClient.writeBytes(headerForClient);}
				}
			}
		}
		//welcomeSocket.close();  //never reached because the server has to stay online to accept clients

	} // End of main method.

	private static String[] getRequest(BufferedInputStream inFromClient, int inputFromClient) throws IOException {
		// Read data from the client
		int a = 0;int b = 0;int c = 0;int d=0;
		boolean requestEnded = false;
		String[] request = new String[100];
		request[0] = "";
		int placeInArray = 0;
		while (!requestEnded && inputFromClient != -1){
			d=c; c =b; b = a; a = inputFromClient;
			request[placeInArray] = request[placeInArray] +(char)inputFromClient;
			if (a == 10 && b == 13 && c == 10 && d == 13){
				requestEnded = true;
			}
			if (a == 10 && b == 13){
				placeInArray += 1;
				request[placeInArray] = "";
			}
			if(!requestEnded){
				System.out.println(request[0]);
				inputFromClient = inFromClient.read();
			}
		}
		return request;
	}
	
	/**
	 * This method returns the current time in the correcct HTTP format
	 * @return
	 */
	private static String getCurrentTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date());
	}

} // End of class TCPServer