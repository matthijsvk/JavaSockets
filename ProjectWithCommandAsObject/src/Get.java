import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Get extends RetrieveDataCommand {
	
protected String extension;

	
public Get(String shortHost,String hostExtension,String HTTPVersion, String command, DataOutputStream outToServer, BufferedInputStream inFromServer) {
	super(shortHost,hostExtension,HTTPVersion, command, outToServer, inFromServer);
}
	

public void execute() throws IOException{

super.execute();


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



// make file to write to
FileOutputStream binWriter = new FileOutputStream("receivedStuff"+ "." + this.extension);


// Read data from the server and write it to the screen.
int dataInFromServer = 0;
while (dataInFromServer != -1){
	dataInFromServer = inFromServer.read();
    binWriter.write(dataInFromServer);
}

//close the stream to text file
binWriter.close();
}
}
