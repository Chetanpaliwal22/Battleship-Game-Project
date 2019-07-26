package model;

import java.util.ArrayList;

/**
 * This class contains the backend logic of AI
 */
import constants.Constants;
import tools.Coordinate;
import tools.GridHelper;

public class AI {

	private Coordinate boardSize;

	boolean targetMode = false;

	int direction = -1;
	int distanceFromHit = 1;
	boolean fixedDirection = false;
	boolean seekAgain = false;
	Coordinate axis;

	private Coordinate previousTarget = new Coordinate(-1, -1);

	private int[][] countGrid;

	private int[][] destroyerCountGrid;
	private int[][] submarineCountGrid;
	private int[][] cruiserCountGrid;
	private int[][] battleshipCountGrid;
	private int[][] carrierCountGrid;

	private int nbDestroyerDestroyed = 0;
	private int nbSubmarineDestroyed = 0;
	private int nbCruiserDestroyed = 0;
	private int nbBattleshipDestroyed = 0;
	private int nbCarrierDestroyed = 0;

	private ArrayList<Coordinate> coordinateToExclude;


	/**
	 * default constructor
	 */
	public AI() {

		this.boardSize = Constants.BOARD_SIZE;

		this.coordinateToExclude = new ArrayList<Coordinate>(boardSize.x * boardSize.y);

		try { updateCountGrid( -1 ); }

		catch (Exception e) { e.printStackTrace(); }

		printCountGrid();
	}


    /**
     * update the count grid given th new information (or just initialize)
     * @param code
     * @throws Exception
     */
	private void updateCountGrid(int code) throws Exception {

		// to temporarily hold the new count grid
		int[][] countGridToUpdate = new int[boardSize.x][boardSize.y];

		// update count grid related to each ship length //
		this.destroyerCountGrid = updateSpecificCountGrid(Constants.DESTROYER_SIZE, code);
		this.submarineCountGrid = updateSpecificCountGrid(Constants.SUBMARINE_SIZE, code);
		this.cruiserCountGrid = updateSpecificCountGrid(Constants.CRUISER_SIZE, code);
		this.battleshipCountGrid = updateSpecificCountGrid(Constants.BATTLESHIP_SIZE, code);
		this.carrierCountGrid = updateSpecificCountGrid(Constants.CARRIER_SIZE, code);

		// add them together to get the new final probabilistic grid

		for (int i = 0; i < Constants.DESTROYER_NB - this.nbDestroyerDestroyed; i++) {
			countGridToUpdate = GridHelper.add(countGridToUpdate, this.destroyerCountGrid);
		}

		for (int i = 0; i < Constants.SUBMARINE_NB - this.nbSubmarineDestroyed; i++) {
			countGridToUpdate = GridHelper.add(countGridToUpdate, this.submarineCountGrid);
		}

		for (int i = 0; i < Constants.CRUISER_NB - this.nbCruiserDestroyed; i++) {
			countGridToUpdate = GridHelper.add(countGridToUpdate, this.cruiserCountGrid);
		}

		for (int i = 0; i < Constants.BATTLESHIP_NB - this.nbBattleshipDestroyed; i++) {
			countGridToUpdate = GridHelper.add(countGridToUpdate, this.battleshipCountGrid);
		}

		for (int i = 0; i < Constants.CARRIER_NB - this.nbCarrierDestroyed; i++) {
			countGridToUpdate = GridHelper.add(countGridToUpdate, this.carrierCountGrid);
		}

		System.out.println("grid created");
		this.countGrid = countGridToUpdate;
	}


    /**
     * update a specific grount grid using the new information (or just initialize)
     * @param shipSize
     * @param code
     * @return
     * @throws Exception
     */
	private int[][] updateSpecificCountGrid(int shipSize, int code) throws Exception {

        int[][] countGridX = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];
        int[][] countGridY = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

