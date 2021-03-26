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

/**
 * A disc that is used on the board, it can change between being a black or white disc.
 *
 * @author Milovan Gveric
 * @version 26/03/2021
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
    
    /**
     * The preferred size of discs is set to 64x64, the background color is also set
     * and the action listener (for players playing moves) is set and the focus when
     * clicking buttons is hidden as it doesn't fit in visually
     * 
     * @param controller        The controller is the Reversi object which the board communicates
     * changes to so the game progresses
     */
    public Disc(Board controller) {
        this.controller = controller;
        this.setBackground(DISCBGCOLOR);
        this.setFocusPainted(false);
        this.addActionListener(this);
        this.setPreferredSize(DISCSIZE);
    }
    
    /**
     * The paintComponent method of the JButton class which decides how the button appears
     * onscreen. If the player can select the disc to play a move and showLegalMoves is
     * toggled on, then it will show a thin blue border indicating that the player can
     * indeed place a colored 'disc' here
     * 
     * @param g     the graphics object to be painted
     */
    @Override
    public void paintComponent(Graphics g) {
        if (!legalMove) {
            this.setBorder(new EmptyBorder(0,0,0,0));
        } else if (legalMove && showLegalMoves) {
            this.setBorder(new LineBorder(Color.BLUE, 2));
        }
        
        super.paintComponent(g);
    }
    
    /**
     * Called when a player plays a move, will only allow actually valid moves otherwise
     * an error message is shown in the status bar
     * 
     * @param e     the action event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (legalMove) {
            controller.playMove(this);
        } else {
            controller.setStatusBar("Illegal Move", Color.RED);
        }
    }
    
    /**
     * Makes the current disc black
     */
    public void makeBlack() {
        this.setIcon(BLACKICON);
        type = "black";
    }
    
    /**
     * Makes the current disc white
     */
    public void makeWhite() {
        this.setIcon(WHITEICON);
        type = "white";
    }
    
    /**
     * Makes the current disc empty
     */
    public void makeEmpty() {
        this.setIcon(null);
        type = "empty";
    }
    
    /**
     * If the disc can be clicked on for a legal move to be played, then this method
     * sets whether said legal move can be seen by players
     */
    public void setShowLegalMoves(boolean flag) {
        showLegalMoves = flag;
    }
    
    /**
     * Make the current disc legal, i.e a player can click on it to make move
     */
    public void makeLegal() {
        legalMove = true;
    }
    
    /**
     * Make the current disc illegal, i.e a player cannot click on it to make any move
     */
    public void makeIllegal() {
        legalMove = false;
    }
    
    /**
     * @return the current type of the disc (black, white, empty)
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return whether the current disc counts as a legal move or not
     */
    public boolean getLegalMove() {
        return legalMove;
    }
    
    /**
     * @return if the current disc is empty (neither black nor white)
     */
    public boolean isEmpty() {
        return type.equals("empty");
    }
    
}
