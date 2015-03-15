package Server;

public class FileNotFoundException extends Exception {
	
	private static final long serialVersionUID = -4775092417022043259L;
	//Parameterless Constructor
	public FileNotFoundException() {}

	      //Constructor that accepts a message
	      public FileNotFoundException(String message)
	      {
	         super(message);
	      }
	 }

