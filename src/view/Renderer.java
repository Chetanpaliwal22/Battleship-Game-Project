package view;

//import controller.Mouse;

import constants.Constants;
import controller.Mouse;
import jaco.mp3.player.MP3Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;


/**
 * Handle graphic rendering
 */
public class Renderer extends JComponent {

    private boolean clearAll = false;

    private boolean calculatedHolePositions = false;

    public static int[][] holeLocationX = new int[9][11], holeLocationY = new int[9][11];

    public static int[][] aiBoardHoleLocationX = new int[9][11], aiBoardHoleLocationY = new int[9][11];

    public static int holeImageSize = 0;

    private Image holeImage, missedHoleImage, hitHoleImage, shipImage, horizontalShipImage, redShipImage, horizontalRedShipImage;

    private Image targetImage;

    int mouseX = 0, mouseY = 0;

    // The id showing which ship is being picked up
    public static int targetShipId = -1;

    // The coordinates to fire
    public static int fireTargetX = -1, fireTargetY = -1;


    // Animation attributes
    private static boolean startPlayingExplosionAnimation = false;
    private static int explosionBoardId = 0;
    public static int explosionX = 0, explosionY = 0;
    private static int explosionFrameIndex = 0;

    private Image explosionImage[] = new Image[40];

    public Renderer() {
        holeImageSize = (Constants.WINDOW_WIDTH / 2) / Constants.BOARD_SIZE.y - 2;

        holeImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/Hole.png");
        missedHoleImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/MissedHole.png");
        hitHoleImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/HitHole.png");
        shipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/Ship.png");
        horizontalShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ShipHorizontal.png");
        redShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/RedShip.png");
        horizontalRedShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/RedShipHorizontal.png");
        targetImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/Target.png");

        for (int i = 1; i <= 40; i++) {
            explosionImage[i - 1] = Toolkit.getDefaultToolkit().getImage("src/view/resources/animation/explosion/" + i + ".png");
        }

    }

