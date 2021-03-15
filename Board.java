// Swing Imports
import javax.swing.JComponent;

// AWT Imports
import java.awt.GridLayout;

// Other Imports


/**
 * Write a description of class Board here.
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Board extends GridLayout
{
    private int boardSize;
    private Disc[][] boardDiscs;
    
    public Board() {
        super(8, 8, 5, 5);
        boardDiscs =  new Disc[8][8];
    }
    
    public void addDisc(Disc newDisc, int row, int column) {
        boardDiscs[row][column] = newDisc;
    }
    
    public void setBoardSize(int size) {
        boardSize = size;
    }
    
}
