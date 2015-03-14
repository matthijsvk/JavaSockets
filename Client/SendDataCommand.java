package Client;

import java.io.IOException;
import java.util.Scanner;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class SendDataCommand extends Command {
	protected String fileToBeSent;

	public SendDataCommand(String shortHost, String hostExtension,
			String HTTPVersion, String command, Socket clientSocket) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, clientSocket);
		
		System.out.println("Enter something here : ");
		 
		
	       Scanner scanIn = new Scanner(System.in);
	       fileToBeSent = scanIn.nextLine();
	       scanIn.close();

	       Path path = Paths.get(fileToBeSent);
	       byte[] data = Files.readAllBytes(path);
	       outToServer.write(data);

		}

}
