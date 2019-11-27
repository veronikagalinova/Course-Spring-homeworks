package bg.sofia.uni.fmi.Blogger.Rest.API.init;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.UsersService;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Role;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Slf4j
@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UsersService usersService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (usersService.getSize() == 0) {
            User user = new User("admin", "admin123!", "Admin", "Admin", Arrays.asList(Role.ROLE_ADMINISTRATOR, Role.ROLE_BLOGGER), true);
            log.info(">>>Created root admin user: {}",  user);
            usersService.createUser(user);
        }
    }
}
