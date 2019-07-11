package model;

import constants.Constants;
import tools.Coordinate;
import tools.GridHelper;

public class AI {

	private Coordinate boardSize;

	// int[][] probabilisticGridX = new int[boardSize.x][boardSize.y];
	// int[][] probabilisticGridY = new int[boardSize.x][boardSize.y];

	private int[][] countGrid;

	//private int[][] probabilisticGridX = new int[9][11];
	//private int[][] probabilisticGridY = new int[9][11];

	//
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

	// current grids //

	boolean isInitialized = false;

	private Coordinate previousTarget;

	/**
	 * default constructor
	 */
	public AI() {
		try {
			computeCountGrid();
			isInitialized = true;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * compute the probabilitic grid, called by constructor initially
	 * @return
	 * @throws Exception
	 */
	private void computeCountGrid() throws Exception {

		int[][] finalCountGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

		// loop over nb of ships and their respective sizes on the board
		this.destroyerCountGrid = specificCountGrid(Constants.DESTROYER_SIZE);
		this.submarineCountGrid = specificCountGrid(Constants.SUBMARINE_SIZE);
		this.cruiserCountGrid = specificCountGrid(Constants.CRUISER_SIZE);
		this.battleshipCountGrid = specificCountGrid(Constants.BATTLESHIP_SIZE);
		this.carrierCountGrid = specificCountGrid(Constants.CARRIER_SIZE);

		for(int i=0; i<Constants.DESTROYER_NB; i++){
			GridHelper.add( finalCountGrid, this.destroyerCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
			GridHelper.add( finalCountGrid, this.submarineCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
			GridHelper.add( finalCountGrid, this.cruiserCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
			GridHelper.add( finalCountGrid, this.battleshipCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
			GridHelper.add( finalCountGrid, this.carrierCountGrid );
		}

		this.countGrid = finalCountGrid;
	}


	/**
	 * compute a specific Count grid, helper for computeCountGrid
	 * @param shipSize
	 * @return
	 * @throws Exception
	 */
	private int[][] specificCountGrid(int shipSize) throws Exception {

		int[][] probabilisticGridX = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];
		int[][] probabilisticGridY = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

		if (shipSize > boardSize.x || shipSize > boardSize.y) {
			throw new Exception("shipLength cannot be bigger x or y");
		}

		this.boardSize = boardSize;

		// vertical ship position probability //

		// compute weight on first column
		for (int i = 0; i < boardSize.y - shipSize + 1; i++) { // start position
			for (int j = 0; j < shipSize; j++) { // loop over shipLength
				probabilisticGridX[0][i + j] += 1;
			}
		}

		// copy weight of first column to every other column
		for (int i = 1; i < boardSize.x; i++) { // for every other column
			for (int j = 0; j < boardSize.y; j++) { // for every values of the first columns
				probabilisticGridX[i][j] = probabilisticGridX[0][j]; // copy
			}
		}

		// horizontal ship position probability //

		// compute weight on first row
		for (int i = 0; i < boardSize.x - shipSize + 1; i++) { // start position
			for (int j = 0; j < shipSize; j++) { // loop over shipLength
				probabilisticGridY[i + j][0] += 1;
			}
		}

		// copy weight of first row to every other row
		for (int i = 1; i < boardSize.y; i++) { // for every other row
			for (int j = 0; j < boardSize.x; j++) { // for every values of the first row
				probabilisticGridY[j][i] = probabilisticGridY[j][0]; // copy
			}
		}

		// add vertical and horizontal probability grid //

		int[][] specificCountGrid = new int[boardSize.x][boardSize.y];

		int cellCount = 0;
		long totalCount = 0;

		for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				cellCount = probabilisticGridX[i][j] + probabilisticGridY[i][j];
				totalCount += cellCount;
				specificCountGrid[i][j] = cellCount;
			}
		}

		// convert weights to probabilities //

		/*for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				specificCountGrid[i][j] = specificCountGrid[i][j] / totalCount;
				System.out.print(specificCountGrid[i][j] + " ");
			}
			System.out.println();
		}*/

		return specificCountGrid;
	}


	/**
	 * register the result of the previous move
	 * @param code
	 */
	public void receiveResult( int code ) throws Exception {

		// if(code == 2){ // miss

		updateCountGrid( this.previousTarget, code );

	}

	/**
	 * After a move is registered, update the probabilistic grid
	 * @param target
	 * @param code
	 * @throws Exception
	 */
	private void updateCountGrid( Coordinate target, int code) throws Exception {

		int[][] countGridToUpdate = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

		// update probabilistic grid related to each ship length //

		this.destroyerCountGrid = updateSpecificCountGrid( this.destroyerCountGrid, target, code);

		this.submarineCountGrid = updateSpecificCountGrid( this.submarineCountGrid, target, code);

		this.cruiserCountGrid = updateSpecificCountGrid( this.cruiserCountGrid, target, code);

		this.battleshipCountGrid = updateSpecificCountGrid( this.battleshipCountGrid, target, code);

		this.carrierCountGrid = updateSpecificCountGrid( this.carrierCountGrid, target, code);


		// add them together to get the new final probabilistic grid

		for(int i=0; i<Constants.DESTROYER_NB - this.nbDestroyerDestroyed; i++){
			GridHelper.add( countGridToUpdate, this.destroyerCountGrid ); }

		for(int i=0; i<Constants.SUBMARINE_NB - this.nbSubmarineDestroyed; i++){
			GridHelper.add( countGridToUpdate, this.submarineCountGrid ); }

		for(int i=0; i<Constants.CRUISER_NB - this.nbCruiserDestroyed; i++){
			GridHelper.add( countGridToUpdate, this.cruiserCountGrid ); }

		for(int i=0; i<Constants.BATTLESHIP_NB - this.nbBattleshipDestroyed; i++){
			GridHelper.add( countGridToUpdate, this.battleshipCountGrid ); }

		for(int i=0; i<Constants.CARRIER_NB - this.nbCarrierDestroyed; i++){
			GridHelper.add( countGridToUpdate, this.carrierCountGrid ); }

		/*countGridToUpdate = GridHelper.divideByScalar(
				countGridToUpdate,
				Constants.SHIP_NB - this.nbDestroyerDestroyed - this.nbSubmarineDestroyed
				- this.nbCruiserDestroyed - this.nbBattleshipDestroyed - this.nbCarrierDestroyed);*/

		this.countGrid = countGridToUpdate;
	}


	/**
	 * update a specific probabilistic grid given the new information
	 * @return
	 */
	public int[][] updateSpecificCountGrid(
		int[][] countGridToUpdate, Coordinate target, int code) throws Exception {

		countGridToUpdate[target.x][target.y] = 0;
		return countGridToUpdate;
	}


	/**
	 * get the next move from the AI player
	 * @return
	 */
	public Coordinate getNextMove(){

		long totalCount = 0;

		for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
				totalCount += countGrid[i][j];
			}
		}

		Coordinate currentCoordinate = new Coordinate(0, 0);
		float currentHighest = 0;

		for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {

				float value = countGrid[i][j] / totalCount;

				if (value > currentHighest) {
					currentHighest = value;
					currentCoordinate.x = i;
					currentCoordinate.y = j;
				}
			}
		}

		this.previousTarget = currentCoordinate;
		return currentCoordinate;
	}

	public Coordinate getNextMove(float[][] doubles, int row, int col) {
		Coordinate currentCoordinate = new Coordinate(0, 0);
		float currentHighest = 0;

		for (int i = 0; i < row; row++) {
			for (int j = 0; j < col; col++) {
				float value = doubles[i][j];
				if (value > currentHighest) {
					currentHighest = value;
					currentCoordinate.x = i;
					currentCoordinate.y = j;
				}
			}
		}
		return currentCoordinate;
	}
}
