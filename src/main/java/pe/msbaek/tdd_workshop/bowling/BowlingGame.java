package pe.msbaek.tdd_workshop.bowling;

public class BowlingGame {
    
    private int[] rolls = new int[21];
    private int currentRoll = 0;
    
    public void roll(int pins) {
        rolls[currentRoll++] = pins;
    }
    
    public int score() {
        int score = 0;
        int rollIndex = 0;
        
        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(rollIndex)) {
                score += 10 + strikeBonus(rollIndex);
                rollIndex += 1;
            } else if (isSpare(rollIndex)) {
                score += 10 + spareBonus(rollIndex);
                rollIndex += 2;
            } else {
                score += sumOfTwoBallsInFrame(rollIndex);
                rollIndex += 2;
            }
        }
        
        return score;
    }
    
    private boolean isStrike(int rollIndex) {
        return rolls[rollIndex] == 10;
    }
    
    private boolean isSpare(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1] == 10;
    }
    
    private int strikeBonus(int rollIndex) {
        return rolls[rollIndex + 1] + rolls[rollIndex + 2];
    }
    
    private int spareBonus(int rollIndex) {
        return rolls[rollIndex + 2];
    }
    
    private int sumOfTwoBallsInFrame(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1];
    }
}
