import java.util.*;
import java.text.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import ChatComm;
import UserNotOnlineException;

/**
 * ListComm keeps track of other co-workers
 *
 * @version 0.1.4
 * @author John T. Ellis
 */
public class ListComm extends JPanel{
public static String[] coworkers, nicks, machines;
static JList coworkersList;
static JFrame frame = new JFrame("Co-Worker List");
static int chatPort;
JTextField nickField, machineField;
JTextArea chatField;
String nick, machine;
JFrame editFrame, chatWindow;
int index;

/**
 * Create a new ListComm window.
 */
public ListComm(int port){
	chatPort = port;
	ChatComm.startServer(chatPort);

	JButton button;
	int width=30;
	setLayout(new BorderLayout());

	//Try to get preferences file
	File prefs = new File("ListComm.def");
	if (!prefs.exists()) createPrefs();
	readPrefs(width);

	//Build tool bar buttons
	JToolBar toolBar = new JToolBar();
	add(toolBar, BorderLayout.NORTH);

	button = new JButton("C");
	button.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			sendMessage();
		} 
	}); 
	toolBar.add(button);

	button = new JButton("E");
	button.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			editWindow();
		} 
	}); 
	toolBar.add(button);

	button = new JButton("N");
	button.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			newProj();
		} 
	}); 
	toolBar.add(button);

	button = new JButton("D");
	button.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			delProj();
		} 
	}); 
	toolBar.add(button);

	//Build list of coworkers
	if(coworkers == null)
		coworkersList = new JList();
	else
		coworkersList = new JList(coworkers);
	coworkersList.setFixedCellWidth(width);
	coworkersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	JScrollPane coworkersPane = new JScrollPane(coworkersList,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	add(coworkersPane);
}

/**
 * Create edit coworkers window.
 */
public void editWindow(){
	editWindow(coworkersList.getSelectedIndex());
}

/**
 * Create edit coworker window.
 * @parm Index into the coworker list
 */
public void editWindow(int i){
	index = i;

	//Set up the edit coworker window.
	nickField = new JTextField(nicks[index]);
	machineField = new JTextField(machines[index]);
	editFrame = new JFrame("Edit CoWorker");

	JPanel editPanel = new JPanel();
	editPanel.setLayout(new GridLayout(0, 1));
	editPanel.add(nickField);
	editPanel.add(machineField);

	//Set up buttons
	JButton okay = new JButton("OK");
	okay.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) {
			nicks[index] = nickField.getText();
			machines[index] = machineField.getText(); 
			coworkers[index] = nicks[index] + " " + machines[index];
			coworkersList.repaint();
			editFrame.dispose();
		} 
	}); 
	JButton cancel = new JButton("Cancel");
	cancel.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			editFrame.dispose();
		} 
	}); 
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridLayout(1, 0));
	buttonPanel.add(okay);
	buttonPanel.add(cancel);

	//Create edit window
	editFrame.getContentPane().add(editPanel, BorderLayout.CENTER);
	editFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	editFrame.pack();
	editFrame.setVisible(true);
}

/**
 * Create new coworker.
 */
public void newProj(){
	int len = 0;
	//Save the current selection index, since we lose it once
	//we do a setListData.
	int selectedIndex = coworkersList.getSelectedIndex();

	if(coworkers != null) len = coworkers.length;
	String[] newCoworkers = new String[len + 1];
	if(coworkers != null)
		System.arraycopy(coworkers, 0, newCoworkers, 0, len);
	coworkers = newCoworkers;
	coworkers[len] = "Name localhost";
	coworkersList.setListData(coworkers);
	coworkersList.setSelectedIndex(selectedIndex);

	String[] newNicks = new String[len + 1];
	if(nicks != null)
		System.arraycopy(nicks, 0, newNicks, 0, len);
	nicks = newNicks;
	nicks[len] = "Name"; 

	String[] newMachines = new String[len + 1];
	if(machines != null)
		System.arraycopy(machines, 0, newMachines, 0, len);
	machines = newMachines;
	machines[len] = "localhost";

	editWindow(len);
}

/**
 * Send message.
 */
public void sendMessage(){
	int selectedIndex = coworkersList.getSelectedIndex();
	try{
		InetAddress dest = InetAddress.getByName(machines[selectedIndex]);
		chatWindow = new JFrame("Message to " + nicks[selectedIndex]);
		chatWindow.getContentPane().add(new ChatComm(dest, chatPort));

		//Set up interface window
		chatWindow.getContentPane().setLayout(new BorderLayout());

		chatField = new JTextArea(5,20);
		chatField.setLineWrap(true);
		chatField.setWrapStyleWord(true);
		JScrollPane chatPane = new JScrollPane(chatField,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		chatWindow.getContentPane().add(chatPane, BorderLayout.NORTH);

		//Set up buttons
		JPanel buttonPanel = new JPanel();
		JButton button = new JButton("Send");
		buttonPanel.add(button);
		button.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				//Write data to port
				ChatComm.sendText(chatField.getText());
				chatWindow.dispose();	
			} 
		}); 
		chatWindow.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		chatWindow.pack();
		chatWindow.setVisible(true);
	}catch(UserNotOnlineException e) {
		System.out.println("User not online. " + e);
		JOptionPane.showMessageDialog(frame,
			"User "+nicks[selectedIndex]+" is not online.",
			"User Not Online", JOptionPane.ERROR_MESSAGE);		
	}catch(UnknownHostException e){
		System.out.println("No IP for host can be found. " + e);
	}catch(SecurityException e){
		System.out.println("Security manager does not allow get host. " + e);
	}
}

