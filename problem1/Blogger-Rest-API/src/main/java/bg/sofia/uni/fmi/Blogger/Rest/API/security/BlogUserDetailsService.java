package bg.sofia.uni.fmi.Blogger.Rest.API.security;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.UsersService;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Role;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class BlogUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user with email=" + email);
        }
        return user;
    }
}
