package ProjectWithCommandAsObject.src;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Head extends RetrieveDataCommand {

	public Head(String shortHost, String hostExtension, String HTTPVersion,
			String command, Socket clientSocket) throws UnknownHostException, IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
	}

	public void execute() throws IOException{
		super.execute();
	}

}
