package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Random;


//This a class for the response of the server to a put command
public class PutRespond extends ReceiveDataRespond {
	
	/**
	 * Constructor
	 */
	public PutRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}
	
	/**
	 * Executes the post, this means that it writes the received data to a new local .txt file
	 */
	public void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException, BadRequestException{
		super.execute();
		PrintWriter out = new PrintWriter("put"+".txt");
		out.print(data);
		out.close();
	}
}
