package uz.pdp.rentseekerlongpolling.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Attachment extends BaseModel {

    @Column(nullable = false, name = "file_id")
    @JsonProperty("file_id")
    private String fileId;

    @Column(nullable = false, name = "file_unique_id")
    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @Column(nullable = false, name = "width")
    @JsonProperty("width")
    private Integer width;

    @Column(nullable = false, name = "height")
    @JsonProperty("height")
    private Integer height;

    @Column(nullable = false, name = "file_size")
    @JsonProperty("file_size")
    private long fileSize;

    @Column(name = "file_path")
    @JsonProperty(value = "file_path")
    private String filePath;

    @JsonProperty("content_type")
    private String contentType;
}
