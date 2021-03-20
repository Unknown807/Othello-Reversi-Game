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

// AWT imports
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;

// Other Imports

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
    private JButton playGameButton;
    private JLabel 
        statusBar,
        player1Title,
        player2Title;
    
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
        createInitialComponents();
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
    
    private void createInitialComponents() {
        gameBoard = new Board(this);
        
        rightSidePanel = new JPanel();
        rightSidePanel.setBorder(new EmptyBorder(10,10,10,10));
        rightSideLayout = new BoxLayout(rightSidePanel, BoxLayout.Y_AXIS);
        rightSidePanel.setLayout(rightSideLayout);
        
        player1Title = new JLabel("Player 1 (White)");
        player1Title.setFont(player1Title.getFont().deriveFont(18f));
        player2Title = new JLabel("Player 2 (Black)");
        player2Title.setFont(player2Title.getFont().deriveFont(18f));        
        player1 = new PlayerPanel();
        player2 = new PlayerPanel();
        playGameButton = new JButton("Play Game");
        
        rightSidePanel.add(player1Title);
        rightSidePanel.add(Box.createVerticalStrut(10));
        rightSidePanel.add(player1);
        rightSidePanel.add(Box.createVerticalStrut(20));
        rightSidePanel.add(player2Title);
        rightSidePanel.add(Box.createVerticalStrut(10));
        rightSidePanel.add(player2);
        rightSidePanel.add(Box.createVerticalGlue());
        rightSidePanel.add(playGameButton);
        
        statusBar = new JLabel("Welcome to Reversi!");
        statusBar.setBorder(new EmptyBorder(10,10,10,10));
        
        mainContainer.add(gameBoard, BorderLayout.CENTER);
        mainContainer.add(rightSidePanel, BorderLayout.EAST);
        mainContainer.add(statusBar, BorderLayout.SOUTH);
    }
    
    private void createActionListeners() {
        
        // Adds simple checks to make sure either player's name isn't the default
        // string or blank
        playGameButton.addActionListener(e -> {
            boolean validNames = true;
            String currentPlayer = "1";
            String playerName = player1.getEnteredName();
            
            if (playerName.isBlank() || playerName.equals("Enter Player Name"))
                validNames = false;
                
            playerName = player2.getEnteredName();
            
            if (playerName.isBlank() || playerName.equals("Enter Player Name")) {
                currentPlayer = "2";
                validNames = false;
            }
                
            if (!validNames) {
                JOptionPane.showMessageDialog(mainFrame, "Player "+currentPlayer+"'s name can't be left blank",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            player1.finalisePlayerName();
            player2.finalisePlayerName();
            player1.setDiscTotal(2);
            player2.setDiscTotal(2);
            playGameButton.setVisible(false);
            setStatusBar("It's "+((turn) ? "Black" : "White")+"'s Turn", Color.BLACK);
            gameBoard.startGame();
            gameBoard.checkAllLegalMoves();
        });
        
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
        
        turn = true;
        player1.setDiscTotal(0);
        player2.setDiscTotal(0);
        gameBoard.newGame();
        playGameButton.setVisible(true);
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
    
    public void setStatusBar(String text, Color fg) {
        statusBar.setText(text);
        statusBar.setForeground(fg);
    }
    
    public boolean getTurn() {
        return turn;
    }
    
}
