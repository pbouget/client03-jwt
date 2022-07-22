package fr.bouget.pinard.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de Sécurité globale pour notre REST API.
 * L'annotation <strong>Configuration</strong> permet d'être scannée au démarrage de l'application
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Methode qui configure la sécurité HTTP. Version simplifiée
	 * @param http the HttpSecurity object to configure.
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors(); // pour besoin ultérieur

		http.csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers("/client/**").hasAuthority("ROLE_READER") 	// accessible avec besoin de s'authentifier comme reader
		.antMatchers("/api/user/sign-in").permitAll() 			// tout le monde peut essayer de se connecter
		.antMatchers("/api/user/sign-up").permitAll() 			// tout le monde peut s'inscrire
		.antMatchers("api/user/admin/all").hasAuthority("ROLE_ADMIN") // uniquement pour le rôle admin
		.anyRequest().authenticated(); 			// tout le reste est autorisé par un utilisateur authentifié

		// Appliquer un filtre avec le token pour toutes requêtes HTTP
		http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

	}

	/**
	 * Methode qui configure la sécurité web.
	 * Pas besoin d'autorisation pour les fichiers dans resources
	 * @param web : WebSecurity
	 * @throws Exception
	 */
	@Override
	public void configure(WebSecurity websecurity) throws Exception {
		websecurity.ignoring().antMatchers("/resources/**");
	}
}