        if (shipSize > boardSize.x || shipSize > boardSize.y) {
            throw new Exception("shipLength cannot be bigger x or y");
        }

        // vertical ship position count //

        // compute weight on first column
        for(int i=0; i<boardSize.x; i++) { // for every column

            for (int j=0; j<boardSize.y-shipSize + 1; j++) { // start position

                for (int k=0; k<shipSize; k++) { // loop over shipLength

                    boolean impossiblePosition = false;
                    for (int l = 0; l < coordinateToExclude.size(); l++) { // loop over known coordinates
                        if (coordinateToExclude.get(l).x == i && coordinateToExclude.get(l).y == j+k) {
                            impossiblePosition = true;
                        }
                    }

                    if( !impossiblePosition ){ // add possible ship position to count grid
                    	countGridX[i][j+k] += 1;
                    }
                }
            }
        }

        // horizontal ship position count //

        for(int i=0; i<boardSize.y; i++) { // for every row

            for (int j=0; j<boardSize.x-shipSize + 1; j++) { // start position

                for (int k=0; k<shipSize; k++) { // loop over shipLength

                    boolean impossiblePosition = false;
                    for (int l = 0; l < coordinateToExclude.size(); l++) { // loop over known coordinates
                        if (coordinateToExclude.get(l).x == j+k && coordinateToExclude.get(l).y == i) {
                            impossiblePosition = true;
                        }
                    }

                    if( !impossiblePosition ){ // add possible ship position to count grid
                    	countGridX[j+k][i] += 1;
                    }
                }
            }
        }

        // add vertical and horizontal count grid //

        int[][] specificCountGrid = new int[this.boardSize.x][this.boardSize.y];

        for (int i = 0; i < boardSize.x; i++) {
            for (int j = 0; j < boardSize.y; j++) {
                specificCountGrid[i][j] = countGridX[i][j] + countGridY[i][j];
            }
        }

