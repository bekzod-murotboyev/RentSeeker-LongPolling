package uz.pdp.rentseekerlongpolling.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity(name = "likes")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"home_id","user_id"})})
public class Like extends BaseModel {

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    Home home;

    @ManyToOne
    User user;

    {
        active = false;
    }
}
