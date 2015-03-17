package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class PutRespond extends ReceiveDataRespond {

	public PutRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
		// TODO Auto-generated constructor stub
	}
	public void execute() throws IOException{
		super.execute();
		double randDouble = Math.random()*100;
		int randInt= (int) Math.round(randDouble);
		PrintWriter out = new PrintWriter("put"+randInt+".txt");
		out.print(data);
		out.close();
	}
}
