package uz.pdp.rentseekerlongpolling.payload.telegram.telegramFeign;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultDelete {
    boolean ok;
    boolean result;
}
