// Swing Imports
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.Icon;

// AWT Imports
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Other Imports


/**
 * A disc that is used on the board, it can change between being a black or white disc.
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Disc extends JButton implements ActionListener
{
    private final Dimension DISCSIZE = new Dimension(64,64);
    private final Color DISCBGCOLOR = new Color(237, 196, 208);
    private final Icon WHITEICON = new DiscIcon(Color.WHITE);
    private final Icon BLACKICON = new DiscIcon(Color.BLACK);
    
    private boolean legalMove = false;
    
    public Disc() {
        this.setBackground(DISCBGCOLOR);
        this.setFocusPainted(false);
        this.addActionListener(this);
        this.setPreferredSize(DISCSIZE);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if (!this.isSelected() && !legalMove) {
            this.setBorder(new EmptyBorder(0,0,0,0));
        } else if (!this.isSelected() && legalMove) {
            this.setBorder(new LineBorder(Color.BLUE, 2));
        }
        
        super.paintComponent(g);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button Clicked");
    }
    
    public void makeBlack() {
        this.setIcon(BLACKICON);
    }
    
    public void makeWhite() {
        this.setIcon(WHITEICON);
    }
    
    
}