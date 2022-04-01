package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.rentseekerlongpolling.entity.Home;

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
}
