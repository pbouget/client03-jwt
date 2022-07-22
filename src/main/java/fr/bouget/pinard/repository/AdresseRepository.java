package fr.bouget.pinard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.bouget.pinard.model.Adresse;

/**
 * @author Philippe
 *
 */
@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {

}
