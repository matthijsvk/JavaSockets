package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;

public class PutRespond extends ReceiveDataRespond {

	public PutRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
		// TODO Auto-generated constructor stub
	}

}
