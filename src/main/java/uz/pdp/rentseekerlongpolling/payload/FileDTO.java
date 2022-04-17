package uz.pdp.rentseekerlongpolling.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileDTO {
    String contentType;
    File file;
}
