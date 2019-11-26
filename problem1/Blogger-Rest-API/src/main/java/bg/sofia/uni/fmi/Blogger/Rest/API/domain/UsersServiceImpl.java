package bg.sofia.uni.fmi.Blogger.Rest.API.domain;

import bg.sofia.uni.fmi.Blogger.Rest.API.dao.UsersRepository;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.InvalidEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.NonexisitngEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Role;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository repository;

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public User createUser(@Valid User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Arrays.asList(Role.BLOGGER));
        }
        user.setActive(true);
        User created = repository.save(user);
        log.debug(">>>Created new user: " + created);
        return created;
    }

    @Override
    public User updateUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getUserById(String id) {
        if(id == null) return null;
        return repository.findById(id).orElseThrow(() ->
                new NonexisitngEntityException(String.format("User with ID=%s not found.", id)));
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null || "".equals(email.trim())) {
            return null;
        }

        Optional<User> foundUser = repository.findByEmail(email);
        if (!foundUser.isPresent()) {
            return null;
        }

        log.debug(">>>>Found user by email: ", foundUser.get());
        return foundUser.get();
    }

    @Override
    public User deleteUser(String id) {
        User user = repository.findById(id).orElseThrow( () ->
                new NonexisitngEntityException(String.format("User with ID=%s not found.", id)));
        repository.deleteById(id);
        return user;
    }
}
