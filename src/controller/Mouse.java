package controller;

import model.DataManager;
import view.MainWindow;
import view.Renderer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class contains all the information about the user action and maps it to the business application.
 */

public class Mouse implements MouseListener {

    public static boolean leftClicked = false;

    /**
     * This methods handles the mouse event nd render the UI
     *
     * @param mouseEvent mouse event action from the user
     */
    public void mouseClicked(MouseEvent mouseEvent) {

        // Right click
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            if (leftClicked) {
                if (Renderer.targetShipId != -1) {
                    if (MainWindow.shipList.get(Renderer.targetShipId).direction < 4)
                        MainWindow.shipList.get(Renderer.targetShipId).direction += 1;
                    else
                        MainWindow.shipList.get(Renderer.targetShipId).direction = 1;
                }
            }
        }
    }

    /**
     * This methods handles the mouse action left click
     *
     * @param mouseEvent mouse click from the user
     */
    public void mousePressed(MouseEvent mouseEvent) {

        // Left click
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            leftClicked = true;

            if (MainWindow.startedGame)
                MainWindow.buttonClick();
        }

        // Mouse wheel
        if (mouseEvent.getButton() == MouseEvent.BUTTON2) {

        }
    }

    /**
     * This methods handles the mouse action - released
     *
     * @param mouseEvent mouse event action from the user - mouse released
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            leftClicked = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
