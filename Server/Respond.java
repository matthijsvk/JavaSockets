package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

public abstract class Respond {
	
	protected String[] request;
	protected DataOutputStream outToClient;
	protected BufferedInputStream inFromClient;
	protected String command;
	protected String host;
	protected String httpVersion;
	protected String shortHost;
	protected String hostExtension;
	protected int port;

	public Respond(String[] request, DataOutputStream outToClient, BufferedInputStream inFromClient, int port) throws IOException {
		this.request = request;
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.parseRequest();
		this.port = port;
		this.checkForHostLineIf11();
		}
	
	//TODO: bij alles eigenlijk is geen rekening gehouden met 1.1 of 1.0
	private void checkForHostLineIf11() throws IOException {
		//if(httpVersion.equals("1.1") && !request[1].equals("Host: localhost"+"\r\n")){
		//		System.out.println("ERROR: HTTP 1.1 requested and no host line extension!");
		//	}
		//TODO dit is uiteraard niet ok
		}

	
	
	private void parseRequest() {
		int counter = 0;
		while(!request[0].substring(counter, counter+1).equals(" ")){
			counter +=1;
		}
		command = request[0].substring(0, counter);
		String remainingPartOfRequest = request[0].substring(counter +1);
		counter = 0;
		while(!remainingPartOfRequest.substring(counter, counter+1).equals(" ")){
			counter +=1;
		}
		host = remainingPartOfRequest.substring(0, counter);
		remainingPartOfRequest = remainingPartOfRequest.substring(counter +1);
		httpVersion = remainingPartOfRequest.substring(5, 8);
		System.out.println(httpVersion);
		
		
		
		
		host = host.replace("http://","");	//filter out http:// chars if they exist
		if ((host.length() > 4) && (host.substring(0, 4).equals("www."))){
		if (host.contains("/")) {
			shortHost = host.substring(0,host.indexOf("/"));
			hostExtension = host.substring(host.indexOf("/"), host.length());
		}
		else{
			shortHost = host;
			hostExtension = "/";
		}
		}
		else{
			shortHost= "www.linux-ip.net";
			hostExtension= host;
		}
	}

	public abstract void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException;

}
