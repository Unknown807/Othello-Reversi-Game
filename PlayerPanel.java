// Swing Imports
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

// AWT Imports
import java.awt.Dimension;

// Other Imports

/**
 * The PlayerPanel class for holding the components that make up a player in the game
 *
 * @author Milovan Gveric
 * @version 16/03/2021
 */
public class PlayerPanel extends JPanel
{
    private final Dimension panelSize = new Dimension(250, 200);
    private final Dimension textFieldSize = new Dimension(Integer.MAX_VALUE, 30);
    
    private Player player;
    
    private BoxLayout layout;
    private JTextField playerNameField;
    private JLabel
        playerName,
        playerScore,
        playerDiscs;

    public PlayerPanel() {
        player = new Player();
        
        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        
        playerNameField = new JTextField("Enter Player Name");
        playerName = new JLabel();
        playerName.setFont(playerName.getFont().deriveFont(16f));
        playerScore = new JLabel("Score (Wins): 0");
        playerDiscs = new JLabel("Discs Total: 0");
        
        this.add(playerNameField);
        this.add(Box.createVerticalStrut(10));
        this.add(playerName);
        this.add(Box.createVerticalStrut(10));
        this.add(playerScore);
        this.add(Box.createVerticalStrut(10));
        this.add(playerDiscs);
        
        playerName.setVisible(false);
        
        this.setPreferredSize(panelSize);
        playerNameField.setPreferredSize(textFieldSize);
        playerNameField.setMaximumSize(textFieldSize);
        this.setBorder(new EmptyBorder(10,10,10,20));
    }
}
