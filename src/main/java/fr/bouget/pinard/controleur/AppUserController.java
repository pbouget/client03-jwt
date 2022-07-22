package fr.bouget.pinard.controleur;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.bouget.pinard.dto.AppUserDto;
import fr.bouget.pinard.exceptions.ExistingUsernameException;
import fr.bouget.pinard.exceptions.InvalidCredentialsException;
import fr.bouget.pinard.exceptions.NotExistingUsernameException;
import fr.bouget.pinard.model.AppUser;
import fr.bouget.pinard.security.JsonWebToken;
import fr.bouget.pinard.service.AppUserService;

/**
 * Ce contrôleur écoute les appels HTTP sur l'url "/api/user"
 * Il gère, l'enregistrement d'un utilisateur, son authentification
 * et fournit la liste des Utilisateurs à l'administrateur (un compte avec le rôle Admin).
 */
@RestController
@RequestMapping("/api/user")
public class AppUserController {
	
	@Autowired
    private AppUserService appUserService;

    /**
     * Methode pour enregistrer un nouvel utilisateur dans la BD.
     * @param user utiliateur.
     * @return un JWT si la connection est OK sinon une mauvaise réponse
     */
    @PostMapping("/sign-up")
    public ResponseEntity<JsonWebToken> signUp(@RequestBody AppUser user) {
        try {
            return ResponseEntity.ok(new JsonWebToken(appUserService.signup(user)));
        } catch (ExistingUsernameException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Methode pour se connecter (le user existe déjà).
     * @param user : utilisateur qui doit se connecter.
     * @return un JWT si la connection est OK sinon une mauvaise réponse.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<JsonWebToken> signIn(@RequestBody AppUser user) {
        try {
        	// ici on créé un JWT en passant l'identifiant et le mot de passe
        	// récupéré de l'objet user passé en paramètre.
            return ResponseEntity.ok(new JsonWebToken(appUserService.signin(user.getUsername(), user.getPassword())));
        } catch (InvalidCredentialsException ex) {
        	// on renvoie une réponse négative
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Methode pour retourner tous les utilisateurs de la BD.
     * Cette méthode est accesible pour les utilisateurs ayant le rôle ROLE_ADMIN.
     * @return liste de tous les utilisateurs enregistrés en BD.
     */
   @GetMapping("/all")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AppUser> getAllUsers() {
        return appUserService.findAllUsers();
     
 }
   
   /**
    * Permet de retourner les infos sans les mots de passe en passant par un objet AppUserDto
    * C'est mieux et plus sécurisant !
    * @return
    */
   	@GetMapping("/admin/all")
//   	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AppUserDto> getAllAdminUsers() {
           return appUserService.findAllUsers().stream().map(appUser -> new AppUserDto(appUser.getUsername(), appUser.getRoleList())).collect(Collectors.toList());
        
    }
   	
   	@DeleteMapping("/delete/{appUserName}")
 	@PreAuthorize("hasRole('ROLE_ADMIN')")
   	public ResponseEntity<?> deleteUser(@PathVariable String appUserName) {
   		try {
            return ResponseEntity.ok(appUserService.deleteByUserName(appUserName));
        } catch (NotExistingUsernameException ex) {
            return ResponseEntity.ok(ex.getMessage());
        }
   		
   	}
    


    /**
     * Methode pour récupérer le user dans la bd à partir de son username.
     * Cette méthode est uniquement accessible par un user Admin.
     * @param appUserName à chercher.
     * @return un User est trouvé , a not found response code otherwise.
     */
    @GetMapping("/{appUserName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUserDto> getOneUser(@PathVariable String appUserName) {
        Optional<AppUser> appUser = appUserService.findUserByUserName(appUserName);
        if (appUser.isPresent()) {
            return ResponseEntity.ok(new AppUserDto(appUser.get().getUsername(), appUser.get().getRoleList()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
}
