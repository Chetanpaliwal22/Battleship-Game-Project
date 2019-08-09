package model;

import view.MainWindow;


/**
 * This class handles the score logic.
 */
public class Score {

    public static int numberOfMissedTime = 0;
    private int finalScore = 0;

    /**
     * Uses a formula to calculate the final score
     * @return final score of the game
     */
    public int calculateFinalScore() {

        if (MainWindow.playerWins)
            finalScore = -1 * numberOfMissedTime * 5 + 90 + 10;
        else
            finalScore = -1 * numberOfMissedTime * 5 + 90;

        return finalScore;
    }

    /**
     * @return returnFinalScore Returns the final score value
     */
    public int returnFinalScore() {
        return finalScore;
    }

    /**
     * @return numberof stars Calculates the number of stars based on the final score
     */
    public int calculateStars() {
        if (finalScore > 90 & finalScore <= 100)
            return 5;
        else if (finalScore >= 80 & finalScore < 100)
            return 4;
        else if (finalScore >= 70 & finalScore <= 80)
            return 3;
        else if (finalScore >= 60 & finalScore <= 70)
            return 2;
        else if (finalScore >= 30 & finalScore <= 60)
            return 1;
        else
            return 0;
    }
}
