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
 * @version 26/03/2021
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
     * @param controller        The controller is the Reversi object which the board communicates
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
     * @param newDisc       the disc object to be added
     * @param row           its row position
     * @param column        its column position
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
     * @param size      the new board size
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
     * Checks each direction clockwise around a disc to see if there are any possible 
     * directions of capture, if not then the arraylist will have a size of 0, so the method
     * returns null (as there are no legal moves to capture)
     * 
     * This method is also used to check for legal moves because if the returned arraylist
     * is null then it knows there are no legal moves
     * 
     * @param r     the row of the current disc
     * @param c     the column of the current disc
     * 
     * @return An arraylist of discs to be captured / indicate legal moves or null to show
     * that there are no legal moves
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
    
    /**
     * Given a position (row and column), along with increments (-1, 0 or +1) this
     * method will go through all the discs at that position to see which discs of the
     * opposite color to the current player can be captured. If the increments to the row
     * or column goes outside of the board's boundary or it comes across an empty disc 
     * (which means the discs up to that point aren't being flanked) then null is
     * returned
     * 
     * @param r     starting row position
     * @param c     starting column position
     * @param rInc  how much to increment/decrement the row at every iteration
     * @param cInc  how much to increment/decrement the column at every iteration
     * 
     * @return An arraylist with all the discs that can be captured in a direction
     * or null if no discs can be captured in that direction
     */
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
    
    /**
     * Any time a legal move disc is clicked it communicates it to this method and the board
     * finds the selected disc to know what the starting row and column positions are to
     * get the discs to be captured from that position. It then switches to the next players
     * turn by calling nextTurn in the Reversi object
     * 
     * @param selectedDisc      the disc that was clicked during one player's turn
     */
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
    
    /**
     * Goes through all the discs on the board and changes their showLegalMoves field to
     * indicate whether they should show their colored borders when/if they are a legal move
     * that can be played on by a player
     * 
     * @param flag      true or false to show or hide legal moves where players can place discs
     */
    public void setShowLegalMoves(boolean flag) {
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                boardDiscs[r][c].setShowLegalMoves(flag);
            }
        }
    }
    
    /**
     * Goes through all discs on the board and makes them empty (neither black or white)
     * used when starting new games or new sessions
     */
    private void resetBoard() {
        for (int r=0; r<boardSize; r++) {
            for (int c=0; c<boardSize; c++) {
                boardDiscs[r][c].makeEmpty();
            }
        }
    }
    
    /**
     * Goes through all the discs on the board and marks them as illegal moves, used when
     * starting new games and new sessions, because by default at the start of the game
     * the user can't make a move anywhere. This resets the legality from any discs made
     * legal in the previous game
     */
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
    
    /**
     * When loading a game the board state needs to be set to how it was, so it goes through
     * all the disc states from the loaded game and reflects onto the current board. uses
     * split because each row of discs is comma-delimited and disc properties are separate
     * with spaces
     * 
     * @param discs     the arraylist containing the states of discs from the loaded game
     */
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
    }    
    
    /**
     * To save the board state when saving a game, the boardDiscs array is iterated through
     * and each discs type and whether its legal move is retrieved and systematically
     * organised and a large string is returned containing all this data
     * 
     * @return the board state data to be saved
     */
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
    
    /**
     * Gets the total number of discs that have been captured by the player whose color
     * equals the currentDiscColor. Used to get both white and black captures via other
     * methods
     * 
     * @return the count of captured discs for a player
     */
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
    
    /**
     * @return the count of captured discs for the black player
     */
    public int getBlackTotal() {
        currentDiscColor = "black";
        return getTotalDiscs();
    }
    
    /**
     * @return the count of captured discs for the white player
     */
    public int getWhiteTotal() {
        currentDiscColor = "white";
        return getTotalDiscs();
    }    
    
    /**
     * Used to communicate useful messages to the Reversi object's status bar
     */
    public void setStatusBar(String text, Color fg) {
        controller.setStatusBar(text, fg);
    }

}
