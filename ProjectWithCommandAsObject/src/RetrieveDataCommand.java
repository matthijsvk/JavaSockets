import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;



public class RetrieveDataCommand extends Command {
	
protected String header = "";
	
	
public RetrieveDataCommand(String shortHost,String hostExtension, String HTTPVersion, String command,DataOutputStream outToServer,BufferedInputStream inFromServer){
	super(shortHost,hostExtension,HTTPVersion, command, outToServer, inFromServer);	
}

public void execute() throws IOException{
	super.execute();
	boolean entityPartStarted = false; // entity = stuff after header
	int a = 0;
	int b = 0;
	int c = 0;
	int d = 0;

	// Read data from the server and write it to the screen.
	int outputFromServer = inFromServer.read();
	while (outputFromServer != -1 && !entityPartStarted){
	if (a == 10 && b == 13 && c == 10 && d == 13){
		entityPartStarted = true;
	}
	if (!entityPartStarted){
		header = header +(char)outputFromServer;
	}
	outputFromServer = inFromServer.read();
	d = c;
	c = b;
	b = a;
	a = outputFromServer;
	}
	System.out.print(header);
}
}
