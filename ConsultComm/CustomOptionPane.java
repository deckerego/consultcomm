import java.util.*;
import java.awt.*;
import javax.swing.*;

class CustomOptionPane extends JOptionPane {
  private final static int lineLength = 40;
  
  /**
   * Split the message into a range of lengths so there isn't just one
   * long narrow dialog box
   * @see javax.swing.JOptionPane.showMessageDialog(Component, Object, String, int)
   */
  public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) throws HeadlessException {
    StringTokenizer tokenizer = new StringTokenizer((String)message, " ", true);
    StringBuffer bufferedMessage = new StringBuffer();
    while(tokenizer.hasMoreTokens()) {
      StringBuffer bufferedLine = new StringBuffer(tokenizer.nextToken());
      while(bufferedLine.length() <= lineLength && tokenizer.hasMoreTokens())
        bufferedLine.append(tokenizer.nextToken());
      if(tokenizer.hasMoreTokens()) bufferedLine.append('\n');
      bufferedMessage.append(bufferedLine);
    }
    JOptionPane.showMessageDialog(parentComponent, bufferedMessage.toString(), title, messageType);
  }
}
