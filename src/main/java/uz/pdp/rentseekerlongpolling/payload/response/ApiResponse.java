package uz.pdp.rentseekerlongpolling.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse {
    boolean success;
    String message;
    Object data;


    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
