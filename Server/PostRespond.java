package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

//This a class for the response of the server to a post command
public class PostRespond extends ReceiveDataRespond {
	
	/**
	 * Constructor
	 */
	public PostRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}
	
	/**
	 * Executes the post, this means that it appends the received data to a local .txt file
	 */
	public void execute() throws IOException, FileNotFoundException, ParseException, NotModifiedSinceException, BadRequestException{
		super.execute();
		String str;
		try{ Path path = Paths.get("posts.txt");
		byte[] data = Files.readAllBytes(path);
		str = new String(data, "UTF-8");
		}catch( IOException exception){str = "";}
		PrintWriter out = new PrintWriter("posts.txt");
		out.print(str);
		System.out.println(data);
		out.print(data);
		out.close();
	}
}
