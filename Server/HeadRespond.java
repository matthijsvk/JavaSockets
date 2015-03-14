package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;

public class HeadRespond extends ReceiveDataRespond {

	public HeadRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
		// TODO Auto-generated constructor stub
	}

}
