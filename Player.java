/**
 * Player class holds the actual data and communicates it via the PlayerPanel class
 *
 * @author Milovan Gveric
 * @version 26/03/2021
 */
public class Player
{
    private String name = "";
    private int score = 0;
    private int discTotal = 0;
    
    /**
     * Ordinary getters and setters for name, score and discTotal
     */
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void setDiscTotal(int total) {
        this.discTotal = total;
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getDiscTotal() {
        return discTotal;
    }
}
