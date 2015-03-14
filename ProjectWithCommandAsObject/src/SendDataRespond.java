package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SendDataRespond extends Respond {

	public SendDataRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
	}

	@Override
	public void execute() throws IOException {
		this.sendHeader();
	}

	private void sendHeader() throws IOException {
			Path path = Paths.get("header.txt");
			byte[] data = Files.readAllBytes(path);
			outToClient.write(data);
	}

}
