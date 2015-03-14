package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class SendDataCommand extends Command {
	protected String fileToBeSent;

	public SendDataCommand(String shortHost, String hostExtension,
			String HTTPVersion, String command, DataOutputStream outToServer,
			BufferedInputStream inFromServer) throws IOException {
		super(shortHost, hostExtension, HTTPVersion, command, outToServer, inFromServer);
		
		System.out.println("Enter something here : ");
		 
		
	       Scanner scanIn = new Scanner(System.in);
	       fileToBeSent = scanIn.nextLine();
	       scanIn.close();

	       Path path = Paths.get(fileToBeSent);
	       byte[] data = Files.readAllBytes(path);
	       outToServer.write(data);

		}

}
