package model;
import tools.Coordinate;

public class AI {

    private Coordinate boardSize;

    public void AI(){
        computeProbabilisticGrid(5, new Coordinate(3, 3));
    }

    private void computeProbabilisticGrid(int shipLength, Coordinate boardSize){

        this.boardSize = boardSize;

        int[][] probabilisticGridX = new int[boardSize.x][boardSize.y];
        int[][] probabilisticGridY = new int[boardSize.x][boardSize.y];

        // vertical ship position probability //

        // compute weight on first column
        for(int i=0; i< boardSize.y-shipLength; i++) { // start position
            for (int j = 0; j < boardSize.y - shipLength; j++) { // loop over shipLength
                probabilisticGridX[0][i+j] += 1;
            }
        }

        // copy weight of first column to every other column
        for(int i = 1; i< boardSize.x; i++){ // for every other column
            for(int j = 0; j< boardSize.y; j++){ // for every values of the first columns
                probabilisticGridX[i][j] = probabilisticGridX[0][j]; // copy
            }
        }

        // horizontal ship position probability //

        // compute weight on first row
        for(int i=0; i< boardSize.x-shipLength; i++) { // start position
            for (int j = 0; j < boardSize.x - shipLength; j++) { // loop over shipLength
                probabilisticGridY[i+j][0] += 1;
            }
        }

        // copy weight of first row to every other row
        for(int i = 1; i< boardSize.y; i++){ // for every other row
            for(int j = 0; j< boardSize.x; j++){ // for every values of the first row
                probabilisticGridY[j][i] = probabilisticGridY[j][i]; // copy
            }
        }

        // add vertical and horizontal probability grid //

        float[][] finalProbabilisticGrid = new float[boardSize.x][boardSize.y];
        int cellCount = 0;
        long totalCount = 0;

        for(int i = 0; i< boardSize.x; i++){
            for(int j = 0; j< boardSize.y; j++){
                cellCount = probabilisticGridX[i][j] + probabilisticGridY[i][j];
                totalCount += cellCount;
                finalProbabilisticGrid[i][j] = cellCount;
            }
        }

        // convert weights to probabilities //

        for(int i = 0; i< boardSize.x; i++){
            for(int j = 0; j< boardSize.y; j++){
                finalProbabilisticGrid[i][j] = finalProbabilisticGrid[i][j] / totalCount;
                System.out.println( finalProbabilisticGrid[i][j] + " ");
            }
            System.out.println( "\n");
        }

    }
}
