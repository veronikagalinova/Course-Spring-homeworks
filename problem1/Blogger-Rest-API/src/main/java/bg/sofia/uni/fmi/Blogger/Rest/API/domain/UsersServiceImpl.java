package bg.sofia.uni.fmi.Blogger.Rest.API.domain;

import bg.sofia.uni.fmi.Blogger.Rest.API.dao.UsersRepository;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.NonexisitngEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.List;

public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository repository;

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public User createUser(@Valid User user) {
        return null;
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
        return null;
    }

    @Override
    public User deleteUser(String id) {
        User user = repository.findById(id).orElseThrow( () ->
                new NonexisitngEntityException(String.format("User with ID=%s not found.", id)));
        repository.deleteById(id);
        return user;
    }
}
