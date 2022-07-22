package fr.bouget.pinard.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.bouget.pinard.model.AppUser;
import fr.bouget.pinard.repository.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username)  {
        final Optional<AppUser> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("utilisateur '" + username + "' introuvable");
        }

        return User
                .withUsername(username)
                .password(user.get().getPassword())
                .authorities(user.get().getRoleList())
//                .accountExpired(false)		// ils sont tous optionnels car tous à false par défaut !
//                .accountLocked(false)			// donc inutiles
//                .credentialsExpired(false)
//                .disabled(false)
                .build();
    }

/// autre écriture possible :    
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Objects.requireNonNull(username);
//        AppUser user = userRepository.findByUsername(username).
//        		orElseThrow(() -> new UsernameNotFoundException("utilisateur '" + username + "' introuvable"));
//        
//        return user;
//    }
}
