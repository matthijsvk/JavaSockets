// Import necessary IO and NETwork libraries
import java.io.*;
import java.net.*;

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
	  
	String host = args[0];
	
	
    // Create a buffered reader to take user input from the console. 
    BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
    System.out.print("FROM CONSOLE: ");

    // Create a socket to localhost (this machine, port 6789).
    Socket clientSocket = new Socket(host, 6789);

    // Create outputstream (convenient data writer) to this host. 
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

    // Create an inputstream (convenient data reader) to this host
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    // Read text from the console and write it to the server. 
    String sentence = inFromUser.readLine();
    outToServer.writeBytes(sentence + '\n');

    // Read text from the server and write it to the screen.
    String modifiedSentence = inFromServer.readLine();
    System.out.println("FROM SERVER: " + modifiedSentence);

    // Close the socket.
    clientSocket.close();

    } // End of main method

  } // End of class TCPClient
