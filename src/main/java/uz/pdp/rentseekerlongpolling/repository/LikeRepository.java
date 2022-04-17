package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.rentseekerlongpolling.entity.Like;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByHomeIdAndUserId(UUID homeId, UUID userId);

    List<Like> findByUserIdAndActiveTrue(UUID userId);

    Page<Like> findByUserIdAndActiveTrue(UUID userId, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from change_home_like(:homeId,:userId)")
    Like changeHomeLike(UUID homeId,UUID userId);

    @Query(nativeQuery = true,value = "select * from change_home_like(:likeId)")
    Like changeHomeLike(UUID likeId);
}
