package bg.sofia.uni.fmi.Blogger.Rest.API.dao;

import bg.sofia.uni.fmi.Blogger.Rest.API.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostsRepository extends MongoRepository<Post, String> {
}
