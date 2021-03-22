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
    
    private Board controller;
    
    private final Dimension DISCSIZE = new Dimension(64,64);
    private final Color DISCBGCOLOR = new Color(237, 196, 208);
    private final Icon WHITEICON = new DiscIcon(Color.WHITE);
    private final Icon BLACKICON = new DiscIcon(Color.BLACK);
    
    // Indicates whether a player can place a 'disc' on this disc on the layout
    private boolean legalMove = false;
    
    // Indicates whether legal moves should be shown to the players
    private boolean showLegalMoves = true;
    
    // empty, black or white
    private String type = "empty";
    
    public Disc(Board controller) {
        this.controller = controller;
        this.setBackground(DISCBGCOLOR);
        this.setFocusPainted(false);
        this.addActionListener(this);
        this.setPreferredSize(DISCSIZE);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if (!legalMove) {
            this.setBorder(new EmptyBorder(0,0,0,0));
        } else if (legalMove && showLegalMoves) {
            this.setBorder(new LineBorder(Color.BLUE, 2));
        }
        
        super.paintComponent(g);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (legalMove) {
            controller.playMove(this);
        } else {
            controller.setStatusBar("Illegal Move", Color.RED);
        }
    }
    
    public void makeBlack() {
        this.setIcon(BLACKICON);
        type = "black";
    }
    
    public void makeWhite() {
        this.setIcon(WHITEICON);
        type = "white";
    }
    
    public void makeEmpty() {
        this.setIcon(null);
        type = "empty";
    }
    
    public void toggleLegalMoves() {
        showLegalMoves = !showLegalMoves;
    }
    
    public void makeLegal() {
        legalMove = true;
    }
    
    public void makeIllegal() {
        legalMove = false;
    }
    
    public String getType() {
        return type;
    }
    
    public boolean getLegalMove() {
        return legalMove;
    }
    
    public boolean isEmpty() {
        return type.equals("empty");
    }
    
}
