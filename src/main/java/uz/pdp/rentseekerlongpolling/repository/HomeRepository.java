package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.rentseekerlongpolling.entity.Home;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HomeRepository extends JpaRepository<Home, UUID> {

    List<Home> findAllByUserId(UUID user_id);

    Optional<Home> findByUser_ChatIdAndActiveFalse(String user_chatId);

    Page<Home> findAllByActiveTrue(Pageable pageable);
}
