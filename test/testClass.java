package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;


public class testClass {

	public static void main(String[] args) throws IOException {
//		System.out.println("Please enter the Header: ");
		 
//	       Scanner scanIn = new Scanner(System.in);
//	       String fileToBeSent = scanIn.nextLine();
//	       //scanIn.close();
//
//	       Path path = Paths.get(fileToBeSent);		//read the file where the input was saved
//	       System.out.println(path);
//	       System.out.println(path.toString());
	       
//	       byte[] data = Files.readAllBytes(path);
//	       System.out.println(data);
	       
//		File fileToChange = new File("C:/myfile.txt");
//
//	    Date filetime = new Date(fileToChange.lastModified());
//	    System.out.println(filetime.toString());
//
//	    fileToChange.setLastModified(System.currentTimeMillis());
//
//	    filetime = new Date(fileToChange.lastModified());
//	    System.out.println(filetime.toString());
//	    
//	     System.out.println(getCurrentTime());
//	     
//	     System.out.println(System.currentTimeMillis()+" "+System.currentTimeMillis()/1000);
	     
	     Path path = Paths.get(System.getProperty("user.dir")+System.getProperty("file.separator")+"Server/"+"trollface.txt");
		 byte[] data = Files.readAllBytes(path);
		 String str = new String(data, "UTF-8");
		 System.out.println(str);
	    
	}
	
//	private static String getCurrentTime(){
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
//        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//        return dateFormat.format(new Date());
//	}
        

}
