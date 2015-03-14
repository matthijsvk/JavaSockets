package test;

public class testClass {

	public static void main(String[] args) {
		String str = "images/dir/dir2/image.gif";
		System.out.println(getExtensionFromPath(str));
	}
	
	public static String getExtensionFromPath(String path){
		// get file EXTENSION (loop in reverse over chars until '.' found
		int placeInUrl = path.length() -1;
		String extension = ".html";
		while (path.charAt(placeInUrl) != ("/").charAt(0) && extension == ".html"){
			if (path.charAt(placeInUrl) == (".").charAt(0)){
				extension = path.substring(placeInUrl);
			}
			placeInUrl = placeInUrl -1;
		}
		
		return extension;
		}

}
