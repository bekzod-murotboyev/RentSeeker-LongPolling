package uz.pdp.rentseekerlongpolling.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.telegram.telegraph.api.objects.Node;
import org.telegram.telegraph.api.objects.NodeElement;
import org.telegram.telegraph.api.objects.NodeText;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Like;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.feign.TelegraphFeign;
import uz.pdp.rentseekerlongpolling.payload.response.ApiResponse;
import uz.pdp.rentseekerlongpolling.payload.home.HomeAddDTO;
import uz.pdp.rentseekerlongpolling.payload.home.HomeEditDTO;
import uz.pdp.rentseekerlongpolling.payload.SearchDTO;
import uz.pdp.rentseekerlongpolling.repository.HomeRepository;
import uz.pdp.rentseekerlongpolling.util.enums.BotState;
import uz.pdp.rentseekerlongpolling.util.enums.HomeStatus;
import uz.pdp.rentseekerlongpolling.util.enums.HomeType;
import uz.pdp.rentseekerlongpolling.util.enums.Language;
import uz.pdp.rentseekerlongpolling.util.security.BaseData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static uz.pdp.rentseekerlongpolling.service.LanguageService.getWord;
import static uz.pdp.rentseekerlongpolling.util.constant.ApiResponseText.*;
import static uz.pdp.rentseekerlongpolling.util.constant.Constant.*;
import static uz.pdp.rentseekerlongpolling.util.security.Telegraph.*;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserService userService;

    private final HomeRepository homeRepository;

    private final ModelMapper modelMapper;

    private final TelegraphFeign telegraphFeign;

    private final ObjectMapper objectMapper;

    private final AttachmentService attachmentService;

    private final LikeService likeService;


    public boolean addHome(Home home, String chatId, BotState state) {
        Home crtHome = getNoActiveHomeByChatId(chatId);
        if (state == null) {
            if (crtHome != null)
                home.setId(crtHome.getId());
            home.setCreatedDate(LocalDateTime.now());
            home.setUpdatedDate(LocalDateTime.now());
            homeRepository.save(home);
            return true;
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
                String detailsPath = getDetailsPath(crtHome);
                if (detailsPath == null)
                    return false;

                crtHome.setDetailsPath(detailsPath);
                crtHome.setActive(true);
            }
            break;
        }
        if (crtHome != null)
            homeRepository.save(crtHome);
        return true;
    }

    public void saveHomeByLocation(Home home, String chatId) {
        Home homeByChatId = getNoActiveHomeByChatId(chatId);
        homeByChatId.setMapUrl(home.getMapUrl());
        homeByChatId.setRegion(home.getRegion());
        homeByChatId.setAddress(home.getAddress());
        homeByChatId.setDistrict(home.getDistrict());
        homeRepository.save(homeByChatId);
    }


    public void changeCountOfInterest(Home home) {
        home.setInterests(home.getInterests() + 1);
        homeRepository.save(home);
    }

    public List<Home> getAllHome() {
        return homeRepository.findAll();
    }

    public List<Home> getAllActiveHomes() {
        return homeRepository.findAllByActiveTrue();
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
        return homeRepository.findAllByUserIdAndActiveTrue(ownerId);
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


    public ApiResponse getFavouriteHomesByUserId(UUID userId, Integer page, Integer size) {
        List<Home> homes = likeService
                .getActiveHomesByUserId(userId, PageRequest.of(page, size, Sort.by("createdDate").ascending()));
        return homes.isEmpty() ? new ApiResponse(false, NOT_FOUND) :
                new ApiResponse(true, SUCCESS, homes);
    }

    public ApiResponse searchHome(SearchDTO searchDTO) {
        List<Home> homes = searchHomes(searchDTO);
        return homes.isEmpty() ? new ApiResponse(false, NOT_FOUND) :
                new ApiResponse(true, SUCCESS, homes);
    }


    public List<Home> searchHomes(SearchDTO searchDTO) {
        return homeRepository.searchHomes(
                searchDTO.getRegion() == null ? "" : searchDTO.getRegion().name(),
                searchDTO.getDistrict() == null ? "" : searchDTO.getDistrict().name(),
                searchDTO.getStatus() == null ? "" : searchDTO.getStatus().name(),
                searchDTO.getHomeType() == null ? "" : searchDTO.getHomeType().name(),
                searchDTO.getNumberOfRooms() == null ? -1 : searchDTO.getNumberOfRooms(),
                searchDTO.getMinPrice() == null ? -1 : searchDTO.getMinPrice(),
                searchDTO.getMaxPrice() == null ? -1 : searchDTO.getMaxPrice());
    }

    public ApiResponse addHome(MultipartHttpServletRequest request) throws JsonProcessingException {
        HomeAddDTO homeAddDTO = objectMapper.readValue(request.getParameter("homeAddDTO"), HomeAddDTO.class);
        if (homeAddDTO == null)
            return new ApiResponse(false, "Invalid data");
        User user = userService.getById(homeAddDTO.getUserId());
        if (user == null)
            return new ApiResponse(false, USER_NOT_FOUND);
        Home home = modelMapper.map(homeAddDTO, Home.class);
        home.setAttachments(attachmentService.saveToFile(request));
        home.setUser(user);
        String detailsPath = getDetailsPath(home);
        if (detailsPath == null) return new ApiResponse(false, "Invalid data");
        home.setDetailsPath(detailsPath);
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


    public ApiResponse editHomeLike(UUID homeId, UUID userId) {
        Like like = likeService.changeLike(homeId, userId);
        return like!=null?new ApiResponse(true,SUCCESS,like):
                new ApiResponse(false,NOT_FOUND);
    }

    public ApiResponse editHomeLike(UUID likeId) {
        Like like = likeService.changeLike(likeId);
        return like!=null?new ApiResponse(true,SUCCESS,like):
                new ApiResponse(false,NOT_FOUND);
    }


    public ApiResponse delete(UUID homeId) {
        if (!homeRepository.existsById(homeId))
            return new ApiResponse(false, HOME_NOT_FOUND);
        homeRepository.deleteById(homeId);
        return new ApiResponse(true, SUCCESS);
    }

    public void deleteHomeAttachment(String chatId) {
        Optional<Home> optionalHome = homeRepository.findByUser_ChatIdAndActiveFalse(chatId);
        optionalHome.ifPresent(home -> {
            home.setAttachments(null);
            homeRepository.save(home);
        });
    }

    @SneakyThrows
    private String getDetailsPath(Home home) {
        try {
            return telegraphFeign.createPage(
                    ACCESS_TOKEN,
                    NAME,
                    new ObjectMapper().writeValueAsString(getNodeList(home, home.getUser().getLanguage()))
            ).getResult().getUrl();
        } catch (Exception ignored) {
            return null;
        }
    }

    public List<Node> getNodeList(Home home, Language lan) {
        List<Node> nodeElements = home.getAttachments().stream().map(item -> new NodeElement(
                "img",
                Map.of(
                        "src",
                        item.getFilePath(),
                        "style",
                        "margin-bottom: 50px"

                ),
                null
        )).collect(Collectors.toList());
        addRow(nodeElements, HOUSE_TYPE, home.getHomeType().equals(HomeType.HOUSE) ? HOUSE : FLAT, lan);
        addRow(nodeElements, STATUS, home.getStatus().equals(HomeStatus.SELL) ? SELL : RENT, lan);
        addRow(nodeElements, ADMIN_HOMES_INFO_REGION, home.getRegion().name(), lan);
        addRow(nodeElements, ADMIN_HOMES_INFO_DISTRICT, home.getDistrict().name(), lan);
        addRowStaticData(nodeElements, ADDRESS, home.getAddress(), lan);
        addRowStaticData(nodeElements, NUMBER_OF_ROOMS, String.valueOf(home.getNumberOfRooms()), lan);
        addRowStaticData(nodeElements, AREA, home.getArea() + " m²", lan);
        addRowStaticData(nodeElements, DATE, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(home.getCreatedDate() == null ? LocalDateTime.now() : home.getCreatedDate()), lan);
        addRowStaticData(nodeElements, ADMIN_HOMES_INFO_PRICE, home.getPrice() + " $", lan);
        addRowStaticData(nodeElements, DESCRIPTION, home.getDescription(), lan);
        nodeElements.add(home.getUser().getUsername() == null ? getH4("☎️ +998" + home.getUser().getPhoneNumber()) :
                getH4WithA("https://t.me/" + home.getUser().getUsername(), "☎️ +998" + home.getUser().getPhoneNumber()));
        nodeElements.add(getBr());
        nodeElements.add(getA("https://t.me/" + BaseData.USERNAME, "@" + BaseData.USERNAME));

        return nodeElements;
    }

    public void addRow(List<Node> nodeElements, String type, String data, Language lan) {
        nodeElements.add(getBold(getWord(type, lan)));
        nodeElements.add(getItalicParagraph(getWord(data, lan)));
        nodeElements.add(getBr());
    }

    public void addRowStaticData(List<Node> nodeElements, String type, String data, Language lan) {
        nodeElements.add(getBold(getWord(type, lan)));
        nodeElements.add(getItalicParagraph(data));
        nodeElements.add(getBr());
    }

    private Node getH3(String text) {
        return new NodeElement("h3", null, List.of(new NodeText(text)));
    }

    private Node getH4(String text) {
        return new NodeElement("h4", null, List.of(new NodeText(text)));
    }

    private Node getH4WithA(String href, String text) {
        return new NodeElement("h4", null, List.of(getA(href, text)));
    }

    private Node getParagraph(String text) {
        return new NodeElement("p", null, List.of(new NodeText(text)));
    }

    private Node getItalicParagraph(String text) {
        return new NodeElement("p", null, List.of(getItalic(text)));
    }

    private Node getA(String href, String text) {
        return new NodeElement("a", Map.of("href", href), List.of(new NodeText(text)));
    }

    private Node getBold(String text) {
        return new NodeElement("b", null, List.of(new NodeText(text)));
    }

    private Node getItalic(String text) {
        return new NodeElement("i", null, List.of(new NodeText(text)));
    }

    private Node getBr() {
        return new NodeElement("br", null, null);
    }

}


