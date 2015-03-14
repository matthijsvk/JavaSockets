package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;


public class Post extends Command {

	public Post(String shortHost, String hostExtension, String HTTPVersion,
			String command, DataOutputStream outToServer,
			BufferedInputStream inFromServer) {
		super(shortHost, hostExtension, HTTPVersion, command, outToServer, inFromServer);
	}


}
