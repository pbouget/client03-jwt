package fr.bouget.pinard.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

/**
 * App User entity that represents an application user.
 */
@Entity
@Table(name="user")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String username;

	@NotNull
	@Column(nullable = false)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private List<Role> roleList;
	
	
	@Column(name = "resetPasswordToken", nullable = true)
	private String resetPasswordToken;
	
	public AppUser() {

	}

	public AppUser(@NotNull String username, @NotNull String password) {
		this.username = username;
		this.password = password;
	}

	public AppUser(@NotNull String username, @NotNull String password, List<Role> roleList) {
		this.username = username;
		this.password = password;
		this.roleList = roleList;
	}
	

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
	
	
}
