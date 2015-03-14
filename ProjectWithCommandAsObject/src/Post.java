package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Post extends SendDataCommand {

	public Post(String shortHost, String hostExtension, String HTTPVersion,
			String command, DataOutputStream outToServer,
			BufferedInputStream inFromServer) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, outToServer, inFromServer);
	}


}
