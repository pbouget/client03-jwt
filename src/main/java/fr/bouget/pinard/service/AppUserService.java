package fr.bouget.pinard.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.bouget.pinard.exceptions.ExistingUsernameException;
import fr.bouget.pinard.exceptions.InvalidCredentialsException;
import fr.bouget.pinard.exceptions.NotExistingUsernameException;
import fr.bouget.pinard.model.AppUser;

@Service
public interface AppUserService {


	/**
     * Methode qui permet à un utilisateur de se connecter.
     * @param username : nom de l'utilisateur.
     * @param password : mot de passe de l'utilisateur.
     * @returnun JWT si credentials est valide, throws InvalidCredentialsException otherwise.
     * @throws InvalidCredentialsException
     */
    String signin(String username, String password) throws InvalidCredentialsException;

    /**
     * Methode qui permet de s'inscrire.
     * @param user nouvel utilisateur.
     * @return un JWT si user n'existe pas déjà !
     * @throws ExistingUsernameException
     */
    String signup(AppUser user) throws ExistingUsernameException;

    /**
     * Methode qui retourne tous les utilisateurs de la bd
     * @return the list of all application users.
     */
    List<AppUser> findAllUsers();

    /**
     * Methode qui retourne un utilisateur à partir de son username
     * @param username the username to look for.
     * @return an Optional object containing user if found, empty otherwise.
     */
    Optional<AppUser> findUserByUserName(String username);
    
    /**
     * Methode qui retourne un utilisateur à partir de son username
     * @param username nom à effacer dans la base
     * @return une chaîne correspondant à une réponse si ok ou ko
     * @throws NotExistingUsernameException
     */
    String deleteByUserName(String username) throws NotExistingUsernameException;
    
 
    /**
     * Méthode pour retourner un utilisateur en fonction du token
     * @param token
     * @return un utilisateur
     */
    public Optional<AppUser> findByResetPasswordToken(String token);
    
}
