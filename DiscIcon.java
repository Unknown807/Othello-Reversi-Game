// Swing Imports
import javax.swing.Icon;

// AWT Imports
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;

/**
 * The oval graphic that can be displayed when a player place a 'disc' on the board
 *
 * @author Milovan Gveric
 * @version 26/03/2021
 */
public class DiscIcon implements Icon
{
    private final int SIZE = 24;
    private Color ovalColor;
    
    /**
     * @param c     The color to set this icon as
     */
    public DiscIcon(Color c) {
        ovalColor = c;
    }
    
    /**
     * Used in painting the icon onscreen
     * 
     * @param c     the component to paint on
     * @param g     the graphics to paint
     * @param x     the starting position horizontally
     * @param y     the starting position vertically
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(ovalColor);
        g.fillOval(x, y, SIZE, SIZE);
    }
    
    /**
     * @return the width of the icon, needed in order for it to be painted onscreen
     */
    public int getIconWidth() {
        return SIZE;
    }
    
    /**
     * @return the height of the icon, needed in order for it to be painted onscreen
     */
    public int getIconHeight() {
        return SIZE;
    }
}
