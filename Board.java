// Swing Imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// AWT Imports
import java.awt.GridLayout;
import java.awt.Color;

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
    
    private String currentDiscColor;
    
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
    
    public ArrayList<Disc> checkDirection(int r, int c, int rInc, int cInc) {
        ArrayList<Disc> legalDiscs = new ArrayList<>();
        
        while ( (r>=0 && r<boardSize) && (c>=0 && c<boardSize) ) {
            Disc tempDisc = boardDiscs[r][c];
            
            if (tempDisc.isEmpty()) {
                return null;
            }
            
            if (tempDisc.getType().equals(currentDiscColor)) {
                if (legalDiscs.size() == 0) {
                    return null;
                } else {
                    return legalDiscs;
                }
            }
            
            legalDiscs.add(tempDisc);
            
            r += rInc;
            c += cInc;
        }
        
        return null;
    }   
    
    public ArrayList<ArrayList<Disc>> getLegalMoves(int r, int c) {
        ArrayList<ArrayList<Disc>> capturedDiscs = new ArrayList<>();
        ArrayList<Disc> tempList;
        
        // Check vertical going up
        tempList = checkDirection(r-1, c, -1, 0);
        if ( tempList != null ) capturedDiscs.add(tempList);
        //Check vertical going down
        tempList = checkDirection(r+1, c, 1, 0);
        if ( tempList != null ) capturedDiscs.add(tempList);
        
        // Check horizontal going left
        tempList = checkDirection(r, c-1, 0, -1);
        if ( tempList != null ) capturedDiscs.add(tempList);
        // Check horizontal going right
        tempList = checkDirection(r, c+1, 0, 1);
        if ( tempList != null ) capturedDiscs.add(tempList);
        
        // Check diagonal going to top left
        tempList = checkDirection(r-1, c-1, -1, -1);
        if ( tempList != null ) capturedDiscs.add(tempList);
        // Check diagonal going to bottom right
        tempList = checkDirection(r+1, c+1, 1, 1);
        if ( tempList != null ) capturedDiscs.add(tempList);
        
        // Check diagonal going to top right
        tempList = checkDirection(r-1, c+1, -1, 1);
        if ( tempList != null ) capturedDiscs.add(tempList);
        // Check diagonal going to bottom left
        tempList = checkDirection(r+1, c-1, 1, -1);
        if ( tempList != null ) capturedDiscs.add(tempList);
        
        // If no legal directions of capture have been added, then return null
        if (capturedDiscs.size() == 0)
            return null;

        return capturedDiscs;
    }
    
    public void checkAllLegalMoves() {
        boolean noLegalMoves = true;
        boolean turn = controller.getTurn();
        currentDiscColor = (turn) ? "black" : "white";
        
        // Go through each disc on the board to find the legal positions
        Disc currentDisc;
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                currentDisc = boardDiscs[r][c];
                if (currentDisc.isEmpty()) {
                    if (getLegalMoves(r, c) != null) {
                        noLegalMoves = false;
                        currentDisc.makeLegal();
                    }
                }
            }
        }
        
        if (noLegalMoves) {
            controller.passTurn();
        }
    }
    
    public void setStatusBar(String text, Color fg) {
        controller.setStatusBar(text, fg);
    }
    
    public void playMove(Disc selectedDisc) {
        boolean turn = controller.getTurn();
        
        selectedDisc.makeIllegal();
        setStatusBar("It's "+((turn) ? "White" : "Black")+"'s Turn", Color.BLACK);
        
        ArrayList<ArrayList<Disc>> capturedDiscs;
        int r = 0;
        int c = 0;
        
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                if (selectedDisc.equals(boardDiscs[i][j])) {
                    r = i;
                    c = j;
                }
            }
        }
        
        // find all discs to be captured

        capturedDiscs = getLegalMoves(r, c);
        capturedDiscs.get(0).add(selectedDisc);
        
        for (ArrayList<Disc> discs: capturedDiscs) {
            for (Disc disc: discs) {
                if (turn) {
                    disc.makeBlack();
                } else {
                    disc.makeWhite();
                }
            }
        }
        
        resetLegalMoves();
        controller.nextTurn();
    }
    
    public int getBlackTotal() {
        currentDiscColor = "black";
        return getTotalDiscs();
    }
    
    public int getWhiteTotal() {
        currentDiscColor = "white";
        return getTotalDiscs();
    }
    
    private int getTotalDiscs() {
        int total = 0;
        
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                if (boardDiscs[r][c].getType().equals(currentDiscColor))
                    total++;
            }
        }
        
        return total;
    }
    
    private void resetLegalMoves() {
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                Disc currentDisc = boardDiscs[r][c];
                if (currentDisc.getLegalMove()) {
                    boardDiscs[r][c].makeIllegal();
                }
                
            }
        }
    }
}
