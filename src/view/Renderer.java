package view;

//import controller.Mouse;

import constants.Constants;
import controller.Mouse;

import javax.swing.*;
import java.awt.*;


/**
 * handle the rendering of graphic components
 */
public class Renderer extends JComponent {

    private boolean clearAll = false;

    private boolean calculatedHolePositions = false;

    public static int[][] holeLocationX = new int[9][11], holeLocationY = new int[9][11];

    public static int holeImageSize = 0;

    private Image holeImage, missedHoleImage, hitHoleImage, shipImage, horizontalShipImage, redShipImage, horizontalRedShipImage;

    private Image targetImage;

    int mouseX = 0, mouseY = 0;

    public static int targetShipId = -1; // The id showing which ship is being picked up


    /**
     * default constructor
     */
    public Renderer() {

        holeImageSize = (Constants.WINDOW_WIDTH / 2) / Constants.BOARD_SIZE.y - 2;

        setMaximumSize(new Dimension(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_WIDTH / 2 + 2 * holeImageSize));

        holeImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Hole.png");
        missedHoleImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/MissedHole.png");
        hitHoleImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/HitHole.png");
        shipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Ship.png");
        horizontalShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/ShipHorizontal.png");
        redShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/RedShip.png");
        horizontalRedShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/RedShipHorizontal.png");
        targetImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Target.png");
    }


    /**
     * method to update the display
     * @param g
     */
    public void paintComponent(Graphics g) {

        setBackground(new Color(88, 111, 78));
        setBackground(new Color(99, 222, 231));

        if (clearAll) {
            g.clearRect(0, 0, getWidth(), getHeight());
        } else {
            Graphics2D graphics2D = (Graphics2D) g;

            g.setColor(new Color(99, 222, 231));
            g.fillRect(0, 0, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_WIDTH / 2 + 2 * holeImageSize);

            int nearestX = 0, nearestY = 0;

            // Check if the mouse is clicked
            if (Mouse.leftClicked && !MainWindow.gameHasStarted) {
                mouseX = MouseInfo.getPointerInfo().getLocation().x - MainWindow.getWindowLocationX();
                mouseY = MouseInfo.getPointerInfo().getLocation().y - MainWindow.getWindowLocationY() - 100;

                // Calculate the shortest distance to find the nearest hole
                double shortestDistance = 999999999;

                for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
                    for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
                        double currentDistance = (mouseX - holeLocationX[j][i]) * (mouseX - holeLocationX[j][i])
                                + (mouseY - holeLocationY[j][i]) * (mouseY - holeLocationY[j][i]);
                        if (currentDistance < shortestDistance) {
                            shortestDistance = currentDistance;

                            nearestX = j;
                            nearestY = i;
                        }
                    }
                }

                if (targetShipId == -1) { // Find the nearest ship by searching all occupied grids

                    for (int i = 0; i < MainWindow.shipList.size(); i++) {
                        for (int gridIndex = 0; gridIndex < MainWindow.shipList.get(i).occupiedGridX
                                .size(); gridIndex++) {
                            int distance = (nearestX - MainWindow.shipList.get(i).occupiedGridX.get(gridIndex))
                                    * (nearestX - MainWindow.shipList.get(i).occupiedGridX.get(gridIndex))
                                    + (nearestY - MainWindow.shipList.get(i).occupiedGridY.get(gridIndex))
                                    * (nearestY - MainWindow.shipList.get(i).occupiedGridY.get(gridIndex));

                            if (distance < 1.2) {
                                targetShipId = i;
                                break;
                            }
                        }
                    }
                }

            } else {
                targetShipId = -1;
            }

