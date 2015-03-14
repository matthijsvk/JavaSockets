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
		Path path = Paths.get("server","homepage.html");
		byte[] data = Files.readAllBytes(path);
		String str = new String(data, "UTF-8");
		System.out.println(str);
		outToClient.write(data);
		System.out.println("Done sending");
	}

}
