package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import model.AI;
import tools.Coordinate;

public class BattleShip extends JFrame implements ActionListener {

	AI myAI = new AI();

	private static BattleShip battleShip;

	private static Mouse mouse;

	// A list contains all the five ships
	public static List<Ship> shipList = new ArrayList<Ship>();

	public static final int WIDTH = 1600 /* (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3) */,
			HEIGHT = 700/* (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5) */;

	static char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K' };

	// Record grid array
	static JLabel[][] recordGridArray = new JLabel[9][11];

	// Button array
	static JButton[][] buttonArray = new JButton[9][11];

	public BattleShip() {
		super("BattleShip");
		setSize(WIDTH, HEIGHT);
		setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		Renderer renderer = new Renderer();
		add(renderer);

		// Register the mouse event
		mouse = new Mouse();
		addMouseListener(mouse);

		// Label label1 = new Label();
		// label1.setText("Select a grid to fire");
		// label1.setSize(10, 5);
		// label1.setAlignment(Label.CENTER);
		// add(label1);

		JPanel jPanel1 = new JPanel(new GridLayout(9, 11, 3, 3));

		// Generating all grids
		for (int i = 8; i >= 0; i--) {
			for (int j = 0; j < 11; j++) {
				buttonArray[i][j] = new JButton(alphabet[j] + " " + (i + 1));
				buttonArray[i][j].setName(i + "," + j);

				buttonArray[i][j].addActionListener(this);

				jPanel1.add(buttonArray[i][j]);
			}
		}

		jPanel1.setMaximumSize(new Dimension(600, 700));

		add(jPanel1);

	}

