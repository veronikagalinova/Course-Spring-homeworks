package bg.sofia.uni.fmi.Blogger.Rest.API.web;

import bg.sofia.uni.fmi.Blogger.Rest.API.domain.posts.PostsService;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostsService service;

    @GetMapping
    public List<Post> getPosts() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post created = service.add(post);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(created.getId()))
                .body(created);
    }
}
