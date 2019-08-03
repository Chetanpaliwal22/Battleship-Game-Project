package view;

import constants.Constants;
import controller.Mouse;
import jaco.mp3.player.MP3Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;


/**
 * Handle graphic rendering
 */
public class Renderer extends JComponent {
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
    private static int[] explosionBoardId = new int[5];

    private static int[] explosionX = new int[5];
    private static int[] explosionY = new int[5];
    private static int[] explosionFrameIndex = new int[5];

    private Image explosionImage[] = new Image[40];


    // Animation attributes
    private static int[] waterSplashBoardId = new int[5];

    private static int[] waterSplashX = new int[5];
    private static int[] waterSplashY = new int[5];
    private static int[] waterSplashFrameIndex = new int[5];

    private Image waterSplashImage[] = new Image[30];

    private int frameCount = 0;

    private Random random = new Random();

    private Image oceanImageLeft, oceanImageRight;

    private int oceanImageLeftPositionX = 0, oceanImageLeftPositionY = 0;

    private int oceanImageLeftMovingDirectionX = 1, oceanImageLeftMovingDirectionY = 1;

    private int oceanImageRightPositionX = 0, oceanImageRightPositionY = 0;

    private int oceanImageRightMovingDirectionX = -1, oceanImageRightMovingDirectionY = -1;

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
        oceanImageLeft = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/OceanLeft.png");
        oceanImageRight = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/OceanRight.png");

        for (int i = 1; i <= 30; i++)
            waterSplashImage[i - 1] = Toolkit.getDefaultToolkit().getImage("src/view/resources/animation/waterSplash/" + i + ".png");

