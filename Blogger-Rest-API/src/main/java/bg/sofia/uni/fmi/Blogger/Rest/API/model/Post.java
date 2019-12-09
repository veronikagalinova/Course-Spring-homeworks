package bg.sofia.uni.fmi.Blogger.Rest.API.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;
    private LocalDateTime published = LocalDateTime.now();
    private String title;
    private String author;
    private String content;
    private List<String> tags = new ArrayList<>();
    private String imageURL;
    private Status status;

}
