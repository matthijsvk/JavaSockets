package ProjectWithCommandAsObject.src;

import java.io.IOException;
import java.net.Socket;


public class Post extends SendDataCommand {

	public Post(String shortHost, String hostExtension, String HTTPVersion,
			String command, Socket clientSocket) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
	}
}
