/*@author Scott Nearing
 *@version 0b00000100
 *This file is unchanged from version 0b00000011
 *This class defines a SocketSet object; this object combines a ServerSocket and a Socket into a 
 *single object so that it may be returned by methods.
 * 
 */

import java.net.*;
//import java.io.*;

public class SocketSet {
/*This field defines serveSock as a ServerSocket
 */
public ServerSocket serveSock;
/*This field defines a sock as a Socket
 */
public Socket		sock;

/*This constructor attempts to open a socket on a passed port number and creates a 
 *SocketSet object from the ServerSocket and Socket.
 *@param portNum is an int port number
 *@exception eSocketSet is the exception thrown when the SocketSet can't be created
 */
public SocketSet(int portNum){
	try{
	serveSock = new ServerSocket(portNum); 
	sock = serveSock.accept();
	}//end try
	catch(Exception eSocketSet){
		System.out.print("Connection failed. Printing stack trace.\n");
		eSocketSet.printStackTrace();
	}//end catch
}//end constructor
}//end SocketSet