        for (int i = 1; i <= 40; i++)
            explosionImage[i - 1] = Toolkit.getDefaultToolkit().getImage("src/view/resources/animation/explosion/" + i + ".png");

    }

    /**
     * a method to render the graphic in the window
     *
     * @param g grphics for the UI
     */
    public void paintComponent(Graphics g) {
        setBackground(new Color(88, 111, 78));
        setBackground(new Color(99, 222, 231));

        Graphics2D graphics2D = (Graphics2D) g;

        frameCount += 1;

        if (frameCount > 999999)
            frameCount = 0;

        // Human board and AI board background
        if (!MainWindow.enableDynamicOcean) {
            // Human board background
            g.setColor(new Color(99, 222, 231));
            g.fillRect(0, 0, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT);

            // AI board background
            g.setColor(new Color(168, 255, 0));
            g.fillRect(Constants.WINDOW_WIDTH / 2, 0, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT);
        } else {
            g.drawImage(oceanImageLeft, oceanImageLeftPositionX - 15, oceanImageLeftPositionY - 15, Constants.WINDOW_WIDTH / 2 + 30, Constants.WINDOW_HEIGHT + 30, this);

            if (frameCount % 6 == 0) {
                oceanImageLeftPositionX += oceanImageLeftMovingDirectionX;
                oceanImageLeftPositionY += oceanImageLeftMovingDirectionY;
            }
 
            if (oceanImageLeftPositionX <= -15) {
                oceanImageLeftPositionX = -15;

                oceanImageLeftMovingDirectionX = random.nextInt(3) + 1;
            } else if (oceanImageLeftPositionX > 15) {
                oceanImageLeftPositionX = 15;

                oceanImageLeftMovingDirectionX = -random.nextInt(3) - 1;
            }

            if (oceanImageLeftPositionY <= -15) {
                oceanImageLeftPositionY = -15;

                oceanImageLeftMovingDirectionY = random.nextInt(3) + 1;
            } else if (oceanImageLeftPositionY > 15) {
                oceanImageLeftPositionY = 15;

                oceanImageLeftMovingDirectionY = -random.nextInt(3) - 1;
            }


            g.drawImage(oceanImageRight, Constants.WINDOW_WIDTH / 2 + oceanImageRightPositionX, oceanImageRightPositionY - 15, Constants.WINDOW_WIDTH / 2 + 30, Constants.WINDOW_HEIGHT + 30, this);

            if (frameCount % 5 == 0) {
//                oceanImageRightPositionX += oceanImageRightMovingDirectionX;
                oceanImageRightPositionY += oceanImageRightMovingDirectionY;
            }

//            if (oceanImageRightPositionX <= -15) {
//                oceanImageRightPositionX = -15;
//
//                oceanImageRightMovingDirectionX = random.nextInt(3) + 1;
//            } else if (oceanImageRightPositionX > 15) {
//                oceanImageRightPositionX = 15;
//
//                oceanImageRightMovingDirectionX = -random.nextInt(3) - 1;
//            }

            if (oceanImageRightPositionY <= -15) {
                oceanImageRightPositionY = -15;

                oceanImageRightMovingDirectionY = random.nextInt(3) + 1;
            } else if (oceanImageRightPositionY > 15) {
                oceanImageRightPositionY = 15;

                oceanImageRightMovingDirectionY = -random.nextInt(3) - 1;
            }
        }


        int nearestX = 0, nearestY = 0;

        mouseX = MouseInfo.getPointerInfo().getLocation().x - MainWindow.getWindowLocationX();
        mouseY = MouseInfo.getPointerInfo().getLocation().y - MainWindow.getWindowLocationY() - 100;

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

                        if (distance < 1.5) {
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
                                MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
                    else
                        graphics2D.drawImage(redShipImage,
                                holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                        .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                        .get(i).pivotGridY] - 30 / 2,
                                MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
                } else {
                    if (MainWindow.shipList.get(i).validity & !MainWindow.shipList.get(i).sunk)
                        graphics2D.drawImage(horizontalShipImage,
                                holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                        .get(i).pivotGridY] - 30 / 2,
                                holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                        .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                40, MainWindow.shipList.get(i).size * holeImageSize - 5, this);
                    else
                        graphics2D.drawImage(horizontalRedShipImage,
                                holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                        .get(i).pivotGridY] - 30 / 2,
                                holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
                                        .get(i).pivotGridY] - (int) (offset * holeImageSize),
                                40, MainWindow.shipList.get(i).size * holeImageSize - 5, this);
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
                                    MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
                        } else {
                            graphics2D.drawImage(horizontalShipImage, holeLocationX[nearestX][nearestY] - 30 / 2,
                                    holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
                                    MainWindow.shipList.get(i).size * holeImageSize - 5, this);
                        }
                    } else {
                        if (MainWindow.shipList.get(targetShipId).direction == 1
                                | MainWindow.shipList.get(targetShipId).direction == 3) {
                            graphics2D.drawImage(redShipImage,
                                    holeLocationX[nearestX][nearestY] - (int) (offset * holeImageSize),
                                    holeLocationY[nearestX][nearestY] - 30 / 2,
                                    MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
                        } else {
                            graphics2D.drawImage(horizontalRedShipImage, holeLocationX[nearestX][nearestY] - 30 / 2,
                                    holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
                                    MainWindow.shipList.get(i).size * holeImageSize - 5, this);
                        }
                    }
                }
            }
        }

        // Render all grids
        for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
            for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
                int relativeX = 0, relativeY = 0;

                if (MainWindow.humanBoard.getBoardState()[i][j] == 0)
                    graphics2D.drawImage(holeImage, holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);
                else if (MainWindow.humanBoard.getBoardState()[i][j] == 1)
                    graphics2D.drawImage(missedHoleImage, holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);
                else if (MainWindow.humanBoard.getBoardState()[i][j] == 2)
                    graphics2D.drawImage(hitHoleImage, holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);

                if (MainWindow.AIBoard.getBoardState()[i][j] == 0)
                    graphics2D.drawImage(holeImage, Constants.WINDOW_WIDTH / 2 + holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);
                else if (MainWindow.AIBoard.getBoardState()[i][j] == 1)
                    graphics2D.drawImage(missedHoleImage, Constants.WINDOW_WIDTH / 2 + holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);
                else if (MainWindow.AIBoard.getBoardState()[i][j] == 2)
                    graphics2D.drawImage(hitHoleImage, Constants.WINDOW_WIDTH / 2 + holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);

                if (!calculatedHolePositions) {
                    relativeX = holeImageSize / 2 + j * holeImageSize;
                    relativeY = holeImageSize / 2 + (Constants.BOARD_SIZE.x - i - 1) * holeImageSize;

                    holeLocationX[j][i] = relativeX + 5;
                    holeLocationY[j][i] = relativeY + 5;

                    aiBoardHoleLocationX[j][i] = Constants.WINDOW_WIDTH / 2 + relativeX + 10;
                    aiBoardHoleLocationY[j][i] = relativeY + 10;

                    // Prewarm the animation
                    for (int index = 0; index < waterSplashX.length; index++) {
                        if (waterSplashFrameIndex[index] < 30) {
                            graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[index]], holeLocationX[waterSplashX[index]][waterSplashY[index]] - 50, holeLocationY[waterSplashX[index]][waterSplashY[index]] - 50, 100, 100, this);
                            graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[index]], aiBoardHoleLocationX[waterSplashX[index]][waterSplashY[index]] - 50, aiBoardHoleLocationY[waterSplashX[index]][waterSplashY[index]] - 50, 100, 100, this);

                            waterSplashFrameIndex[index] += 1;
                        } else {
                            waterSplashX[index] = -1;
                            waterSplashY[index] = -1;
                        }
                    }

                    for (int index = 0; index < explosionX.length; index++) {
                        if (explosionFrameIndex[index] < 40) {
                            graphics2D.drawImage(explosionImage[explosionFrameIndex[index]], holeLocationX[explosionX[index]][explosionY[index]] - 50, holeLocationY[explosionX[index]][explosionY[index]] - 50, 100, 100, this);
                            graphics2D.drawImage(explosionImage[explosionFrameIndex[index]], aiBoardHoleLocationX[explosionX[index]][explosionY[index]] - 50, aiBoardHoleLocationY[explosionX[index]][explosionY[index]] - 50, 100, 100, this);

                            explosionFrameIndex[index] += 1;
                        } else {
                            explosionX[index] = -1;
                            explosionY[index] = -1;
                        }
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
                    double currentDistance = (mouseX - aiBoardHoleLocationX[j][i]) * (mouseX - aiBoardHoleLocationX[j][i]) + (mouseY - aiBoardHoleLocationY[j][i] - 30) * (mouseY - aiBoardHoleLocationY[j][i] - 30);
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

        // Water splash animation
        for (int animationIndex = 0; animationIndex < waterSplashX.length; animationIndex++) {
            if (waterSplashX[animationIndex] != -1) {
                if (waterSplashFrameIndex[animationIndex] < 30) {
                    if (waterSplashBoardId[animationIndex] == 1)
                        graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[animationIndex]], holeLocationX[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50, holeLocationY[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50, 100, 100, this);
                    else if (waterSplashBoardId[animationIndex] == 2)
                        graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[animationIndex]], aiBoardHoleLocationX[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50, aiBoardHoleLocationY[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50, 100, 100, this);

                    waterSplashFrameIndex[animationIndex] += 1;
                } else {
                    waterSplashX[animationIndex] = -1;
                    waterSplashY[animationIndex] = -1;
                }
            }
        }

        // Explosion animation
        for (int animationIndex = 0; animationIndex < explosionX.length; animationIndex++) {
            if (explosionX[animationIndex] != -1) {
                if (explosionFrameIndex[animationIndex] < 40) {
                    if (explosionBoardId[animationIndex] == 1)
                        graphics2D.drawImage(explosionImage[explosionFrameIndex[animationIndex]], holeLocationX[explosionX[animationIndex]][explosionY[animationIndex]] - 50, holeLocationY[explosionX[animationIndex]][explosionY[animationIndex]] - 50, 100, 100, this);
                    else if (explosionBoardId[animationIndex] == 2)
                        graphics2D.drawImage(explosionImage[explosionFrameIndex[animationIndex]], aiBoardHoleLocationX[explosionX[animationIndex]][explosionY[animationIndex]] - 50, aiBoardHoleLocationY[explosionX[animationIndex]][explosionY[animationIndex]] - 50, 100, 100, this);

                    explosionFrameIndex[animationIndex] += 1;
                } else {
                    explosionX[animationIndex] = -1;
                    explosionY[animationIndex] = -1;
                }
            }
        }

        graphics2D.finalize();

        // Use sleep method to reduce the performance overhead
        try {
            if (calculatedHolePositions)
                Thread.sleep(12);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void playWaterSplashAnimation(int boardId, int targetWaterSplashX, int targetWaterSplashY) {
        if (MainWindow.enableSpecialEffect)
            assignWaterSplashAnimation(boardId, targetWaterSplashX, targetWaterSplashY);

        if (MainWindow.enableSoundEffect) {
            try {
                new MP3Player(new File(Constants.WATER_SPLASH_SOUND)).play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void assignWaterSplashAnimation(int boardId, int targetWaterSplashX, int targetWaterSplashY) {
        for (int i = 0; i < waterSplashX.length; i++) {
            if (waterSplashX[i] == -1) {
                waterSplashBoardId[i] = boardId;
                waterSplashFrameIndex[i] = 0;
                waterSplashX[i] = targetWaterSplashX;
                waterSplashY[i] = targetWaterSplashY;
                return;
            }
        }
    }

    public static void playExplosionAnimation(int boardId, int targetExplosionX, int targetExplosionY) {
        if (MainWindow.enableSpecialEffect)
            assignExplosionAnimation(boardId, targetExplosionX, targetExplosionY);

        if (MainWindow.enableSoundEffect) {
            try {
                new MP3Player(new File(Constants.EXPLOSION_SOUND)).play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void assignExplosionAnimation(int boardId, int targetExplosionX, int targetExplosionY) {
        for (int i = 0; i < explosionX.length; i++) {
            if (explosionX[i] == -1) {
                explosionBoardId[i] = boardId;
                explosionFrameIndex[i] = 0;
                explosionX[i] = targetExplosionX;
                explosionY[i] = targetExplosionY;
                return;
            }
        }
    }
}