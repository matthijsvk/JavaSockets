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
	       
	       String dataToBeSentHeader = ""; String dataToBeSentBody = "";
	       char nextChar;
	       String lastTwo = "  ";
	       while (!lastTwo.equals("\n\n")){		// first get the header from the command line
	    	   nextChar = (char)System.in.read();
	    	   System.out.println(nextChar);
	    	   dataToBeSentHeader = dataToBeSentHeader + nextChar;
	    	   lastTwo = lastTwo.substring(1, lastTwo.length()) + nextChar; //shift one and add new character   
	       }
	       dataToBeSentHeader = dataToBeSentHeader.substring(0, dataToBeSentHeader.indexOf("\n\n"));
	       
	       System.out.println(dataToBeSentHeader);
	       
	       System.out.println("Header received. Now please enter your data/text:");
	       lastTwo= "  ";
	       while (!lastTwo.equals("\n\n")){		// now get the message body
	    	   nextChar = (char)System.in.read();
	    	   dataToBeSentBody = dataToBeSentBody + nextChar;
	    	   lastTwo = lastTwo.substring(1, lastTwo.length()) + nextChar; //shift one and add new character   
	       }
	       dataToBeSentBody = dataToBeSentBody.substring(0, dataToBeSentBody.indexOf("\n\n"));
	       
	       System.out.println(dataToBeSentBody);
	       
	       // add body length data to header
	       int bodyLength = dataToBeSentBody.length();
	       dataToBeSentHeader = "Content-Length: " + bodyLength + "\r\n" + dataToBeSentHeader;
	       
	       // add content type to header
	       dataToBeSentHeader = "Content-Type: " + "text/txt" + "\r\n" + dataToBeSentHeader;
	       
	       System.out.println("dataToBeSent received. Sending to server...");
	       //System.out.println(dataToBeSent);
	       System.out.println("----------------------------------");
	       
	       // make header and body one message
	       this.dataToBeSent = dataToBeSentHeader + "\r\n\r\n" + dataToBeSentBody; 
	       	       
		}
	
	public void execute() throws IOException{
		super.execute();						//send command and host
		outToServer.writeBytes(dataToBeSent);	//send header and data body, because this is put/post you have to send more than just 
	}
	
	@Override
	public void createDataToBeSent(String command){		// you have to modify this because only one "\r\n"; more has to follow
		
		if (HTTPVersion.equals("1.0")){
			toBeSent = command + " "  + hostExtension + " HTTP/1.0"+"\r\n";
		}
		else if (HTTPVersion.equals("1.1")){
			toBeSent = command + " " + hostExtension + " HTTP/1.1" + "\r\n" + "Host: " + shortHost+"\r\n";
		}
		else{
			//throw error
		}
		
		//System.out.println("TO BE SENT: "+toBeSent);
	}

}
