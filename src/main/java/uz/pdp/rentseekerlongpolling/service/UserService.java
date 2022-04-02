package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.payload.response.ApiResponse;
import uz.pdp.rentseekerlongpolling.payload.LanStateDTO;
import uz.pdp.rentseekerlongpolling.repository.UserRepository;
import uz.pdp.rentseekerlongpolling.util.enums.BotState;
import uz.pdp.rentseekerlongpolling.util.enums.Language;
import uz.pdp.rentseekerlongpolling.util.enums.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ApiResponse getUsers(boolean active) {
        List<User> userList = userRepository.findByActive(active);
        return userList.isEmpty() ? new ApiResponse(false, "NOT FOUND") :
                new ApiResponse(true, "SUCCESS", userList);
    }


    public LanStateDTO getAndCheck(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String chatId = message.getChatId().toString();
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        User user;
        if (optionalUser.isEmpty()) {
            var from = message.getFrom();
            user = new User(from.getFirstName(), "", from.getUserName(),
                    chatId, Language.RU, BotState.CHOOSE_LANGUAGE, Role.USER, false);
            userRepository.save(user);
        } else user = optionalUser.get();
        return new LanStateDTO(user.getLanguage(), user.getState(), user.getRole(), user.isAdmin(), user.isActive());
    }

    public void saveStateAndLan(Update update, LanStateDTO lanStateDTO) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String chatId = message.getChatId().toString();
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        optionalUser.ifPresent(item -> {
            item.setState(lanStateDTO.getState());
            item.setLanguage(lanStateDTO.getLanguage());
            item.setRole(lanStateDTO.getRole());
            item.setAdmin(lanStateDTO.isAdmin());
            userRepository.save(item);
        });
    }

    public User getByChatId(String chatId) {
        return userRepository.findByChatId(chatId).orElse(null);
    }

    public void savePhoneNumber(String phoneNumber, String chatId) {
        userRepository.findByChatId(chatId).ifPresent(user -> {
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);
        });
    }

    public boolean phoneNumberValidation(String phone) {
        if (phone.length() != 13 || !phone.startsWith("+998"))
            return false;

        for (int i = phone.length() - 1; i > 3; i--)
            if (!Character.isDigit(phone.charAt(i)))
                return false;
        return true;
    }

    public boolean checkByPhoneNumber(String number) {
        return userRepository.existsByPhoneNumber(number);
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getSearchUser(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    public User banOrUnbanUser(String phoneNumber) {
        for (User user : userRepository.findAll()) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                user.setActive(!user.isActive());
                userRepository.save(user);
                return user;
            }
        }
        return null;
    }


    public void changeIsAdmin(String chatId, boolean isAdmin) {
        userRepository.findByChatId(chatId).ifPresent(user -> {
            user.setAdmin(isAdmin);
            userRepository.save(user);
        });
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void changeUserPageByChatId(String chatId, int page) {
        userRepository.changeUserPageByChatId(chatId, page);
    }

}
