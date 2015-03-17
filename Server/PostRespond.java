<<<<<<< HEAD
package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sun.security.tools.PathList;

public class PostRespond extends ReceiveDataRespond {

	public PostRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
	}
	
	public void execute() throws IOException{
		super.execute();
		String str;
		try{ Path path = Paths.get("posts.txt");
		byte[] data = Files.readAllBytes(path);
		str = new String(data, "UTF-8");
		}catch( IOException exception){str = "";}
		PrintWriter out = new PrintWriter("posts.txt");
		out.print(str);
		out.print(data);
		out.close();
	}
}
=======
package Server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PostRespond extends ReceiveDataRespond {

	public PostRespond(String[] request, DataOutputStream outToClient,
			BufferedInputStream inFromClient, int port) throws IOException {
		super(request, outToClient, inFromClient, port);
		// TODO Auto-generated constructor stub
	}

}
>>>>>>> branch 'master' of https://github.com/matthijsvk/JavaSockets
