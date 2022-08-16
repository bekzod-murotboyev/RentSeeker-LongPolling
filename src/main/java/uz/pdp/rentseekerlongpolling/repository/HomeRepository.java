package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.util.enums.District;
import uz.pdp.rentseekerlongpolling.util.enums.HomeStatus;
import uz.pdp.rentseekerlongpolling.util.enums.HomeType;
import uz.pdp.rentseekerlongpolling.util.enums.Region;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HomeRepository extends JpaRepository<Home, UUID> {

    List<Home> findAllByUserIdAndActiveTrue(UUID user_id);

    Page<Home> findByUser_PhoneNumber(String phoneNumber, Pageable pageable);

    Optional<Home> findByUser_ChatIdAndActiveFalse(String user_chatId);

    Optional<Home> findByUserIdAndActiveFalse(UUID userId);

    Page<Home> findAllByActiveTrue(Pageable pageable);

    List<Home> findAllByActiveTrue();

    List<Home> findByActive(boolean active);

    @Modifying
    @Query(nativeQuery = true, value = "select * from search_home(:region,:district,:status,:homeType,:numberOfRooms,:minPrice,:maxPrice)")
    List<Home> searchHomes(
             String region,
             String district,
             String status,
             String homeType,
             Integer numberOfRooms,
             Double minPrice,
             Double maxPrice);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(nativeQuery = true,value = "call delete_all_homes()")
    void deleteAllHomes();
}
