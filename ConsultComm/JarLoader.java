import java.io.*;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.jar.*;
import java.awt.*;

/**
 * Utility methods for obtaining information and files
 * from Java Archive Resources.
 */
public class JarLoader {
  public final static int FILE_OK = 0;
  public final static int FILE_EXISTS = -1;
  public final static int FILE_ERROR = -2;
    
    /**
     * Load an image stored within the <I>current</I> JAR file that contains
     * this JarLoader class
     * @param file The filename (including path relative to the JAR) to be extracted
     * @return The AWT Image
     * @see JarLoader#loadImage(String, Class)
     */
    public static Image loadImage(String file) {
        return loadImage(file, JarLoader.class);
    }
    
    /**
     * Load an image stored within another class' JAR file
     * @param file The filename (including path relative to the JAR) to be extracted
     * @param resourceClass The Class that is relative to the JAR used for extraction
     * @return The AWT Image
     * @see JarLoader#loadImage(String)
     */
    public static Image loadImage(String file, Class resourceClass) {
        Image image = null;
        try{
            URL url = resourceClass.getResource(file);
            image = Toolkit.getDefaultToolkit().getImage(url);
        } catch(Exception e){
            System.err.println("Error loading image "+file+": "+e);
        }
        return image;
    }

    /**
     * Load an image stored within the <I>current</I> JAR file that contains
     * this JarLoader class
     * @param file The filename (including path relative to the JAR) to be extracted
     * @return The AWT ImageIcon
     * @see JarLoader#loadImageIcon(String, Class)
     */
    public static ImageIcon loadImageIcon(String file) {
        return loadImageIcon(file, JarLoader.class);
    }
    
    /**
     * Load an image stored within another class' JAR file
     * @param file The filename (including path relative to the JAR) to be extracted
     * @param resourceClass The Class that is relative to the JAR used for extraction
     * @return The AWT ImageIcon
     * @see JarLoader#loadImageIcon(String)
     */
    public static ImageIcon loadImageIcon(String file, Class resourceClass) {
        ImageIcon image = null;
        try{
            URL url = resourceClass.getResource(file);
            image = new ImageIcon(url);
        } catch(Exception e){
            System.err.println("Error loading image "+file+": "+e);
        }
        return image;
    }

    /**
     * Extract a platform-specific library file into the PluginManager's
     * library directory
     * @param path The relative path to the file stored in a Java Archive
     * @param resourceClass The Class that is relative to the JAR used for extraction
     * @return <li>FILE_EXISTS if the file exists
     *         <li>FILE_ERRROR if there was an error extracting the file
     * @see JarLoader#loadFile(String, File, Class)
     */
    public static int loadNativeLibrary(String path, Class resourceClass) {
        return loadFile(path, PluginManager.libsdir, resourceClass);
    }
    
    /**
     * Translate a file from a bytestream in the JAR file. This will extract the
     * file into an established destination directory.
     * @param path The relative path to the file stored in a Java Archive
     * @param destinationPath The directory to extract the file into
     * @param resourceClass The Class that is relative to the JAR used for extraction
     * @return <li>FILE_EXISTS if the file exists
     *         <li>FILE_ERRROR if there was an error extracting the file
     * @see JarLoader#loadFile(String, File, Class)
     */
    public static int loadFile(String path, File destination, Class resourceClass) {
        try{
            String fileName = path;
            File file = new File(destination, fileName);
            if(file.exists()) return FILE_EXISTS; //Don't reload the file if it already exists
            file.deleteOnExit();
            InputStream in = new BufferedInputStream(resourceClass.getResourceAsStream(path));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[4096];
            while(true) { //Read in the file byte by byte
                int nBytes = in.read(buffer);
                if (nBytes <= 0) break;
                out.write(buffer, 0, nBytes);
            }
            out.flush();
            out.close();
            in.close();
            return FILE_OK;
        } catch(Exception e){
            System.err.println("Error loading file "+path+": "+e);
            return FILE_ERROR;
        }
    }
    
    /**
     * Find the list of values for a main manifest attribute within given JAR
     * @param jarFile The JAR to parse
     * @param attribute The attribute to return values for
     * @return The value of the attribute
     */
    public static String getManifestAttribute(JarFile jarFile, String attribute) {
        try {
            Manifest jarMfst = jarFile.getManifest();
            Attributes jarAttribs = jarMfst.getMainAttributes();
            return jarAttribs.getValue(attribute);
        } catch (Exception e) {
            System.err.println("Couldn't load "+jarFile.toString()+" Manifest: "+e);
            return null;
        }
    }
}
