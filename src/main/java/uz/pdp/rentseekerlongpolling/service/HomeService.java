package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Like;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.payload.ApiResponse;
import uz.pdp.rentseekerlongpolling.payload.HomeAddDTO;
import uz.pdp.rentseekerlongpolling.payload.HomeEditDTO;
import uz.pdp.rentseekerlongpolling.payload.SearchDTO;
import uz.pdp.rentseekerlongpolling.repository.AttachmentRepository;
import uz.pdp.rentseekerlongpolling.repository.HomeRepository;
import uz.pdp.rentseekerlongpolling.util.enums.BotState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.pdp.rentseekerlongpolling.util.constant.ApiResponseText.*;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserService userService;

    private final HomeRepository homeRepository;

    private final ModelMapper modelMapper;

    private final AttachmentRepository attachmentRepository;

    public void addHome(Home home, String chatId, BotState state) {
        Home crtHome = getNoActiveHomeByChatId(chatId);
        if (state == null) {
            if (crtHome != null)
                home.setId(crtHome.getId());
            home.setCreatedDate(LocalDateTime.now());
            home.setUpdatedDate(LocalDateTime.now());
            homeRepository.save(home);
            return;
        }

        if (crtHome != null) switch (state) {
            case GIVE_DISTRICT:
                crtHome.setRegion(home.getRegion());
                break;
            case GIVE_ADDRESS:
                crtHome.setDistrict(home.getDistrict());
                break;
            case GIVE_HOME_TYPE_SEND:
                crtHome.setAddress(home.getAddress());
                break;
            case GIVE_HOME_NUMBER:
                crtHome.setHomeType(home.getHomeType());
                break;
            case GIVE_HOME_AREA_SEND:
                crtHome.setNumberOfRooms(home.getNumberOfRooms());
                break;
            case GIVE_HOME_PHOTO_SEND:
                crtHome.setArea(home.getArea());
                break;
            case GIVE_HOME_PHOTO_SEND_AGAIN: {
                if (crtHome.getAttachments().isEmpty())
                    crtHome.setAttachments(home.getAttachments());
                else
                    crtHome.getAttachments().addAll(home.getAttachments());
            }
            break;
            case GIVE_HOME_DESCRIPTION:
                crtHome.setPrice(home.getPrice());
                break;
            case SAVE_HOME_TO_STORE: {
                crtHome.setDescription(home.getDescription());
                crtHome.setActive(true);
            }
            break;
        }
        if (crtHome != null)
            homeRepository.save(crtHome);
    }

    public void saveHomeByLocation(Home home, String chatId) {
        Home homeByChatId = getNoActiveHomeByChatId(chatId);
        homeByChatId.setMapUrl(home.getMapUrl());
        homeByChatId.setRegion(home.getRegion());
        homeByChatId.setAddress(home.getAddress());
        homeByChatId.setDistrict(home.getDistrict());
        homeRepository.save(homeByChatId);
    }

    public void changeCountOfLike(Like like, Home home) {
        home.setLikes(like.isActive() ? home.getLikes() + 1 : home.getLikes() - 1);
        homeRepository.save(home);
    }

    public void changeCountOfInterest(Home home) {
        home.setInterests(home.getInterests() + 1);
        homeRepository.save(home);
    }

    public List<Home> getAllHome() {
        return homeRepository.findAll();
    }

    public List<Home> getDayHomes() {
        List<Home> homeDay = new ArrayList<>();
        for (Home home : homeRepository.findAll())
            if (home.isActive() && home.getCreatedDate().getDayOfYear() - LocalDate.now().getDayOfYear() <= 1)
                homeDay.add(home);

        return homeDay;
    }

    public List<Home> getWeekHomes() {
        List<Home> homeDay = new ArrayList<>();
        for (Home home : homeRepository.findAll())
            if (home.isActive() && home.getCreatedDate().getDayOfYear() - LocalDate.now().getDayOfYear() <= 7)
                homeDay.add(home);

        return homeDay;
    }

    public List<Home> getByOwnerId(UUID ownerId) {
        return homeRepository.findAllByUserId(ownerId);
    }

    public Page<Home> getAllActiveHomes(Integer page, Integer size) {
        return homeRepository.findAllByActiveTrue(PageRequest.of(page, size, Sort.by("createdDate").ascending()));
    }

    public Home getNoActiveHomeByChatId(String chatId) {
        Optional<Home> optionalHome = homeRepository.findByUser_ChatIdAndActiveFalse(chatId);
        return optionalHome.orElse(null);
    }

    public ApiResponse getNoActiveHomeByUserId(UUID userId) {
        Optional<Home> optionalHome = homeRepository.findByUserIdAndActiveFalse(userId);
        return optionalHome.isEmpty() ? new ApiResponse(false, HOME_NOT_FOUND) :
                new ApiResponse(true, SUCCESS, optionalHome.get());
    }

    public Home getById(UUID id) {
        return homeRepository.findById(id).orElse(null);
    }

    public String getHomeOwnerPhoneNumber(UUID ownerId) {
        return userService.getById(ownerId).getPhoneNumber();
    }

    public boolean checkById(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return false;
        }
        return homeRepository.existsById(uuid);
    }

    public void deleteHome(UUID homeId) {
        homeRepository.deleteById(homeId);
    }


    public ApiResponse getHomeById(UUID id) {
        Optional<Home> optionalHome = homeRepository.findById(id);
        return optionalHome.isEmpty() ? new ApiResponse(false, HOME_NOT_FOUND) :
                new ApiResponse(true, SUCCESS, optionalHome.get());
    }

    public ApiResponse getAllHome(Integer page, Integer size) {
        List<Home> homes = homeRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate"))).getContent();
        return homes.isEmpty() ? new ApiResponse(false, NOT_FOUND) :
                new ApiResponse(true, SUCCESS, homes);
    }

    public ApiResponse getActiveHomes(Integer page, Integer size) {
        List<Home> homes = homeRepository.findAllByActiveTrue(PageRequest.of(page, size, Sort.by("createdDate").ascending())).getContent();
        return homes.isEmpty() ? new ApiResponse(false, NOT_FOUND) :
                new ApiResponse(true, SUCCESS, homes);
    }

    public ApiResponse getByUserPhone(String phone, Integer page, Integer size) {
        List<Home> homes = homeRepository.findByUser_PhoneNumber(phone, PageRequest.of(page, size, Sort.by("createdDate").ascending())).getContent();
        return homes.isEmpty() ? new ApiResponse(false, NOT_FOUND) :
                new ApiResponse(true, SUCCESS, homes);
    }

    public ApiResponse searchHome(SearchDTO search) {
        List<Home> homes = new ArrayList<>();
        for (Home home : homeRepository.findByActive(true)) {
            if (search.getRegion() != null)
                if (search.getRegion().equals(home.getRegion())) {
                    if (search.getDistrict() != null)
                        if (!search.getDistrict().equals(home.getDistrict()))
                            continue;
                } else continue;

            if (search.getHomeType() != null)
                if (!home.getHomeType().equals(search.getHomeType()))
                    continue;
            if (search.getStatus() != null)
                if (!search.getStatus().equals(home.getStatus()))
                    continue;
            if (search.getNumberOfRooms() != -1)
                if (search.getNumberOfRooms() != home.getNumberOfRooms())
                    continue;
            if (search.getMinPrice() != -1)
                if (search.getMinPrice() > home.getPrice())
                    continue;
            if (search.getMaxPrice() != -1)
                if (search.getMaxPrice() < home.getPrice())
                    continue;
            homes.add(home);
        }
        return homes.isEmpty() ? new ApiResponse(false, NOT_FOUND) :
                new ApiResponse(true, SUCCESS, homes);
    }

    public ApiResponse addHome(HomeAddDTO homeAddDTO) {
        Home home = modelMapper.map(homeAddDTO, Home.class);
        User user = userService.getById(homeAddDTO.getUserId());
        if (user == null)
            return new ApiResponse(false, USER_NOT_FOUND);
        home.setUser(user);
        home.setActive(true);
        return new ApiResponse(true, SUCCESS, homeRepository.save(home));
    }

    public ApiResponse editHome(UUID id, HomeEditDTO homeEditDTO) {
        Optional<Home> optionalHome = homeRepository.findById(id);
        if (optionalHome.isEmpty())
            return new ApiResponse(false, HOME_NOT_FOUND);
        Home home = optionalHome.get();
        modelMapper.map(homeEditDTO, home);
        return new ApiResponse(true, SUCCESS, homeRepository.save(home));
    }


    public ApiResponse delete(UUID homeId) {
        if (!homeRepository.existsById(homeId))
            return new ApiResponse(false, HOME_NOT_FOUND);
        homeRepository.deleteById(homeId);
        return new ApiResponse(true, SUCCESS);
    }

    public void deleteHomeAttachment(String chatId) {
        Optional<Home> optionalHome = homeRepository.findByUser_ChatIdAndActiveFalse(chatId);
        optionalHome.ifPresent(home-> {
            home.setAttachments(null);
            homeRepository.save(home);
        });
    }
}
