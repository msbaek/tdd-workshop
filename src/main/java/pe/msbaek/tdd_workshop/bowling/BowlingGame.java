package pe.msbaek.tdd_workshop.bowling;

public class BowlingGame {
    
    private int totalScore = 0;
    
    public void roll(int pins) {
        totalScore += pins;
    }
    
    public int score() {
        return totalScore;
    }
}
