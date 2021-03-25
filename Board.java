// Swing Imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// AWT Imports
import java.awt.GridLayout;
import java.awt.Color;

// Other Imports
import java.util.ArrayList;


/**
 * The game board. This stores and keep track of NxN number of disc objects and is responsible
 * for calculating legal moves and captures and also communicates with the main Reversi
 * object changes in board state
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
    
    // Board Related Methods
    
    /**
     * the boardDiscs array stores all the disc objects and utilises a grid layout
     * 
     * @param controller The controller is the Reversi object which the board communicates
     * changes to so the game progresses
     */
    public Board(Reversi controller) {
        this.controller = controller;
        
        layout = new GridLayout(8,8,5,5);
        boardDiscs =  new Disc[8][8];
        
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(layout);
    }
    
    /**
     * Creates blank discs, used when initialising the board and whenever its size is set
     * by the user
     */
    private void populateBoard() {
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                Disc newDisc = new Disc(this);
                addDisc(newDisc, r, c);
            }
        }
    }
    
    /**
     * Used to add the disc object to both the boardDiscs array and to the gridlayout so
     * its visible to players
     * 
     * @param newDisc the disc object to be added
     * @param row its row position
     * @param column its column position
     */
    private void addDisc(Disc newDisc, int row, int column) {
        boardDiscs[row][column] = newDisc;
        this.add(newDisc);
    }    
    
    /**
     * sets the board size, uses the gridlayouts setRows and setColumns methods and then
     * repopulates the board (because the boardDiscs array has to be resized, so the discs
     * lose reference and are garbage collected). After doing this to make sure its visible
     * to the user the board (JPanel) is repainted and revalidated
     * 
     * @param size the new board size
     */
    public void setBoardSize(int size) {
        boardSize = size;
        boardDiscs = new Disc[size][size];
        this.removeAll();
        layout.setRows(size);
        layout.setColumns(size);
        populateBoard();
        this.repaint();
        this.revalidate();
    }    
    
    /**
     * At the start of the game there are 4 discs of alternating colours in a 2x2 grid in
     * the middle of the board
     */
    public void startGame() {
        int pos = boardSize/2-1;
        
        boardDiscs[pos][pos].makeWhite();
        boardDiscs[pos][pos+1].makeBlack();
        boardDiscs[pos+1][pos].makeBlack();
        boardDiscs[pos+1][pos+1].makeWhite();
        
    }
    
    /**
     * When a new game is started the board state must be reset, so all moves that were
     * previously legal are made illegal and each disc is set to be empty (neither black
     * or white)
     */
    public void newGame() {
        resetLegalMoves();
        resetBoard();
    }
    
    /**
     * Used by the controller to initially populate the board 
     * (since the size is by default 8x8).
     */
    public void setBoardSize() {
        populateBoard();
    }    
    
    // Move Related Methods
    
    /**
     * At the start of each turn this method is run to check if there are any moves a player
     * can make. If the noLegalMoves flag remains true by the end then no legal moves were
     * found and so the player has to pass the turn.
     * 
     * Using getTurn, then the method checks for the player each disc in the 
     * boardDiscs array and sees if its empty (because you can't make a move on colored discs)
     * and if you can capture any discs from that position. If its a legal move then the
     * disc at that position is made legal
     */
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
    
    /**
     * 
     */
    private ArrayList<Disc> getLegalMoves(int r, int c) {
        ArrayList<Disc> capturedDiscs = new ArrayList<>();
        ArrayList<Disc> tempList;
        
        // Check vertical going up
        tempList = checkDirection(r-1, c, -1, 0);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        //Check vertical going down
        tempList = checkDirection(r+1, c, 1, 0);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        
        // Check horizontal going left
        tempList = checkDirection(r, c-1, 0, -1);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        // Check horizontal going right
        tempList = checkDirection(r, c+1, 0, 1);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        
        // Check diagonal going to top left
        tempList = checkDirection(r-1, c-1, -1, -1);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        // Check diagonal going to bottom right
        tempList = checkDirection(r+1, c+1, 1, 1);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        
        // Check diagonal going to top right
        tempList = checkDirection(r-1, c+1, -1, 1);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        // Check diagonal going to bottom left
        tempList = checkDirection(r+1, c-1, 1, -1);
        if ( tempList != null ) capturedDiscs.addAll(tempList);
        
        // If no legal directions of capture have been found, then return null
        if (capturedDiscs.size() == 0)
            return null;

        return capturedDiscs;
    }
    
    private ArrayList<Disc> checkDirection(int r, int c, int rInc, int cInc) {
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
    
    public void playMove(Disc selectedDisc) {
        boolean turn = controller.getTurn();
        
        selectedDisc.makeIllegal();
        setStatusBar("It's "+((turn) ? "White" : "Black")+"'s Turn", Color.BLACK);
        
        ArrayList<Disc> capturedDiscs;
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
        capturedDiscs.add(selectedDisc);
        
        for (Disc disc: capturedDiscs) {
            if (turn) {
                disc.makeBlack();
            } else {
                disc.makeWhite();
            }
        }
        
        resetLegalMoves();
        controller.nextTurn();
    }    
    
    // "Utility" Methods
    
    public void setShowLegalMoves(boolean flag) {
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                boardDiscs[r][c].setShowLegalMoves(flag);
            }
        }
    }
    
    private void resetBoard() {
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                boardDiscs[r][c].makeEmpty();
            }
        }
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
    
    public void setData(ArrayList<String> discs) {
        for (int r=0; r<boardSize; r++) {
            String[] discPropRow = discs.get(r).split(",");
            for (int c=0; c<boardSize; c++) {
                Disc currentDisc = boardDiscs[r][c];
                String[] discProps = discPropRow[c].split(" ");
                
                boolean discLegalMove = Boolean.parseBoolean(discProps[1]);
                
                switch (discProps[0]){
                    case "black":
                        currentDisc.makeBlack();
                        break;
                    case "white":
                        currentDisc.makeWhite();
                        break;
                    case "empty":
                        currentDisc.makeEmpty();
                        break;
                }
                
                if (discLegalMove) {
                    currentDisc.makeLegal();
                } else {
                    currentDisc.makeIllegal();
                }
            }
        }

        currentDiscColor = (controller.getTurn()) ? "black" : "white";
    }    
    
    public String getData() {
        String data = boardSize+"\n";
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                Disc currentDisc = boardDiscs[r][c];
                data += currentDisc.getType()+" "+
                    currentDisc.getLegalMove()+",";
            }
            data += "\n";
        }
        
        return data;
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
    
    public void setStatusBar(String text, Color fg) {
        controller.setStatusBar(text, fg);
    }
    
    public int getBlackTotal() {
        currentDiscColor = "black";
        return getTotalDiscs();
    }
    
    public int getWhiteTotal() {
        currentDiscColor = "white";
        return getTotalDiscs();
    }

}
