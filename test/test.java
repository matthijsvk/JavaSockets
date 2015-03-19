package test;

import java.io.IOException;

public class test {
public static void main(String[] args) throws IOException {
	System.out.println("Enter the Header: ");
	 
    
    
    String dataToBeSentHeader = ""; String dataToBeSentBody = "";
    char nextChar;
    String lastFour = "  "
    		+ "";
    while (!lastFour.equals("\n\r")){
 	   nextChar = (char)System.in.read();
 	   dataToBeSentHeader = dataToBeSentHeader + nextChar;
 	   lastFour = lastFour.substring(1, lastFour.length()) + nextChar; //shift one and add new character  
 	   System.out.println((int)nextChar);
    }
    
    System.out.println("Header received. Now please enter your data/text:");
}
	

}
