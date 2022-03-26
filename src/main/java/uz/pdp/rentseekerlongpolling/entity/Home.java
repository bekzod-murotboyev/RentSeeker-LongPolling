package uz.pdp.rentseekerlongpolling.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;
import uz.pdp.rentseekerlongpolling.util.enums.District;
import uz.pdp.rentseekerlongpolling.util.enums.HomeStatus;
import uz.pdp.rentseekerlongpolling.util.enums.HomeType;
import uz.pdp.rentseekerlongpolling.util.enums.Region;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Home extends BaseModel {

    @ManyToOne
    User user;

    @Enumerated(value = EnumType.STRING)
    HomeStatus status;

    @Enumerated(value = EnumType.STRING)
    HomeType homeType;

    @Enumerated(value = EnumType.STRING)
    Region region;

    @Enumerated(value = EnumType.STRING)
    District district;

    @Column(columnDefinition = "text")
    String address;

    int numberOfRooms;

    double area;

    double price;

    @Column(columnDefinition = "text")
    String description;

    long interests;

    String mapUrl;

    String fileId;

    int fileSize;

    boolean ban;

    long likes;
    {
        active=false;
    }


}