            // Render all five ships
            for (int i = 0; i < MainWindow.shipList.size(); i++) {
                double offset = 0;

                switch (MainWindow.shipList.get(i).size) {
                    case 2:
                        if (MainWindow.shipList.get(i).direction == 1)
                            offset = 0.5;
                        else if (MainWindow.shipList.get(i).direction == 2)
                            offset = 0.5;
                        else if (MainWindow.shipList.get(i).direction == 3)
                            offset = 1.25;
                        else
                            offset = 1.25;
                        break;
                    case 3:
                        offset = 1.25;
                        break;
                    case 4:
                        if (MainWindow.shipList.get(i).direction == 1)
                            offset = 1.25;
                        else if (MainWindow.shipList.get(i).direction == 2)
                            offset = 1.25;
                        else if (MainWindow.shipList.get(i).direction == 3)
                            offset = 2.25;
                        else
                            offset = 2.25;
                        break;
                    case 5:
                        offset = 2.25;
                        break;
                }

                if (i != targetShipId) {
                    if (Mouse.leftClicked && !MainWindow.gameHasStarted)
                        MainWindow.shipList.get(i).validateLocation();

                    if (MainWindow.shipList.get(i).direction == 1 | MainWindow.shipList.get(i).direction == 3) {
                        if (MainWindow.shipList.get(i).validity)
                            graphics2D.drawImage(shipImage,
                                    holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    MainWindow.shipList.get(i).size * holeImageSize - 2, 40, this);
                        else
                            graphics2D.drawImage(redShipImage,
                                    holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    MainWindow.shipList.get(i).size * holeImageSize - 2, 40, this);
                    } else {
                        if (MainWindow.shipList.get(i).validity)
                            graphics2D.drawImage(horizontalShipImage,
                                    holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    40, MainWindow.shipList.get(i).size * holeImageSize - 2, this);
                        else
                            graphics2D.drawImage(horizontalRedShipImage,
                                    holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    40, MainWindow.shipList.get(i).size * holeImageSize - 2, this);
                    }
                } else {
                    if (targetShipId > -1) {
                        MainWindow.shipList.get(targetShipId)
                                .recalculate(MainWindow.shipList.get(targetShipId).direction, nearestX, nearestY);

                        MainWindow.shipList.get(targetShipId).validateLocation();

                        if (MainWindow.shipList.get(targetShipId).validity) {
                            if (MainWindow.shipList.get(targetShipId).direction == 1
                                    | MainWindow.shipList.get(targetShipId).direction == 3) {
                                graphics2D.drawImage(shipImage,
                                        holeLocationX[nearestX][nearestY] - (int) (offset * holeImageSize),
                                        holeLocationY[nearestX][nearestY] - 30 / 2,
                                        MainWindow.shipList.get(i).size * holeImageSize - 2, 40, this);
                            } else {
                                graphics2D.drawImage(horizontalShipImage, holeLocationX[nearestX][nearestY] - 30 / 2,
                                        holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
                                        MainWindow.shipList.get(i).size * holeImageSize - 2, this);
                            }
                        } else {
                            if (MainWindow.shipList.get(targetShipId).direction == 1
                                    | MainWindow.shipList.get(targetShipId).direction == 3) {
                                graphics2D.drawImage(redShipImage,
                                        holeLocationX[nearestX][nearestY] - (int) (offset * holeImageSize),
                                        holeLocationY[nearestX][nearestY] - 30 / 2,
                                        MainWindow.shipList.get(i).size * holeImageSize - 2, 40, this);
                            } else {
                                graphics2D.drawImage(horizontalRedShipImage, holeLocationX[nearestX][nearestY] - 30 / 2,
                                        holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
                                        MainWindow.shipList.get(i).size * holeImageSize - 2, this);
                            }
                        }
                    }
                }
            }

            // Render all grids
            for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
                for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
                    int relativeX = holeImageSize / 2 + j * holeImageSize;
                    int relativeY = holeImageSize / 2 + (Constants.BOARD_SIZE.x - i - 1) * holeImageSize;

                    graphics2D.drawImage(holeImage, relativeX, relativeY, 20, 20, this);

                    if (!calculatedHolePositions) {
                        holeLocationX[j][i] = relativeX + 5;
                        holeLocationY[j][i] = relativeY + 5;
                    }
                }
            }

            calculatedHolePositions = true;

            graphics2D.finalize();
        }

        // Use sleep method to reduce the performance overhead
        try {
            if (Mouse.leftClicked && !MainWindow.gameHasStarted)
                Thread.sleep(8);
            else
                Thread.sleep(75);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * clear the display
     */
    public void clearAll() {

        clearAll = true;
        repaint();
    }
}