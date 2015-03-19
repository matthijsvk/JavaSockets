package Server;

//Exception thrown when the requested file is not found on the server
public class FileNotFoundException extends Exception {
	private static final long serialVersionUID = -4775092417022043259L;
	public FileNotFoundException() {}
	      public FileNotFoundException(String message)
	      {super(message);}
	 }

