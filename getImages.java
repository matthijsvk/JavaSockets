/**
 * 
 */

/**
 * @author r0364010
 *
 */
public class getImages {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
    }
	
	/**
	 * @effect  This method gets an image from a server and saves it locally. 
	 *  
	 * @param imageURL	The url of the image to get from server and save locally
	 * @param shortHost The host from which to get the file
	 * @param port		The port to use for the connection
	 */
	public static void saveImageToFile(String imageURL, String shortHost, int port){
		
		// Create a socket to localhost (this machine, port 6789).
	    Socket clientSocket = new Socket(shortHost, port);

	    // Create outputstream (convenient data writer) to this host. 
	    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	    
	    // Create an inputstream (convenient data reader) to this host
	    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    
		
		// Parse image name 
	    // TODO check if images is server url or local adress (eg /images/image.jpg)
	 	System.out.println("requestedImage: "+ requestedImage);
	 	
	 	// TODO http 1.0 or 1.1
	
	    outToServer.writeBytes("GET "+requestedImage+" HTTP/1.1\r\n");
		outToServer.writeBytes("host: "+shortHost+":80\r\n\r\n");
		outToServer.flush();
		
		OutputStream dos = new FileOutputStream("c:\\"+imageURL+"testtttt.jpg");
		int count;
		byte[] buffer = new byte[4096];
		boolean eohFound = false;		// only get data after header
		while ((count = inFromServer.read(buffer)) != -1)
		{
		    if(!eohFound){
		        String string = new String(buffer, 0, count);
		        int indexOfEOH = string.indexOf("\r\n\r\n");
		        if(indexOfEOH != -1) {
		            count = count-indexOfEOH-4;
		            buffer = string.substring(indexOfEOH+4).getBytes();
		            eohFound = true;
		        } else {
		            count = 0;
		        }
		    }
		  dos.write(buffer, 0, count);
		  dos.flush();
		}
		inFromServer.close();
		dos.close();

	}

}
