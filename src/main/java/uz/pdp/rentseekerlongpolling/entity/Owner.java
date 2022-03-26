package uz.pdp.rentseekerlongpolling.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Owner extends BaseModel implements UserDetails {

    @Column(unique = true, nullable = false)
    String username;

    @Column(unique = true, nullable = false)
    String mail;

    String password;

    boolean enabled = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;
    boolean accountNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
