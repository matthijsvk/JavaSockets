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

		String command = args[0];
		String host = args[1];
		String shortHost;	// only www.abc.def
		String requestedFile; // everything after the shorthost
		
		host = host.replace("http://","");	//filter out http:// chars if they exist
		if (host.contains("/")) {
			shortHost = host.substring(0,host.indexOf("/"));
			requestedFile = host.substring(host.indexOf("/"), host.length());
			System.out.println("requestedFile: "+ requestedFile);
		}
		else{
			shortHost = host;
			requestedFile = "/";
		}

		//System.out.println("dude this isn't even the first loop");

		// look for file extension (loop in reverse over chars until '.' found
		int placeInUrl = requestedFile.length() -1;
		String extension = ".html";
		while (requestedFile.charAt(placeInUrl) != ("/").charAt(0) && extension == ".html"){
			if (requestedFile.charAt(placeInUrl) == (".").charAt(0)){
				extension = requestedFile.substring(placeInUrl);

			}
			placeInUrl = placeInUrl -1;
		}

		String port = args[2];
		String httpVersion = args[3];

		System.out.println("we got this far");

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
		PrintWriter textWriter = new PrintWriter("receivedStuff"+extension); 				// for ascii type files (eg html)
		FileOutputStream binWriter = new FileOutputStream("receivedStuffBin"+extension);	// for binary files (eg images)


		System.out.println("a little further even");

		boolean entityPartStarted = false;	// entity = stuff after header

		// Read text from the server and write it to the screen.
		String outputFromServer = inFromServer.readLine();
		System.out.println(outputFromServer);
		while (outputFromServer != null){
			    	if (outputFromServer.length() < 3){  		// < 3  because that marks the newlines at the end of the header
			    		entityPartStarted = true;
			    	}
			    	if (!entityPartStarted){
			    		System.out.println(outputFromServer);	// just print 
			    	}
			    	outputFromServer = inFromServer.readLine();
			    	if (entityPartStarted == true && outputFromServer != null){
			    		textWriter.println(outputFromServer);
			    		byte[] outputFromServerBin = outputFromServer.getBytes();
			    		binWriter.write(outputFromServerBin);
			    	}
//			    	if (outputFromServer.length() < 3){  		// < 3  because that marks the newlines at the end of the header
//			    		entityPartStarted = true;
//			    	}
//			    	if (!entityPartStarted){
//			    		System.out.println(outputFromServer);	// just print 
//			    	}
//			    	else {
//			    		System.out.println(outputFromServer);
//			    		textWriter.println(outputFromServer);						// for ascii type files (eg html)
//			    		byte[] outputFromServerBin = outputFromServer.getBytes();	// for binary files (eg images)
//			    		binWriter.write(outputFromServerBin);
//			    	}
//			    	outputFromServer = inFromServer.readLine();
		}
		System.out.println("and we are done!");

		//close the stream to text file
		textWriter.close();
		binWriter.close();


		// Close the socket.
		clientSocket.close();

	} // End of main method

} // End of class TCPClient
