import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import UserNotOnlineException;

/**
 * PortListener is closely attached to ChatComm - is serves as a process that simply
 * listens for incoming data.
 *
 * @version 0.1.4
 * @author John T. Ellis
 */
class PortListener extends Thread {
static int kill, port, update;
static String serverString;
static JFrame textWindow;

/**
 * Create a new port listener to watch for incoming messages.
 *
 * @parm p - The port to listen on
 * @parm refresh - How often to listen (in milliseconds)
 */
public PortListener (int p, int refresh){
	kill = 0;
	serverString = null;
	port = p;
	update = refresh;
}

/**
 * Create a new port listener to watch for incoming messages.
 *
 * @parm p - The port to listen on
 */
public PortListener (int p){
	kill = 0;
	serverString = null;
	port = p;
	update = 1000;
}

/**
 * To string create a string representation of the listening socket.
 */
public String toString(){
	return serverString;
}

/**
 * Open up a window to display incoming messages.
 *
 * @parm text - The incoming text string
 */
public void incomingTextWindow (String text) {
	JTextArea chatText = new JTextArea(5, 20);
	chatText.setLineWrap(true);
	chatText.setWrapStyleWord(true);
	chatText.setEditable(false);
	chatText.setForeground(Color.blue);
	chatText.append(text + "\n");
	JScrollPane chatPane = new JScrollPane(chatText,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	textWindow = new JFrame("Incoming Message");
	textWindow.getContentPane().add(chatPane, BorderLayout.CENTER);
	textWindow.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			textWindow.dispose();
		}
	});

	textWindow.pack();
	textWindow.setVisible(true);
}

public void run(){
	ServerSocket serverPort = null;		
	String inString = null;
	BufferedReader readBuffer = null;

	try{
		while(kill == 0){
			if (null == serverPort) {
				//Open server port
				serverPort = new ServerSocket(port);
				//Accept connections on port
				Socket receive = serverPort.accept();
				//Server string allows us to see settings (for convenience)
				serverString = receive.toString();
				//Start read buffer
				readBuffer = new BufferedReader(new InputStreamReader(receive.getInputStream()));
			}

			//Read data from port.
/*			try{
				sleep(update);
			}catch(InterruptedException except){
				System.out.println("Error sleeping PortListener thread. " + except);
			}
*/
			while((serverPort != null) && ((inString = readBuffer.readLine()) != null)){
				incomingTextWindow(inString);
				try{
					serverPort.close();
					serverPort = null;
				}
				catch(IOException e){
					System.out.println("Error closing server port. " + e);
				}
			}
		}
	}catch(IOException e){
		System.out.println("Error reading from buffer. " + e);
	}

	System.out.println("Killed server thread, received " + kill);
	return;
}
}

/**
 * ChatComm is just a small chat window for collaborating with another co-worker.
 *
 * @version 0.1
 * @author John T. Ellis
 */
public class ChatComm extends JPanel {
static JFrame frame = new JFrame("Project Communicator");
static PortListener server;
static Socket connect;
static JTextField chatField;
static BufferedWriter writeBuffer;

/**
 * Create a new chat window
 * @parm ipAddress - The destination IP address to connect to
 * @parm port - The destination port to connect to
 */
public ChatComm(InetAddress ipAddress, int port) throws UserNotOnlineException {
	connect = openSocket(ipAddress, port);
	if (connect == null)
		throw new UserNotOnlineException();
	else
		//Create writebuffers for data
		try{
			writeBuffer = new BufferedWriter(new OutputStreamWriter(connect.getOutputStream()));
		}
		catch(IOException except){
			System.out.println("IO Exception setting write buffer. " + except);
		}
}

/**
 * Start a new port listener
 *
 * @parm main - The main ChatComm window.
 * @parm port - The port to listen on.
 */
public static void startServer(int port){
	//Run server thread
	server = new PortListener(port);
	server.start();
}

/**
 * Open the client socket
 *
 * @parm ipAddress - The destination IP address.
 * @parm port - The destination port.
 */
public Socket openSocket(InetAddress ipAddress, int port) {
	Socket connect;

	//Try to connect to the server socket
	try{
		connect = new Socket(ipAddress, port);
		System.out.println("Client Socket: " + connect.toString());
		return connect;
	}
	catch(IOException e){
		System.out.println("Could not connect.");
		return null;
	}
	catch(SecurityException e){
		System.out.println("Security manager does not allow opening socket. " + e);
		return null;
	}
}

/**
 * Send data to the write buffer.
 *
 * @parm sendString - String to send
 */
public static void sendText(String sendString){
	try{
		writeBuffer.write(sendString);
		writeBuffer.newLine();
		writeBuffer.flush();
	} catch(IOException except){
		System.out.println("Error writing to buffer. " + except);
	}
}

/**
 * Kill the ChatComm process.
 */
public void destroy() {
	try{
		connect.close();
	}
	catch(IOException e){
		System.out.println("IO Exception closing client socket. " + e);
	}
	try{
		writeBuffer.close();
	}
	catch(IOException e){
		System.out.println("Cannot close write buffer. " + e);
	}
	//Finally, stop the server thread.
	PortListener.kill = 1;
}

/**
 * Although this class is meant to be imported and used within another program,
 * a main method exists to perform a loopback test or a connection to another client.
 *
 * @parm IP address of client to connect to.
 */
public static void main(String[] args) {
	if (args.length > 0){
		//Get destination address, connect to remote client.
		try{
			InetAddress destination = InetAddress.getByName(args[0]);
			System.out.println("Remote IP is: " + destination.toString());
			//Create main window
			startServer(7780);
			frame.getContentPane().add(new ChatComm(destination, 7780));
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
		catch(UserNotOnlineException e) {
			System.out.println("User not online. " + e);
		}
		catch(UnknownHostException e){
			System.out.println("No IP for host can be found. " + e);
		}
		catch(SecurityException e){
			System.out.println("Security manager does not allow getLocalHost. " + e);
		}
	}else{
		//Get the local host address, connect to ourselves
		try{
			InetAddress loopback = InetAddress.getLocalHost();
			System.out.println("Current IP is: " + loopback.toString());
			//Create main window
			startServer(7780);
			frame.getContentPane().add(new ChatComm(loopback, 7780));
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
		catch(UserNotOnlineException e) {
			System.out.println("User not online. " + e);
		}
		catch(UnknownHostException e){
			System.out.println("No IP for host can be found. " + e);
		}
		catch(SecurityException e){
			System.out.println("Security manager does not allow getLocalHost. " + e);
		}
	}

	//Set up interface window
	frame.getContentPane().setLayout(new BorderLayout());

	chatField = new JTextField(10);
	frame.getContentPane().add(chatField, BorderLayout.NORTH);

	//Set up buttons
	JPanel buttonPanel = new JPanel();
	JButton button = new JButton("Send");
	buttonPanel.add(button);
	button.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			//Write data to port
			sendText(chatField.getText());
			chatField.setText("");	
		} 
	}); 
	frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	frame.pack();
	frame.setVisible(true);
}
}