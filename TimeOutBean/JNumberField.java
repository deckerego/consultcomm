/*
 * JNumberField.java
 *
 * Created on December 15, 2002, 9:13 PM
 */

import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author  dtrusty
 */
public class JNumberField extends JTextField {
    
    /** Creates a new instance of JNumberField */
    public JNumberField(int cols) {
         super(cols);
     }
 
     protected Document createDefaultModel() {
 	      return new NumberDocument();
     }
 
     static class NumberDocument extends PlainDocument {
 
         public void insertString(int offs, String str, AttributeSet a) 
 	          throws BadLocationException {
 	          if (str == null) {
 		      return;
 	          }
 	          StringBuffer sb = new StringBuffer(str);
                  StringBuffer sb2 = new StringBuffer();
                  int length = sb.length();
 	          for (int i = 0; i < length; i++) {
 		      char c  = sb.charAt(i);
                      if(c >= '0' && c <= '9'){
                        sb2.append(c);
                      }
 	          }
 	          super.insertString(offs, sb2.toString(), a);
 	      }
     }

    
}
