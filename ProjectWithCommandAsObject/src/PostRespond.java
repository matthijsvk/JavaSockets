package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;

public class PostRespond extends ReceiveDataRespond {

	public PostRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
		// TODO Auto-generated constructor stub
	}

}
