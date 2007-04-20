package consultcomm;

import java.io.File;

/**
 *
 * @author jellis
 */
public class Preferences
{
  
  /**
   * Obtain a directory to store preferences files. This can be dependent based
   * on the user's operating system. For example, this should exist in
   * the "Documents and Settings" folder on Windows, and in the user's home
   * directory (as a hidden directory) on *nix platforms.
   * @return The directory used to store preferences files
   */
  public static File getPrefsDir()
  {
    String path;
    
    //TODO: Make Windows-specific directories
    path = System.getProperty("user.home")+System.getProperty("file.separator")+".consultcomm";
    
    return new File(path);
  }
  

}
