import java.util.jar.*;
import java.net.*;
import CsltComm;

public class JarLauncher {
  final static String file = "CsltComm.jar";

  JarLauncher() {
    new CsltComm().show();
  }

  public static void main(String args[]) {
    new JarLauncher();
  }
}
