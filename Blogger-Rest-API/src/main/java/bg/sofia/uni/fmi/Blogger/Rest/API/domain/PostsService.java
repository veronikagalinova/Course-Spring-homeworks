package bg.sofia.uni.fmi.Blogger.Rest.API.domain;

import bg.sofia.uni.fmi.Blogger.Rest.API.model.Post;

import java.util.List;

public interface PostsService {
    List<Post> findAll();
    Post findById(String id);
    Post add(Post post);
    Post update(Post post);
    Post remove(String postId);
    long getSize();
}
