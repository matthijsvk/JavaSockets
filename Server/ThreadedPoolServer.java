/**
 * 
 */
package Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadedPoolServer implements Runnable{
	public ThreadedPoolServer(int port){
        this.serverPort = port;
    }
    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool =  Executors.newFixedThreadPool(10);		//thread pool contains 10 threads

    /**Start a server and keep it running for a while, then shut it down
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadedPoolServer server = new ThreadedPoolServer(9000);
		new Thread(server).start();	//this starts a new thread which executes the run() method in this class. 

		try {
		    Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();	//stop the while loop and finish all existing threads, refusing new connections
	}
    
    /**
     * This method is executed when server (and thus the main method) is started.
     */
    public void run(){
        synchronized(this){this.runningThread = Thread.currentThread();}	//something to synchronize the threads
        try { 
        	this.serverSocket = new ServerSocket(this.serverPort);			//create a new socket on the port to listen for new Clients
        } catch (IOException e) {throw new RuntimeException("Cannot open port 8080", e);}
        
        while(! isStopped()){
            Socket clientSocket = null;
            try {clientSocket = this.serverSocket.accept();} 				// listen for Clients
            catch (IOException e) {throw new RuntimeException("Error accepting client connection", e);}
            
            this.threadPool.execute(		// start a new thread from the threadPool for the Client on the accept-socket. If the threadpool is empty, the Client will have to wait.
                new WorkerRunnable(clientSocket,"The best server in the world, by Harald en Matthijs"));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } 
        catch (IOException e) {throw new RuntimeException("Error closing server", e); }
    }

    
    /**
     * This class implements the
     * @author M
     *
     */
    public class WorkerRunnable implements Runnable{

        protected Socket clientSocket = null;
        protected String serverText   = null;
        private String line,input;

        public WorkerRunnable(Socket clientSocket, String serverText) {
            this.clientSocket = clientSocket;
            this.serverText   = serverText;
        }

        /**
         * This method is executed for each client when they receive a thread, and this method does the same as the single-threaded server would normally do.
         */
        public void run() {
            try {
                
                DataInputStream inFromClient = new DataInputStream (clientSocket.getInputStream());
    			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

    			while((line = inFromClient.readLine()) != null && !line.equals(".")) {
    				input=input + line;
    				outToClient.writeBytes("I got:" + line);
    			}

    			// Now write to the client
    			System.out.println("Overall message is:" + input);
    			outToClient.writeBytes("Overall message is:" + input);

    			clientSocket.close();
    			
            } catch (IOException e) {
                //report exception somewhere.
                e.printStackTrace();
            }
        }
    }
}
