package Client;

import java.io.IOException;
import java.util.Scanner;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class SendDataCommand extends Command {
	protected String dataToBeSent;

	public SendDataCommand(String shortHost, String hostExtension,
			String HTTPVersion, String command, Socket clientSocket) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
		
		System.out.println("Enter the Header: ");
		 
		
//	       Scanner scanIn = new Scanner(System.in);
//	       fileToBeSent = scanIn.nextLine();
//	       scanIn.close();
//
//	       Path path = Paths.get(fileToBeSent);		//read the file where the input was saved
//	       byte[] data = Files.readAllBytes(path);
//	       outToServer.write(data);
	       
	       
	       String dataToBeSentHeader = ""; String dataToBeSentBody = "";
	       char nextChar;
	       String lastFour = "    ";
	       while (!lastFour.equals("\r\n\r\n")){
	    	   nextChar = (char)System.in.read();
	    	   dataToBeSentHeader = dataToBeSentHeader + nextChar;
	    	   lastFour = lastFour.substring(1, lastFour.length()) + nextChar; //shift one and add new character   
	       }
	       
	       System.out.println("Header received. Now please enter your data/text:");
	       lastFour= "    ";
	       while (!lastFour.equals("\r\n\r\n")){
	    	   nextChar = (char)System.in.read();
	    	   dataToBeSentBody = dataToBeSentBody + nextChar;
	    	   lastFour = lastFour.substring(1, lastFour.length()) + nextChar; //shift one and add new character   
	       }
	       
	       // add body length data to header
	       int bodyLength = dataToBeSentBody.length();
	       dataToBeSentHeader = "Content-Length: " + bodyLength + dataToBeSentHeader;
	       
	       // add content type to header
	       dataToBeSentHeader = "Content-Type:" + " text/txt" + dataToBeSentHeader;
	       
	       System.out.println("dataToBeSent received. Sending to server...");
	       //System.out.println(dataToBeSent);
	       System.out.println("----------------------------------");
	       
	       // make header and body one message
	       this.dataToBeSent = dataToBeSentHeader + dataToBeSentBody; 
	       	       
		}
	
	public void execute() throws IOException{
		super.execute();						//send command and host
		outToServer.writeBytes(dataToBeSent);	//send header and data body
	}

}
