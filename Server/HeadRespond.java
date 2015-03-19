package Server;

//This a class for the response of the server to a get command
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HeadRespond extends SendDataRespond {
	
	/**
	 * Constructor
	 */
	public HeadRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}

}
