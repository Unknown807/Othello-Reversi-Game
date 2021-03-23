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

// AWT imports
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;

// Other Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classic game of Reversi
 *
 * @author Milovan Gveric
 * @version 15/03/2021
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
    
    private void createActionListeners() {
        playGameButton.addActionListener(e -> playGame());
        legalMovesToggle.addActionListener(e -> setShowLegalMoves());
        newSessionItem.addActionListener(e -> startNewSession());
        setBoardSizeItem.addActionListener(e -> setBoardSize());
        saveGameItem.addActionListener(e -> saveGame());
        loadGameItem.addActionListener(e -> loadGame());
    }
    
    // Session Related Methods
    
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
    
    private void loadGame() {
    }
    
    // Game Related Methods
    
    private void playGame() {
        // Adds simple checks to make sure neither player's name isn't the default
        // string or blank
        String playerName = player1.getEnteredName();
        
        if (playerName.isBlank() || playerName.equals("Enter Player Name")) {
            showErrorDialog("Player 1's name can't be left blank");
            return;
        }
        
        String playerName2 = player2.getEnteredName();
        
        if (playerName2.isBlank() || playerName2.equals("Enter Player Name")) {
            showErrorDialog("Player 2's name can't be left blank");
            return;
        }
        
        player1.finalisePlayerName(playerName);
        player2.finalisePlayerName(playerName2);
        player1.setDiscTotal(2);
        player2.setDiscTotal(2);
        playGameButton.setVisible(false);
        legalMovesToggle.setVisible(false);
        setStatusBar("It's "+((turn) ? "Black" : "White")+"'s Turn", Color.BLACK);
        gameBoard.startGame();
        gameBoard.checkAllLegalMoves();
    }
    
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
    
    private void newGame() {
        turn = true;
        passedTurns = 0;
        player1.setDiscTotal(0);
        player2.setDiscTotal(0);
        gameBoard.newGame();
        playGameButton.setVisible(true);
        legalMovesToggle.setVisible(true);
    }
    
    public void nextTurn() {
        passedTurns = 0;
        player1.setDiscTotal(gameBoard.getWhiteTotal());
        player2.setDiscTotal(gameBoard.getBlackTotal());
        turn = !turn;
        gameBoard.checkAllLegalMoves();
    }
    
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
    
    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(
            mainFrame, 
            msg,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void setShowLegalMoves() {
        showLegalMoves = !showLegalMoves;
        gameBoard.setShowLegalMoves(showLegalMoves);
        legalMovesToggle.setText( (showLegalMoves) ? "Hide Moves" : "Show Moves");
    }
    
    public void setStatusBar(String text, Color fg) {
        statusBar.setText(text);
        statusBar.setForeground(fg);
    }
    
    public boolean getTurn() {
        return turn;
    }    
    
}
