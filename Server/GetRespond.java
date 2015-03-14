package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetRespond extends SendDataRespond{

	public GetRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}
	

	@Override
	public void execute() throws IOException {
		System.out.println("Started sending a get");
		try{
		super.execute();
		this.sendEntity();
		} catch (IOException didNotFindFile){
			System.out.println("ERROR 404 NOT FOUND");
			String headerForClient = "HTTP/1.1 404 NOT FOUND\r\n"+"Content-Type: text/html\r\n"+"Content-Length: 23" + "\r\n\r\n" + "<html> not found <html>";
			System.out.println(headerForClient);
			outToClient.writeBytes(headerForClient);
		}
	}

	private void sendEntity() throws IOException {
		byte[] data = Files.readAllBytes(this.path);
		//String str = new String(data, "UTF-8");
		//System.out.println(str);
		outToClient.write(data);
		System.out.println("Done sending");
	}

}
