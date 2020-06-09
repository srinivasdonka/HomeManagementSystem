package com.homemanagement.exception;


public class StorageException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7453016689694379998L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

}