package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.rentseekerlongpolling.component.EmailComponent;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Like;
import uz.pdp.rentseekerlongpolling.entity.Search;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.model.locationModels.LocationsItem;
import uz.pdp.rentseekerlongpolling.util.enums.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.pdp.rentseekerlongpolling.service.LanguageService.getWord;
import static uz.pdp.rentseekerlongpolling.util.constant.Constant.*;
import static uz.pdp.rentseekerlongpolling.util.enums.BotState.SHOW_HOME_PHONE_MENU_ALL;


@Service
@RequiredArgsConstructor
public class BotService {

    @Value("${spring.pageable.size}")
    private int pageableSize;

    private final UserService userService;

    private final HomeService homeService;

    private final InterestService interestService;

    private final LikeService likeService;

    private final SearchService searchService;

    private final EmailComponent emailComponent;

    public SendMessage chooseLanguage(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(UZ, RU, EN)
        ), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_LANGUAGE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText changeLanguage(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(UZ, RU, EN),
                List.of(BACK_TO_SETTINGS_MENU_EDIT)
        ), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_LANGUAGE_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }


    public EditMessageText getMainMenuEditForAdminEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADD_ACCOMMODATION,
                        SHOW_ACCOMMODATIONS),
                List.of(MY_NOTES,
                        SETTINGS),
                List.of(BACK_TO_ADMIN_MENU)
        ), lan);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage getMainMenuEditForAdminSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADD_ACCOMMODATION,
                        SHOW_ACCOMMODATIONS),
                List.of(MY_NOTES,
                        SETTINGS),
                List.of(BACK_TO_ADMIN_MENU)
        ), lan);

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(MENU_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }


    public EditMessageText getAdminMenuEdit(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(USER, ADMIN)), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(CHOOSE_ACTION, lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setReplyMarkup(markup);
        return editMessage;
    }


    public SendMessage getAdminMenuSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(USER, ADMIN)), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACTION, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText setMenuEdit(Update update, Language lan, Role role) {
        if (role.equals(Role.ADMIN)) return getMainMenuEditForAdminEdit(update, lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADD_ACCOMMODATION,
                        SHOW_ACCOMMODATIONS),
                List.of(MY_NOTES,
                        SETTINGS)), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage setMenuSend(Update update, Language lan, Role role) {
        if (role.equals(Role.ADMIN)) return getMainMenuEditForAdminSend(update, lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADD_ACCOMMODATION,
                        SHOW_ACCOMMODATIONS),
                List.of(MY_NOTES,
                        SETTINGS)), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText getSettingMenuEdit(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(CHANGE_LANGUAGE, REGISTRATION),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_ACTION, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage getSettingMenuSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(CHANGE_LANGUAGE, REGISTRATION),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACTION, lan));
        sendMessage.setReplyMarkup(markup);
        sendMessage.setChatId(message.getChatId().toString());
        return sendMessage;
    }

    public boolean saveContact(Update update, Language lan) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() && !message.getText().equals(getWord(BACK, lan))) {
                String phone = message.getText();
                if (userService.phoneNumberValidation(phone)) {
                    userService.savePhoneNumber(phone, message.getChatId().toString());
                } else return false;
            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                userService.savePhoneNumber(contact.getPhoneNumber(), message.getChatId().toString());
            }
        }
        return true;
    }

    public SendMessage getRegister(Update update, Language lan) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        List<KeyboardRow> rowList = new ArrayList<>();
        markup.setKeyboard(rowList);
        KeyboardRow row = new KeyboardRow();
        rowList.add(row);
        KeyboardButton button = new KeyboardButton(getWord(MY_PHONE_NUMBER, lan));
        button.setRequestContact(true);
        row.add(button);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(getWord(BACK, lan)));
        rowList.add(row1);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), message.getChat().getFirstName() + getWord(ENTER_PHONE_NUMBER_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }


    public SendMessage setError(Update update) {
        Message message = update.hasMessage() ? update.getMessage() :
                update.getCallbackQuery().getMessage();
        return new SendMessage(message.getChatId().toString(), ERROR);
    }

    public DeleteMessage deleteMessage(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        return new DeleteMessage(message.getChatId().toString(), message.getMessageId());
    }

    public SendMessage removeKeyBoardMarkup(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "⬅️⬅️⬅️");
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        return sendMessage;
    }


    public EditMessageText setHomeStatusMenu(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(FOR_RENTING, FOR_SELLING),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(SET_ACCOMMODATION_FOR_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        return editMessageText;
    }

    public EditMessageText setWarningRegister(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(REGISTER_WARNING_TEXT, lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public EditMessageText setWriteOrSendLocationMenuEdit(Update update, Language lan) {
        saveHomeStatus(update);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(WRITE_ADDRESS, SEND_LOCATION),
                List.of(BACK_TO_GIVE_HOME_STATUS)
        ), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(WRITE_SEND_LOCATION_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage setWriteOrSendLocationMenuSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(WRITE_ADDRESS, SEND_LOCATION),
                List.of(BACK_TO_GIVE_HOME_STATUS)
        ), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(WRITE_SEND_LOCATION_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public SendMessage getMenuLocation(Update update, Language lan) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        List<KeyboardRow> rowList = new ArrayList<>();
        markup.setKeyboard(rowList);
        KeyboardRow row = new KeyboardRow();
        rowList.add(row);
        KeyboardButton button = new KeyboardButton(getWord(SEND_LOCATION, lan));
        button.setRequestLocation(true);
        row.add(button);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(getWord(BACK, lan)));
        rowList.add(row1);

        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        assert user != null;
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(SEND_LOCATION_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText giveRegionMenu(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (Region value : Region.values()) {
            if (row.size() != 3) {
                row.add(value.name());
            } else {
                rows.add(row);
                row = new ArrayList<>();
                row.add(value.name());
            }
        }
        rows.add(row);
        rows.add(List.of(BACK_TO_WRITE_SEND_LOCATION));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows, lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_REGION_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public EditMessageText giveDistrictMenu(Update update, Language lan, BotState state) {
        Message message = update.getCallbackQuery().getMessage();
        Region region;
        if (!update.getCallbackQuery().getData().contains("BACK"))
            region = saveHomeRegion(update, state);
        else
            region = homeService.getNoActiveHomeByChatId(message.getChatId().toString()).getRegion();


        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (District value : District.values())
            if (value.getRegionId() == region.getId()) {
                if (row.size() != 3) {
                    row.add(value.name());
                } else {
                    rows.add(row);
                    row = new ArrayList<>();
                    row.add(value.name());
                }
            }
        rows.add(row);
        rows.add(List.of(BACK_TO_GIVE_REGION));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows, lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_DISTRICT_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    // SAVE HOME DATA
    public void saveHomeStatus(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        User user = userService.getByChatId(query.getMessage().getChatId().toString());
        Home home = new Home();
        home.setStatus(query.getData().equals(FOR_RENTING) ? HomeStatus.RENT : query.getData().equals(FOR_SELLING) ? HomeStatus.SELL : null);
        home.setUser(user);
        homeService.addHome(home,query.getMessage().getChatId().toString(),null);
    }

    private Region saveHomeRegion(Update update, BotState state) {
        CallbackQuery query = update.getCallbackQuery();
        String chatId = query.getMessage().getChatId().toString();
        User user = userService.getByChatId(chatId);
        Region region = Region.valueOf(query.getData());
        Home home = new Home();
        home.setUser(user);
        home.setRegion(region);
        homeService.addHome(home, chatId, state);
        return region;
    }

    private void saveHomeDistrict(Update update, BotState state) {
        CallbackQuery query = update.getCallbackQuery();
        String chatId = query.getMessage().getChatId().toString();
        User user = userService.getByChatId(chatId);
        Home home = new Home();
        home.setUser(user);
        home.setDistrict(District.valueOf(query.getData()));
        homeService.addHome(home, chatId, state);
    }

    private void saveHomeAddress(Update update, BotState state) {
        Message message = update.getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        Home home = new Home();
        home.setUser(user);
        home.setAddress(message.getText());
        homeService.addHome(home, user.getChatId(), state);
    }

    private void saveHomeType(Update update, BotState state) {
        CallbackQuery query = update.getCallbackQuery();
        User user = userService.getByChatId(query.getMessage().getChatId().toString());
        Home home = new Home();
        home.setUser(user);
        home.setHomeType(HomeType.valueOf(query.getData().toUpperCase()));
        homeService.addHome(home, user.getChatId(), state);
    }

    public boolean saveNumberOfRoom(Update update, BotState state) {
        Message message = update.getMessage();
        String text = message.getText();
        for (char c : text.toCharArray())
            if (!Character.isDigit(c))
                return false;
        int rooms = Integer.parseInt(text);
        if (rooms > 15)
            return false;

        User user = userService.getByChatId(message.getChatId().toString());
        Home home = new Home();
        home.setUser(user);
        home.setNumberOfRooms(rooms);
        homeService.addHome(home, user.getChatId(), state);
        return true;
    }

    public boolean saveHomeArea(Update update, BotState state) {
        Message message = update.getMessage();
        String text = message.getText();
        User user = userService.getByChatId(message.getChatId().toString());
        Home home = new Home();
        home.setUser(user);

        try {
            home.setArea(Double.parseDouble(text));
        } catch (Exception e) {
            return false;
        }

        homeService.addHome(home, user.getChatId(), state);
        return true;
    }

    public void saveHomePhoto(Update update, BotState state) {
        Message message = update.getMessage();
        PhotoSize photoSize = message.getPhoto().get(message.getPhoto().size() - 1);
        Home home = new Home();
        User user = userService.getByChatId(message.getChatId().toString());
        home.setFileId(photoSize.getFileId());
        home.setFileSize(photoSize.getFileSize());
        home.setUser(user);
        homeService.addHome(home, message.getChatId().toString(), state);
    }

    public boolean saveHomePrice(Update update, BotState state) {
        Message message = update.getMessage();
        String text = message.getText();
        User user = userService.getByChatId(message.getChatId().toString());
        Home home = new Home();
        home.setUser(user);
        try {
            double price = Double.parseDouble(text);
            if (price <= 0) return false;
            home.setPrice(price);
        } catch (Exception e) {
            return false;
        }

        homeService.addHome(home, user.getChatId(), state);
        return true;
    }

    public void saveHomeDescription(Update update, BotState state) {
        Message message = update.getMessage();
        String text = message.getText();
        User user = userService.getByChatId(message.getChatId().toString());
        Home home = new Home();
        home.setUser(user);
        home.setDescription(text);
        homeService.addHome(home, user.getChatId(), state);
    }
    //

    // SAVE SEARCH DATA
    private Region saveSearchRegion(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        String chatId = query.getMessage().getChatId().toString();
        User user = userService.getByChatId(chatId);
        Search search = searchService.getSearchByUserId(user);
        search.setUser(user);

        if (query.getData().equals(SKIP) || query.getData().contains(BACK)) {
            search.setRegion(null);
            searchService.addSearch(search);
            return null;
        } else {
            try {
                Region region = Region.valueOf(query.getData());
                search.setRegion(region);
                searchService.addSearch(search);
                return region;
            } catch (Exception e) {
                search.setRegion(null);
                searchService.addSearch(search);
                return null;
            }
        }

    }

    private void saveSearchDistrict(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        String chatId = query.getMessage().getChatId().toString();
        User user = userService.getByChatId(chatId);
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        search.setDistrict(query.getData().equals(SKIP) || query.getData().contains("BACK") ? null : District.valueOf(query.getData()));
        searchService.addSearch(search);
    }

    public void saveSearchStatus(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        User user = userService.getByChatId(query.getMessage().getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        search.setStatus(query.getData().equals(GET_RENTING) ? HomeStatus.RENT : query.getData().equals(FOR_BUY) ? HomeStatus.SELL : null);
        searchService.addSearch(search);
    }

    private void saveSearchType(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        User user = userService.getByChatId(query.getMessage().getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        search.setHomeType(query.getData().equals(SKIP) || query.getData().contains("BACK") ? null : HomeType.valueOf(query.getData().toUpperCase()));
        searchService.addSearch(search);
    }

    public boolean saveSearchNumber(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.contains("BACK"))
                search.setNumberOfRooms(-1);
            searchService.addSearch(search);
            return true;
        }
        String text = message.getText();
        for (char c : text.toCharArray())
            if (!Character.isDigit(c))
                return false;
        int rooms = Integer.parseInt(text);
        if (rooms > 15)
            return false;
        search.setNumberOfRooms(rooms);
        searchService.addSearch(search);
        return true;
    }

    public boolean saveSearchMinPrice(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.contains("BACK"))
                search.setMinPrice(-1);

            searchService.addSearch(search);
            return true;
        }
        String text = message.getText();
        double price;
        try {
            price = Double.parseDouble(text);
        } catch (Exception e) {
            return false;
        }
        search.setMinPrice(price);
        searchService.addSearch(search);
        return true;
    }

    public boolean saveSearchMaxPrice(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.contains("BACK"))
                search.setMaxPrice(-1);
            searchService.addSearch(search);
            return true;

        }
        String text = message.getText();
        double price;
        try {
            price = Double.parseDouble(text);
        } catch (Exception e) {
            return false;
        }
        search.setMaxPrice(price);
        searchService.addSearch(search);
        return true;
    }
    //

    // CHECKING SOMETHING

    public boolean isRegion(Update update) {
        String data = update.getCallbackQuery().getData();
        for (Region value : Region.values())
            if (data.equals(value.name()))
                return true;
        return false;
    }

    public boolean isHomeType(Update update) {
        String data = update.getCallbackQuery().getData();
        return data.equals(FLAT) || data.equals(HOUSE);
    }

    public boolean checkByPhone(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        assert user != null;
        return !user.getPhoneNumber().equals("");
    }
    //


    public EditMessageText giveAddressMenu(Update update, Language lan, BotState state) {
        if (!update.getCallbackQuery().getData().contains("BACK"))
            saveHomeDistrict(update, state);
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_DISTRICT)), lan);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(WRITE_ADDRESS, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage giveHomeTypeMenuSend(Update update, Language lan, BotState state) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        if (!update.hasCallbackQuery() && !update.getMessage().hasLocation())
            saveHomeAddress(update, state);

        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(HOUSE, FLAT),
                List.of(message.hasLocation() ? BACK_SEND_LOCATION : BACK_TO_GIVE_ADDRESS)
        ), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACCOMMODATION_TYPE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText giveHomeTypeMenuEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(HOUSE, FLAT),
                List.of(message.hasLocation() ? BACK_SEND_LOCATION : BACK_TO_GIVE_ADDRESS)
        ), lan);
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setText(getWord(CHOOSE_ACCOMMODATION_TYPE_MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public boolean checkLocation(Update update) {
        if (!update.hasMessage()) return true;
        Message message = update.getMessage();
        if (!message.hasLocation()) return true;
        Location location = message.getLocation();
        LocationsItem data = LocationService.getData(location.getLatitude(), location.getLongitude());
        if (data != null) {
            Home home = new Home();
            home.setAddress(data.getStreet());
            try {
                Region region = Region.valueOf(data.getAdminArea5().toUpperCase());
                for (District value : District.values())
                    if (value.getRegionId() == region.getId()) {
                        home.setDistrict(value);
                        break;
                    }
                home.setRegion(region);
            } catch (Exception e) {
                return false;
            }

            home.setMapUrl(data.getMapUrl());
            homeService.saveHomeByLocation(home, message.getChatId().toString());
            return true;
        }
        return false;
    }

    public SendMessage LocationNotFound(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        return new SendMessage(message.getChatId().toString(), getWord(LOCATION_NOT_FOUND, lan));
    }

    public EditMessageText giveHomeNumberMenu(Update update, Language lan, BotState state) {
        if (state.equals(BotState.GIVE_HOME_NUMBER) && update.hasCallbackQuery() && !update.getCallbackQuery().getData().contains("BACK"))
            saveHomeType(update, state);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(NUMBER_OF_ROOMS, lan));
        editMessageText.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_TYPE)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage giveHomeNumberMenuSend(Update update, Language lan, BotState state) {
        if (state.equals(BotState.GIVE_HOME_NUMBER))
            saveHomeType(update, state);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(NUMBER_OF_ROOMS, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_TYPE)), lan));
        return sendMessage;
    }

    public SendMessage giveHomeAreaMenuSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ENTER_AREA_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_NUMBER)), lan));
        return sendMessage;
    }

    public EditMessageText giveHomeAreaMenuEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ENTER_AREA_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_NUMBER)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage giveHomePhotoMenuSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(SEND_PHOTO_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_AREA)), lan));
        return sendMessage;
    }

    public EditMessageText giveHomePhotoMenuEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(SEND_PHOTO_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_AREA)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage giveHomePriceMenuSend(Update update, Language lan, BotState state) {
        if (state.equals(BotState.GIVE_HOME_PRICE_SEND) && !update.hasCallbackQuery())
            saveHomePhoto(update, state);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ENTER_PRICE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PHOTO)), lan));
        return sendMessage;
    }

    public EditMessageText giveHomePriceMenuEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(ENTER_PRICE_MENU_TEXT, lan));
        editMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PHOTO)), lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        return editMessage;
    }

    public SendMessage giveHomeDescription(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(WRITE_DESCRIPTION_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PRICE)), lan));
        return sendMessage;
    }

    public SendMessage successfullySaved(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        return new SendMessage(message.getChatId().toString(), getWord(SUCCESSFULLY_SAVED, lan));
    }

    public EditMessageText getShowMenuEdit(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(SEARCH, SHOW_ALL),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);

        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_ACTION, lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage getShowMenuSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(SEARCH, SHOW_ALL),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);

        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACTION, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public List<SendPhoto> showAllHomes(Update update, Language lan) {
        InlineKeyboardMarkup markup;
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1;
        CallbackQuery query = update.getCallbackQuery();
        Message message = query.getMessage();
        String data = query.getData();
        User user = userService.getByChatId(message.getChatId().toString());
        int page = user.getCrtPage() + (data.equals(PREV) ? -1 : data.startsWith(NEXT) ? 1 : 0);
        if (page < 0) return null;
        Pageable pageable = PageRequest.of(page, pageableSize, Sort.by(Sort.Order.asc("createdDate")));
        Page<Home> allHome = homeService.getAllActiveHomes(pageable);
        if (allHome.isEmpty()) {
            if (user.getCrtPage() > 0)
                userService.changeUserPageByChatId(user.getChatId(), user.getCrtPage() - 1);
            return null;
        } else if (user.getCrtPage() != page)
            userService.changeUserPageByChatId(user.getChatId(), page);


        List<SendPhoto> sendMessageList = new ArrayList<>();
        SendPhoto sendPhoto = null;

        for (Home home : allHome) {
            if (sendPhoto != null)
                sendMessageList.add(sendPhoto);
            markup = new InlineKeyboardMarkup();
            rowList = new ArrayList<>();
            markup.setKeyboard(rowList);
            row1 = new ArrayList<>();
            rowList.add(row1);

            sendPhoto = new SendPhoto(
                    message.getChatId().toString(),
                    new InputFile(home.getFileId()));
            sendPhoto.setCaption(getCaptionByHome(home, lan));
            sendPhoto.setReplyMarkup(markup);

            Like like = likeService.getLikeByHomeIdAndUserId(home, user);
            row1.add(InlineKeyboardService
                    .getButton(home.getId().toString(),
                            interestService.getVisible(home, user) ? userService.getById(home.getUser().getId()).getPhoneNumber()
                                    : getWord(GET_PHONE_NUMBER, lan)));
            row1.add(InlineKeyboardService
                    .getButton(like.getId().toString(),
                            home.getLikes()
                                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE)));

        }
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardService.getButton(PREV, PREV));
        row2.add(InlineKeyboardService.getButton(BACK_TO_SHOW_MENU_SEND, getWord(BACK, lan)));
        row2.add(InlineKeyboardService.getButton(BACK_TO_MAIN_MENU_SEND, getWord(MENU, lan)));
        row2.add(InlineKeyboardService.getButton(NEXT, NEXT));
        rowList.add(row2);
        sendMessageList.add(sendPhoto);
        return sendMessageList;
    }

    public SendMessage homeNotFound(Update update, Language lan, String back) {
        InlineKeyboardButton backButton = new InlineKeyboardButton(getWord(BACK, lan));
        backButton.setCallbackData(back);
        InlineKeyboardButton menu = new InlineKeyboardButton(getWord(MENU, lan));
        menu.setCallbackData(back.endsWith("EDIT") ? BACK_TO_MAIN_MENU_EDIT : BACK_TO_MAIN_MENU_SEND);
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(backButton);
        if (!back.equals(BACK_TO_ADMIN_HOMES_FILTER_EDIT))
            buttonList.add(menu);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(HOMES_NOT_FOUND, lan));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    public EditMessageCaption changeVisibleHomePhone(Update update, Language lan, String backName, BotState state) {
        Message message = update.getCallbackQuery().getMessage();
        Home home = homeService.getById(UUID.fromString(update.getCallbackQuery().getData()));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        markup.setKeyboard(rowList);
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        User user = userService.getByChatId(message.getChatId().toString());
        Like like = likeService.getLikeByHomeIdAndUserId(home, user);


        InlineKeyboardButton likeButton = InlineKeyboardService.getButton(like.getId().toString(),
                home.getLikes()
                        + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
        row1.add(InlineKeyboardService.getButton(home.getId().toString(),
                interestService.changeVisible(home, user) ?
                        userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan)));


        row2.add(InlineKeyboardService.getButton(backName, getWord(BACK, lan)));
        row2.add(InlineKeyboardService.getButton(BACK_TO_MAIN_MENU_SEND, getWord(MENU, lan)));

        if (state.equals(BotState.SHOW_HOME_PHONE_MENU_MY_ACCOMMODATIONS)) {
            row1.add(likeButton);
            row2.add(1, InlineKeyboardService.getButton(DELETE + home.getId(), DELETE));
        } else if (state.equals(BotState.SHOW_HOME_PHONE_MENU_ALL)) {
            row1.add(likeButton);
            row2.add(0, InlineKeyboardService.getButton(PREV, PREV));
            row2.add(3, InlineKeyboardService.getButton(NEXT, NEXT));
        } else
            row2.add(1, likeButton);


        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setMessageId(message.getMessageId());
        editMessageCaption.setChatId(message.getChatId().toString());
        editMessageCaption.setCaption(getCaptionByHome(home, lan));
        editMessageCaption.setReplyMarkup(markup);
        return editMessageCaption;
    }

    /////////// CAPTION
    public String getCaptionByHome(Home home, Language lan) {
        return getWord(HOUSE_TYPE, lan) + "\t" + getWord(home.getHomeType().equals(HomeType.HOUSE) ? HOUSE : FLAT, lan) + "\t\t\t\t\t\t\t\t\t\t" +
                getWord(ADMIN_HOMES_INFO_NUMBER_OF_INTERESTED, lan) + home.getInterests() + "\n" +
                getWord(STATUS, lan) + "\t" + getWord(home.getStatus().equals(HomeStatus.SELL) ? SELL : RENT, lan) + "\n" +
                getWord(ADMIN_HOMES_INFO_REGION, lan) + home.getRegion() + "\n" +
                getWord(ADMIN_HOMES_INFO_DISTRICT, lan) + (home.getDistrict() != null ? home.getDistrict() : " ") + "\n" +
                getWord(ADDRESS, lan) + home.getAddress() + "\n" +
                getWord(NUMBER_OF_ROOMS, lan) + home.getNumberOfRooms() + "\n" +
                getWord(AREA, lan) + home.getArea() + " m²" + "\n" +
                getWord(DATE, lan) + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(home.getCreatedDate()) + "\n" +
                getWord(ADMIN_HOMES_INFO_PRICE, lan) + home.getPrice() + " $" + "\n" +
                getWord(DESCRIPTION, lan) + home.getDescription();
    }

    public EditMessageCaption changeHomeLike(Update update, Language lan, String backName, BotState state) {
        Message message = update.getCallbackQuery().getMessage();
        Like like = likeService.getById(UUID.fromString(update.getCallbackQuery().getData()));
        User user = userService.getByChatId(message.getChatId().toString());
        Home home = homeService.getById(like.getHome().getId());
        like = likeService.changeLike(like,home);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        markup.setKeyboard(rowList);
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        InlineKeyboardButton phoneButton = InlineKeyboardService.getButton(home.getId().toString(), interestService.getVisible(home, user) ?
                userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
        InlineKeyboardButton likeButton = InlineKeyboardService.getButton(like.getId().toString(), home.getLikes()
                + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));


        row2.add(InlineKeyboardService.getButton(backName, getWord(BACK, lan)));
        row2.add(InlineKeyboardService.getButton(BACK_TO_MAIN_MENU_SEND, getWord(MENU, lan)));
        row1.add(phoneButton);
        if (state.equals(BotState.CHANGE_HOME_LIKE_MENU_MY_ACCOMMODATIONS)) {
            row1.add(likeButton);
            row2.add(1, InlineKeyboardService.getButton(DELETE + home.getId(), DELETE));
        } else if (state.equals(BotState.CHANGE_HOME_LIKE_MENU_ALL)) {
            row1.add(likeButton);
            row2.add(0, InlineKeyboardService.getButton(PREV, PREV));
            row2.add(3, InlineKeyboardService.getButton(NEXT, NEXT));
        } else
            row2.add(1, likeButton);


        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setMessageId(message.getMessageId());
        editMessageCaption.setChatId(message.getChatId().toString());
        editMessageCaption.setCaption(getCaptionByHome(home, lan));
        editMessageCaption.setReplyMarkup(markup);
        return editMessageCaption;
    }

    public EditMessageText chooseRegionMenu(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        Message message = query.getMessage();
        saveSearchRegion(update);
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (Region value : Region.values()) {
            if (row.size() != 3) {
                row.add(value.name());
            } else {
                rows.add(row);
                row = new ArrayList<>();
                row.add(value.name());
            }
        }
        rows.add(row);
        rows.add(List.of(BACK_TO_SHOW_MENU_EDIT, SKIP));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows, lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_REGION_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    // GET STATE
    public BotState getState(Update update, BotState state) {
        String data = update.getCallbackQuery().getData();
        if (isRegion(update)) {
            if (state.equals(BotState.GIVE_REGION))
                return BotState.GIVE_DISTRICT;
            else
                return BotState.CHOOSE_DISTRICT;
        }
        if (isHomeType(update))
            return state.equals(BotState.CHOOSE_HOME_TYPE) ? BotState.CHOOSE_HOME_NUMBER_EDIT : BotState.GIVE_HOME_NUMBER;
        if (likeService.isLike(data))
            return state.equals(BotState.SHOW_OPTIONS) ? BotState.CHANGE_HOME_LIKE_MENU_ALL : state.equals(BotState.SHOW_SORTED_OPTIONS) ?
                    BotState.CHANGE_HOME_LIKE_MENU_SEARCH : state.equals(BotState.MY_FAVOURITES) ? BotState.CHANGE_HOME_LIKE_MENU_FAVOURITES :
                    state.equals(BotState.MY_HOMES) ? BotState.CHANGE_HOME_LIKE_MENU_MY_ACCOMMODATIONS : BotState.CHANGE_HOME_LIKE_MENU_ALL;
        if (homeService.checkById(data))
            return state.equals(BotState.SHOW_OPTIONS) ? SHOW_HOME_PHONE_MENU_ALL : state.equals(BotState.SHOW_SORTED_OPTIONS) ?
                    BotState.SHOW_HOME_PHONE_MENU_SEARCH : state.equals(BotState.MY_FAVOURITES) ? BotState.SHOW_HOME_PHONE_MENU_FAVOURITES :
                    state.equals(BotState.MY_HOMES) ? BotState.SHOW_HOME_PHONE_MENU_MY_ACCOMMODATIONS : SHOW_HOME_PHONE_MENU_ALL;

        return data.startsWith(DELETE) ? BotState.DELETE_ACCOMMODATION : state.equals(BotState.CHOOSE_DISTRICT) ? BotState.CHOOSE_HOME_STATUS :
                BotState.GIVE_ADDRESS;
    }

    public BotState getStateBySkip(Update update, BotState state) {
        switch (state) {
            case CHOOSE_REGION :  {
                saveSearchRegion(update);
                state = BotState.CHOOSE_HOME_STATUS;
            }break;
            case CHOOSE_DISTRICT :  state = BotState.CHOOSE_HOME_STATUS;break;
            case CHOOSE_HOME_STATUS :  state = BotState.CHOOSE_HOME_TYPE;break;
            case CHOOSE_HOME_TYPE :  state = BotState.CHOOSE_HOME_NUMBER_EDIT;break;
            case CHOOSE_HOME_NUMBER_EDIT:
                case CHOOSE_HOME_NUMBER_SEND :  state = BotState.CHOOSE_HOME_MIN_PRICE_EDIT;break;
            case CHOOSE_HOME_MIN_PRICE_SEND:
                case CHOOSE_HOME_MIN_PRICE_EDIT :  state = BotState.CHOOSE_HOME_MAX_PRICE_EDIT;break;
            case CHOOSE_HOME_MAX_PRICE_SEND:
                case CHOOSE_HOME_MAX_PRICE_EDIT :  state = BotState.SHOW_SORTED_OPTIONS;break;
            default :  state = BotState.ERROR;
        }

        return state;
    }



    public EditMessageText chooseDistrict(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        Message message = query.getMessage();

        Region region;
        if (!query.getData().contains("BACK"))
            region = saveSearchRegion(update);
        else {
            saveSearchDistrict(update);
            Search search = searchService.getSearchByUserId(userService.getByChatId(message.getChatId().toString()));
            if (search == null || search.getRegion() == null) return chooseRegionMenu(update, lan);
            region = search.getRegion();
        }

        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (District value : District.values())
            if (value.getRegionId() == region.getId()) {
                if (row.size() != 3) {
                    row.add(value.name());
                } else {
                    rows.add(row);
                    row = new ArrayList<>();
                    row.add(value.name());
                }
            }
        rows.add(row);
        rows.add(List.of(BACK_TO_CHOOSE_REGION, SKIP));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows, lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_DISTRICT_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public EditMessageText chooseHomeStatus(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        if (query.getData().contains("BACK"))
            saveSearchStatus(update);
        else
            saveSearchDistrict(update);


        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(GET_RENTING, FOR_BUY),
                List.of(BACK_TO_CHOOSE_DISTRICT, SKIP)
        ), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(SET_ACCOMMODATION_FOR_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setChatId(query.getMessage().getChatId().toString());
        editMessageText.setMessageId(query.getMessage().getMessageId());
        return editMessageText;
    }

    public EditMessageText chooseHomeType(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        CallbackQuery query = update.getCallbackQuery();
        if (update.hasCallbackQuery()) {
            if (query.getData().contains("BACK"))
                saveSearchType(update);
            else
                saveSearchStatus(update);
        }

        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(HOUSE, FLAT),
                List.of(BACK_TO_CHOOSE_HOME_STATUS, SKIP)
        ), lan);
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setText(getWord(CHOOSE_ACCOMMODATION_TYPE_MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText chooseHomeNumberEdit(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        if (update.hasCallbackQuery()) {
            if (query.getData().contains("BACK"))
                saveSearchNumber(update);
            else
                saveSearchType(update);
        }
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(NUMBER_OF_ROOMS, lan));
        editMessageText.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_TYPE, SKIP)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;

    }

    public SendMessage chooseHomeNumberSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(NUMBER_OF_ROOMS, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_TYPE, SKIP)), lan));
        return sendMessage;
    }

    public SendMessage chooseMinPriceMenuSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(MIN_PRICE, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_NUMBER, SKIP)), lan));
        return sendMessage;
    }

    public EditMessageText chooseMinPriceMenuEdit(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        if (update.hasCallbackQuery()) {
            if (query.getData().contains("BACK"))
                saveSearchMinPrice(update);
            if (query.getData().equals(SKIP))
                saveSearchNumber(update);
        }
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(MIN_PRICE, lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_NUMBER, SKIP)), lan));
        return editMessage;
    }

    public SendMessage chooseMaxPriceMenuSend(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        if (update.hasCallbackQuery()) {
            if (query.getData().contains("BACK"))
                saveSearchMaxPrice(update);
            if (query.getData().equals(SKIP))
                saveSearchMinPrice(update);
        }
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(MAX_PRICE, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_CHOOSE_MIN_PRICE, SKIP)), lan));
        return sendMessage;
    }

    public EditMessageText chooseMaxPriceMenuEdit(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        if (update.hasCallbackQuery()) {
            if (query.getData().contains("BACK"))
                saveSearchMaxPrice(update);
            if (query.getData().equals(SKIP))
                saveSearchMinPrice(update);
        }
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(MAX_PRICE, lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(List.of(BACK_TO_CHOOSE_MIN_PRICE, SKIP)), lan));
        return editMessage;
    }

    public List<SendPhoto> showSortedOptionsSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = null;
        List<List<InlineKeyboardButton>> rowList;
        List<InlineKeyboardButton> row1;
        List<InlineKeyboardButton> row2 = null;
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        Search search = searchService.getSearchByUserId(userService.getByChatId(message.getChatId().toString()));
        List<Home> allHome = searchService.searchHome(search);
        if (allHome.isEmpty())
            return null;

        User user = userService.getByChatId(message.getChatId().toString());
        List<SendPhoto> sendMessageList = new ArrayList<>();
        SendPhoto sendPhoto = null;

        for (Home home : allHome) {
            if (!home.isActive()) continue;
            if (sendPhoto != null)
                sendMessageList.add(sendPhoto);
            markup = new InlineKeyboardMarkup();
            rowList = new ArrayList<>();
            markup.setKeyboard(rowList);
            row1 = new ArrayList<>();
            row2 = new ArrayList<>();
            rowList.add(row1);
            rowList.add(row2);

            sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(home.getFileId()));
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            Like like = likeService.getLikeByHomeIdAndUserId(home, user);

            InlineKeyboardButton phoneButton =
                    new InlineKeyboardButton(interestService.getVisible(home, user) ?
                            userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
            phoneButton.setCallbackData(home.getId().toString());


            InlineKeyboardButton likeButton = new InlineKeyboardButton(home.getLikes()
                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
            likeButton.setCallbackData(like.getId().toString());
            row1.add(phoneButton);
            row2.add(likeButton);
            sendPhoto.setReplyMarkup(markup);
        }

        InlineKeyboardButton back = new InlineKeyboardButton(getWord(BACK, lan));
        back.setCallbackData(BACK_TO_CHOOSE_MAX_PRICE_SEND);
        InlineKeyboardButton menu = new InlineKeyboardButton(getWord(MENU, lan));
        menu.setCallbackData(BACK_TO_MAIN_MENU_SEND);
        assert row2 != null;
        row2.add(0, back);
        row2.add(menu);
        sendPhoto.setReplyMarkup(markup);
        sendMessageList.add(sendPhoto);
        return sendMessageList;
    }

    public EditMessageText getMyNotesMenuEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(MY_ACCOMMODATIONS, MY_FAVOURITES),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_ACTION, lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage getMyNotesMenuSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(MY_ACCOMMODATIONS, MY_FAVOURITES),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACTION, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public List<SendPhoto> getMyFavouriteHomes(Update update, Language lan) {
        InlineKeyboardMarkup markup = null;
        List<List<InlineKeyboardButton>> rowList;
        List<InlineKeyboardButton> row1;
        List<InlineKeyboardButton> row2 = null;
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();

        User user = userService.getByChatId(message.getChatId().toString());
        List<Home> allHomes = likeService.getActiveHomesByUserId(user.getId());
        if (allHomes.isEmpty())
            return null;

        List<SendPhoto> sendMessageList = new ArrayList<>();
        SendPhoto sendPhoto = null;

        for (Home home : allHomes) {
            if (!home.isActive()) continue;
            if (sendPhoto != null)
                sendMessageList.add(sendPhoto);
            markup = new InlineKeyboardMarkup();
            rowList = new ArrayList<>();
            markup.setKeyboard(rowList);
            row1 = new ArrayList<>();
            row2 = new ArrayList<>();
            rowList.add(row1);
            rowList.add(row2);

            sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(home.getFileId()));
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            Like like = likeService.getLikeByHomeIdAndUserId(home, user);

            InlineKeyboardButton phoneButton =
                    new InlineKeyboardButton(interestService.getVisible(home, user) ?
                            userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
            phoneButton.setCallbackData(home.getId().toString());


            InlineKeyboardButton likeButton = new InlineKeyboardButton(home.getLikes()
                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
            likeButton.setCallbackData(like.getId().toString());
            row1.add(phoneButton);
            row2.add(likeButton);
            sendPhoto.setReplyMarkup(markup);
        }

        InlineKeyboardButton back = new InlineKeyboardButton(getWord(BACK, lan));
        back.setCallbackData(BACK_TO_MY_NOTES_MENU_SEND);
        InlineKeyboardButton menu = new InlineKeyboardButton(getWord(MENU, lan));
        menu.setCallbackData(BACK_TO_MAIN_MENU_SEND);
        assert row2 != null;
        row2.add(0, back);
        row2.add(menu);
        sendPhoto.setReplyMarkup(markup);
        sendMessageList.add(sendPhoto);
        return sendMessageList;
    }

    public List<SendPhoto> getMyHomes(Update update, Language lan) {
        InlineKeyboardMarkup markup = null;
        List<List<InlineKeyboardButton>> rowList;
        List<InlineKeyboardButton> row1;
        List<InlineKeyboardButton> row2 = null;
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();

        User user = userService.getByChatId(message.getChatId().toString());
        List<Home> allHomes = homeService.getByOwnerId(user.getId());
        if (allHomes.isEmpty())
            return null;

        List<SendPhoto> sendMessageList = new ArrayList<>();
        SendPhoto sendPhoto = null;

        for (Home home : allHomes) {
            if (!home.isActive()) continue;
            if (sendPhoto != null)
                sendMessageList.add(sendPhoto);
            markup = new InlineKeyboardMarkup();
            rowList = new ArrayList<>();
            markup.setKeyboard(rowList);
            row1 = new ArrayList<>();
            row2 = new ArrayList<>();
            rowList.add(row1);
            rowList.add(row2);

            sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(home.getFileId()));
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            Like like = likeService.getLikeByHomeIdAndUserId(home, user);

            InlineKeyboardButton phoneButton =
                    new InlineKeyboardButton(interestService.getVisible(home, user) ?
                            userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
            phoneButton.setCallbackData(home.getId().toString());

            InlineKeyboardButton likeButton = new InlineKeyboardButton(home.getLikes()
                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
            likeButton.setCallbackData(like.getId().toString());

            InlineKeyboardButton deleteButton = new InlineKeyboardButton(DELETE);
            deleteButton.setCallbackData(DELETE + home.getId());


            row1.add(phoneButton);
            row1.add(likeButton);
            row2.add(deleteButton);
            sendPhoto.setReplyMarkup(markup);
        }

        InlineKeyboardButton back = new InlineKeyboardButton(getWord(BACK, lan));
        back.setCallbackData(BACK_TO_MY_NOTES_MENU_SEND);
        InlineKeyboardButton menu = new InlineKeyboardButton(getWord(MENU, lan));
        menu.setCallbackData(BACK_TO_MAIN_MENU_SEND);
        assert row2 != null;
        row2.add(0, back);
        row2.add(menu);
        sendPhoto.setReplyMarkup(markup);
        sendMessageList.add(sendPhoto);
        return sendMessageList;
    }

    public DeleteMessage deleteAccommodation(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        homeService.deleteHome(UUID.fromString(query.getData().substring(DELETE.length())));
        return deleteMessage(update);
    }


    public void changeIsAdminUser(Update update, boolean isAdmin) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        userService.changeIsAdmin(message.getChatId().toString(), isAdmin);
    }

    public boolean checkByCode(Update update) {
        Message message = update.getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        if (user.getCode().equals(message.getText())) {
            user.setCode(null);
            user.setRole(Role.ADMIN);
            userService.save(user);
            return true;
        }
        return false;
    }

    public SendMessage checkUserMenu(Update update, Language lan) {
        Message message = update.getMessage();
        User user = userService.getByChatId(message.getChatId().toString());
        user.setCode(UUID.randomUUID().toString().substring(0, 4));
        new Thread(() ->  emailComponent.sendCode(user.getPhoneNumber(), user.getCode())).start();
        userService.save(user);

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHECK_USER_BY_ADMIN_TEXT, lan));
        sendMessage.setReplyMarkup(InlineKeyboardService.createMarkup(List.of(
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan));
        return sendMessage;
    }

    public SendMessage banUserMenu(Update update,Language lan) {
        Message message = getMessage(update);
        return new SendMessage(message.getChatId().toString(),getWord(BAN_MENU_TEXT,lan));
    }

    private Message getMessage(Update update){
        return update.hasMessage()?update.getMessage():update.getCallbackQuery().getMessage();
    }
}
