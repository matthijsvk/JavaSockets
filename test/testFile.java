package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class testFile {

  public static void main(String[] args) throws Exception {
    //File fileToChange = new File("C:/mtsayfile.txt");
    
	  //get working directory
    System.out.println(System.getProperty("user.dir"));
    
    // get path of file relative to working dir and append to working dir using filesystem-specific separator
    String shortHost = "www.tcpipguide.com";
	String hostDirName = shortHost.replace("www.","");int puntIndex=hostDirName.indexOf(".");hostDirName = hostDirName.substring(0, puntIndex);
    String path = System.getProperty("user.dir") + System.getProperty("file.separator") + hostDirName;
    
    File fileToChange = new File(path);
    
    System.out.println(fileToChange.exists());

    Date filetime = new Date(fileToChange.lastModified());
    
    
    // format the time to HTTP standard time
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
         
    System.out.println(dateFormat.format(filetime));

    System.out.println(fileToChange.setLastModified(System.currentTimeMillis()));

    filetime = new Date(fileToChange.lastModified());
    System.out.println(filetime.toString());

  }
  
}
