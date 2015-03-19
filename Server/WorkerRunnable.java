package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * This class implements the
 * @author M
 *
 */
public class WorkerRunnable implements Runnable{

	protected int serverPort;
	protected Socket clientSocket = null;
	protected String serverText   = null;

	public WorkerRunnable(Socket clientSocket, String serverText, int serverPort) {
		this.clientSocket = clientSocket;
		this.serverText   = serverText;
		this.serverPort = serverPort;
	}

	/**
	 * This method is executed for each client when they receive a thread, and this method does the same as the single-threaded server would normally do.
	 */
	public void run() {
		try {

			// Create an inputstream (convenient data reader) to this host
			BufferedInputStream inFromClient = new BufferedInputStream(clientSocket.getInputStream());

			// Create outputstream (convenient data writer) to this host.
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

			//			
			int inputFromClient = 0;

			System.out.println("starting server...");
			String HTTPVersion = "1.1";
			while (inputFromClient != -1){
				inputFromClient = inFromClient.read();
				System.out.println("read data" + inputFromClient);

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

						String str;
						try{Path path = Paths.get(System.getProperty("user.dir")+System.getProperty("file.separator")+"Server/"+"404.html");
						byte[] data = Files.readAllBytes(path);
						str = new String(data, "UTF-8");
						}
						catch(Exception e){System.out.println("raar...");str = "";}
						int strLength = str.length();
						int contentLength = strLength;

						String headerForClient = "HTTP/1.1 404 Not Found\r\n"+"Content-Type: text/html\r\n"+"Content-Length: "+contentLength+"\r\n\r\n" + str;

						System.out.println(headerForClient);
						outToClient.writeBytes(headerForClient);
					}catch (IOException anyOtherException){
						System.out.println("ERROR 500 SERVER ERROR");
						String headerForClient = "HTTP/1.1 500 Server Error\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 27" + "\r\n\r\n" + "<html> server error </html>";
						System.out.println(headerForClient);
						outToClient.writeBytes(headerForClient);
					}catch (NotModifiedSinceException notModifiedSinceException){
						System.out.println("ERROR 304 NOT MODIFIED");
						String headerForClient = "HTTP/1.1 304 Not Modified\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 27" + "\r\n\r\n" + "<html> not modified </html>";
						System.out.println(headerForClient);
						outToClient.writeBytes(headerForClient);
					}catch (BadRequestException badRequestException){
						System.out.println("ERROR 400 BAD REQUEST");
						String headerForClient = "HTTP/1.1 400 Bad request\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 26" + "\r\n\r\n" + "<html> bad request </html>";
						System.out.println(headerForClient);
						outToClient.writeBytes(headerForClient);}
					if(query.httpVersion.equals("1.0")){break;}
				}
			}
			clientSocket.close();
		}
		catch(Exception e){e.printStackTrace();
		}
	}
}