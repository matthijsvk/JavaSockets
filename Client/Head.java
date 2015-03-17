package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Head extends RetrieveDataCommand {

	public Head(String shortHost, String hostExtension, String HTTPVersion,
			String command, Socket clientSocket) throws UnknownHostException, IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
	}

	public void execute() throws IOException{
		super.execute();
		PrintWriter out = new PrintWriter("header.txt");
		out.println(this.header);
		out.close();
		if (this.HTTPVersion.equals("1.0")){
			this.terminate();	// terminate the connection created by this Command
								//must be here instead ofin superclass because maybe you want to do other stuff after the super.execute, but before the this.terminate
		}
	}
} 
