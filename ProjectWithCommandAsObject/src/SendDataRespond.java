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
			Path path = Paths.get("server","header.txt");
			byte[] data = Files.readAllBytes(path);
			String str = new String(data, "UTF-8");
			System.out.println(str);
			outToClient.write(data);
	}

}
