package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public abstract class Command {

	protected String shortHost;
	protected String HTTPVersion;
	protected String hostExtension;
	protected String toBeSent;
	protected DataOutputStream outToServer;
	protected BufferedInputStream inFromServer;
	
	protected Socket clientSocket;

	public Command(String shortHost,String hostExtension, String HTTPVersion, String command, Socket clientSocket) throws UnknownHostException, IOException{
		this.shortHost = shortHost;
		this.HTTPVersion = HTTPVersion;
		this.hostExtension = hostExtension;
		createDataToBeSent(command);
		
		this.clientSocket = new Socket(shortHost, 80);//clientSocket; //  TODO when you make a new Socket here, it works, but like HTTP 1.0 (new connection for each file, Slow Start,...)
																	  // if you make a new socket here, you have to call terminate() at the end of GET.execute()
		// Create new outputstream (convenient data writer) to this host. 
		DataOutputStream outToServer = new DataOutputStream(this.clientSocket.getOutputStream());

		// Create an new inputstream (convenient data reader) to this host
		BufferedInputStream inFromServer = new BufferedInputStream(this.clientSocket.getInputStream());
		
		this.outToServer = outToServer;
		this.inFromServer = inFromServer;
	}

	public void execute() throws IOException{
		outToServer.writeBytes(toBeSent);
	}

	public void createDataToBeSent(String command){
		if (HTTPVersion.equals("1.0")){
			toBeSent = command + " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n";
			System.out.println(command +  " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n");
		}
		else if (HTTPVersion.equals("1.1")){
			toBeSent = command + " " + hostExtension + " HTTP/1.1" + "\r\n" + "host:" + shortHost + "\r\n\r\n";
		}
		else{
			//throw error
		}
		
		System.out.println("TO BE SENT: "+toBeSent);
		System.out.println("END TO BE SENT");
	}
	
	public void terminate() throws IOException{
		this.clientSocket.close();
	}

}