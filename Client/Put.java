package Client;

import java.io.IOException;
import java.net.Socket;


public class Put extends SendDataCommand{	// most of the work is done in the superclass

	public Put(String shortHost, String hostExtension, String HTTPVersion, 
			String command, Socket clientSocket) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
	}

	public void execute() throws IOException{
		super.execute();
		if (this.HTTPVersion.equals("1.0")){
			this.terminate();	// terminate the connection created by this Command
								//must be here instead of in superclass because maybe you want to do other stuff after the super.execute, but before the this.terminate
		}
	}
}
