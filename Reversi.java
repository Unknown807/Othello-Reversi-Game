// Swing imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

// AWT imports
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;

// Other Imports
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Responsible for managing the Board and both playerPanel objects as well as which player's
 * turn it is currently, whether legal moves should be shown and the menu item behaviour
 * which is setting the board size, new sessions, saving and loading games
 *
 * @author Milovan Gveric
 * @version 26/03/2021
 */
public class Reversi
{
    
    // true is player 1 (Black), false is for player 2 (White). Black starts first
    private boolean turn = true;
    
    // Whether legal moves should be shown to both players, this value is communicated
    // to the individual discs to hide or show their colored border
    private boolean showLegalMoves = true;
    
    // 0 (able to playmoves), 1 (one player had to pass), 2 (both players passed, end game)
    private int passedTurns = 0;
    
    // Main Layout Components
    private JFrame mainFrame;
    private Container mainContainer;
    private BorderLayout mainLayout;
    
    // Other Components
    private JPanel rightSidePanel;
    private PlayerPanel player1, player2;
    private BoxLayout rightSideLayout;
    private Board gameBoard; 
    
    private JButton playGameButton, legalMovesToggle;
    private JLabel statusBar;
    
    // Menus and Items
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem 
        newSessionItem, 
        setBoardSizeItem, 
        saveGameItem, 
        loadGameItem;
    
