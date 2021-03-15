// Swing Imports
import javax.swing.JButton;

// AWT Imports
import java.awt.Dimension;

// Other Imports


/**
 * A disc that is used on the board, it can change between being a black or white disc.
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Disc extends JButton
{
    private final int discSize = 64;
    
    public Disc() {
        this.setContentAreaFilled(false);
        this.setPreferredSize(new Dimension(discSize, discSize));
    }
}
