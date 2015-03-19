/**
 * 
 */
package Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Main class which starts the server and calls threads which open connections to clients
public class ThreadedPoolServer implements Runnable{
	public ThreadedPoolServer(int port){
		this.serverPort = port;
	}
	protected static int   serverPort   = 6071;
	protected ServerSocket serverSocket = null;
	protected boolean      isStopped    = false;
	protected Thread       runningThread= null;
	protected ExecutorService threadPool =  Executors.newFixedThreadPool(100);		//thread pool contains 10 threads

	
	/**
	 * Start a server and keep it running for a while, then shut it down
	 */
	public static void main(String[] args) {
		ThreadedPoolServer server = new ThreadedPoolServer(serverPort);
		new Thread(server).start();	//this starts a new thread which executes the run() method in this class. 
	}

	
	/**
	 * This method is executed when server thread (and thus the main method) is started.
	 */
	@Override
	public void run(){
		synchronized(this){this.runningThread = Thread.currentThread();}	//synchronize the threads
		try { 
			this.serverSocket = new ServerSocket(this.serverPort);		//create a new socket on the port to listen for new Clients
		} catch (IOException e) {throw new RuntimeException("Cannot open port"+serverPort, e);}

		while(! isStopped()){
			Socket clientSocket = null;
			try {clientSocket = this.serverSocket.accept();} 				// listen for Clients
			catch (IOException e) {throw new RuntimeException("Error accepting client connection", e);}

			this.threadPool.execute(		// start a new thread from the threadPool for the Client on the accept-socket. If the threadpool is empty, the Client will have to wait.
					new WorkerRunnable(clientSocket,"The best server in the world, by Harald en Matthijs", serverPort));
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


	
}
