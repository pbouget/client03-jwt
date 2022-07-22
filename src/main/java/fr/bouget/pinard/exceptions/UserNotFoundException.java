package fr.bouget.pinard.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "Désolé, l'utilisateur introuvable !"; 
	}
	
}
