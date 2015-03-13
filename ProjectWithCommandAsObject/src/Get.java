import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Get extends RetrieveDataCommand {
	
protected String extension;
protected int length;


	
public Get(String shortHost,String hostExtension,String HTTPVersion, String command, DataOutputStream outToServer, BufferedInputStream inFromServer) {
	super(shortHost,hostExtension,HTTPVersion, command, outToServer, inFromServer);
}
	

public void execute() throws IOException{

super.execute();
this.parseHeader();
this.pullEntity();
}




private void pullEntity() throws IOException {
	// make file to write to
	FileOutputStream binWriter = new FileOutputStream("receivedStuff"+ "." + this.extension);
	

	// Read data from the server and write it to the screen.
	int dataInFromServer = 0;
	int counter = length;
	while (dataInFromServer != -1){
		dataInFromServer = inFromServer.read();
	    binWriter.write(dataInFromServer);
	    counter -= 1;
	}

	//close the stream to text file
	binWriter.close();
	
}


private void parseHeader() {

//Parse for file extension
int indexOfType = header.indexOf("Content-Type:");
boolean extensionNotFound = true;
while (extensionNotFound ){
	if( (header.substring(indexOfType,indexOfType+1)).equals("/")){
	extensionNotFound = false;
	}
	indexOfType += 1;
}
boolean endOfExtensionNotFound = true;
int indexOfEndOfType = indexOfType;
while (endOfExtensionNotFound ){
	indexOfEndOfType += 1;
	if( (header.charAt(indexOfEndOfType)==13) || (header.substring(indexOfEndOfType,indexOfEndOfType+1)).equals(" ")) {
	endOfExtensionNotFound = false;
	}
}
extension = header.substring(indexOfType, indexOfEndOfType);



//parse for content length
int indexOfLength = header.indexOf("Content-Length:");
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
}
