
/**
 * The Player class holds the actual data about name, score and total discs
 *
 * @author Milovan Gveric
 * @version 16/03/2021
 */
public class Player
{
    private String name;
    private int score = 0;
    private int discTotal = 0;
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void setDiscTotal(int discTotal) {
        this.discTotal = discTotal;
    }
    
    public void incScore() {
        this.score++;
    }
    
    public void incDiscTotal() {
        this.discTotal++;
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
