package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//This is a class for responses to head and get requests
public class SendDataRespond extends Respond {

	protected String header = "";
	protected Path path;

	/**
	 * Constructor
	 */
	public SendDataRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}

	/**
	 * This executes the request
	 */
	@Override
	public void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException, BadRequestException {
		super.execute();
		this.sendHeader();
	}
	
	/**
	 * Scans the requested file and makes a header for it, then sends the header to the client
	 */
	private void sendHeader() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException {
		byte[] data;
		String pathAsString = this.getPath();
		path = Paths.get(pathAsString);
		try {data = Files.readAllBytes(this.path);}
		catch (IOException didNotFindFile){throw new FileNotFoundException();}
		this.checkIfModified();
		Date date = new Date();
		// TODO: status updates moeten nog goed 
		header += "HTTP/1.1 200 OK\r\n";
		// TODO: geen idee of die datum ok is
		header += "Date: " + date.toString() + "\r\n";
		String extension = getExtensionFromPath(pathAsString);
		header += "Content-Type: ";
		if(extension.equals("html") || extension.equals("htm") || extension.equals("txt")){
			header += "text/" + extension + "\r\n";
		}
		else{
			header += "image/" + extension + "\r\n";
		}
		if(this.httpVersion.equals("1.0")){header += "Connection: close" + "\r\n";}
		header += "Content-Length: " + Integer.toString(data.length) + "\r\n\r\n";
		System.out.println(header);
	}


	/**
	 * Checks if the client has an outdated version of the requested file,
	 * throws a NotModifiedException if not.
	 */
	private void checkIfModified() throws ParseException, NotModifiedSinceException {
		System.out.println("checking date now");
		int counter = 0;
		while(this.request[counter].length() > 5  && !this.request[counter].substring(0, 7).equals("If-Modi")){
			counter += 1;
		}
		if(!this.request[counter].equals("\r\n")){
			String dateAsString = this.request[counter].substring(19);
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date result = dateFormat.parse(dateAsString);
			System.out.println(result.getTime());
			System.out.println(this.path.toFile().lastModified());
			if(result.getTime() > this.path.toFile().lastModified()){
				throw new NotModifiedSinceException();
			}
		}
	}
	
	/**
	 * Gets the path to the requested file from the requested URL
	 */
	private String getExtensionFromPath(String pathAsString) {
		int counter = pathAsString.length();
		while(!pathAsString.substring(counter-1, counter).equals(".")){
			counter += -1;
		}
		return pathAsString.substring(counter);
	}

	/**
	 * this function returns the path to a file. If the file is a website (www.test.com), is is stored in test/test.html
	 * 											 If the file is a normal file (www.test.com/dir/file), it is stored in test/dir/file
	 */
	public String getPath(){
		String path = null;
		String hostDirName = this.shortHost.replace("www.","");int puntIndex=hostDirName.indexOf(".");hostDirName = hostDirName.substring(0, puntIndex);

		if (hostExtension.equals("/")){							//if you're requesting 'www.test.com' or 'www.test.com/',
			path = hostDirName+ "/" + hostDirName + ".html";	// it is stored under 'test/test.html'
		}
		else{
			if (hostExtension.indexOf("/") == 0)	//for normal files
				path = hostDirName + hostExtension;
			else
				path= hostDirName + "/" + "somethingWentWrong.error";
		}
		return path;
	}

}
