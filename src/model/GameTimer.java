package model;

import sun.applet.Main;
import view.MainWindow;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
    private static Score score = new Score();

    private static boolean startTimer = false;

    private static int totalMinutes = 0, totalSeconds = 10;

    private static String timeString = "";

    public void run() {
        if (!MainWindow.gameOver) {
            if (startTimer) {
//            if (totalSeconds >= 60) {
//                totalSeconds = 0;
//                totalMinutes += 1;
//            } else {
//                totalSeconds += 1;
//            }

                if (totalSeconds > 0)
                    totalSeconds -= 1;
                else {
                    score.numberOfMissedTime += 1;
                    pause();
                }

                formatTime();
                MainWindow.timerLabel.setText("Time " + timeString);
            }
        }
    }

    // Format the display of time
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

    // Return the time in unit of second
    public static int returnTime() {
        return totalMinutes * 60 + totalSeconds;
    }

    public static void start() {
        startTimer = true;
    }

    public static void pause() {
        startTimer = false;

        formatTime();

        MainWindow.timerLabel.setText("Paused " + timeString);
    }

    public static void reset() {
        totalSeconds = 10;
        totalMinutes = 0;
    }
}