package bg.sofia.uni.fmi.Blogger.Rest.API.domain;

import bg.sofia.uni.fmi.Blogger.Rest.API.dao.PostsRepository;
import bg.sofia.uni.fmi.Blogger.Rest.API.exception.NonexisitngEntityException;
import bg.sofia.uni.fmi.Blogger.Rest.API.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostsServiceImpl implements PostsService {

    @Autowired
    private PostsRepository repository;


    @Override
    public List<Post> findAll() {
        return repository.findAll();
    }

    @Override
    public Post findById(String id) {
        return repository.findById(id).orElseThrow(() -> new NonexisitngEntityException(
                String.format("Post with ID=%s does not exist.", id)));
    }

    @Override
    public Post add(Post post) {
        return repository.insert(post);
    }

    @Override
    public Post update(Post post) {
        Optional<Post> postInDB = repository.findById(post.getId());
        if (!postInDB.isPresent()) {
            throw new NonexisitngEntityException(
                    String.format("Post with ID='%s' does not exist.", post.getId()));
        }

        post.setPublished(LocalDateTime.now());
        return repository.save(post);
    }

    @Override
    public Post remove(String postId) {

        Optional<Post> old = repository.findById(postId);
        log.info("!!!!!! DELETING = " + postId);
        if (!old.isPresent()) {
            throw new NonexisitngEntityException(
                    String.format("Post with ID='%s' does not exist.", postId));
        }
        repository.deleteById(postId);
        return old.get();
    }
}
