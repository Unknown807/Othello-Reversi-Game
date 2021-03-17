// Swing Imports
import javax.swing.Icon;

// AWT Imports
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;

// Other Imports

/**
 * The oval graphic that can be displayed when a player place a 'disc' on the board
 *
 * @author Milovan Gveric
 * @version 17/03/2021
 */
public class DiscIcon implements Icon
{
    private final int SIZE = 32;
    private Color ovalColor;
    
    public DiscIcon(Color c) {
        ovalColor = c;
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(ovalColor);
        g.fillOval(x, y, SIZE, SIZE);
    }
    
    public int getIconWidth() {
        return SIZE;
    }
    
    public int getIconHeight() {
        return SIZE;
    }
}
