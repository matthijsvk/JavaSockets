import java.io.File;
import java.io.IOException;


public class test2 {

	public static void main(String[] args) throws IOException {
		String imageURL = "www.javatpoint.com/images/imageFiles/moreImageFolders/imageOne.jpg";
		String imagePath = imageURL.replace("www.javatpoint.com","");
		
		// get the name of the image itself by removing the directory names
		String shorter = imagePath.substring(imagePath.indexOf("/"), imagePath.length());
		while (shorter.contains("/")){
			shorter= shorter.substring(shorter.indexOf("/")+1, shorter.length());
		}
		
		// get the directory name = url - imageName
		String dirNames = imagePath.replace(shorter,"");
		
		//remove the leading and trailing slashes
		dirNames = dirNames.substring(1, dirNames.length()-1); 
		
		//System.out.println(shorter);
		//System.out.println("DIRNAMES: "+ dirNames);
		File dir = new File("./" + dirNames);
	     
	    // create directory structure
	    boolean successful = dir.mkdirs();
	    if (successful)
	      System.out.println("directories were created successfully");
	    else
	      System.out.println("failed trying to create the directories");
		
	    // create the file to which received data will be written
	    imagePath = imagePath.substring(1, imagePath.length());
	    System.out.println(imagePath);
	    File file = new File(imagePath); 	//TODO put here a proper name (with folder slashes)
	    
	    successful = file.createNewFile();
	    if (successful)
	      System.out.println("file was created successfully");
	    else
	      System.out.println("failed trying to create the file");

	}

}
