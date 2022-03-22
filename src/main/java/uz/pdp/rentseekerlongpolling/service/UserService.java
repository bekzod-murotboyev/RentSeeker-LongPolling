package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.payload.ApiResponse;
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

    public  ApiResponse getUsers(boolean active) {
        List<User> userList = userRepository.findByActive(active);
        return userList.isEmpty()?new ApiResponse(false,"NOT FOUND"):
                new ApiResponse(true,"SUCCESS",userList);
    }


    public LanStateDTO getAndCheck(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String chatId = message.getChatId().toString();
        User user = userRepository.findByChatId(chatId);
        if (user == null) {
            var from = message.getFrom();
            user = new User(from.getFirstName(), "", from.getUserName(),
                    chatId, Language.RU, BotState.CHOOSE_LANGUAGE, Role.USER, false);
            userRepository.save(user);
        }

        return new LanStateDTO(user.getLanguage(), user.getState(), user.getRole(), user.isAdmin(),user.isActive());
    }

    public void saveStateAndLan(Update update, LanStateDTO lanStateDTO) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String chatId = message.getChatId().toString();
        User user = userRepository.findByChatId(chatId);
        user.setState(lanStateDTO.getState());
        user.setLanguage(lanStateDTO.getLanguage());
        user.setRole(lanStateDTO.getRole());
        user.setAdmin(lanStateDTO.isAdmin());
        userRepository.save(user);

    }

    public User getByChatId(String chatId) {
        return userRepository.findByChatId(chatId);
    }

    public void savePhoneNumber(String phoneNumber, String chatId) {
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
        User user = userRepository.findByChatId(chatId);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
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
        for (User user : userRepository.findAll())
            if (user.getPhoneNumber() != null && user.getPhoneNumber().equals(number))
                return true;
        return false;
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
        User user = userRepository.findByChatId(chatId);
        user.setAdmin(isAdmin);
        userRepository.save(user);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public void changeUserPageByChatId(String chatId,int page){
        userRepository.changeUserPageByChatId(chatId,page);
    }

}
