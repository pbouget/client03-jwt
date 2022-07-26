package fr.bouget.pinard.exceptions;

/**
 * Specific exception that should be thrown when user credentials are not valid.
 */
public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = -6483691380297851921L;

	@Override
	public String getMessage()
	{
		return "L'accréditation est invalide !"; 
	}
}
