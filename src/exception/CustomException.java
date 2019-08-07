package exception;

public class CustomException extends Exception {

	/**
	 * This class contains the custom exception cases used across project.
	 */

	private static final long customExceptionVersionUID = 7718828512143293558L;

	public CustomException() {
		super();
	}

	public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomException(String message) {
		System.out.println(message);
	}

	public CustomException(Throwable cause) {
		super(cause);
	}

}
