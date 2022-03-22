package uz.pdp.rentseekerlongpolling.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
public class Interest extends BaseModel {

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    Home home;

    @ManyToOne
    User user;

    boolean visible;

    {
        visible = false;
        active = false;
    }
}