	// // Button click method
	public void actionPerformed(ActionEvent e) {
		try {
			JButton temporaryButton = (JButton) e.getSource();

			System.out.println(temporaryButton.getText() + " clicked, coordinate: " + temporaryButton.getName());

			temporaryButton.setEnabled(false);

			Coordinate target = new Coordinate(Integer.parseInt(temporaryButton.getName().split(",")[0]),
					Integer.parseInt(temporaryButton.getName().split(",")[1]));

			myAI.recomputeProbabilisticGrid(5, new Coordinate(9, 11), target);

			temporaryButton.setText("Hit");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static int getWindowLocationX() {
		return battleShip.getLocationOnScreen().x;
	}

	public static int getWindowLocationY() {
		return battleShip.getLocationOnScreen().y;
	}

	public static void main(String[] args) {
		battleShip = new BattleShip();
		battleShip.setVisible(true);

		shipList.add(new Ship(2, 2, 6, 1));
		shipList.add(new Ship(3, 3, 2, 3));
		shipList.add(new Ship(3, 3, 6, 3));
		shipList.add(new Ship(4, 3, 2, 7));
		shipList.add(new Ship(5, 3, 4, 5));

		for (int i = 0; i < shipList.get(0).occupiedGridX.size(); i++) {
			System.out.println(
					i + ": " + shipList.get(0).occupiedGridX.get(i) + ", " + shipList.get(0).occupiedGridY.get(i));
		}

		// for (int i = 0; i < shipList.get(1).occupiedGridX.size(); i++) {
		// System.out.println(i + ": " + shipList.get(1).occupiedGridX.get(i) + ", " +
		// shipList.get(1).occupiedGridY.get(i));
		// }

		System.out.println(shipList.get(0).validateLocation());
		// System.out.println(shipList.get(1).validateLocation());
	}

}

class Ship {

	// The coordinates based on the window
	public double x = 0, y = 0;

	public int length = 0;

	/*
	 * Direction the ship faces 1: Right 2: Down 3: Left 4: Up
	 */
	public int direction = 0;

	// The occupied grids of this ship
	public List<Integer> occupiedGridX = new ArrayList<Integer>(), occupiedGridY = new ArrayList<Integer>();

	// The pivot of the ship, also known as the reference point
	public int pivotGridX = 0, pivotGridY = 0;

	public boolean validity = true;

	// Constructor for initializing the attributes
	public Ship(int shipLength, int shipDirection, int shipPivotGridX, int shipPivotGridY) {
		length = shipLength;

		recalculate(shipDirection, shipPivotGridX, shipPivotGridY);
	}

	// Every time the direction or location of the ship changed, recalculate its
	// occupied grids
	public void recalculate(int shipDirection, int shipPivotGridX, int shipPivotGridY) {

		direction = shipDirection;

		pivotGridX = shipPivotGridX;
		pivotGridY = shipPivotGridY;

		// Reset the occupied grids
		occupiedGridX = new ArrayList<Integer>();
		occupiedGridY = new ArrayList<Integer>();

		if (length == 2) {
			// Check the ship direction
			if (direction == 1) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 1);
				occupiedGridY.add(shipPivotGridY);
			} else if (direction == 2) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 1);
			} else if (direction == 3) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 1);
				occupiedGridY.add(shipPivotGridY);
			} else if (direction == 4) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 1);
			}
		} else if (length == 3) {
			// Check the ship direction
			if (direction == 1 | direction == 3) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 1);
				occupiedGridY.add(shipPivotGridY);
			} else if (direction == 2 | direction == 4) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 1);
			}
		} else if (length == 4) {
			// Check the ship direction
			if (direction == 1) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 2);
				occupiedGridY.add(shipPivotGridY);
			} else if (direction == 2) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 2);
			} else if (direction == 3) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 2);
				occupiedGridY.add(shipPivotGridY);
			} else if (direction == 4) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 2);
			}
		} else if (length == 5) {
			// Check the ship direction
			if (direction == 1 | direction == 3) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX + 2);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 1);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX - 2);
				occupiedGridY.add(shipPivotGridY);
			} else if (direction == 2 | direction == 4) {
				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY + 2);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 1);

				occupiedGridX.add(shipPivotGridX);
				occupiedGridY.add(shipPivotGridY - 2);
			}
		}
	}

	// Check if the ship is outside the board
	public boolean validateLocation() {
		for (int i = 0; i < occupiedGridX.size(); i++) {
			if (occupiedGridX.get(i) < 0 | occupiedGridX.get(i) > 10) {
				validity = false;
				return false;

			}
		}

		for (int i = 0; i < occupiedGridY.size(); i++) {
			if (occupiedGridY.get(i) < 0 | occupiedGridY.get(i) > 8) {
				validity = false;
				return false;

			}
		}

		// Check the collision
		for (int i = 0; i < BattleShip.shipList.size(); i++) {
			if (BattleShip.shipList.get(i) != this) {
				// Compare the coordinates of this ship to other ships' coordinates
				for (int j = 0; j < occupiedGridX.size(); j++) {
					for (int gridIndex = 0; gridIndex < BattleShip.shipList.get(i).occupiedGridX.size(); gridIndex++) {
						// Check if they have the same X and Y
						if (occupiedGridX.get(j) == BattleShip.shipList.get(i).occupiedGridX.get(gridIndex)
								& occupiedGridY.get(j) == BattleShip.shipList.get(i).occupiedGridY.get(gridIndex)) {
							validity = false;
							return false;
						}
					}

				}
			}
		}

		validity = true;

		return true;
	}
}

class Renderer extends JComponent {
	private boolean clearAll = false;

	public static int[][] holeLocationX = new int[11][9], holeLocationY = new int[11][9];

	private Image holeImage, shipImage, horizontalShipImage, redShipImage, horizontalRedShipImage;

	int mouseX = 0, mouseY = 0;

	// The id showing which ship is being picked up
	public static int targetShipId = -1;

	public Renderer() {
		holeImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Hole.png");
		shipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/Ship.png");
		horizontalShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/ShipHorizontal.png");
		redShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/RedShip.png");
		horizontalRedShipImage = Toolkit.getDefaultToolkit().getImage("src/view/resources/RedShipHorizontal.png");
	}

