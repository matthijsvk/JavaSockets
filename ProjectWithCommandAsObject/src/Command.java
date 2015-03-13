import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public abstract class Command {

	protected String shortHost;
	protected String HTTPVersion;
	protected String hostExtension;
	protected String toBeSent;
	protected DataOutputStream outToServer;
	protected BufferedInputStream inFromServer;
	
	public Command(String shortHost,String hostExtension, String HTTPVersion, String command,DataOutputStream outToServer,BufferedInputStream inFromServer){
		this.shortHost = shortHost;
		this.HTTPVersion = HTTPVersion;
		this.hostExtension = hostExtension;
		this.outToServer = outToServer;
		this.inFromServer = inFromServer;
		createDataToBeSent(command);
	}
	
	
	
	public void execute() throws IOException{
		System.out.println(outToServer);
		outToServer.writeBytes(toBeSent);
	}
	
	
	
	
	public void createDataToBeSent(String command){
	if (HTTPVersion.equals("1.0")){
    	System.out.println("1.0 entered");
        toBeSent = command + " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n";
        System.out.println(command +  " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n");
        }
    else if (HTTPVersion.equals("1.1")){
    	System.out.println("1.1 entered");
    toBeSent = command + hostExtension + " HTTP/1.1" + "\r\n" + "host:" + shortHost + "\r\n\r\n";
    }
    else{
    	//throw error
    }
	}
	
	
	
}
