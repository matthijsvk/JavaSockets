package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Respond {
	
	protected String request;
	protected DataOutputStream outToClient;
	protected BufferedInputStream inFromClient;

	public Respond(String request, DataOutputStream outToClient, BufferedInputStream inFromClient) {
		this.request = request;
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		}

	public abstract void execute() throws IOException;

}
