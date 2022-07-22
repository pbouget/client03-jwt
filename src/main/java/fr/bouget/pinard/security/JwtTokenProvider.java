package fr.bouget.pinard.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.bouget.pinard.exceptions.InvalidJWTException;
import fr.bouget.pinard.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT : classe utilitaire chargée de fournir le Jeton (Token) et les vérifications
 */
@Component
public class JwtTokenProvider {

	// on récupère le secret dans notre fichier application.properties
	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	// ici on met la valeur par défaut
	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h pour être pénard

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Cette méthode d'initialisation s'exécute avant le constructeur
	 * Elle encode notre code secret en base64 pour la transmission dans le header
	 */
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}


	/**
	 * @param username the user username.
	 * @param roles the user roles.
	 * @return the created JWT as String.
	 * @throws JsonProcessingException 
	 */
	public String createToken(String username, List<Role> roles){

		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

		System.out.println("claims = "+claims);
		// claims = {sub=pbouget, auth=[ROLE_ADMIN, ROLE_CREATOR, ROLE_READER]}
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		String leToken = Jwts.builder()//
				.setClaims(claims)								// le username avec les roles ou setPayload()
				.setIssuedAt(now)								// 1589817421  pour le 18 mai 2020 à 17 heure 57
				.setExpiration(validity)						// 1589821021 même date avec 1 heure de plus
				.signWith(SignatureAlgorithm.HS256, secretKey) 	// la signature avec la clef secrête.
				.compact();										// concatène l'ensemble pour construire une chaîne

		return leToken;
	}

	/**
	 * Methode qui retourne un objet Authentication basé sur JWT.
	 * @param token : le token pour l'authentification.
	 * @return the authentication si Username est trouvé.
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * Methode qui extrait le userName (le sub ou subject) du JWT.
	 * @param token : Token a analyser.
	 * @return le UserName comme chaîne de caractères.
	 */
	public String getUsername(String token) {

		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Méthode qui récupère la requete HTTP et retourne une chaîne.
	 * L'entête doit contenir un champ d'autorisation ou JWT ajoute le token après le mot clef Bearer.
	 * @param requete : la requête à tester.
	 * @return le JWT depuis l'entête HTTP.
	 */
	public String resolveToken(HttpServletRequest requeteHttp) {
		String bearerToken = requeteHttp.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	/**
	 * Methode qui vérifie la validité du token.
	 * La signature doit être correcte et la durée de validité du Token doit être après "now" (maintenant)
	 * @param token : Token à valider
	 * @return True si le Token est valide sinon on lance l'exception InvalidJWTException.
	 * @throws InvalidJWTException
	 */
	public boolean validateToken(String token) throws InvalidJWTException {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new InvalidJWTException();
		}
	}
}
