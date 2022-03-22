package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.rentseekerlongpolling.entity.Like;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByHomeIdAndUserId(UUID home_id, UUID user_id);



    List<Like> findByUserIdAndActiveTrue(UUID user_id);
}
