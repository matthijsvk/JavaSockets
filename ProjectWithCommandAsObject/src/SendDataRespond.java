package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;

public class SendDataRespond extends Respond {

	public SendDataRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
	}

}
