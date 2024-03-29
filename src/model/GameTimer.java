package model;

import view.MainWindow;

import javax.swing.*;
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
                    Score.numberOfMissedTime += 1;
                    pauseTimer();
                }

                formatTime();

                if (MainWindow.gameMode.equalsIgnoreCase("advanced"))
                    MainWindow.timerLabel.setText("<html>Number of shots: " + MainWindow.numberOfPlayerShots + "/" + MainWindow.numberOfPlayerMaxShots + "<br>Time " + timeString + "</html>");
                else
                    MainWindow.timerLabel.setText("Time" + timeString);
            }
        }
    }

    /**
     * This method Formats the display of time
     */
    private static void formatTime() {
        if (totalMinutes < 10) {
            if (totalSeconds < 10)
                timeString = "0" + totalMinutes + ":0" + totalSeconds + " Missed Time: " + Score.numberOfMissedTime;
            else
                timeString = "0" + totalMinutes + ":" + totalSeconds + " Missed Time: " + Score.numberOfMissedTime;
        } else {
            if (totalSeconds < 10)
                timeString = totalMinutes + ":0" + totalSeconds + " Missed Time: " + Score.numberOfMissedTime;
            else
                timeString = totalMinutes + ":" + totalSeconds + " Missed Time: " + Score.numberOfMissedTime;
        }
    }

    /**
     * This method Return the time in unit of second
     * @return int returns the remaining time
     */
    public static int getRemainingTime() {
        return totalMinutes * 60 + totalSeconds;
    }

    /**
     * This method is used for starting the timer object
     */
    public static void startTimer() {
        startTimer = true;
    }

    /**
     * This method is used for pausing the timer object
     */
    public static void pauseTimer() {
        startTimer = false;

        formatTime();

        MainWindow.timerLabel.setText("Paused " + timeString);
    }

    /**
     * This method is used for reseting the timer object
     */
    public static void resetTimer() {
        totalSeconds = 10;
        totalMinutes = 0;
    }
}