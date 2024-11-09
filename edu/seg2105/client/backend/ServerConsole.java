package edu.seg2105.client.backend;

import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.ui.ClientConsole;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF{
	
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 
	  
	  EchoServer server;
	  
	  
	  public ServerConsole(EchoServer server) {
		  this.server = server;
		  fromConsole = new Scanner(System.in); 
	  }

	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        handleServerMessage(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	  
	  private void handleServerMessage(String message) {
		  try {
		      if(message.startsWith("#")) {
		    	  if(message.startsWith("#quit")) {
		    		  server.close();
		    		  System.exit(0);
		    	  } else if(message.startsWith("#stop")) {
		    		  server.stopListening();
		    	  } else if(message.startsWith("#close")) {
		    		  server.stopListening();
		    		  server.close();
		    	  } else if(message.startsWith("#setport")) {
		    		  String[] s = message.split(" ");
		    		  if(!server.isListening() && server.getNumberOfClients() == 0) {
		    			  server.setPort(Integer.parseInt(s[1]));
		    			  System.out.println("Port set to " + s[1]);
		    		  }else {
		    			  System.out.println("Error: You're already logged in.");
		    		  }
		    	  } else if(message.startsWith("#start")) {
		    		  if(server.isListening() == false) {
		    			  server.listen();
		    		  } else {
		    			  System.out.println("Error: Server is already listening.");
		    		  }
		    	  } else if(message.startsWith("#getport")) {
		    		  System.out.println(server.getPort());
		    	  }
		      }else {
		    	  System.out.println("Message received from server console: " + message);
		    	  server.sendToAllClients("SERVER MSG>");
		    	  server.sendToAllClients(message);
		      }
		  } catch(IOException e) {
			  System.out.println("Encountered error: " + e.getMessage() + ". Exiting system.");
		  }
	  }

	 
}
