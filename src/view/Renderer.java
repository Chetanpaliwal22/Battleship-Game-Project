package view;

import constants.Constants;
import controller.Mouse;
import jaco.mp3.player.MP3Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

/**
 * Handles graphic rendering including game board, ships, animation
 */
public class Renderer extends JComponent {
	private boolean calculatedHolePositions = false;

	public static int[][] holeLocationX = new int[9][11], holeLocationY = new int[9][11];

	public static int[][] aiBoardHoleLocationX = new int[9][11], aiBoardHoleLocationY = new int[9][11];

	public static int holeImageSize = 0;

	private Image holeImage, missedHoleImage, hitHoleImage;

	// private Image shipImage, horizontalShipImage, redShipImage,
	// horizontalRedShipImage;

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

	private Image destroyerImage1, destroyerImage2, destroyerImage3, destroyerImage4;
	private Image destroyerRedImage1, destroyerRedImage2, destroyerRedImage3, destroyerRedImage4;

	private Image submarineImage1, submarineImage2, submarineImage3, submarineImage4;
	private Image submarineRedImage1, submarineRedImage2, submarineRedImage3, submarineRedImage4;

	private Image cruiserImage1, cruiserImage2, cruiserImage3, cruiserImage4;
	private Image cruiserRedImage1, cruiserRedImage2, cruiserRedImage3, cruiserRedImage4;

	private Image battleshipImage1, battleshipImage2, battleshipImage3, battleshipImage4;
	private Image battleshipRedImage1, battleshipRedImage2, battleshipRedImage3, battleshipRedImage4;

	private Image carrierImage1, carrierImage2, carrierImage3, carrierImage4;
	private Image carrierRedImage1, carrierRedImage2, carrierRedImage3, carrierRedImage4;

	private int oceanImageLeftPositionX = 0, oceanImageLeftPositionY = 0;

	private int oceanImageLeftMovingDirectionX = 1, oceanImageLeftMovingDirectionY = 1;

	private int oceanImageRightPositionX = 0, oceanImageRightPositionY = 0;

	private int oceanImageRightMovingDirectionX = -1, oceanImageRightMovingDirectionY = -1;

	public Renderer() {
		holeImageSize = (Constants.WINDOW_WIDTH / 2) / Constants.BOARD_SIZE.y - 2;

		holeImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/Hole.png");
		missedHoleImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/MissedHole.png");
		hitHoleImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/HitHole.png");
		// shipImage =
		// Toolkit.getDefaultToolkit().getImage("src/view/resources/images/Ship.png");
		// horizontalShipImage =
		// Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ShipHorizontal.png");
		// redShipImage =
		// Toolkit.getDefaultToolkit().getImage("src/view/resources/images/RedShip.png");
		// horizontalRedShipImage =
		// Toolkit.getDefaultToolkit().getImage("src/view/resources/images/RedShipHorizontal.png");
		targetImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/Target.png");

		destroyerImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Destroyer1.png");
		destroyerImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Destroyer2.png");
		destroyerImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Destroyer3.png");
		destroyerImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Destroyer4.png");

		destroyerRedImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedDestroyer1.png");
		destroyerRedImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedDestroyer2.png");
		destroyerRedImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedDestroyer3.png");
		destroyerRedImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedDestroyer4.png");

		submarineImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Submarine1.png");
		submarineImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Submarine2.png");
		submarineImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Submarine3.png");
		submarineImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Submarine4.png");

		submarineRedImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedSubmarine1.png");
		submarineRedImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedSubmarine2.png");
		submarineRedImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedSubmarine3.png");
		submarineRedImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedSubmarine4.png");

		cruiserImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Cruiser1.png");
		cruiserImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Cruiser2.png");
		cruiserImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Cruiser3.png");
		cruiserImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Cruiser4.png");

		cruiserRedImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCruiser1.png");
		cruiserRedImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCruiser2.png");
		cruiserRedImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCruiser3.png");
		cruiserRedImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCruiser4.png");

		battleshipImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/BattleShip1.png");
		battleshipImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/BattleShip2.png");
		battleshipImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/BattleShip3.png");
		battleshipImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/BattleShip4.png");

		battleshipRedImage1 = Toolkit.getDefaultToolkit()
				.getImage("src/view/resources/images/ships/RedBattleShip1.png");
		battleshipRedImage2 = Toolkit.getDefaultToolkit()
				.getImage("src/view/resources/images/ships/RedBattleShip2.png");
		battleshipRedImage3 = Toolkit.getDefaultToolkit()
				.getImage("src/view/resources/images/ships/RedBattleShip3.png");
		battleshipRedImage4 = Toolkit.getDefaultToolkit()
				.getImage("src/view/resources/images/ships/RedBattleShip4.png");

		carrierImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Carrier1.png");
		carrierImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Carrier2.png");
		carrierImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Carrier3.png");
		carrierImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/Carrier4.png");

		carrierRedImage1 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCarrier1.png");
		carrierRedImage2 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCarrier2.png");
		carrierRedImage3 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCarrier3.png");
		carrierRedImage4 = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/ships/RedCarrier4.png");

		oceanImageLeft = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/OceanLeft.png");
		oceanImageRight = Toolkit.getDefaultToolkit().getImage("src/view/resources/images/OceanRight.png");

		for (int i = 1; i <= 30; i++)
			waterSplashImage[i - 1] = Toolkit.getDefaultToolkit()
					.getImage("src/view/resources/animation/waterSplash/" + i + ".png");

		for (int i = 1; i <= 40; i++)
			explosionImage[i - 1] = Toolkit.getDefaultToolkit()
					.getImage("src/view/resources/animation/explosion/" + i + ".png");

	}

