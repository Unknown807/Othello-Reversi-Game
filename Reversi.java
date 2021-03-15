// Swing imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

// AWT imports
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;

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
    private BoxLayout rightSideLayout;
    private JLabel statusBar;
    private JPanel boardPanel;
    private Board gameBoard;
   
    // Menus and Items
    
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newSessionItem;
    private JMenuItem setBoardSizeItem;
    private JMenuItem saveGameItem;
    private JMenuItem loadGameItem;
    
    public Reversi() {
        mainFrame = new JFrame("Game of Reversi");
        mainContainer = mainFrame.getContentPane();
        mainLayout = new BorderLayout(10, 10);
        mainContainer.setLayout(mainLayout);
        
        this.createMenuComponents();
        this.createInitialComponents();
        this.initGame();
        
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
        boardPanel = new JPanel();
        boardPanel.setBorder(new EmptyBorder(10,10,10,10));
        gameBoard = new Board();
        boardPanel.setLayout(gameBoard);
        
        rightSidePanel = new JPanel();
        rightSidePanel.setBorder(new EmptyBorder(10,10,10,10));
        rightSideLayout = new BoxLayout(rightSidePanel, BoxLayout.Y_AXIS);
        rightSidePanel.setLayout(rightSideLayout);
        
        // Create right side content here
        
        statusBar = new JLabel("Welcome to Reversi!");
        statusBar.setBorder(new EmptyBorder(10,10,10,10));
        
        mainContainer.add(boardPanel, BorderLayout.CENTER);
        mainContainer.add(rightSidePanel, BorderLayout.EAST);
        mainContainer.add(statusBar, BorderLayout.SOUTH);
    }
    
    private void initGame() {
        
        for (int r=0; r<8; r++) {
            for (int c=0; c<8; c++) {
                Disc newDisc = new Disc();
                gameBoard.addDisc(newDisc, r, c);
                boardPanel.add(newDisc);
            }
        }
        
    }
}
