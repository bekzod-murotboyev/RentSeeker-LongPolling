package uz.pdp.rentseekerlongpolling.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.pdp.rentseekerlongpolling.util.enums.BotState;
import uz.pdp.rentseekerlongpolling.util.enums.Language;
import uz.pdp.rentseekerlongpolling.util.enums.Role;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LanStateDTO {
    Language language;
    BotState state;
    Role role;
    boolean isAdmin;
    boolean isActive;
}
