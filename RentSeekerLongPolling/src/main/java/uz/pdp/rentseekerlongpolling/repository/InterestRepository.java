package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.rentseekerlongpolling.entity.Interest;

import java.util.Optional;
import java.util.UUID;

public interface InterestRepository extends JpaRepository<Interest, UUID> {
    Optional<Interest> findByHomeIdAndUserId(UUID home_id, UUID user_id);

}
