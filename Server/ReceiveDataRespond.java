
package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReceiveDataRespond extends Respond {

	protected int length;
	protected String data = "";

	public ReceiveDataRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws IOException {
		parseHeader();
		readData();
	}
	
	protected void readData() throws IOException {
		int counter = 0;
		while(counter < length){
			data += inFromClient.read();
		}
	}

	public void parseHeader() {
		int counter = 1;
		String header = ""; 
		while(counter < request.length){
			if(!request[counter].equals(null)){header += request[counter];}
		}


		//parse for content length
		int indexOfLength = header.indexOf("Content-Length:");
		if (indexOfLength >0){
			boolean lengthNotFound = true;
			while (lengthNotFound ){
				if( (header.substring(indexOfLength,indexOfLength+1)).equals(" ")){
					lengthNotFound = false;
				}
				indexOfLength += 1;
			}
			boolean endOfLengthNotFound = true;
			int indexOfEndOfLength = indexOfLength;
			while (endOfLengthNotFound ){
				indexOfEndOfLength += 1;
				if( (header.charAt(indexOfEndOfLength)==13) || (header.substring(indexOfEndOfLength,indexOfEndOfLength+1)).equals(" ")) {
					endOfLengthNotFound = false;
				}
			}
			length = Integer.parseInt(header.substring(indexOfLength, indexOfEndOfLength));
		}
		else{
			length = -1;
		}
	}
}