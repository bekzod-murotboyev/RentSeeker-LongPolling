package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Like;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.repository.HomeRepository;
import uz.pdp.rentseekerlongpolling.repository.LikeRepository;

import javax.management.RuntimeOperationsException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final HomeRepository homeRepository;

    private final LikeRepository likeRepository;


    public Like getLikeByHomeAndUser(Home home, User user) {
        Optional<Like> like = likeRepository.findByHomeIdAndUserId(home.getId(), user.getId());
        return like.orElseGet(() -> likeRepository.save(new Like(home, user)));
    }

    public Like getLikeByHomeAndUser(UUID homeId, UUID userId) {
        Optional<Like> like = likeRepository.findByHomeIdAndUserId(homeId, userId);
        return like.orElse(null);
    }

    public Like getById(UUID id) {
        return likeRepository.findById(id).orElse(null);
    }

    public List<Home> getActiveHomesByUserId(UUID userId) {
        return likeRepository.findByUserIdAndActiveTrue(userId).stream().map(Like::getHome).collect(Collectors.toList());
    }

    public List<Home> getActiveHomesByUserId(UUID userId, Pageable pageable) {
        return likeRepository.findByUserIdAndActiveTrue(userId, pageable).stream().map(Like::getHome).collect(Collectors.toList());
    }

    public Like changeLike(UUID homeId, UUID userId) {
        return likeRepository.changeHomeLike(homeId, userId);
    }

    public Like changeLike(UUID likeId) {
        return likeRepository.changeHomeLike(likeId);
    }
}
