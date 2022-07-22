package fr.bouget.pinard;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.bouget.pinard.model.Adresse;
import fr.bouget.pinard.model.AppUser;
import fr.bouget.pinard.model.Client;
import fr.bouget.pinard.model.Role;
import fr.bouget.pinard.repository.ClientRepository;
import fr.bouget.pinard.service.AppUserService;

@SpringBootApplication
@EnableWebMvc
@Configuration
public class Client03Application implements CommandLineRunner{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AppUserService userService;

	public static void main(String[] args) {
		SpringApplication.run(Client03Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		AppUser jg = new AppUser("jojo", "jojo", new ArrayList<>(Arrays.asList(Role.ROLE_ADMIN, Role.ROLE_CREATOR, Role.ROLE_READER)));
		userService.signup(jg);

		AppUser pb = new AppUser("pbouget", "pbouget", new ArrayList<>(Arrays.asList(Role.ROLE_ADMIN, Role.ROLE_CREATOR, Role.ROLE_READER)));
		userService.signup(pb);

		AppUser jt = new AppUser("titi", "titi", new ArrayList<>(Arrays.asList(Role.ROLE_CREATOR, Role.ROLE_READER)));
		userService.signup(jt);
		
		AppUser bb = new AppUser("bob", "bob", new ArrayList<>(Arrays.asList(Role.ROLE_READER)));
		userService.signup(bb);

		Client client1=new Client("MARTIN","Timothée");
		clientRepository.saveAndFlush(client1);

		Adresse adresse1=new Adresse("5, rue du Renard","","75015","PARIS","FRANCE");
		Client client2=new Client("DUPONT","Amélie",adresse1);
		clientRepository.saveAndFlush(client2);

		Client client3=new Client("DURAND","Martial",new Adresse("20, boulevard Gambetta","","78300","POISSY","FRANCE"));
		client3=clientRepository.saveAndFlush(client3);

		Client client4=new Client("MADEC","Max",new Adresse("29, boulevard Devaux","","78300","POISSY","FRANCE"));
		client4=clientRepository.saveAndFlush(client4);

	}

	/**
	 * Ceci est un Bean, un composant
	 * Méthode de Hachage
	 * Bcrypt est un algorithme de hachage considéré comme le plus sûr.
	 * Bcrypt est un algorithme de hashage unidirectionnel,
	 * vous ne pourrez jamais retrouver le mot de passe sans connaitre à la fois le grain de sel,
	 * la clé et les différentes passes que l'algorithme à utiliser.
	 * Voir le <a href="https://bcrypt-generator.com/"> site pour effectuer un test</a>
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}



}