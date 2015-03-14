package ProjectWithCommandAsObject.src;

import java.io.IOException;
import java.net.Socket;


public class Put extends SendDataCommand{

	public Put(String shortHost, String hostExtension, String HTTPVersion,
			String command, Socket clientSocket) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
	}

	public void execute() throws IOException{
		super.execute();
	}
}
