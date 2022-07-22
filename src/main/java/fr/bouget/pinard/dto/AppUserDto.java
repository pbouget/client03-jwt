package fr.bouget.pinard.dto;

import java.util.List;

import com.sun.istack.NotNull;

import fr.bouget.pinard.model.Role;

/**
 * Specifique : AppUser DTO permet de renvoyer un User sans le mot de passe (REST response).
 */
public class AppUserDto {

    private Long id;
    private String username;
    private List<Role> roleList;

    
    public AppUserDto() { }

    public AppUserDto(@NotNull String username) {
        this(username,null);
    }

    public AppUserDto(@NotNull String username, List<Role> roleList) {
        this.username = username;
        this.roleList = roleList;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
    
}
