
public class Command {

	private String shortHost;
	private String HTTPVersion;
	private String hostExtension;
	private String query;
	
	public Command(String shortHost,String hostExtension, String HTTPVersion, String command){
		this.shortHost = shortHost;
		this.HTTPVersion = HTTPVersion;
		this.hostExtension = hostExtension;
		
		if (HTTPVersion.equals("1.0")){
	    	System.out.println("1.0 entered");
	        query = command + " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n";
	        System.out.println(command +  " "  + hostExtension + " HTTP/1.0" + "\r\n\r\n");
	        }
	    else if (HTTPVersion.equals("1.1")){
	    	System.out.println("1.1 entered");
	    query = command + hostExtension + " HTTP/1.1" + "\r\n" + "host:" + shortHost + "\r\n\r\n";
	    }
	    else{
	    	//throw error
	    }
	}
}