		return specificCountGrid;
	}


	/**
	 * get the next move from the AI player
	 * 
	 * @return the coordinate of next move
	 */
	public Coordinate getNextMove() {
		
		if( targetMode ){

			Coordinate bestNextTarget;

			if( !fixedDirection  ) { // direction is not known

				int maxCount = 0;

				System.out.println(this.axis.x);
				System.out.println(this.axis.y);
				System.out.println(countGrid[this.axis.x][this.axis.y]);
				System.out.println();

				maxCount = countGrid[this.axis.x+1][this.axis.y];
				bestNextTarget = new Coordinate(this.axis.x, this.axis.y + 1);
				System.out.println(countGrid[this.axis.x][this.axis.y + 1]);
				direction = 0; // north by default


				// find the best target around the last target //

				if (countGrid[this.axis.x+1][this.axis.y] > maxCount) { // try south
					maxCount = countGrid[this.axis.x+1][this.axis.y];
					bestNextTarget = new Coordinate(this.axis.x, this.axis.y-1);
					direction = 2;
					System.out.println("to");
				}

				System.out.println(countGrid[this.axis.x][this.axis.y - 1]);

				if (countGrid[this.axis.x][this.axis.y+1] > maxCount) { // try east
					maxCount = countGrid[this.axis.x][this.axis.y+1];
					bestNextTarget = new Coordinate(this.axis.x + 1, this.axis.y);
					direction = 1;
					System.out.println("to");
				}

				System.out.println(countGrid[this.axis.y][this.axis.x+1]);

				if (countGrid[this.axis.x][this.axis.y-1] > maxCount) { // try west
					maxCount = countGrid[this.axis.x][this.axis.y-1];
					bestNextTarget = new Coordinate(this.axis.x - 1, this.axis.y);
					direction = 3;
					System.out.println("to");
				}

				System.out.println(countGrid[this.axis.y][this.axis.x-1]);

				seekAgain = false;

			} else { // direction is known

				bestNextTarget = new Coordinate(this.axis.x, this.axis.y);

				if( direction == 0 ) { // north
					bestNextTarget = new Coordinate(this.axis.x, this.axis.y + distanceFromHit);

				} else if( direction == 1 ){ // east
					bestNextTarget = new Coordinate(this.axis.x + distanceFromHit, this.axis.y);

				} else if( direction == 2 ){ // south
					bestNextTarget = new Coordinate(this.axis.x , this.axis.y - distanceFromHit);

				} else if( direction == 3 ){ // west
					bestNextTarget = new Coordinate(this.axis.x - distanceFromHit, this.axis.y );
				}
			}

			//System.out.println(bestNextTarget.x);
			//System.out.println(bestNextTarget.y);
			this.previousTarget = bestNextTarget;

		} else { this.previousTarget = getGlobalHighestCount(); }

		coordinateToExclude.add( new Coordinate( this.previousTarget.y, this.previousTarget.x ) );

		return this.previousTarget;
	}


	private Coordinate getGlobalHighestCount(){

		Coordinate currentCoordinate = new Coordinate(0, 0);
		float currentHighest = 0;
		for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {

			for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {

				float value = countGrid[i][j];

				if (value > currentHighest) {
					currentHighest = value;
					currentCoordinate.x = j;
					currentCoordinate.y = i;
				}
			}
		}

		return currentCoordinate;
	}


	/**
	 * get the next five move from the AI player
	 * 
	 * @return the coordinate list of next move
	 */
	public ArrayList<Coordinate> getFiveNextMove() {

		ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();

		int[][] copyCountGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

		for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
				copyCountGrid[i][j] = countGrid[i][j];
			}
		}

		for (int k = 0; k < 5; k++) {
			Coordinate currentCoordinate = new Coordinate(0, 0);
			float currentHighest = 0;

			System.out.println("AI count grid :");
			for (int i = this.boardSize.x - 1; i >= 0; i--) {
				for (int j = 0; j < this.boardSize.y; j++) {
					System.out.print(this.countGrid[i][j] + " ");
				}
				System.out.println("");
			}

			for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {
				for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {

					float value = copyCountGrid[i][j];

					if (value > currentHighest) {
						currentHighest = value;
						currentCoordinate.y = i;
						currentCoordinate.x = j;
					}
				}
			}
			copyCountGrid[currentCoordinate.y][currentCoordinate.x] = Integer.MIN_VALUE;
			coordinateList.add(currentCoordinate);
		}
		return coordinateList;
	}


	/**
	 * display the content of the AI count grid in the console
	 */
	public void printCountGrid() {

		System.out.println("AI count grid :");
		for (int i = this.boardSize.x - 1; i >= 0; i--) {
			for (int j = 0; j < this.boardSize.y; j++) {
				System.out.print(this.countGrid[i][j] + " ");
			}
			System.out.println("");
		}
	}


	/**
	 * register the result of the previous move
	 *
	 * @param code
	 *            AI accepts code as input
	 * @throws Exception
	 *             throws exception
	 */
	public void receiveResult(int code) throws Exception {

		if ( code == 0 && targetMode && fixedDirection) { // change direction to seek ship position

			if( direction == 0 ){ // if north, then go south

				direction = 2;
				distanceFromHit = 1;

			} else if( direction == 1 ){ // if east, then go west

				direction = 3;
				distanceFromHit = 1;

			} else if( direction == 2 ){ // if south, then go north

				direction = 1;
				distanceFromHit = 1;

			} else if( direction == 3 ){ // if west, then go east

				direction = 1;
				distanceFromHit = 1;
			}

		} else if( code == 1 ){ // hit

			if( targetMode ){

				distanceFromHit += 1;
				fixedDirection = true;

			} else { // enable target mode

				targetMode = true;
				axis = this.previousTarget;
			}

		} else if( code == 2 ){ // sunk

			targetMode = false;
			direction = -1;
			fixedDirection = false;
			distanceFromHit = 1;
		}

		updateCountGrid( code );
	}

}