    /**
     * The frame for the game is created first followed by methods which create the menus and
     * their items second, then the actual UI components (board, player panels), populates
     * the board initially with nothing and finally creates the listeners for each of the
     * menu items
     */
    public Reversi() {
        mainFrame = new JFrame("Game of Reversi");
        mainContainer = mainFrame.getContentPane();
        mainLayout = new BorderLayout(10, 10);
        mainContainer.setLayout(mainLayout);
        
        createMenuComponents();
        createComponents();
        gameBoard.setBoardSize();
        createActionListeners();
        
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    /**
     * The menubar only has one Game menu and then four menu items which:
     * 1) start a new session, overwriting the current game session
     * 2) set a new board size
     * 3) save the current game
     * 4) load another game
     */
    private void createMenuComponents() {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        newSessionItem = new JMenuItem("new session");
        setBoardSizeItem = new JMenuItem("set board size");
        saveGameItem = new JMenuItem("save game");
        loadGameItem = new JMenuItem("load game");
        
        mainFrame.setJMenuBar(menuBar);
        menuBar.add(gameMenu);
        gameMenu.add(newSessionItem);
        gameMenu.add(setBoardSizeItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
    }
    
    /**
     * Here the game board is created and the player panels with boxlayouts, empty borders
     * for padding and struts/glue for spacing between widgets that are together
     */
    private void createComponents() {
        gameBoard = new Board(this);

        rightSidePanel = new JPanel();
        rightSidePanel.setBorder(new EmptyBorder(10,10,10,10));
        rightSideLayout = new BoxLayout(rightSidePanel, BoxLayout.Y_AXIS);
        rightSidePanel.setLayout(rightSideLayout);
              
        player1 = new PlayerPanel("White");
        player2 = new PlayerPanel("Black");
        playGameButton = new JButton("Play Game");
        legalMovesToggle = new JButton("Hide Moves");
        
        rightSidePanel.add(Box.createVerticalStrut(10));
        rightSidePanel.add(player1);
        rightSidePanel.add(Box.createVerticalStrut(10));
        rightSidePanel.add(player2);
        rightSidePanel.add(Box.createVerticalGlue());
        rightSidePanel.add(legalMovesToggle);
        rightSidePanel.add(Box.createVerticalStrut(10));
        rightSidePanel.add(playGameButton);
        
        statusBar = new JLabel("Welcome to Reversi!");
        statusBar.setBorder(new EmptyBorder(10,10,10,10));
        
        mainContainer.add(gameBoard, BorderLayout.CENTER);
        mainContainer.add(rightSidePanel, BorderLayout.EAST);
        mainContainer.add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Creates listeners which point to methods here within the Reversi object
     */
    private void createActionListeners() {
        playGameButton.addActionListener(e -> playGame());
        legalMovesToggle.addActionListener(e -> setShowLegalMoves());
        newSessionItem.addActionListener(e -> startNewSession());
        setBoardSizeItem.addActionListener(e -> setBoardSize());
        saveGameItem.addActionListener(e -> saveGame());
        loadGameItem.addActionListener(e -> loadGame());
    }
    
    // Session Related Methods
    
    /**
     * Before starting a new session the user is asked with a prompt, if they agree
     * it calls the newGame method and resets player scores and shows the input fields for
     * new names in each player panel object and finally updates the status bar to show that
     * a new session has been started
     */
    private void startNewSession() {
        int result = JOptionPane.showConfirmDialog(mainFrame,
            "Do you want to start a new session?",
            "Board Reset",
            JOptionPane.YES_NO_OPTION);
            
        if (result != JOptionPane.YES_OPTION) return;
        
        newGame();
        player1.setScore(0);
        player2.setScore(0);
        
        setStatusBar("New Session Started", Color.BLACK);
        
        player1.showPlayerNameField();
        player2.showPlayerNameField();
    }
    
    /**
     * A prompt is created asking the user what the board size should be set to. By default
     * it is of size 8x8. If the board size is not a number or even then it gives the user
     * an error dialog and cancels the operation. When the board size is set a new game is
     * also started.
     */
    private void setBoardSize() {
        String result = (String) JOptionPane.showInputDialog(
            mainFrame,
            "Type your new board size (must be even).\nNOTE: this will start a new game",
            "Board Size Change",
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            "8");
            
        if (result == null) return;
        if (!result.matches("\\d+")) {
            showErrorDialog("You have to enter a number");
            return;
        }
        
        int newSize = Integer.parseInt(result);
        
        if (newSize % 2 != 0 || newSize <= 0) {
            showErrorDialog("The number has to be even");
            return;
        }
        
        gameBoard.setBoardSize(newSize);
        newGame();
        setStatusBar("Changed Board Size", Color.BLACK);
    }
    
    /**
     * The game will not save if either user's name is blank because it indicates that there
     * is no game in progress. For a game in progress it will save:
     * 1) whose turn it is
     * 2) how many turns have passed (in case its near endgame)
     * 3) whether legal moves are to be shown or not
     * 4) player names
     * 5) player scores
     * 6) player disc totals
     * 7) and the game board's state
     * 
     * When this data is combined into a single string separated by new lines, a hashcode is
     * taken from the string and put at the start in order to read it when loading the game
     * to make sure none of the data has changed
     * 
     * The user is then asked where to save the file, it can be named anything and has a
     * txt extension and finally the status bar is updated to show it has been saved
     * successfully
     */
    private void saveGame() {
        
        if (player1.getName().isBlank() || player2.getName().isBlank()) {
            showErrorDialog("There is currently no game in progress to save");
            return;
        }
        
        String data = turn+"\n"+
            passedTurns+"\n"+
            showLegalMoves+"\n"+
            player1.getName()+"\n"+
            player1.getScore()+"\n"+
            player1.getDiscTotal()+"\n"+
            player2.getName()+"\n"+
            player2.getScore()+"\n"+
            player2.getDiscTotal()+"\n"+
            gameBoard.getData();
        
        data = data.hashCode()+"\n"+data;
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose the Session Save File Name");
        int result = fileChooser.showSaveDialog(mainFrame);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            try {
                FileWriter fileWriter = new FileWriter(saveFile.getAbsolutePath()+".txt");
                fileWriter.write(data);
                setStatusBar("Session Successfully Saved!", Color.GREEN);
                fileWriter.close();              
            } catch (IOException e) {
                showErrorDialog("An error occurred when saving the game!");
            }
            
        }
    }
    
    /**
     * When loading the game the user chooses which save with a file chooser. The first line
     * is the hashcode of the data when it was saved, an arraylist keeps track of all the
     * lines after this one. When the file has been read, the hashcode is checked against
     * a new hashcode of the same data and if they differ then something has changed and an
     * error dialog is shown and the loading cancels.
     * 
     * If the file has not changed then the lines are removed one by one from the arraylist to
     * set things like whose turn it is, player names, scores, etc. By removing them it returns
     * the value returned and eventually the arraylist has nothing but the board state data left
     * (each disc), which is passed into the gameBoard's setData method which sets the new
     * board state
     */
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Save Files", "txt");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(mainFrame);
        if (result != JFileChooser.APPROVE_OPTION) return;
        
        
        try {
            File saveFile = fileChooser.getSelectedFile();
            Scanner fileReader = new Scanner(saveFile);
            
            ArrayList<String> lines = new ArrayList<>();
            int fileHashCode = Integer.parseInt(fileReader.nextLine());
            
            while (fileReader.hasNextLine()) {
                lines.add(fileReader.nextLine());
            }
            fileReader.close();
            
            String fileData = String.join("\n",lines)+"\n";
            if (fileHashCode != fileData.hashCode())
                throw new RuntimeException();
            
            // Setting game state
            
            turn = Boolean.parseBoolean(lines.remove(0));
            passedTurns = Integer.parseInt(lines.remove(0));
            showLegalMoves = !Boolean.parseBoolean(lines.remove(0));
            playGameButton.setVisible(false);
            legalMovesToggle.setVisible(false);
            
            // Setting player 1
            
            player1.finalisePlayerName(lines.remove(0));
            player1.setScore(Integer.parseInt(lines.remove(0)));
            player1.setDiscTotal(Integer.parseInt(lines.remove(0)));
            
            // Setting player 2
            
            player2.finalisePlayerName(lines.remove(0));
            player2.setScore(Integer.parseInt(lines.remove(0)));
            player2.setDiscTotal(Integer.parseInt(lines.remove(0)));
            
            // Setting board state
            
            int boardSize = Integer.parseInt(lines.remove(0));
            gameBoard.setBoardSize(boardSize);
            gameBoard.setData(lines);
            setShowLegalMoves();
            gameBoard.checkAllLegalMoves();
            
            setStatusBar("It's "+((turn) ? "Black" : "White")+"'s Turn", Color.BLACK);
            
        } catch (FileNotFoundException e) {
            showErrorDialog("File not found!");
        } catch (RuntimeException e) {
            showErrorDialog("Malformed save file, cannot be loaded");
        }
    }
    
    // Game Related Methods
    
    /**
     * When the play game button is pressed if the players names are valid then the game is
     * started (with black starting first), from calling the board's startGame and
     * checkAllLegalMoves method (to see if players can make any moves). The disc total is
     * by default 2 as thats how many there are at the start of the game
     */
    private void playGame() {
        // Adds simple checks to make sure neither player's name isn't the default
        // string or blank
        String playerName1 = player1.getEnteredName();
        
        if (playerName1.isBlank() || playerName1.equals("Enter Player Name")) {
            showErrorDialog("Player 1's name can't be left blank");
            return;
        }
        
        String playerName2 = player2.getEnteredName();
        
        if (playerName2.isBlank() || playerName2.equals("Enter Player Name")) {
            showErrorDialog("Player 2's name can't be left blank");
            return;
        }
        
        player1.finalisePlayerName(playerName1);
        player2.finalisePlayerName(playerName2);
        player1.setDiscTotal(2);
        player2.setDiscTotal(2);
        playGameButton.setVisible(false);
        legalMovesToggle.setVisible(false);
        setStatusBar("It's "+((turn) ? "Black" : "White")+"'s Turn", Color.BLACK);
        gameBoard.startGame();
        gameBoard.checkAllLegalMoves();
    }
    
    /**
     * The disc totals are checked to see which player wins the game and if both players
     * have the same number of discs then they are both awarded a point and after the dialog
     * announcing the results is closed, a new game is started
     */
    private void endGame() {
        setStatusBar("Neither player can move", Color.RED);
        String msg = "";
        int totalB = player2.getDiscTotal();
        int totalW = player1.getDiscTotal();
        
        if (totalB > totalW) {
            msg = "Black wins this game!";
            player2.incScore();
        } else if (totalW > totalB) {
            msg = "White wins this game!";
            player1.incScore();
        } else {
            msg = "Both players have the same amount of discs, tie!";
            player1.incScore();
            player2.incScore();
        }
        
        JOptionPane.showMessageDialog(mainFrame, msg);
        
        newGame();
        setStatusBar("New Game Started", Color.BLACK);
    }
    
    /**
     * In order to start a new game the turn is set back to the default of black first, the
     * number of passed turns is set back to 0 as well as each player's disc totals (which 
     * will get set to 2 upon calling the board's newGame method, which subsequently populate
     * the board). and the the play game button and the button to toggle showing legal moves
     * are shown to let the players decide how/when to play the next game
     */
    private void newGame() {
        turn = true;
        passedTurns = 0;
        player1.setDiscTotal(0);
        player2.setDiscTotal(0);
        gameBoard.newGame();
        playGameButton.setVisible(true);
        legalMovesToggle.setVisible(true);
    }
    
    /**
     * When the next turn occurs the number of passed turns is set to 0 because if it was 1
     * (from one player passing a turn) then if the other player were to pass a turn (while the
     * other could make a turn) the game may prematurely end because it gets incremented to 2.
     * 
     * Both player's disc totals are tallied and set to show the users what they are and the
     * turn is given to the next player, which is why legal moves are now checked for that
     * player
     */
    public void nextTurn() {
        passedTurns = 0;
        player1.setDiscTotal(gameBoard.getWhiteTotal());
        player2.setDiscTotal(gameBoard.getBlackTotal());
        turn = !turn;
        gameBoard.checkAllLegalMoves();
    }
    
    /**
     * If a player could not make a move then it notifies them and passes the turn to the
     * other player, if the number of passed turns is at 2 it means neither player could
     * move and so the game is ended. Otherwise the turn is switched and the game keeps going
     */
    public void passTurn() {
        String currentPlayer = (turn) ? "Black" : "White";
        setStatusBar(currentPlayer+" passed", Color.RED);
        JOptionPane.showMessageDialog(mainFrame, "There are no moves for "+currentPlayer+
            ", so you have to pass");
        
        passedTurns++;
        if (passedTurns == 2) {
            endGame();
        } else {
            turn = !turn;
            gameBoard.checkAllLegalMoves();
        }
    }

    // Other Methods
    
    /**
     * Error dialogs are used quite frequently so this is a convenience method
     * 
     * @param msg       The error message to be shown
     */
    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(
            mainFrame, 
            msg,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * When toggling whether the legal moves should be shown, it has to call the game board
     * and set each disc to not show its colored border if its not toggled on
     */
    private void setShowLegalMoves() {
        showLegalMoves = !showLegalMoves;
        gameBoard.setShowLegalMoves(showLegalMoves);
        legalMovesToggle.setText( (showLegalMoves) ? "Hide Moves" : "Show Moves");
    }
    
    /**
     * This method can set the status bar text and its color, used for normal, error and
     * success messages
     * 
     * @param text      the text to give the status bar
     * @param fg        the color of the status bar text
     */
    public void setStatusBar(String text, Color fg) {
        statusBar.setText(text);
        statusBar.setForeground(fg);
    }
    
    /**
     * @@return whose turn it currently is, used by 'child' objects to correctly 
     * execute certain methods
     */
    public boolean getTurn() {
        return turn;
    }    
    
}
