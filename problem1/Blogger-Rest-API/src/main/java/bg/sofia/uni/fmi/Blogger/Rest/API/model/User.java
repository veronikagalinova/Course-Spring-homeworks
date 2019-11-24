package bg.sofia.uni.fmi.Blogger.Rest.API.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;


@Document("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    @NotNull
    @Length(min = 3, max = 30)
    @NonNull
    private String firstName;

    @NotNull
    @Length(min = 3, max = 30)
    @NonNull
    private String lastName;

    @NotNull
    @Length(min = 5, max = 30)
    @NonNull
    private String email;

    @NotNull
    @Length(min = 5, max = 30)
    @NonNull
    private String password;

    private Role role;

    private String imageURL;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
