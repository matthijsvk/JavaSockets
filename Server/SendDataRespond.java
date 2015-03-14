package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class SendDataRespond extends Respond {

	protected String header = "";
	protected Path path;



	public SendDataRespond(String request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}

	@Override
	public void execute() throws IOException {
		this.sendHeader();
	}

	private void sendHeader() throws IOException {
			String pathAsString = this.getPath();
			path = Paths.get(pathAsString);
			byte[] data = Files.readAllBytes(path);
			Date date = new Date();
			// TODO: status updates moeten nog goed 
			header += "HTTP/1.1 200 Found";
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
			header += "Content-Length: " + Integer.toString(data.length) + "\r\n\r\n";
			outToClient.writeBytes(header);
	}
	
	
	
	private String getExtensionFromPath(String pathAsString) {
		int counter = pathAsString.length();
		while(pathAsString.substring(counter-1, counter).equals(".")){
			counter = -1;
		}
		return pathAsString.substring(counter);
	}

	/**
	 * this function returns the path to a file. If the file is a website (www.test.com), is is stored in test/test.html
	 * 											 If the file is a normal file (www.test.com/dir/file), it is stored in test/dir/file
	 * @param shortHost
	 * @param hostExtension
	 * @return
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
