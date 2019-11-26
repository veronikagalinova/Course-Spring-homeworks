package bg.sofia.uni.fmi.Blogger.Rest.API.web;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.UsersService;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService service;

    @GetMapping
    public List<User> getUsers() {
        return service.getUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = service.createUser(user);
        URI location = MvcUriComponentsBuilder.fromMethodName(UsersController.class, "createUser", User.class)
                .pathSegment("{id}").buildAndExpand(created.getId()).toUri() ;
        return ResponseEntity.created(location).body(created);
    }

}
