package fr.bouget.pinard.controleur;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.bouget.pinard.model.Client;
import fr.bouget.pinard.repository.ClientRepository;

/**
 * @author Philippe
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientRepository clientRepository;
	
	// ici pas besoin d'autorisation mais du coup, dans la configuration,
	// il faut au moins être authentifié comme READER
	@GetMapping(value = "/all")
	public ResponseEntity<?> getAll(){
		List<Client> liste = null;
		try
		{
			liste = clientRepository.findAll();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(liste);
	}

	
	/**
	 * Methode pour créer film (movie).
	 * Accessible pour les rôles Admin ou Creator.
	 * @param newMovie nouveau film à créer.
	 * @return le movie créé.
	 */
	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CREATOR')")
	public ResponseEntity<Client> createMovie(@RequestBody Client newClient) {
		return ResponseEntity.ok(clientRepository.saveAndFlush(newClient));
	}

	/**
	 * Méthode pour détruire un client
	 * Accessible pour les rôles Admin ou Creator.
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CREATOR')")
	public ResponseEntity<?> deleteClient(@PathVariable Integer id) {

		try {
			this.clientRepository.deleteById(id);
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	/**
	 * Méthode pour mettre à jour un client
	 * Accessible pour les rôles Admin ou Creator.
	 * @param updateMovie
	 * @return
	 */
	@PutMapping("/update")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CREATOR')")
	public ResponseEntity<?> updateClient(@RequestBody Client updateClient)
	{
		try {

			this.clientRepository.saveAndFlush(updateClient);
			return ResponseEntity.ok("ok");
			} catch(Exception e)
				{
					return ResponseEntity.notFound().build();
				}	
	}
	
}
