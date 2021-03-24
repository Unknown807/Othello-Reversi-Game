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
 * The PlayerPanel class for holding the UI components that make up a player in the game
 *
 * @author Milovan Gveric
 * @version 16/03/2021
 */
public class PlayerPanel extends JPanel
{
    private final Dimension PANELSIZE = new Dimension(250, 200);
    private final Dimension TEXTFIELDSIZE = new Dimension(Integer.MAX_VALUE, 30);
    
    private Player player;
    
    private BoxLayout layout;
    private JTextField playerNameField;
    private JLabel
        playerTitle,
        playerName,
        playerScore,
        playerDiscs;

    public PlayerPanel(String playerColor) {
        player = new Player();
        
        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        
        playerTitle = new JLabel("The "+playerColor+" Player");
        playerTitle.setFont(playerTitle.getFont().deriveFont(18f));  
        playerNameField = new JTextField("Enter Player Name");
        playerName = new JLabel();
        playerName.setFont(playerName.getFont().deriveFont(16f));
        playerScore = new JLabel("Score: 0");
        playerDiscs = new JLabel("Discs Total: 0");
        
        this.add(playerTitle);
        this.add(Box.createVerticalStrut(10));
        this.add(playerNameField);
        this.add(Box.createVerticalStrut(10));
        this.add(playerName);
        this.add(Box.createVerticalStrut(10));
        this.add(playerScore);
        this.add(Box.createVerticalStrut(10));
        this.add(playerDiscs);
        
        playerName.setVisible(false);
        
        this.setPreferredSize(PANELSIZE);
        playerNameField.setPreferredSize(TEXTFIELDSIZE);
        playerNameField.setMaximumSize(TEXTFIELDSIZE);
        this.setBorder(new EmptyBorder(10,10,10,20));
    }
    
    public void finalisePlayerName(String name) {   
        player.setName(name);
        playerNameField.setText(name);
        playerName.setText("Name: "+name);
        
        playerName.setVisible(true);
        playerNameField.setVisible(false);
    }
    
    public void showPlayerNameField() {
        player.setName("");
        playerNameField.setText("Enter Player Name");
        
        playerNameField.setVisible(true);
        playerName.setVisible(false);
    }
    
    public void incScore() {
        setScore(player.getScore()+1);
    }
    
    public void setScore(int score) {
        player.setScore(score);
        playerScore.setText("Score: "+score);
    }
    
    public void setDiscTotal(int total) {
        player.setDiscTotal(total);
        playerDiscs.setText("Discs Total: "+total);
    }
    
    public String getEnteredName() {
        return playerNameField.getText();
    }
    
    public String getName() {
        return player.getName();
    }
    
    public int getScore() {
        return player.getScore();
    }
    
    public int getDiscTotal() {
        return player.getDiscTotal();
    }
}
