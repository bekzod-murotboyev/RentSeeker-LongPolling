package uz.pdp.rentseekerlongpolling.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;
import uz.pdp.rentseekerlongpolling.util.enums.BotState;
import uz.pdp.rentseekerlongpolling.util.enums.Language;
import uz.pdp.rentseekerlongpolling.util.enums.Role;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User extends BaseModel {
    String name;
    String phoneNumber;
    String username;
    String chatId;
    String code;
    int crtPage;

    @Enumerated(value = EnumType.STRING)
    Language language;

    @Enumerated(value = EnumType.STRING)
    BotState state;

    @Enumerated(value = EnumType.STRING)
    Role role;

    boolean admin = false;

    public User(String name, String phoneNumber, String username, String chatId, Language language, BotState state, Role role, boolean admin) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.chatId = chatId;
        this.language = language;
        this.state = state;
        this.role = role;
        this.admin = admin;
    }
}
