package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	public void execute() throws IOException{
		System.out.println("we are executing a put");
		super.execute();
		System.out.println("we got past the super");
		double randDouble = Math.random()*100;
		int randInt= (int) Math.round(randDouble);
		PrintWriter out = new PrintWriter("put"+randInt+".txt");
		out.print(data);
		out.close();
		System.out.println("we have executed a put");
	}
}
