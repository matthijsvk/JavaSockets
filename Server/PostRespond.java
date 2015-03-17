package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PostRespond extends ReceiveDataRespond {

	public PostRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
		// TODO Auto-generated constructor stub
	}

}
