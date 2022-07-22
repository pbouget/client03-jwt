package fr.bouget.pinard.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.bouget.pinard.exceptions.ExistingUsernameException;
import fr.bouget.pinard.exceptions.InvalidCredentialsException;
import fr.bouget.pinard.exceptions.NotExistingEmailException;
import fr.bouget.pinard.exceptions.NotExistingUsernameException;
import fr.bouget.pinard.exceptions.UserNotFoundException;
import fr.bouget.pinard.model.AppUser;
import fr.bouget.pinard.repository.AppUserRepository;
import fr.bouget.pinard.security.JwtTokenProvider;

@Service
public class AppUserServiceImpl implements AppUserService {

	@Autowired
	private AppUserRepository appUserRepository; // permet communication avec la BD

	@Autowired
	private BCryptPasswordEncoder passwordEncoder; // permet l'encodage du mot de passe

	@Autowired
	private JwtTokenProvider jwtTokenProvider;	// permet la fourniture du Jeton (Token)

	@Autowired
	private AuthenticationManager authenticationManager; // gestionnaire d'authentification


	/**
	 * Permet de se connecter en encodant le mot de passe avec génération du token.
	 */
	@Override
	public String signin(String username, String password) throws InvalidCredentialsException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return jwtTokenProvider.createToken(username, appUserRepository.findByUsername(username).get().getRoleList());
		} catch (AuthenticationException e) {
			throw new InvalidCredentialsException();
		}
	}

	@Override
	public String signup(AppUser user) throws ExistingUsernameException {
		if (!appUserRepository.existsByUsername(user.getUsername())) {
			AppUser userToSave = new AppUser(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRoleList());
			appUserRepository.save(userToSave);
			return jwtTokenProvider.createToken(user.getUsername(), user.getRoleList());
		} else {
			throw new ExistingUsernameException();
		}
	}

	@Override
	public List<AppUser> findAllUsers() {
		return appUserRepository.findAll();
	}

	@Override
	public Optional<AppUser> findUserByUserName(String username) {
		return appUserRepository.findByUsername(username);
	}

	@Override
	public String deleteByUserName(String username) throws NotExistingUsernameException {

		if (appUserRepository.existsByUsername(username))
		{
			appUserRepository.deleteByUsername(username);
			return "Utilisateur effacé !";
		}
		else {
			throw new NotExistingUsernameException();
		}

	}

	@Override
	public Optional<AppUser> findByResetPasswordToken(String token) {
		
		return null;
	}
	
	// à implémenter in the future pour pouvoir renouveler son mot de passe en envoyant grâce à
	// un token...
//	 public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
//	        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
//	        if (appUser.isPresent()) {
//	            appUser.get().setResetPasswordToken(token);
//	            appUserRepository.save(appUser.get());
//	        } else {
//	            throw new UserNotFoundException();
//	        }
//	    }

	 public Optional<AppUser> getByResetPasswordToken(String token) {
	        return appUserRepository.findByResetPasswordToken(token);
	    }
	     
	    public void updatePassword(AppUser appUser, String newPassword) {
	        appUser.setPassword(passwordEncoder.encode(newPassword));
	        appUser.setResetPasswordToken(null);
	        appUserRepository.save(appUser);
	    }

}
