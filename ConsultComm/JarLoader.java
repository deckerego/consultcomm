import java.io.*;
import javax.swing.ImageIcon;
import java.net.URL;
import java.awt.*;

public class JarLoader {
    
    public static Image loadImage(String file) {
        return loadImage(file, JarLoader.class);
    }
    
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

    public static ImageIcon loadImageIcon(String file) {
        return loadImageIcon(file, JarLoader.class);
    }
    
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
     * Translate a file from a bytestream in the JAR file
     * @param path The relative path to the file stored in a Java Archive
     * @return True if the library was loaded, false if it was already present
     * or an exception occured
     */
    public static boolean loadNativeLibrary(String path, Class resourceClass) {
        try{
            String fileName = path;
            File file = new File(PluginManager.libsdir, fileName);
            if(file.exists()) return false; //Don't reload the file if it already exists
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
            return true;
        } catch(Exception e){
            System.err.println("Error loading file "+path+": "+e);
            return false;
        }
    }
}
