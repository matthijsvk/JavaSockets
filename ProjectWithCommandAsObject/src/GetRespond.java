package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;

public class GetRespond extends SendDataRespond{

	public GetRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
	}

}
