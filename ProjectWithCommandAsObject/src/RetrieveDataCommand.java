

public class RetrieveDataCommand extends Command {
	
private String extension;
	
	
public RetrieveDataCommand(String shortHost,String hostExtension, String HTTPVersion, String command) {
	super(shortHost,hostExtension,HTTPVersion, command);
	
	int placeInUrl = hostExtension.length() -1;
	extension = ".html";
	while (hostExtension.charAt(placeInUrl) != ("/").charAt(0) && extension == ".html"){
		if (hostExtension.charAt(placeInUrl) == (".").charAt(0)){
			extension = hostExtension.substring(placeInUrl);
		}
		placeInUrl = placeInUrl -1;
	}
}

}
