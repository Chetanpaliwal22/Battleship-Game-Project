package model;

import view.MainWindow;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
    private static boolean startTimer = false;

    private static int totalMinutes = 0, totalSeconds = 0;

    private static String timeString = "";

    public void run() {
        if (startTimer) {
            if (totalSeconds >= 60) {
                totalSeconds = 0;
                totalMinutes += 1;
            } else {
                totalSeconds += 1;
            }

            formatTime();

            MainWindow.timerLabel.setText("Time " + timeString);
        }
    }

    // Format the display of time
    private static void formatTime() {
        if (totalMinutes < 10) {
            if (totalSeconds < 10)
                timeString = "0" + totalMinutes + ":0" + totalSeconds;
            else
                timeString = "0" + totalMinutes + ":" + totalSeconds;
        } else {
            if (totalSeconds < 10)
                timeString = totalMinutes + ":0" + totalSeconds;
            else
                timeString =totalMinutes + ":" + totalSeconds;
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
        totalSeconds = 0;
        totalMinutes = 0;
    }
}