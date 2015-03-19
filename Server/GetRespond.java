package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

//This a class for the response of the server to a get command
public class GetRespond extends SendDataRespond{

	
	/**
	 * Constructor
	 */
	public GetRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}
	
	/**
	 * Executes this command
	 */
	@Override
	public void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException {
		System.out.println("Started sending a get");
		super.execute();
		this.sendEntity();
	}

	/**
	 * Sends the entity part of the request to the client.
	 */
	private void sendEntity() throws IOException {
		byte[] data = Files.readAllBytes(this.path);
		//String str = new String(data, "UTF-8");
		//System.out.println(str);
		outToClient.writeBytes(header);
		outToClient.write(data);
		System.out.println("Done sending");
	}
}
