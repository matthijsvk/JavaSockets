import java.io.BufferedInputStream;
import java.io.DataOutputStream;


public class Head extends RetrieveDataCommand {

	public Head(String shortHost, String hostExtension, String HTTPVersion,
			String command,DataOutputStream outToServer,BufferedInputStream inFromServer) {
		super(shortHost, hostExtension, HTTPVersion, command, outToServer, inFromServer);
	}

}
