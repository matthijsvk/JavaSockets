package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReceiveDataRespond extends Respond {

	public ReceiveDataRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws IOException {
		// TODO Auto-generated method stub

	}

}
