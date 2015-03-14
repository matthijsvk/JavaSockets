package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
	       
		File fileToChange = new File("C:/myfile.txt");

	    Date filetime = new Date(fileToChange.lastModified());
	    System.out.println(filetime.toString());

	    fileToChange.setLastModified(System.currentTimeMillis());

	    filetime = new Date(fileToChange.lastModified());
	    System.out.println(filetime.toString());
	    
	}

}
