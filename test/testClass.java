package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class testClass {

	public static void main(String[] args) throws IOException {
		System.out.println("Please enter the Header: ");
		 
//	       Scanner scanIn = new Scanner(System.in);
//	       String fileToBeSent = scanIn.nextLine();
//	       //scanIn.close();
//
//	       Path path = Paths.get(fileToBeSent);		//read the file where the input was saved
//	       System.out.println(path);
//	       System.out.println(path.toString());
	       
//	       byte[] data = Files.readAllBytes(path);
//	       System.out.println(data);
	       
	       String message="";
	       char nextChar;
	       String lastFour = "    ";
	       while (!lastFour.equals("\r\n\r\n")){
	    	   nextChar = (char)System.in.read();
	    	   message = message + nextChar;
	    	   lastFour = lastFour.substring(1, lastFour.length()) + nextChar; //shift one and add new character   
	       }
	       
	       System.out.println("Header received. Now please enter your data/text:");
	       lastFour= "    ";
	       while (!lastFour.equals("\r\n\r\n")){
	    	   nextChar = (char)System.in.read();
	    	   message = message + nextChar;
	    	   lastFour = lastFour.substring(1, lastFour.length()) + nextChar; //shift one and add new character   
	       }
	       
	       System.out.println("Message received. Sending to server...");
	       System.out.println(message);
	       System.out.println("----------------------------------");
	}

}
