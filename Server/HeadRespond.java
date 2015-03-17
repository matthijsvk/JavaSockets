package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HeadRespond extends SendDataRespond {

	public HeadRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
		outToClient.writeBytes(header);
	}

}
