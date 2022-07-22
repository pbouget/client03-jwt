package fr.bouget.pinard.exceptions;

/**
 * Classe personnalisée pour gérer un message si l'utilisateur n'existe pas en Base de données
 */
public class NotExistingUsernameException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "Désolé, l'utilisateur n'existe pas dans la Base de données !"; 
	}
}