    /**
     * a method to render the graphic in the window
     *
     * @param g grphics for the UI
     */
    public void paintComponent(Graphics g) {
        setBackground(new Color(88, 111, 78));
        setBackground(new Color(99, 222, 231));

        if (clearAll) {

            g.clearRect(0, 0, getWidth(), getHeight());

        } else {

            Graphics2D graphics2D = (Graphics2D) g;

            g.setColor(new Color(99, 222, 231));
            g.fillRect(0, 0, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT);

            // THe AI board
//            g.setColor(new Color(255, 138, 0));
            g.setColor(new Color(168, 255, 0));
//            g.setColor(new Color(255, 147, 20));
            g.fillRect(Constants.WINDOW_WIDTH / 2, 0, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT);

            int nearestX = 0, nearestY = 0;

            mouseX = MouseInfo.getPointerInfo().getLocation().x - MainWindow.getWindowLocationX();
            mouseY = MouseInfo.getPointerInfo().getLocation().y - MainWindow.getWindowLocationY() - 70;

            // Check if the mouse is clicked
            if (Mouse.leftClicked && !MainWindow.startedGame) {
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

                    if (Mouse.leftClicked && !MainWindow.startedGame)
                        MainWindow.shipList.get(i).validateLocation();

                    if (MainWindow.shipList.get(i).direction == 1 | MainWindow.shipList.get(i).direction == 3) {
                        if (MainWindow.shipList.get(i).validity & !MainWindow.shipList.get(i).sunk)
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
                        if (MainWindow.shipList.get(i).validity & !MainWindow.shipList.get(i).sunk)
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

                        if (MainWindow.shipList.get(targetShipId).validity & !MainWindow.shipList.get(targetShipId).sunk) {
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

                    if (MainWindow.humanBoard.getBoardState()[i][j] == 0)
                        graphics2D.drawImage(holeImage, relativeX, relativeY, 20, 20, this);
                    else if (MainWindow.humanBoard.getBoardState()[i][j] == 1)
                        graphics2D.drawImage(missedHoleImage, relativeX, relativeY, 20, 20, this);
                    else if (MainWindow.humanBoard.getBoardState()[i][j] == 2)
                        graphics2D.drawImage(hitHoleImage, relativeX, relativeY, 20, 20, this);

                    if (MainWindow.AIBoard.getBoardState()[i][j] == 0)
                        graphics2D.drawImage(holeImage, Constants.WINDOW_WIDTH / 2 + relativeX, relativeY, 20, 20, this);
                    else if (MainWindow.AIBoard.getBoardState()[i][j] == 1)
                        graphics2D.drawImage(missedHoleImage, Constants.WINDOW_WIDTH / 2 + relativeX, relativeY, 20, 20, this);
                    else if (MainWindow.AIBoard.getBoardState()[i][j] == 2)
                        graphics2D.drawImage(hitHoleImage, Constants.WINDOW_WIDTH / 2 + relativeX, relativeY, 20, 20, this);

//                    graphics2D.drawImage(holeImage, relativeX, relativeY, 20, 20, this);

                    if (!calculatedHolePositions) {
                        holeLocationX[j][i] = relativeX + 5;
                        holeLocationY[j][i] = relativeY + 5;

                        aiBoardHoleLocationX[j][i] = Constants.WINDOW_WIDTH / 2 + relativeX + 10;
                        aiBoardHoleLocationY[j][i] = relativeY + 10;

                        // Prewarm the animation function
                        if (explosionFrameIndex < 40) {
                            graphics2D.drawImage(explosionImage[explosionFrameIndex], aiBoardHoleLocationX[explosionX][explosionY] - 50, aiBoardHoleLocationY[explosionX][explosionY] - 50, 100, 100, this);
                            explosionFrameIndex += 1;
                        } else {
                            startPlayingExplosionAnimation = false;
                        }
                    }
                }
            }

            calculatedHolePositions = true;

            if (MainWindow.startedGame) {
                // Calculate the shortest distance to find the nearest hole
                double shortestDistance = 999999999;

                for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
                    for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
                        double currentDistance = (mouseX - aiBoardHoleLocationX[j][i]) * (mouseX - aiBoardHoleLocationX[j][i])
                                + (mouseY - aiBoardHoleLocationY[j][i]) * (mouseY - aiBoardHoleLocationY[j][i]);
                        if (currentDistance < shortestDistance) {
                            shortestDistance = currentDistance;

                            fireTargetX = j;
                            fireTargetY = i;
                        }
                    }
                }

                // Render the target image following the cursor
                if (Renderer.fireTargetX != -1 & Renderer.fireTargetY != -1) {
                    if (shortestDistance < 1000 & MainWindow.AIBoard.getBoardState()[Renderer.fireTargetY][Renderer.fireTargetX] == 0) {
                        graphics2D.drawImage(targetImage, aiBoardHoleLocationX[fireTargetX][fireTargetY] - 35, aiBoardHoleLocationY[fireTargetX][fireTargetY] - 35, 70, 70, this);
                    } else {
                        fireTargetX = -1;
                        fireTargetY = -1;
                    }
                }
            }

            if (startPlayingExplosionAnimation) {
                if (explosionFrameIndex < 40) {
                    if (explosionBoardId == 1)
                        graphics2D.drawImage(explosionImage[explosionFrameIndex], holeLocationX[explosionX][explosionY] - 50, holeLocationY[explosionX][explosionY] - 50, 100, 100, this);
                    else if (explosionBoardId == 2)
                        graphics2D.drawImage(explosionImage[explosionFrameIndex], aiBoardHoleLocationX[explosionX][explosionY] - 50, aiBoardHoleLocationY[explosionX][explosionY] - 50, 100, 100, this);

                    explosionFrameIndex += 1;
                } else {
                    startPlayingExplosionAnimation = false;
                }
            }

            graphics2D.finalize();
        }

        try { // Use sleep method to reduce the performance overhead
//            if (Mouse.leftClicked && !MainWindow.startedGame)
            Thread.sleep(8);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void playExplosionAnimation(int boardId) {
        explosionBoardId = boardId;

        try {
            new MP3Player(new File(Constants.EXPLOSION_SOUND)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        explosionFrameIndex = 0;
        startPlayingExplosionAnimation = true;
    }

    public static void playWaterSplashSound() {
        try {
            new MP3Player(new File(Constants.WATER_SPLASH_SOUND)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear the content of the window
     */
    public void clearAll() {
        clearAll = true;
        repaint();
    }
}