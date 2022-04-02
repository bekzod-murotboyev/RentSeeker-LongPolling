package uz.pdp.rentseekerlongpolling.payload.home;


import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.pdp.rentseekerlongpolling.entity.Attachment;
import uz.pdp.rentseekerlongpolling.util.enums.District;
import uz.pdp.rentseekerlongpolling.util.enums.HomeStatus;
import uz.pdp.rentseekerlongpolling.util.enums.HomeType;
import uz.pdp.rentseekerlongpolling.util.enums.Region;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeAddDTO {

    @NotNull
    UUID userId;

    @NotNull
    HomeStatus status;

    @NotNull
    HomeType homeType;

    @NotNull
    Region region;

    @NotNull
    District district;

    @NotNull
    String address;

    @NotNull
    int numberOfRooms;

    @NotNull
    double area;

    @NotNull
    double price;

    @NotNull
    String description;

    String mapUrl;

    @NotNull
    List<Attachment> attachments;

}
