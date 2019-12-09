package bg.sofia.uni.fmi.Blogger.Rest.API.web;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.PostsService;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.InvalidEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Post;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Role;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    private static final String INVALID_ENTITY_ERROR_MSG = "User ID=%s from path is different from Entity ID=%s";
    private static final String FORBIDDEN_UPDATE_ERROR_MSG = "Post by author %s cannot be edited by user with username %s.";
    private static final String FORBIDDEN_DELETE_ERROR_MSG = "Post with id %s cannot be deleted by user with username %s.";

    @Autowired
    private PostsService service;

    @GetMapping
    public List<Post> getPosts() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Post getArticleById(@PathVariable("id") String articleId) {
        return service.findById(articleId);
    }

    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post created = service.add(post);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(created.getId()))
                .body(created);
    }

    @PutMapping("{id}")
    public Post update(@PathVariable String id, @Valid @RequestBody Post post, Authentication authentication) {
        if(!id.equals(post.getId())) {
            throw new InvalidEntityException(
                    String.format(INVALID_ENTITY_ERROR_MSG, post.getId(), id));
        }
        User principal = (User) authentication.getPrincipal();

        if (!isUserAdministrator(principal) && !post.getAuthor().equals(principal.getUsername())) {
            throw new AccessDeniedException(String.format(FORBIDDEN_UPDATE_ERROR_MSG, post.getAuthor(),
                    principal.getUsername()));
        }
        return service.update(post);
    }

    @DeleteMapping("{id}")
    public Post remove(@PathVariable String id, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();

        Post post = service.findById(id);
        if (!isUserAdministrator(principal) && !post.getAuthor().equals(principal.getUsername())) {
            throw new AccessDeniedException(String.format(FORBIDDEN_DELETE_ERROR_MSG, id,
                    principal.getUsername()));
        }
        return service.remove(id);
    }

    private boolean isUserAdministrator(User user) {
        return user.getRoles().indexOf(Role.ROLE_ADMINISTRATOR) != -1;
    }
}
