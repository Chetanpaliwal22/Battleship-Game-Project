package tools;

public class GridHelper {

    /**
     * add two grid together
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    public static float[][] add(float[][] a, float[][] b) throws Exception {

        if( a.length != b.length || a[0].length != b[0].length ){
            throw new Exception("Provided arrays are not of the same shape.");
        }

        float[][] result = new float[a.length][a[0].length];

        for(int i=0; i<a.length; i++){
            for(int j=0; j<a[0].length; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }

        return result;
    }

    /**
     * add two grid together
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    public static int[][] add(int[][] a, int[][] b) throws Exception {

        if( a.length != b.length || a[0].length != b[0].length ){
            throw new Exception("Provided arrays are not of the same shape.");
        }

        int[][] result = new int[a.length][a[0].length];

        for(int i=0; i<a.length; i++){
            for(int j=0; j<a[0].length; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }

        return result;
    }

    /**
     * compute mean of two grid
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    public static float[][] mean(float[][] a, float[][] b) throws Exception {

        float[][] result = add(a, b);

        for(int i=0; i<a.length; i++){
            for(int j=0; j<a[0].length; j++) {
                result[i][j] = result[i][j] / 2;
            }
        }

        return result;
    }

    /**
     * divide grid by scalar
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    public static float[][] divideByScalar(float[][] a, float b) throws Exception {

        float[][] result = new float[a.length][a[0].length];

        result = add(result, a);

        for(int i=0; i<a.length; i++){
            for(int j=0; j<a[0].length; j++) {
                result[i][j] = result[i][j] / b;
            }
        }

        return result;
    }
}
