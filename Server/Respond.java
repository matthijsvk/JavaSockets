package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

//Main class for all the responses to commands sent by the client
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

	/**
	 * Constructor, which also parses the request to be more usable for the server
	 */
	public Respond(String[] request, DataOutputStream outToClient, BufferedInputStream inFromClient, int port) throws IOException {
		this.request = request;
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.parseRequest();
		this.port = port;
	}

	/**
	 * Checks if request is conform to HTTP 1.1 syntax, throws error if this is not the case
	 * @throws BadRequestException 
	 */
	//TODO: bij alles eigenlijk is geen rekening gehouden met 1.1 of 1.0
	private void checkForHostLineIf11() throws IOException, BadRequestException {
		if(httpVersion.equals("1.1") && !request[1].equals("Host: localhost"+"\r\n")){
			System.out.println("ERROR: HTTP 1.1 requested and no host header line!");
			throw new BadRequestException();
		}
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

	public void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException, BadRequestException{
		this.checkForHostLineIf11();
	}

}
