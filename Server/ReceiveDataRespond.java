
package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

//This a class for the response of the server to a put or post command
public class ReceiveDataRespond extends Respond {

	protected int length;
	protected String data = "";
	
	/**
	 * Constructor
	 */
	public ReceiveDataRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This executes the command, this means parsing the header and reading the sent entity data
	 */
	@Override
	public void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException, BadRequestException {
		super.execute();
		parseHeader();
		System.out.println("we parsed the headers");
		readData();
	}
	
	/**
	 * Reads the entity data sent by the client
	 */
	protected void readData() throws IOException {
		System.out.println("the length of data is" + length);
		int counter = 0;
		while(counter < length){
			data += (char) inFromClient.read();
			counter += 1; 
		}
	}
	
	/**
	 * Parses the header
	 */
	public void parseHeader() {
		int counter = 0;
//		String header = ""; 
		while(counter < request.length){
			System.out.println(request[counter]);
			if(request[counter] !=(null) && request[counter].contains("Content-Length: ")){
				length = Integer.parseInt(request[counter].substring(16,request[counter].length()-2));
				}
			counter += 1;
		}


		//parse for content length
//		int indexOfLength = header.indexOf("Content-Length:");
//		if (indexOfLength >0){
//			boolean lengthNotFound = true;
//			while (lengthNotFound ){
//				if( (header.substring(indexOfLength,indexOfLength+1)).equals(" ")){
//					lengthNotFound = false;
//				}
//				indexOfLength += 1;
//			}
//			boolean endOfLengthNotFound = true;
//			int indexOfEndOfLength = indexOfLength;
//			while (endOfLengthNotFound ){
//				indexOfEndOfLength += 1;
//				if( (header.charAt(indexOfEndOfLength)==13) || (header.substring(indexOfEndOfLength,indexOfEndOfLength+1)).equals(" ")) {
//					endOfLengthNotFound = false;
//				}
//			}
//			length = Integer.parseInt(header.substring(indexOfLength, indexOfEndOfLength));
//		}
//		else{
//			length = -1;
//		}
	}
}