/**
 * Delete a coworker.
 */
public void delProj(){
	int len = 0;
	int selectedIndex = coworkersList.getSelectedIndex();

	if(selectedIndex > -1){
		//Warn the user
		Object[] options = {"OK", "Cancel"};
		int dialog = JOptionPane.showOptionDialog(frame, 
			"CoWorker will be deleted. Continue?",
			"Delete CoWorker", JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		if(dialog == 0){
		if(coworkers != null) len = coworkers.length;
		String[] newCoworkers = new String[len - 1];
		System.arraycopy(coworkers, 0, newCoworkers, 0, selectedIndex);
		if(len > selectedIndex + 1)
			System.arraycopy(coworkers, selectedIndex + 1, newCoworkers, selectedIndex, len - (selectedIndex + 1));
		coworkers = newCoworkers;
		coworkersList.setListData(coworkers);

		if(nicks != null) len = nicks.length;
		String[] newNicks = new String[len - 1];
		System.arraycopy(nicks, 0, newNicks, 0, selectedIndex);
		if(len > selectedIndex + 1)
			System.arraycopy(nicks, selectedIndex + 1, newNicks, selectedIndex, len - (selectedIndex + 1));
		nicks = newNicks;

		if(machines != null) len = machines.length;
		String[] newMachines = new String[len - 1];
		System.arraycopy(machines, 0, newMachines, 0, selectedIndex);
		if(len > selectedIndex + 1)
			System.arraycopy(machines, selectedIndex + 1, newMachines, selectedIndex, len - (selectedIndex + 1));
		machines = newMachines;

		coworkersList.repaint();
	}
	}
}

/**
 * Read through preferances file
 * @parm wide - Variable to store the width of the columns
 * @parm high - Variable to store the height of the columns 
 */
public void readPrefs(int wide) {
	StringTokenizer parameters; 
	String str, parm;

	try {
		BufferedReader in = new BufferedReader(new FileReader("ListComm.def")); 
		while((str = in.readLine()) != null){
			parameters = new StringTokenizer(str);
			parm = parameters.nextToken();
			if (parm.equals("Width"))
					wide = Integer.parseInt(parameters.nextToken());
			else if (parm.equals("Nicks")){
					String[] temp = new String[parameters.countTokens()];
					for(int i = 0; parameters.hasMoreTokens(); i++)
						//Replace all '°' characters with spaces
						temp[i] = parameters.nextToken().replace('°', ' ');
					nicks = temp;
			}
			else if (parm.equals("Machines")){
					String[] temp = new String[parameters.countTokens()];
					for(int i = 0; parameters.hasMoreTokens(); i++)
						temp[i] = parameters.nextToken();
					machines = temp;
			}					
		}
		if (nicks != null){
			String[] temp = new String[nicks.length];
			for(int i = 0; i < nicks.length ; i++)
				temp[i] = nicks[i] + " " + machines[i];
			coworkers = temp;
		}

		in.close();
	} catch (IOException e) {
		System.out.println("Error reading prefs file.");
	}
}

/**
 * Create a new preferances file with default values
 */
public void createPrefs() {
	try {
		BufferedWriter out = new BufferedWriter(new FileWriter("ListComm.def"));
		out.write("Width 10\n");
		out.close();
	} catch (IOException e) {
		System.out.println("Error writing to prefs file.");
	}
}

public static void savePrefs() {
	try {
		BufferedWriter out = new BufferedWriter(new FileWriter("ListComm.def"));
		out.write("Width " + coworkersList.getFixedCellWidth() + "\n");
		if((nicks != null) && (machines != null)){
			out.write("Nicks");
			for(int i = 0; i < nicks.length; i++)
				//Replace spaces with some weird character so the tokenizer
				//doesn't get messed up.
				out.write(" " + nicks[i].replace(' ', '°'));
			out.write("\n");
			out.write("Machines");
			for(int i = 0; i < machines.length; i++)
				out.write(" " + machines[i]);
			out.write("\n");
		}
		out.close();
	} catch (IOException e) {
		System.out.println("Error writing to prefs file.");
	}
}

/**
 * The class is meant to be imported into another program, but a main method
 * exists to allow ListComm to run on its own.
 */
public static void main(String[] args) {
	//Create main window
	if(args.length > 0)
		frame.getContentPane().add(new ListComm(Integer.parseInt(args[0])));
	else
		frame.getContentPane().add(new ListComm(7790));
	frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			savePrefs();
			System.exit(0);
		}
	});
	frame.pack();
	frame.setVisible(true);
}
}
