package fr.bouget.pinard.exceptions;

public class NotExistingEmailException extends Exception {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "Désolé, l'email n'existe pas !"; 
	}

}
