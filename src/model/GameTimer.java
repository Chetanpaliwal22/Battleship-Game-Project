package model;

import view.MainWindow;

import java.util.TimerTask;


/**
 * This class handles the timer logic.
 */
public class GameTimer extends TimerTask {
    private static Score score = new Score();

    private static boolean startTimer = false;

    private static int totalMinutes = 0, totalSeconds = 10;

    private static String timeString = "";

    public void run() {
        if (!MainWindow.gameOver) {
            if (startTimer) {

                if (totalSeconds > 0)
                    totalSeconds -= 1;
                else {
                    score.numberOfMissedTime += 1;
                    pauseTimer();
                }

                formatTime();
                MainWindow.timerLabel.setText("Time " + timeString);
            }
        }
    }

    /*
     * This method Formats the display of time
     *
     */ 
    private static void formatTime() {
        if (totalMinutes < 10) {
            if (totalSeconds < 10)
                timeString = "0" + totalMinutes + ":0" + totalSeconds + " Missed Time: " + score.numberOfMissedTime;
            else
                timeString = "0" + totalMinutes + ":" + totalSeconds + " Missed Time: " + score.numberOfMissedTime;
        } else {
            if (totalSeconds < 10)
                timeString = totalMinutes + ":0" + totalSeconds + " Missed Time: " + score.numberOfMissedTime;
            else
                timeString = totalMinutes + ":" + totalSeconds + " Missed Time: " + score.numberOfMissedTime;
        }
    }

    /*
     * This method Return the time in unit of second
     */
    public static int getRemainingTime() {
        return totalMinutes * 60 + totalSeconds;
    }

    public static void startTimer() {
        startTimer = true;
    }

    public static void pauseTimer() {
        startTimer = false;

        formatTime();

        MainWindow.timerLabel.setText("Paused " + timeString);
    }

    public static void resetTimer() {
        totalSeconds = 10;
        totalMinutes = 0;
    }
}