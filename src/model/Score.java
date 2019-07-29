package model;

import view.MainWindow;

public class Score {

    public int numberOfMissedTime = 0;

    public int calculateFinalScore() {
        if (MainWindow.playerWins)
            return -1 * numberOfMissedTime * 5 + 90 + 10;
        else
            return -1 * numberOfMissedTime * 5 + 90;
    }
}
