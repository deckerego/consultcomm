import java.io.*;
import java.awt.*;

public class JarImage {
    public static Image loadImage(String file) {
        Image image = null;
        byte[] tn = null;
        InputStream in = JarImage.class.getResourceAsStream(file);
        try{
            int length = in.available();
            tn = new byte[length];
            in.read(tn);
            image = Toolkit.getDefaultToolkit().createImage(tn);
        } catch(Exception e){
            System.out.println("Error loading image "+file+": "+e);
        }
        return image;
    }
}