	public void paint(Graphics g) {
		setBackground(new Color(4, 192, 185));
		setBackground(new Color(99, 222, 231));

		// setOpaque(true);

		if (clearAll) {
			g.clearRect(0, 0, getWidth(), getHeight());
		} else {

			Graphics2D graphics2D = (Graphics2D) g;

			int holeImageSize = 800 / 11 - 2;

			// System.out.println(holeImageSize);

			// Render all grids
			for (int i = 8; i >= 0; i--) {
				for (int j = 0; j < 11; j++) {
					int relativeX = holeImageSize / 2 + j * holeImageSize;
					int relativeY = holeImageSize / 2 + (8 - i) * holeImageSize;

					graphics2D.drawImage(holeImage, relativeX, relativeY, 20, 20, this);

					holeLocationX[j][i] = relativeX + 5;
					holeLocationY[j][i] = relativeY + 5;
				}
			}

			int nearestX = 0, nearestY = 0;

			// Check if the mouse is clicked
			if (Mouse.leftClicked) {
				mouseX = MouseInfo.getPointerInfo().getLocation().x - BattleShip.getWindowLocationX();
				mouseY = MouseInfo.getPointerInfo().getLocation().y - BattleShip.getWindowLocationY();

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
					BattleShip.shipList.get(i).validateLocation();
					// System.out.println(BattleShip.shipList.get(3).length * holeImageSize + "
					// ??");
					if (BattleShip.shipList.get(i).direction == 1 | BattleShip.shipList.get(i).direction == 3) {
						if (BattleShip.shipList.get(i).validity)
							graphics2D.drawImage(shipImage,
									holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - (int) (offset * holeImageSize),
									holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - 25 / 2,
									BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
						else
							graphics2D.drawImage(redShipImage,
									holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - (int) (offset * holeImageSize),
									holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - 25 / 2,
									BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
					} else {
						if (BattleShip.shipList.get(i).validity)
							graphics2D.drawImage(horizontalShipImage,
									holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - 25 / 2,
									holeLocationY[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - (int) (offset * holeImageSize),
									40, BattleShip.shipList.get(i).length * holeImageSize - 2, this);
						else
							graphics2D.drawImage(horizontalRedShipImage,
									holeLocationX[BattleShip.shipList.get(i).pivotGridX][BattleShip.shipList
											.get(i).pivotGridY] - 25 / 2,
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
										holeLocationY[nearestX][nearestY] - 25 / 2,
										BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
							} else {
								graphics2D.drawImage(horizontalShipImage, holeLocationX[nearestX][nearestY] - 25 / 2,
										holeLocationY[nearestX][nearestY] - (int) (offset * holeImageSize), 40,
										BattleShip.shipList.get(i).length * holeImageSize - 2, this);
							}
						} else {
							if (BattleShip.shipList.get(targetShipId).direction == 1
									| BattleShip.shipList.get(targetShipId).direction == 3) {
								graphics2D.drawImage(redShipImage,
										holeLocationX[nearestX][nearestY] - (int) (offset * holeImageSize),
										holeLocationY[nearestX][nearestY] - 25 / 2,
										BattleShip.shipList.get(i).length * holeImageSize - 2, 40, this);
							} else {
								graphics2D.drawImage(horizontalRedShipImage, holeLocationX[nearestX][nearestY] - 25 / 2,
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

class Mouse implements MouseListener {
	public static boolean leftClicked = false;

	public void mouseClicked(MouseEvent mouseEvent) {
		// Right click
		if (mouseEvent.getButton() == MouseEvent.BUTTON3) {

			if (leftClicked) {
				if (BattleShip.shipList.get(Renderer.targetShipId).direction < 4)
					BattleShip.shipList.get(Renderer.targetShipId).direction += 1;
				else
					BattleShip.shipList.get(Renderer.targetShipId).direction = 1;
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