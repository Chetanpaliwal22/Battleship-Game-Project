package controller;

import view.MainWindow;
//import view.Renderer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
public class Mouse implements MouseListener {
    public static boolean leftClicked = false;

    public void mouseClicked(MouseEvent mouseEvent) {
        // Right click
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {

            if (leftClicked) {
                if (MainWindow.shipList.get(Renderer.targetShipId).direction < 4)
                    MainWindow.shipList.get(Renderer.targetShipId).direction += 1;
                else
                    MainWindow.shipList.get(Renderer.targetShipId).direction = 1;
            }
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        // Left click
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            leftClicked = true;
        }

        // Mouse wheel
        if (mouseEvent.getButton() == MouseEvent.BUTTON2) {

        }
    }

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
*/