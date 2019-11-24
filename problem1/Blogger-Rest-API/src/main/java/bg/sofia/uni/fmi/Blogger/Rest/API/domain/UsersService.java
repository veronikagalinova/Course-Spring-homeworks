package bg.sofia.uni.fmi.Blogger.Rest.API.domain;

import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UsersService {
    List<User> getUsers();
    User createUser(@Valid User user);
    User updateUser(User user);
    User getUserById(String id);
    User getUserByEmail(String email);
    User deleteUser(String id);
}
