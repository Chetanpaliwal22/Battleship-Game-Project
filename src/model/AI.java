package model;

import constants.Constants;
import tools.Coordinate;
import tools.GridHelper;

public class AI {

	private Coordinate boardSize;
	private int[][] countGrid;
    boolean isInitialized = false;
    private Coordinate previousTarget;

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


	/**
	 * default constructor
	 */
	public AI() {

		boardSize = Constants.BOARD_SIZE;

		try{ computeCountGrid(); }
		catch(Exception e){ e.printStackTrace(); }

		isInitialized = true;
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
			finalCountGrid = GridHelper.add( finalCountGrid, this.destroyerCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
            finalCountGrid = GridHelper.add( finalCountGrid, this.submarineCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
            finalCountGrid = GridHelper.add( finalCountGrid, this.cruiserCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
            finalCountGrid = GridHelper.add( finalCountGrid, this.battleshipCountGrid );
		}

		for(int i=0; i<Constants.DESTROYER_NB; i++){
            finalCountGrid = GridHelper.add( finalCountGrid, this.carrierCountGrid );
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

		// vertical ship position probability //

		// compute weight on first column
		for (int i = 0; i < boardSize.y - shipSize +1; i++) { // start position
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
		for (int i = 0; i < boardSize.x - shipSize +1; i++) { // start position
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

		int[][] specificCountGrid = new int[this.boardSize.x][this.boardSize.y];

		int cellCount = 0;
		long totalCount = 0;

		for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				cellCount = probabilisticGridX[i][j] + probabilisticGridY[i][j];
				totalCount += cellCount;
				specificCountGrid[i][j] = cellCount;
			}
		}

		return specificCountGrid;
	}


	/**
	 * register the result of the previous move
	 * @param code
	 */
	public void receiveResult( int code ) throws Exception {

		updateCountGrid( this.previousTarget, code );

	}

	/**
	 * After a move is registered, update the probabilistic grid
	 * @param target
	 * @param code
	 * @throws Exception
	 */
	private void updateCountGrid( Coordinate target, int code) throws Exception {

		// to temporarily hold the new count grid
		int[][] countGridToUpdate = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

		// update probabilistic grid related to each ship length //

		this.destroyerCountGrid = updateSpecificCountGrid( this.destroyerCountGrid, target, code);

		this.submarineCountGrid = updateSpecificCountGrid( this.submarineCountGrid, target, code);

		this.cruiserCountGrid = updateSpecificCountGrid( this.cruiserCountGrid, target, code);

		this.battleshipCountGrid = updateSpecificCountGrid( this.battleshipCountGrid, target, code);

		this.carrierCountGrid = updateSpecificCountGrid( this.carrierCountGrid, target, code);


		// add them together to get the new final probabilistic grid

		for(int i=0; i<Constants.DESTROYER_NB - this.nbDestroyerDestroyed; i++){
			countGridToUpdate = GridHelper.add( countGridToUpdate, this.destroyerCountGrid ); }

		for(int i=0; i<Constants.SUBMARINE_NB - this.nbSubmarineDestroyed; i++){
			countGridToUpdate = GridHelper.add( countGridToUpdate, this.submarineCountGrid ); }

		for(int i=0; i<Constants.CRUISER_NB - this.nbCruiserDestroyed; i++){
			countGridToUpdate = GridHelper.add( countGridToUpdate, this.cruiserCountGrid ); }

		for(int i=0; i<Constants.BATTLESHIP_NB - this.nbBattleshipDestroyed; i++){
			countGridToUpdate = GridHelper.add( countGridToUpdate, this.battleshipCountGrid ); }

		for(int i=0; i<Constants.CARRIER_NB - this.nbCarrierDestroyed; i++){
			countGridToUpdate = GridHelper.add( countGridToUpdate, this.carrierCountGrid ); }

		this.countGrid = countGridToUpdate;
	}


	/**
	 * update a specific probabilistic grid given the new information
	 * @return
	 */
	public int[][] updateSpecificCountGrid(
		int[][] countGridToUpdate, Coordinate target, int code) throws Exception {

		if( code == 0 ){
			countGridToUpdate[target.y][target.x] = 0;
		} else if( code == 1 ){
			//countGridToUpdate[target.y][target.x] = 0;
		} else if( code == 2 ){
			//countGridToUpdate[target.y][target.x] = 0;
		}

		/*System.out.println("OHOH");
		for(int i=0; i<this.boardSize.x; i++){
			for(int j=0; j<this.boardSize.y; j++){
				System.out.print(countGridToUpdate[i][j] + " ");
			}
			System.out.print("");
		}*/

		return countGridToUpdate;
	}


	/**
	 * get the next move from the AI player
	 * @return
	 */
	public Coordinate getNextMove(){

		Coordinate currentCoordinate = new Coordinate(0, 0);
		float currentHighest = 0;

		for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {

				float value = countGrid[i][j];

				if (value > currentHighest) {
					currentHighest = value;
					currentCoordinate.y = i;
					currentCoordinate.x = j;
				}
			}
		}

		this.previousTarget = currentCoordinate;
		return currentCoordinate;
	}


	/**
	 * display the content of the AI count grid in the console
	 */
	public void printCountGrid(){

		System.out.println("AI count grid :");
		for(int i=this.boardSize.x-1; i>=0; i--){
			for(int j=0; j<this.boardSize.y; j++){
				System.out.print(this.countGrid[i][j] + " ");
			}
			System.out.println("");
		}
	}

}
