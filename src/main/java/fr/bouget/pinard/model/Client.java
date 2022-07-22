package fr.bouget.pinard.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Client implements Serializable {
	public static final long serialVersionUID=987654378L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length = 45)
	private String nom;
	@Column(length = 45)
	private String prenom;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="ADDRESS_ID")
	private Adresse adresse;

	public Client(){
		this(null,null);
	}

	public Client(String nom, String prenom){
		this(nom, prenom, null);
	}

	public Client(String nom, String prenom, Adresse adresse){
		this.prenom=prenom;
		this.nom=nom;
		this.adresse=adresse;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	@Override
	public String toString(){
		if(adresse==null){
			return id+" "+nom+" "+prenom+ "Aucune adresse";
		}else{
			return id+" "+nom+" "+prenom+" adresse :"+adresse.toString();
		}
	}
}
