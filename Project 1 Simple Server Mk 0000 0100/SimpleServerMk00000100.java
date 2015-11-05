/*@author Scott Nearing
 * @version 0b00000100
 * This class implements a simple server that listens for a simple command set on port
 * 10000. An object, SocketSet, was defined to combine a ServerSocket and Socket.
 * Methods to open and close the connection are included in this class. A method to
 * parse input from the client and send a reply is also included in this class.
 */

//import java.net.*;
import java.io.*;

/*import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
*/

public class SimpleServerMk00000100 {
	
	/*This main method takes an array of strings, but these arguments are unused. The main method
	 * handles the program's flow through opening, managing, and closing connections. The int close shows
	 * if the client has sent a HELO command or a Quit command.
	 * @param args a String[] of arugments, it is unused
	 * @return void this method returns nothing
	 * @exception IOe1 is the exception thrown when the PrintStream for output or the BufferedReader for input,
	 * can't be instantiated
	 * @exception IOe2 is the exception thrown when a line of input from the client can't be read
	 * @exception IOe3 is the exception thrown when the program fails to interpret input from the client;
	 * typically caused by client process terminating without sending a QUIT command.
	 */

	public static void main(String[] args) {
		
		while(true){
			
			String salutation = "I'm ready to ignore you!\n";
			//String defaultReply = "Message recieved and ignored.\n";
			String message = "Place-Holder";
			
			int portNumber = 10000;
			
			SocketSet setMain;
			
			PrintStream output;
			BufferedReader reader;
			
			int close = -1;
			
			setMain = buildConnection(portNumber);
			
			try{
			output =  new PrintStream(setMain.sock.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(setMain.sock.getInputStream()));
			}//end try
			catch(Exception IOe1){
				System.out.print("Failed to instantiate PrintStream or BufferedReader. Printing stack trace.\n");
				IOe1.printStackTrace();
				tearDownConnection(setMain);
				continue;
			}//end catch
			
			output.println(salutation);
			//System.out.println("Salutation sent.");
			
			while(close != 2){
				try{message = reader.readLine();}//end try
				catch(Exception IOe2){
					System.out.println("Failed to readLine(); printing stacktrace.");
					IOe2.printStackTrace();
					}//end catch
				
				System.out.println(message);
				try{close = interpretAndRespond(output, message, close);}//end try
				catch(Exception IOe3){
					System.out.println("Failed to interpret input; sending error message to client, printing stacktrace and closing connection.");
					output.println("550 Failed to interpret input");
					IOe3.printStackTrace();
					break;
				}//end catch
				
			}//end while close == false
			
			tearDownConnection(setMain);
		
		}//end while true
	}//end main
	
	/*This method takes a int that corresponds to a port number and returns a SocketSet object
	 * @param port an int that corresponds to the desired port number
	 * @return	a SocketSet object built with the passed port number
	 */
	public static SocketSet buildConnection(int port){
		SocketSet setBuild = new SocketSet(port);
		return setBuild;
	}//end buildConnection()
	
	/*This method takes a SocketSet and closes it; nothing is returned by this method
	 * @param setTearDown A SocketSet object that is to be closed
	 * @return	This method returns nothing
	 * @exception eTearDown is the exception thrown when the SocketSet can't can't be closed
	 */
	public static void tearDownConnection(SocketSet setTearDown){
		try{
		setTearDown.sock.close();
		setTearDown.serveSock.close();
		}//end try
		catch(Exception eTearDown){
			System.out.print("Failed to close sockets. Printing stack trace.\n");
			eTearDown.printStackTrace();
		}//end catch
	}//end tearDownConnection()
	
	/*This method takes a PrintStream to output to and a string that corresponds to a line from the client
	 * @param outputIR A PrintStream that output will be sent on
	 * @param messageIR A String that will be parsed; the output will depend on the content of this String
	 * @param status An int: 0 indicates the server should expect a HELO command from the client, 
	 * 1 indicates the server should expect further input from the client, and
	 * 2 indicates that client has sent a command to terminate the connection and no further input is expected
	 * @return	This method returns the current status as an int: 0 indicates the server should expect a HELO command from the client, 
	 * 1 indicates the server should expect further input from the client, and
	 * 2 indicates that client has sent a command to terminate the connection and no further input is expected
	 */
	public static int interpretAndRespond(PrintStream outputIR, String messageIR, int status){
		//String[] parsed = messageIR.split("\\t | \\s+ | \\n");
		String[] parsed = messageIR.split("\\s+");

		
		if(parsed[0].equals("HELO")==true){
			if(parsed.length > 1 == true){
				outputIR.println("220 Please send a command, " + parsed[1]);
				return 1;
			}//end if
			else{outputIR.println("220 please send a command");}//end else
				outputIR.flush();
				return 1;
		}//end if message starts with "HELO"
		
		else if(parsed[0].equals("MINE")==true){
			if(status != 1){
				outputIR.println("416 Aren't you going to say HELO?");
				outputIR.flush();
				return status;
			}//end if status != 1
			else{
				outputIR.println("403 No, it's mine");
				outputIR.flush();
				return 1;
			}
		}//end else if message starts with "MINE"
		
		else if(parsed[0].equals("YOURS")==true){
			if(status != 1){
				outputIR.println("416 Aren't you going to say HELO?");
				outputIR.flush();
				return status;
			}//end if status != 1
			else{
				outputIR.println("203 Of course it's mine");
				outputIR.flush();
				return 1;
			}
		}//end if message starts with "YOURS"
		
		else if(parsed[0].equals("SHARE")==true){
			if(status != 1){
				outputIR.println("416 Aren't you going to say HELO?");
				outputIR.flush();
				return status;
			}//end if status != 1
			else{
				outputIR.println("404 Not on your life");
				outputIR.flush();
				return 1;
			}
		}//end if message starts with "SHARE"
		
		else if(parsed[0].equals("QUIT")==true){
			if(status != 1){
				outputIR.println("416 Aren't you going to say HELO?");
				outputIR.flush();
				return status;
			}//end if status != 1
			else{
				outputIR.println("100 Don't go away mad, just go away");
				outputIR.flush();
				return 2;
			}
		}//end if message starts with QUIT
		
		else{
			outputIR.println("401 unrecognized input");
			outputIR.flush();
			return status;
		}//end else
	}//end interpretAndRespond()
	
	}//end SimpleServerMk00000100