	/**
	 * a method to render the graphic in the window
	 *
	 * @param g
	 *            grphics for the UI
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
			g.drawImage(oceanImageLeft, oceanImageLeftPositionX - 15, oceanImageLeftPositionY - 15,
					Constants.WINDOW_WIDTH / 2 + 30, Constants.WINDOW_HEIGHT + 30, this);

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

			g.drawImage(oceanImageRight, Constants.WINDOW_WIDTH / 2 + oceanImageRightPositionX,
					oceanImageRightPositionY - 15, Constants.WINDOW_WIDTH / 2 + 30, Constants.WINDOW_HEIGHT + 30, this);

			if (frameCount % 5 == 0) {
				// oceanImageRightPositionX += oceanImageRightMovingDirectionX;
				oceanImageRightPositionY += oceanImageRightMovingDirectionY;
			}

			// if (oceanImageRightPositionX <= -15) {
			// oceanImageRightPositionX = -15;
			//
			// oceanImageRightMovingDirectionX = random.nextInt(3) + 1;
			// } else if (oceanImageRightPositionX > 15) {
			// oceanImageRightPositionX = 15;
			//
			// oceanImageRightMovingDirectionX = -random.nextInt(3) - 1;
			// }

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
		mouseY = MouseInfo.getPointerInfo().getLocation().y - MainWindow.getWindowLocationY() - 105;

		// Check if the mouse is clicked
		if (Mouse.leftClicked && !MainWindow.startedGame) {
			// Calculate the shortest distance to find the nearest hole
			double shortestDistance = 999999999;

			for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
				for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
					double currentDistance = (mouseX - holeLocationX[j][i]) * (mouseX - holeLocationX[j][i])
							+ (mouseY - holeLocationY[j][i] - 30) * (mouseY - holeLocationY[j][i] - 30);
					if (currentDistance < shortestDistance) {
						shortestDistance = currentDistance;

						nearestX = j;
						nearestY = i;
					}
				}
			}

			if (targetShipId == -1) { // Find the nearest ship by searching all occupied grids

				for (int i = 0; i < MainWindow.shipList.size(); i++) {
					for (int gridIndex = 0; gridIndex < MainWindow.shipList.get(i).occupiedGridX.size(); gridIndex++) {
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

		// Render all grids
		for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
			for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
				int relativeX = 0, relativeY = 0;

				if (MainWindow.humanBoard.getBoardState()[i][j] == 0)
					graphics2D.drawImage(holeImage, holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);
				else if (MainWindow.humanBoard.getBoardState()[i][j] == 1)
					graphics2D.drawImage(missedHoleImage, holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20,
							this);
				else if (MainWindow.humanBoard.getBoardState()[i][j] == 2)
					graphics2D.drawImage(hitHoleImage, holeLocationX[j][i] - 5, holeLocationY[j][i] - 5, 20, 20, this);

				if (MainWindow.AIBoard.getBoardState()[i][j] == 0)
					graphics2D.drawImage(holeImage, Constants.WINDOW_WIDTH / 2 + holeLocationX[j][i] - 5,
							holeLocationY[j][i] - 5, 20, 20, this);
				else if (MainWindow.AIBoard.getBoardState()[i][j] == 1)
					graphics2D.drawImage(missedHoleImage, Constants.WINDOW_WIDTH / 2 + holeLocationX[j][i] - 5,
							holeLocationY[j][i] - 5, 20, 20, this);
				else if (MainWindow.AIBoard.getBoardState()[i][j] == 2)
					graphics2D.drawImage(hitHoleImage, Constants.WINDOW_WIDTH / 2 + holeLocationX[j][i] - 5,
							holeLocationY[j][i] - 5, 20, 20, this);

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
							graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[index]],
									holeLocationX[waterSplashX[index]][waterSplashY[index]] - 50,
									holeLocationY[waterSplashX[index]][waterSplashY[index]] - 50, 100, 100, this);
							graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[index]],
									aiBoardHoleLocationX[waterSplashX[index]][waterSplashY[index]] - 50,
									aiBoardHoleLocationY[waterSplashX[index]][waterSplashY[index]] - 50, 100, 100,
									this);

							waterSplashFrameIndex[index] += 1;
						} else {
							waterSplashX[index] = -1;
							waterSplashY[index] = -1;
						}
					}

					for (int index = 0; index < explosionX.length; index++) {
						if (explosionFrameIndex[index] < 40) {
							graphics2D.drawImage(explosionImage[explosionFrameIndex[index]],
									holeLocationX[explosionX[index]][explosionY[index]] - 50,
									holeLocationY[explosionX[index]][explosionY[index]] - 50, 100, 100, this);
							graphics2D.drawImage(explosionImage[explosionFrameIndex[index]],
									aiBoardHoleLocationX[explosionX[index]][explosionY[index]] - 50,
									aiBoardHoleLocationY[explosionX[index]][explosionY[index]] - 50, 100, 100, this);

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
					double currentDistance = (mouseX - aiBoardHoleLocationX[j][i])
							* (mouseX - aiBoardHoleLocationX[j][i])
							+ (mouseY - aiBoardHoleLocationY[j][i] - 30) * (mouseY - aiBoardHoleLocationY[j][i] - 30);
					if (currentDistance < shortestDistance) {
						shortestDistance = currentDistance;

						fireTargetX = j;
						fireTargetY = i;
					}
				}
			}

			// Render the target image following the cursor
			if (!MainWindow.freezing) {
				if (Renderer.fireTargetX != -1 & Renderer.fireTargetY != -1) {
					if (shortestDistance < 1000
							& MainWindow.AIBoard.getBoardState()[Renderer.fireTargetY][Renderer.fireTargetX] == 0) {
						graphics2D.drawImage(targetImage, aiBoardHoleLocationX[fireTargetX][fireTargetY] - 35,
								aiBoardHoleLocationY[fireTargetX][fireTargetY] - 35, 70, 70, this);
					} else {
						fireTargetX = -1;
						fireTargetY = -1;
					}
				}
			}
		}

		// graphics2D.drawImage(destroyerImage1, holeLocationX[3][4] - 15,
		// holeLocationY[5][5] - 15, 86, 35, this);
		// graphics2D.drawImage(destroyerImage2, holeLocationX[2][7] - 15,
		// holeLocationY[3][3] - 15, 35, 86, this);
		// graphics2D.drawImage(destroyerImage3, holeLocationX[5][1] - 15,
		// holeLocationY[8][7] - 15, 86, 35, this);
		// graphics2D.drawImage(destroyerImage4, holeLocationX[7][7] - 15,
		// holeLocationY[5][5] - 15, 35, 86, this);

		// graphics2D.drawImage(submarineImage1, holeLocationX[3][4] - 15,
		// holeLocationY[5][5] - 15, 157, 40, this);
		// graphics2D.drawImage(submarineImage2, holeLocationX[2][7] - 15,
		// holeLocationY[3][3] - 15, 40, 157, this);
		// graphics2D.drawImage(submarineImage3, holeLocationX[5][1] - 15,
		// holeLocationY[8][7] - 15, 157, 40, this);
		// graphics2D.drawImage(submarineImage4, holeLocationX[7][7] - 15,
		// holeLocationY[5][5] - 15, 40, 157, this);

		// graphics2D.drawImage(cruiserImage1, holeLocationX[3][4] - 15,
		// holeLocationY[5][5] - 15, 141, 40, this);
		// graphics2D.drawImage(cruiserImage2, holeLocationX[2][7] - 15,
		// holeLocationY[3][3] - 15, 40, 141, this);
		// graphics2D.drawImage(cruiserImage3, holeLocationX[5][1] - 15,
		// holeLocationY[8][7] - 15, 141, 40, this);
		// graphics2D.drawImage(cruiserImage4, holeLocationX[7][7] - 15,
		// holeLocationY[5][5] - 15, 40, 141, this);

		// graphics2D.drawImage(battleshipImage1, holeLocationX[3][4] - 15,
		// holeLocationY[5][5] - 15, 196, 40, this);
		// graphics2D.drawImage(battleshipImage2, holeLocationX[2][7] - 15,
		// holeLocationY[3][3] - 15, 40, 196, this);
		// graphics2D.drawImage(battleshipImage3, holeLocationX[5][1] - 15,
		// holeLocationY[8][7] - 15, 196, 40, this);
		// graphics2D.drawImage(battleshipImage4, holeLocationX[7][7] - 15,
		// holeLocationY[5][5] - 15, 40, 196, this);

		// graphics2D.drawImage(carrierImage1, holeLocationX[3][4] - 15,
		// holeLocationY[5][5] - 15, 245, 48, this);
		// graphics2D.drawImage(carrierImage2, holeLocationX[2][7] - 15,
		// holeLocationY[3][3] - 15, 48, 245, this);
		// graphics2D.drawImage(carrierImage3, holeLocationX[5][1] - 15,
		// holeLocationY[8][7] - 15, 245, 48, this);
		// graphics2D.drawImage(carrierImage4, holeLocationX[7][7] - 15,
		// holeLocationY[5][5] - 15, 48, 245, this);

		// Render all five ships
		for (int i = 0; i < MainWindow.shipList.size(); i++) {
			double offset = 0;

			switch (MainWindow.shipList.get(i).size) {
			case 2:
				if (MainWindow.shipList.get(i).direction == 1)
					offset = 0.2;
				else if (MainWindow.shipList.get(i).direction == 2)
					offset = 0.2;
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
					if (MainWindow.shipList.get(i).validity & !MainWindow.shipList.get(i).sunk) {
						if (MainWindow.shipList.get(i).direction == 1) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						} else if (MainWindow.shipList.get(i).direction == 3) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						}
						// graphics2D.drawImage(shipImage,
						// holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - (int) (offset * holeImageSize),
						// holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - 30 / 2, MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
					} else {
						if (MainWindow.shipList.get(i).direction == 1) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						} else if (MainWindow.shipList.get(i).direction == 3) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						}
						// graphics2D.drawImage(redShipImage,
						// holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - (int) (offset * holeImageSize),
						// holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - 30 / 2, MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
					}
				} else {
					if (MainWindow.shipList.get(i).validity & !MainWindow.shipList.get(i).sunk) {
						if (MainWindow.shipList.get(i).direction == 2) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						} else if (MainWindow.shipList.get(i).direction == 4) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						}
						// graphics2D.drawImage(horizontalShipImage,
						// holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - 30 / 2,
						// holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - (int) (offset * holeImageSize), 40, MainWindow.shipList.get(i).size *
						// holeImageSize - 5, this);
					} else {
						if (MainWindow.shipList.get(i).direction == 2) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						} else if (MainWindow.shipList.get(i).direction == 4) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						}

						// graphics2D.drawImage(horizontalRedShipImage,
						// holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - 30 / 2,
						// holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList.get(i).pivotGridY]
						// - (int) (offset * holeImageSize), 40, MainWindow.shipList.get(i).size *
						// holeImageSize - 5, this);
					}
				}
			} else {
				if (targetShipId > -1) {
					MainWindow.shipList.get(targetShipId).recalculate(MainWindow.shipList.get(targetShipId).direction,
							nearestX, nearestY);

					MainWindow.shipList.get(targetShipId).validateLocation();

					if (MainWindow.shipList.get(targetShipId).validity & !MainWindow.shipList.get(targetShipId).sunk) {
						if (MainWindow.shipList.get(i).direction == 1) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						} else if (MainWindow.shipList.get(i).direction == 3) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						} else if (MainWindow.shipList.get(i).direction == 2) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						} else if (MainWindow.shipList.get(i).direction == 4) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						}

						// if (MainWindow.shipList.get(targetShipId).direction == 1 |
						// MainWindow.shipList.get(targetShipId).direction == 3) {
						// graphics2D.drawImage(shipImage, holeLocationX[nearestX][nearestY] - (int)
						// (offset * holeImageSize), holeLocationY[nearestX][nearestY] - 30 / 2,
						// MainWindow.shipList.get(i).size * holeImageSize - 5, 40, this);
						// } else {
						// graphics2D.drawImage(horizontalShipImage, holeLocationX[nearestX][nearestY] -
						// 30 / 2, holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize),
						// 40, MainWindow.shipList.get(i).size * holeImageSize - 5, this);
						// }
					} else {
						if (MainWindow.shipList.get(i).direction == 1) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage1,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						} else if (MainWindow.shipList.get(i).direction == 3) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										86, 35, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										157, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										141, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										196, 40, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage3,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										245, 48, this);
						} else if (MainWindow.shipList.get(i).direction == 2) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage2,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						} else if (MainWindow.shipList.get(i).direction == 4) {
							if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Destroyer") == 0)
								graphics2D.drawImage(destroyerRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										35, 86, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Submarine") == 0)
								graphics2D.drawImage(submarineRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 147, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Cruiser") == 0)
								graphics2D.drawImage(cruiserRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 141, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Battleship") == 0)
								graphics2D.drawImage(battleshipRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 15,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										40, 196, this);
							else if (MainWindow.shipList.get(i).name.compareToIgnoreCase("Carrier") == 0)
								graphics2D.drawImage(carrierRedImage4,
										holeLocationX[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - 17,
										holeLocationY[MainWindow.shipList.get(i).pivotGridX][MainWindow.shipList
												.get(i).pivotGridY] - (int) (offset * holeImageSize),
										48, 245, this);
						}
					}
				}
			}
		}

		// Water splash animation
		for (int animationIndex = 0; animationIndex < waterSplashX.length; animationIndex++) {
			if (waterSplashX[animationIndex] != -1) {
				if (waterSplashFrameIndex[animationIndex] < 30) {
					if (waterSplashBoardId[animationIndex] == 1)
						graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[animationIndex]],
								holeLocationX[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50,
								holeLocationY[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50, 100,
								100, this);
					else if (waterSplashBoardId[animationIndex] == 2)
						graphics2D.drawImage(waterSplashImage[waterSplashFrameIndex[animationIndex]],
								aiBoardHoleLocationX[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50,
								aiBoardHoleLocationY[waterSplashX[animationIndex]][waterSplashY[animationIndex]] - 50,
								100, 100, this);

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
						graphics2D.drawImage(explosionImage[explosionFrameIndex[animationIndex]],
								holeLocationX[explosionX[animationIndex]][explosionY[animationIndex]] - 50,
								holeLocationY[explosionX[animationIndex]][explosionY[animationIndex]] - 50, 100, 100,
								this);
					else if (explosionBoardId[animationIndex] == 2)
						graphics2D.drawImage(explosionImage[explosionFrameIndex[animationIndex]],
								aiBoardHoleLocationX[explosionX[animationIndex]][explosionY[animationIndex]] - 50,
								aiBoardHoleLocationY[explosionX[animationIndex]][explosionY[animationIndex]] - 50, 100,
								100, this);

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

	/**
	 * Play the water splash animation and sound effect
	 * 
	 * @param boardId
	 *            show board id
	 * @param targetWaterSplashX
	 *            x location
	 * @param targetWaterSplashY
	 *            y location
	 */
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

	/**
	 * Get a water splash animation object from the object pool
	 * 
	 * @param boardId
	 *            show board id
	 * @param targetWaterSplashX
	 *            x location
	 * @param targetWaterSplashY
	 *            y location
	 */
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

	/**
	 * Play the explosion animation and sound effect
	 * 
	 * @param boardId
	 *            boardid
	 * @param targetExplosionX
	 *            target explosion x
	 * @param targetExplosionY
	 *            target explosion y
	 */
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

	/**
	 * Get an explosion animation object from the object pool
	 * 
	 * @param boardId
	 *            boardid
	 * @param targetExplosionX
	 *            target explosion x
	 * @param targetExplosionY
	 *            target explosion y
	 */
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