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

// Other Imports

/**
 * Classic game of Reversi
 *
 * @author Milovan Gveric
 * @version 15/03/2021
 */
public class Reversi
{
    
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
        
        this.createMenuComponents();
        this.createInitialComponents();
        gameBoard.populateBoard();
        this.createActionListeners();
        
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
        gameBoard = new Board();
        
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
            playGameButton.setVisible(false);
            statusBar.setText(player1Title.getText()+"'s Turn");
            gameBoard.startGame();
        });
        
    }
    
}
