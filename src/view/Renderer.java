package view;

import controller.Mouse;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JComponent {
    private boolean clearAll = false;

    private boolean calculatedHolePositions = false;

    public static int[][] holeLocationX = new int[11][9], holeLocationY = new int[11][9];

    public static int holeImageSize = 0;

    private Image holeImage, shipImage, horizontalShipImage, redShipImage, horizontalRedShipImage;

    int mouseX = 0, mouseY = 0;

    // The id showing which ship is being picked up
    public static int targetShipId = -1;

    public Renderer() {
        setBackground(new Color(99, 222, 231));
//        setOpaque(true);

        holeImageSize = (BattleShip.WIDTH / 2) / 11 - 2;

        setMaximumSize(new Dimension(BattleShip.WIDTH / 2, BattleShip.WIDTH / 2 - 2 * holeImageSize));

        holeImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Hole.png");
        shipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Ship.png");
        horizontalShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/ShipHorizontal.png");
        redShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/RedShip.png");
        horizontalRedShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/RedShipHorizontal.png");
    }

    public void paintComponent(Graphics g) {

        if (clearAll) {
            g.clearRect(0, 0, getWidth(), getHeight());
        } else {

            Graphics2D graphics2D = (Graphics2D) g;

            // Render all grids
            for (int i = 8; i >= 0; i--) {
                for (int j = 0; j < 11; j++) {
                    int relativeX = holeImageSize / 2 + j * holeImageSize;
                    int relativeY = holeImageSize / 2 + (8 - i) * holeImageSize;

                    graphics2D.drawImage(holeImage, relativeX, relativeY, 20, 20, this);

                    if (!calculatedHolePositions) {
                        holeLocationX[j][i] = relativeX + 5;
                        holeLocationY[j][i] = relativeY + 5;
                    }
                }
            }

            calculatedHolePositions = true;

            int nearestX = 0, nearestY = 0;

            // Check if the mouse is clicked
            if (Mouse.leftClicked) {
                mouseX = MouseInfo.getPointerInfo().getLocation().x - BattleShip.getWindowLocationX();
                mouseY = MouseInfo.getPointerInfo().getLocation().y - BattleShip.getWindowLocationY() - 100;

                // Calculate the shortest distance to find the nearest hole
                double shortestDistance = 999999999;

                for (int i = 8; i >= 0; i--) {
                    for (int j = 0; j < 11; j++) {
                        // System.out.println((mouseX - holeLocationX[i][j]) * (mouseX -
                        // holeLocationX[i][j]) + (mouseY - holeLocationY[i][j]) * (mouseY -
                        // holeLocationY[i][j]) + " ??");

                        double currentDistance = (mouseX - holeLocationX[j][i]) * (mouseX - holeLocationX[j][i])
                                + (mouseY - holeLocationY[j][i]) * (mouseY - holeLocationY[j][i]);
                        if (currentDistance < shortestDistance) {
                            shortestDistance = currentDistance;

                            nearestX = j;
                            nearestY = i;

                        }
                    }
                }

                if (targetShipId == -1) {
                    // Find the nearest ship by searching all occupied grids
                    for (int i = 0; i < BattleShip.shipList.size(); i++) {
                        for (int gridIndex = 0; gridIndex < BattleShip.shipList.get(i).occupiedGridX
                                .size(); gridIndex++) {
                            int distance = (nearestX - BattleShip.shipList.get(i).occupiedGridX.get(gridIndex))
                                    * (nearestX - BattleShip.shipList.get(i).occupiedGridX.get(gridIndex))
                                    + (nearestY - BattleShip.shipList.get(i).occupiedGridY.get(gridIndex))
                                    * (nearestY - BattleShip.shipList.get(i).occupiedGridY.get(gridIndex));

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
            for (int i = 0; i < BattleShip.shipList.size(); i++) {
                double offset = 0;

                switch (BattleShip.shipList.get(i).length) {
                    case 2:
                        if (BattleShip.shipList.get(i).direction == 1)
                            offset = 0.5;
                        else if (BattleShip.shipList.get(i).direction == 2)
                            offset = 0.5;
                        else if (BattleShip.shipList.get(i).direction == 3)
                            offset = 1.25;
                        else
                            offset = 1.25;
                        break;
                    case 3:
                        offset = 1.25;
                        break;
                    case 4:
                        if (BattleShip.shipList.get(i).direction == 1)
                            offset = 1.25;
                        else if (BattleShip.shipList.get(i).direction == 2)
                            offset = 1.25;
                        else if (BattleShip.shipList.get(i).direction == 3)
                            offset = 2.25;
                        else
                            offset = 2.25;
                        break;
                    case 5:
                        offset = 2.25;
                        break;
                }

                if (i != targetShipId) {
                    if (Mouse.leftClicked)
                        BattleShip.shipList.get(i).validateLocation();

                    if (BattleShip.shipList.get(i).direction == 1 | BattleShip.shipList.get(i).direction == 3) {
                        if (BattleShip.shipList.get(i).validity)
                            graphics2D.drawImage(shipImage,
                                    holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
                        else
                            graphics2D.drawImage(redShipImage,
                                    holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
                    } else {
                        if (BattleShip.shipList.get(i).validity)
                            graphics2D.drawImage(horizontalShipImage,
                                    holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    40, BattleShip.shipList.get(i).length * holeImageSize - 2, this);
                        else
                            graphics2D.drawImage(horizontalRedShipImage,
                                    holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - 30 / 2,
                                    holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
                                            .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                    40, BattleShip.shipList.get(i).length * holeImageSize - 2, this);
                    }
                } else {
                    if (targetShipId > -1) {
                        BattleShip.shipList.get(targetShipId)
                                .recalculate(BattleShip.shipList.get(targetShipId).direction, nearestX, nearestY);

                        BattleShip.shipList.get(targetShipId).validateLocation();

                        if (BattleShip.shipList.get(targetShipId).validity) {
                            if (BattleShip.shipList.get(targetShipId).direction == 1
                                    | BattleShip.shipList.get(targetShipId).direction == 3) {
                                graphics2D.drawImage(shipImage,
                                        holeLocationX[nearestX][nearestY] - (int) (offset * holeImageSize),
                                        holeLocationY[nearestX][nearestY] - 30 / 2,
                                        BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
                            } else {
                                graphics2D.drawImage(horizontalShipImage, holeLocationX[nearestX][nearestY] - 30 / 2,
                                        holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
                                        BattleShip.shipList.get(i).length * holeImageSize - 2, this);
                            }
                        } else {
                            if (BattleShip.shipList.get(targetShipId).direction == 1
                                    | BattleShip.shipList.get(targetShipId).direction == 3) {
                                graphics2D.drawImage(redShipImage,
                                        holeLocationX[nearestX][nearestY] - (int) (offset * holeImageSize),
                                        holeLocationY[nearestX][nearestY] - 30 / 2,
                                        BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
                            } else {
                                graphics2D.drawImage(horizontalRedShipImage, holeLocationX[nearestX][nearestY] - 30 / 2,
                                        holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
                                        BattleShip.shipList.get(i).length * holeImageSize - 2, this);
                            }
                        }
                    }
                }
            }

            graphics2D.finalize();
        }
    }

    public void clearAll() {
        clearAll = true;
        repaint();
    }
}