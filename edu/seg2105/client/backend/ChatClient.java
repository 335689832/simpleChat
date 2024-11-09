// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginID) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      if(message.startsWith("#")) {
    	  if(message.startsWith("#quit")) {
    		  quit();
    	  }
    	  else if(message.startsWith("#logoff")) {
    		  try {
    			  closeConnection();
    			  connectionClosed();
    		  } catch(IOException e) {}
    	  }
    	  else if(message.startsWith("#sethost")) {
    		  String[] s = message.split(" ");
    		  if(isConnected() == false) {
    			  setHost(s[1]);
    			  clientUI.display("Host set to " + s[1]);
    		  }else {
    			  clientUI.display("Error: You're already logged in.");
    		  }
    	  }
    	  else if(message.startsWith("#setport")) {
    		  String[] s = message.split(" ");
    		  if(isConnected() == false) {
    			  setPort(Integer.parseInt(s[1]));
    			  clientUI.display("Port set to " + s[1]);
    		  }else {
    			  clientUI.display("Error: You're already logged in.");
    		  }
    	  }
    	  else if(message.startsWith("#login")) {
    		  if(isConnected() == false) {
    			  try {
    				  openConnection();
    			  }catch(IOException e) {}
    		  }else {
    			  clientUI.display("Error: You're already logged in.");
    		  }
    	  }
    	  else if(message.startsWith("#gethost")) {
    		  clientUI.display(getHost());
    	  }
    	  else if(message.startsWith("#getport")) {
    		  clientUI.display((Integer.toString(getPort())));
    	  }
      }else {
    	  sendToServer(message);
      }
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
      connectionClosed();
    }
    catch(IOException e) {}
    System.exit(0);
  }

	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	@Override
	protected void connectionClosed() {
		clientUI.display("Connection has been closed");
		
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	@Override
	protected void connectionException(Exception exception) {
		quit();
	}

	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	protected void connectionEstablished() {
		
	}


}
//End of ChatClient class
