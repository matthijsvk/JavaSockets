package ProjectWithCommandAsObject.src;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetRespond extends SendDataRespond{

	public GetRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient) {
		super(request, outToClient, inFromClient);
	}
	
	@Override
	public void execute() throws IOException {
		super.execute();
		this.sendEntity();
	}

	private void sendEntity() throws IOException {
		Path path = Paths.get("homepage.html");
		byte[] data = Files.readAllBytes(path);
		outToClient.write(data);
	}

}
