// Swing Imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// AWT Imports
import java.awt.GridLayout;

// Other Imports


/**
 * The game board
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Board extends JPanel
{
    private int boardSize = 8;
    private Disc[][] boardDiscs;
    private GridLayout layout;
    
    public Board() {
        layout = new GridLayout(8,8,5,5);
        boardDiscs =  new Disc[8][8];
        
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(layout);
    }
    
    private void addDisc(Disc newDisc, int row, int column) {
        boardDiscs[row][column] = newDisc;
        this.add(newDisc);
    }
    
    public void setBoardSize(int size) {
        boardSize = size;
        boardDiscs = new Disc[size][size];
        this.layout.setRows(size);
        this.layout.setColumns(size);
        // run mainFrame.validate();
        this.populateBoard();
    }
    
    public void populateBoard() {
        
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                Disc newDisc = new Disc();
                this.addDisc(newDisc, r, c);
            }
        }
    }
    
    public void startGame() {
    }
}
