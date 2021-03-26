// Swing Imports
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

// AWT Imports
import java.awt.Dimension;

/**
 * The PlayerPanel class for holding the UI components that make up a player in the game
 *
 * @author Milovan Gveric
 * @version 26/03/2021
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

    /**
     * Components are created here to display each player's scores, total number of captured
     * discs and their name
     * 
     * @param playerColor       this sets the large font label to indicate 
     * which color the player is, the color they represent in game is already decided 
     * as player 1 is white and player 2 is black
     */
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
    
    /**
     * This method is called when each player's entered name in the playerNameField is valid
     * and this will set their name, and thus show the name label, and hide the playerNameField.
     * This is also used when loading a game because the current session may not have the
     * playerNameFields hidden and so it makes sure to load over it.
     * 
     * @param name      The name of the player to be set
     */
    public void finalisePlayerName(String name) {   
        player.setName(name);
        playerNameField.setText(name);
        playerName.setText("Name: "+name);
        
        playerName.setVisible(true);
        playerNameField.setVisible(false);
    }
    
    /**
     * This can be seen as the opposite to the finalisePlayerName method as it hides the
     * name label and shows the playerNameField for users to re-enter their names. This is
     * used when starting a new session
     */
    public void showPlayerNameField() {
        player.setName("");
        playerNameField.setText("Enter Player Name");
        
        playerNameField.setVisible(true);
        playerName.setVisible(false);
    }
    
    /**
     * This is used to increment the score, it calls the setScore method in order to
     * both set the label and actual score value of the player to avoid unnecessary code
     */
    public void incScore() {
        setScore(player.getScore()+1);
    }
    
    /**
     * Sets the player's score in the appropriate player object and the playerPanel label
     * 
     * @param score     the new score
     */
    public void setScore(int score) {
        player.setScore(score);
        playerScore.setText("Score: "+score);
    }
    
    /**
     * Sets the player's total captured discs in the appropriate player object and the
     * playerPanel label
     * 
     * @param total     the new total number of captured discs
     */
    public void setDiscTotal(int total) {
        player.setDiscTotal(total);
        playerDiscs.setText("Discs Total: "+total);
    }
    
    /**
     * @return the entered text in the playerNameField for validation
     */
    public String getEnteredName() {
        return playerNameField.getText();
    }
    
    /**
     * @return the appropriate player object's name
     */
    public String getName() {
        return player.getName();
    }
    
    /**
     * @return the appropriate player object's score
     */
    public int getScore() {
        return player.getScore();
    }
    
    /**
     * @return the appropriate player object's total number of captured discs
     */
    public int getDiscTotal() {
        return player.getDiscTotal();
    }
}
