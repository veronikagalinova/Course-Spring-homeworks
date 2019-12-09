package bg.sofia.uni.fmi.Blogger.Rest.API.web;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.UsersService;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.InvalidEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.NonexisitngEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Role;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UsersController {

    @Autowired
    private UsersService service;

    private static final String INVALID_ENTITY_ERROR_MSG = "User ID=%s from path is different from Entity ID=%s";
    private static final String FORBIDDEN_ERROR_MSG = "Only ADMINISTRATOR can change user roles!";
    private static final String EXISTING_USER_ERROR_MSG = "User %s already exist!";



    @GetMapping
    public List<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("{id}")
    public User getUserByID(@PathVariable String id, Authentication authentication) {
        verifyUserCredentials(id, authentication);
        return service.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userInDb = null;
        try {
            userInDb = service.findByUsername(user.getUsername());
        } catch (NonexisitngEntityException ex) {
            log.debug(ex.getMessage());
        }
        if (userInDb != null) {
            throw new InvalidEntityException(
                    String.format(EXISTING_USER_ERROR_MSG, user.getUsername()));
        }
        User created = service.createUser(user);
        URI location = MvcUriComponentsBuilder.fromMethodName(UsersController.class, "createUser", User.class)
                .pathSegment("{id}").buildAndExpand(created.getId()).toUri() ;
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user, Authentication authentication) {
        verifyUserCredentials(id, authentication);
        if(!user.getId().equals(id)) throw new InvalidEntityException(
                String.format(INVALID_ENTITY_ERROR_MSG, id, user.getId()));
        User updated = service.updateUser(user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> remove(@PathVariable String id, Authentication authentication) {
        verifyUserCredentials(id, authentication);
        return ResponseEntity.ok(service.deleteUser(id));
    }

    private void verifyUserCredentials(String id, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        if (!isUserAdministrator(principal) && !id.equals(principal.getId())) {
            throw new AccessDeniedException(String.format(INVALID_ENTITY_ERROR_MSG, principal.getId(), id));
        }
    }


    private boolean isUserAdministrator(User user) {
        return user.getRoles().indexOf(Role.ROLE_ADMINISTRATOR) != -1;
    }

}
