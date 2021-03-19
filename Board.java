// Swing Imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// AWT Imports
import java.awt.GridLayout;

// Other Imports
import java.util.ArrayList;


/**
 * The game board
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Board extends JPanel
{
    private Reversi controller;
    private int boardSize = 8;
    private Disc[][] boardDiscs;
    private GridLayout layout;
    
    public Board(Reversi controller) {
        this.controller = controller;
        
        layout = new GridLayout(8,8,5,5);
        boardDiscs =  new Disc[8][8];
        
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(layout);
    }
    
    private void addDisc(Disc newDisc, int row, int column) {
        boardDiscs[row][column] = newDisc;
        this.add(newDisc);
    }
    
    private void populateBoard() {
        
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                Disc newDisc = new Disc(this);
                this.addDisc(newDisc, r, c);
            }
        }
    }
    
    public void setBoardSize() {
        this.populateBoard();
    }
    
    public void setBoardSize(int size) {
        boardSize = size;
        boardDiscs = new Disc[size][size];
        this.layout.setRows(size);
        this.layout.setColumns(size);
        // run mainFrame.validate();
        this.populateBoard();
    }
    
    public void startGame() {
        int pos = boardSize/2-1;
        
        boardDiscs[pos][pos].makeWhite();
        boardDiscs[pos][pos+1].makeBlack();
        boardDiscs[pos+1][pos].makeBlack();
        boardDiscs[pos+1][pos+1].makeWhite();
        
    }
    
    public boolean checkBoundary(int r, int c) {
        return ( (r>=0 && r<boardSize) && (c>=0 && c<boardSize) );
    }
    
    public ArrayList<Disc> checkDirection(String myColor, int r, int c, int rInc, int cInc) {
        ArrayList<Disc> legalDiscs = new ArrayList<>(boardSize-1-r);
        
        int i = r;
        while (checkBoundary(i, c)) {
            Disc tempDisc = boardDiscs[i][c];
            
            if (tempDisc.isEmpty()) {
                return null;
            }
            
            if (tempDisc.getType().equals(myColor)) {
                if (legalDiscs.size() == 0) {
                    return null;
                } else {
                    return legalDiscs;
                }
            }
            
            legalDiscs.add(tempDisc);
            
            i += rInc;
            c += cInc;
        }
        
        return null;
    }
    
    public boolean checkVerticals(String myColor, int r, int c) {
        // Check vertical going up
        if ( checkDirection(myColor, r-1, c, -1, 0) != null ) return true;
        //Check vertical going down
        if ( checkDirection(myColor, r+1, c, 1, 0) != null ) return true;
        
        return false;
    }
    
    public boolean checkHorizontals(String myColor, int r, int c) {
        // Check horizontal going left
        if ( checkDirection(myColor, r, c-1, 0, -1) != null ) return true;
        // Check horizontal going right
        if ( checkDirection(myColor, r, c+1, 0, 1) != null ) return true;
        
        return false;
    }
    
    public boolean checkLegalMove(String myColor, int r, int c) {
        // Check verticals
        if (checkVerticals(myColor, r, c)) return true;
        // Check horizontals
        if (checkHorizontals(myColor, r, c)) return true;
        // Check left diagonal
        
        // Check right diagonal
        
        return false;
        
        // At each step of each check see if:
        // 1) go to the end of the direction (x, y, diagonal), if along the way
        //    you encounter an empty disc, then you KNOW that that whole path has no
        //    legal moves.
        // 2) If you encounter your colored disc, and everything so far has been your
        //    opponents disc (no empties), then that path is valid and thus the original
        //    pos is a legal move.
        
        // Then mark the current disc's legalmove to true, this will change the color of it
    }
    
    public void checkLegalMoves(boolean turn) {
        // What is the opponents color in order to find the legal positions against them
        String myColor = (turn) ? "black" : "white";
        String oppColor = (turn) ? "white" : "black";
        
        // Go through each disc on the board to find the legal positions
        Disc currentDisc;
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                currentDisc = boardDiscs[r][c];
                if (currentDisc.isEmpty()) {
                    if (checkLegalMove(myColor, r, c)) {
                        boardDiscs[r][c].makeLegal();
                    }
                }
            }
        }
    }
}
