package uz.pdp.rentseekerlongpolling.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {
    boolean success;
    String message;
    Object object;


    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
