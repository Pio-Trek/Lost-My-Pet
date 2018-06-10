package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.model.users.User;
import graded.unit.lostmypetwebapp.model.users.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements
        UserDetailsService, Serializable {

    private final UserService service;

    @Autowired
    public UserDetailsServiceImpl(UserService service) {
        this.service = service;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = service.getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        if (!user.getEnabled()) {
            throw new AccessDeniedException(email);
        }

        List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }


    private static List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<>();
        for (UserRole role : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new ArrayList<>(setAuths);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication
            (User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User
                (user.getEmail(), user.getPassword(), authorities);
    }

    public static void authenticateUpdateUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), buildUserAuthority(user.getRoles()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


}
