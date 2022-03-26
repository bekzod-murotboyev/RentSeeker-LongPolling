package uz.pdp.rentseekerlongpolling.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.entity.base.BaseModel;
import uz.pdp.rentseekerlongpolling.util.enums.District;
import uz.pdp.rentseekerlongpolling.util.enums.HomeStatus;
import uz.pdp.rentseekerlongpolling.util.enums.HomeType;
import uz.pdp.rentseekerlongpolling.util.enums.Region;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchDTO  {
    Region region;

    District district;

    HomeStatus status;

    HomeType homeType;

    Integer numberOfRooms = -1;

    Integer minPrice = -1;

    Integer maxPrice = -1;

}
