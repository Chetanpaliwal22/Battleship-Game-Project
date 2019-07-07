package model;

import tools.Coordinate;

public class AI {

	private Coordinate boardSize;

	public AI() {
		try {
			computeProbabilisticGrid(5, new Coordinate(9, 11));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// int[][] probabilisticGridX = new int[boardSize.x][boardSize.y];
	// int[][] probabilisticGridY = new int[boardSize.x][boardSize.y];

	int[][] probabilisticGridX = new int[10][11];
	int[][] probabilisticGridY = new int[10][11];

	private void computeProbabilisticGrid(int shipLength, Coordinate boardSize) throws Exception {

		if (shipLength > boardSize.x || shipLength > boardSize.y) {
			throw new Exception("shipLength cannot be bigger x or y");
		}

		this.boardSize = boardSize;

		// vertical ship position probability //

		// compute weight on first column
		for (int i = 0; i < boardSize.y - shipLength + 1; i++) { // start position
			for (int j = 0; j < shipLength; j++) { // loop over shipLength
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
		for (int i = 0; i < boardSize.x - shipLength + 1; i++) { // start position
			for (int j = 0; j < shipLength; j++) { // loop over shipLength
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

		float[][] finalProbabilisticGrid = new float[boardSize.x][boardSize.y];
		int cellCount = 0;
		long totalCount = 0;

		for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				cellCount = probabilisticGridX[i][j] + probabilisticGridY[i][j];
				totalCount += cellCount;
				finalProbabilisticGrid[i][j] = cellCount;
			}
		}

		// convert weights to probabilities //

		for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				finalProbabilisticGrid[i][j] = finalProbabilisticGrid[i][j] / totalCount;
				System.out.print(finalProbabilisticGrid[i][j] + " ");
			}
			System.out.println();
		}

	}

	public void recomputeProbabilisticGrid(int shipLength, Coordinate boardSize, Coordinate target) throws Exception {

		if (shipLength > boardSize.x || shipLength > boardSize.y) {
			throw new Exception("shipLength cannot be bigger x or y");
		}

		System.out.println("Recomputing Probability.");

		float[][] finalProbabilisticGrid = new float[boardSize.x][boardSize.y];
		int cellCount = 0;
		long totalCount = 0;

		probabilisticGridX[target.x][target.y] = 0;
		probabilisticGridY[target.x][target.y] = 0;

		for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				cellCount = probabilisticGridX[i][j] + probabilisticGridY[i][j];
				totalCount += cellCount;
				finalProbabilisticGrid[i][j] = cellCount;
			}
		}

		totalCount = (long) (totalCount - finalProbabilisticGrid[target.x][target.y]);
		finalProbabilisticGrid[target.x][target.y] = 0;

		// convert weights to probabilities //

		for (int i = 0; i < boardSize.x; i++) {
			for (int j = 0; j < boardSize.y; j++) {
				finalProbabilisticGrid[i][j] = finalProbabilisticGrid[i][j] / totalCount;
				System.out.print(finalProbabilisticGrid[i][j] + " ");
			}
			System.out.println();
		}

	}
}
