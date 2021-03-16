// Swing Imports
import javax.swing.JButton;

// AWT Imports
import java.awt.Dimension;
import java.awt.Color;

// Other Imports


/**
 * A disc that is used on the board, it can change between being a black or white disc.
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Disc extends JButton
{
    private final Dimension discSize = new Dimension(64,64);
    private final Color discColor = new Color(237, 196, 208);
    
    public Disc() {
        this.setBackground(discColor);
        this.setPreferredSize(discSize);
    }
}
