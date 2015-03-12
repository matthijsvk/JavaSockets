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
>>>>>>> 90eabac444a77c7dfbe7ba39a44c94dcd7f5131e
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
<<<<<<< HEAD
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
    BufferedInputStream inFromServer = new BufferedInputStream(clientSocket.getInputStream());
    
    System.out.println("just a little further");
    
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
    FileOutputStream binWriter = new FileOutputStream("receivedStuff"+extension); // for binary files (eg images) AND text files
    
    
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
    
    
    if (outputFromServer.length() < 3){  		// < 3  because that marks the newlines at the end of the header
//		entityPartStarted = true;
//	}
//	if (!entityPartStarted){
//		System.out.println(outputFromServer);	// just print 
//	}
//	else {
//		System.out.println(outputFromServer);
//		textWriter.println(outputFromServer);						// for ascii type files (eg html)
//		byte[] outputFromServerBin = outputFromServer.getBytes();	// for binary files (eg images)
//		binWriter.write(outputFromServerBin);
//	}
//	outputFromServer = inFromServer.readLine();
    
    
    
    
    
    
    
    
    
    
    
    //close the stream to text file
    binWriter.close();
    
    
    // Close the socket.
    clientSocket.close();

    } // End of main method

  } // End of class TCPClient
  
