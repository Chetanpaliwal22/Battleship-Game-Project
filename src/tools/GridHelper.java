package tools;

/**
 * This util class contains the basic method applied of the grid across the
 * applicaiton
 */
public class GridHelper {

	/**
	 * add two grid together
	 * 
	 * @param a
	 *            for the first grid
	 * @param b
	 *            for the second grid
	 * @return the sum of two float grid
	 * @throws Exception
	 *             if any error occured
	 */
	public static float[][] add(float[][] a, float[][] b) throws Exception {

		if (a.length != b.length || a[0].length != b[0].length) {
			throw new Exception("Provided arrays are not of the same shape.");
		}

		float[][] result = new float[a.length][a[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				result[i][j] = a[i][j] + b[i][j];
			}
		}

		return result;
	}

	/**
	 * add two grid together
	 * 
	 * @param a
	 *            for the first array
	 * @param b
	 *            for the second array
	 * @return the sum of two int array
	 * @throws Exception
	 *             if any error
	 */
	public static int[][] add(int[][] a, int[][] b) throws Exception {

		if (a.length != b.length || a[0].length != b[0].length) {
			throw new Exception("Provided arrays are not of the same shape.");
		}

		int[][] result = new int[a.length][a[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				result[i][j] = a[i][j] + b[i][j];
			}
		}

		return result;
	}

	/**
	 * compute mean of two grid
	 * 
	 * @param a
	 *            the first array
	 * @param b
	 *            the second array
	 * @return the mean of two float array
	 * @throws Exception
	 *             if any error
	 */
	public static float[][] mean(float[][] a, float[][] b) throws Exception {

		float[][] result = add(a, b);

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				result[i][j] = result[i][j] / 2;
			}
		}

		return result;
	}

	/**
	 * divide grid by scalar
	 * 
	 * @param a
	 *            the first array
	 * @param b
	 *            the scaler quantity
	 * @return the divide result of array
	 * @throws Exception
	 *             if any error
	 */
	public static float[][] divideByScalar(float[][] a, float b) throws Exception {

		float[][] result = new float[a.length][a[0].length];

		result = add(result, a);

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				result[i][j] = result[i][j] / b;
			}
		}

		return result;
	}
}
