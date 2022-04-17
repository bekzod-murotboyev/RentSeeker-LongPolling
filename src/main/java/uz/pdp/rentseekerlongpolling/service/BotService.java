package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.rentseekerlongpolling.component.EmailComponent;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.entity.*;
import uz.pdp.rentseekerlongpolling.feign.TelegramFeign;
import uz.pdp.rentseekerlongpolling.model.locationModels.LocationsItem;
import uz.pdp.rentseekerlongpolling.payload.home.HomePageableDTO;
import uz.pdp.rentseekerlongpolling.payload.telegram.simple_telegram.FileDataDTO;
import uz.pdp.rentseekerlongpolling.util.enums.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static uz.pdp.rentseekerlongpolling.service.LanguageService.getWord;
import static uz.pdp.rentseekerlongpolling.util.Url.*;
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

    private final ModelMapper modelMapper;

    private final TelegramFeign telegramFeign;


    public AnswerCallbackQuery getAnswerCallbackQuery(CallbackQuery query, String text, Language lan) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(query.getId());
        answerCallbackQuery.setText(getWord(text, lan));
        answerCallbackQuery.setShowAlert(true);
        return answerCallbackQuery;
    }

    public AnswerCallbackQuery setWarningRegister(Update update, Language lan) {
        return getAnswerCallbackQuery(update.getCallbackQuery(), REGISTER_WARNING_TEXT, lan);
    }

    public AnswerCallbackQuery itHasOnlyOnePic(Update update, Language lan) {
        return getAnswerCallbackQuery(update.getCallbackQuery(), IT_HAS_ONLY_ONE_PIC, lan);
    }


    public SendMessage chooseLanguage(Update update, Language lan) {
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(UZ, RU, EN)
        ), lan);
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_LANGUAGE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText changeLanguage(Update update, Language lan) {
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(UZ, RU, EN),
                List.of(BACK_TO_SETTINGS_MENU_EDIT)
        ), lan);
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_LANGUAGE_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }


    public EditMessageText getMainMenuEditForAdminEdit(Update update, Language lan) {
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(USER, ADMIN)), lan);
        Message message = getMessage(update);
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(CHOOSE_ACTION, lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setReplyMarkup(markup);
        return editMessage;
    }


    public SendMessage getAdminMenuSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(USER, ADMIN)), lan);
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACTION, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText setMenuEdit(Update update, Language lan, Role role) {
        if (role.equals(Role.ADMIN)) return getMainMenuEditForAdminEdit(update, lan);
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(CHANGE_LANGUAGE, REGISTRATION),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_ACTION, lan));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage getSettingMenuSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(CHANGE_LANGUAGE, REGISTRATION),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);
        Message message = getMessage(update);
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
                    userService.savePhoneNumber(phone.substring(4), message.getChatId().toString());
                } else return false;
            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                userService.savePhoneNumber(contact.getPhoneNumber().substring(3), message.getChatId().toString());
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
        Message message = getMessage(update);

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
        Message message = getMessage(update);
        return new DeleteMessage(message.getChatId().toString(), message.getMessageId());
    }

    public DeleteMessage deleteMessage(Message message) {
        return new DeleteMessage(message.getChatId().toString(), message.getMessageId());
    }

    public SendMessage removeKeyBoardMarkup(Update update) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "⬅️⬅️⬅️");
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        return sendMessage;
    }


    public EditMessageText setHomeStatusMenu(Update update, Language lan) {
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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


    public EditMessageText setWriteOrSendLocationMenuEdit(Update update, Language lan) {
        Message message = getMessage(update);
        if (update.hasCallbackQuery() && !update.getCallbackQuery().getData().startsWith("BACK"))
            saveHomeStatus(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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

        Message message = getMessage(update);
        User user = userService.getByChatId(message.getChatId().toString());
        assert user != null;
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(SEND_LOCATION_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText giveRegionMenu(Update update, Language lan) {
        Message message = getMessage(update);
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
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(rows, lan);
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
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(rows, lan);
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
        homeService.addHome(home, query.getMessage().getChatId().toString(), null);
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
        FileDataDTO filePath = telegramFeign.getFilePath(photoSize.getFileId());
        photoSize.setFilePath(TELEGRAM_BASE+TELEGRAM_GET_FILE+filePath.getResult().getFilePath());
        Home home = new Home();
        User user = userService.getByChatId(message.getChatId().toString());
        home.setAttachments(List.of(modelMapper.map(photoSize, Attachment.class)));
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

    public boolean saveHomeDescription(Update update, BotState state) {
        Message message = update.getMessage();
        String text = message.getText();
        User user = userService.getByChatId(message.getChatId().toString());
        Home home = new Home();
        home.setUser(user);
        home.setDescription(text);
        return homeService.addHome(home, user.getChatId(), state);

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
        } else {
            try {
                Region region = Region.valueOf(query.getData());
                search.setRegion(region);
            } catch (Exception e) {
                search.setRegion(null);
            }
        }
        searchService.addSearch(search);
        return search.getRegion();

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
        search.setDistrict(query.getData().equals(SKIP) || query.getData().contains("BACK") ?
                null : District.valueOf(query.getData().toUpperCase()));
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
        search.setHomeType(query.getData().equals(SKIP) || query.getData().contains("BACK") ?
                null : HomeType.valueOf(query.getData().toUpperCase()));
        searchService.addSearch(search);
    }

    public boolean saveSearchNumber(Update update) {
        Message message = getMessage(update);
        User user = userService.getByChatId(message.getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.contains("BACK"))
                search.setNumberOfRooms(null);
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
        Message message = getMessage(update);
        User user = userService.getByChatId(message.getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.contains("BACK"))
                search.setMinPrice(null);

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
        Message message = getMessage(update);
        User user = userService.getByChatId(message.getChatId().toString());
        Search search = searchService.getSearchByUserId(user);
        if (search == null) {
            search = new Search();
            search.setUser(user);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.contains("BACK"))
                search.setMaxPrice(null);
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

    public boolean checkByPhone(Update update) {
        Message message = getMessage(update);
        User user = userService.getByChatId(message.getChatId().toString());
        assert user != null;
        return !user.getPhoneNumber().equals("");
    }
    //


    public EditMessageText giveAddressMenu(Update update, Language lan, BotState state) {
        if (!update.getCallbackQuery().getData().contains("BACK"))
            saveHomeDistrict(update, state);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_DISTRICT)), lan);
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(WRITE_ADDRESS, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage giveHomeTypeMenuSend(Update update, Language lan, BotState state) {
        Message message = getMessage(update);
        if (!update.hasCallbackQuery() && !update.getMessage().hasLocation())
            saveHomeAddress(update, state);

        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(HOUSE, FLAT),
                List.of(message.hasLocation() ? BACK_SEND_LOCATION : BACK_TO_GIVE_ADDRESS)
        ), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACCOMMODATION_TYPE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText giveHomeTypeMenuEdit(Update update, Language lan) {
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        return new SendMessage(message.getChatId().toString(), getWord(LOCATION_NOT_FOUND, lan));
    }

    public EditMessageText giveHomeNumberMenu(Update update, Language lan, BotState state) {
        if (state.equals(BotState.GIVE_HOME_NUMBER) && update.hasCallbackQuery() && !update.getCallbackQuery().getData().contains("BACK"))
            saveHomeType(update, state);
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(NUMBER_OF_ROOMS, lan));
        editMessageText.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_TYPE)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage giveHomeNumberMenuSend(Update update, Language lan, BotState state) {
        if (state.equals(BotState.GIVE_HOME_NUMBER))
            saveHomeType(update, state);
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(NUMBER_OF_ROOMS, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_TYPE)), lan));
        return sendMessage;
    }

    public SendMessage giveHomeAreaMenuSend(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ENTER_AREA_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_NUMBER)), lan));
        return sendMessage;
    }

    public EditMessageText giveHomeAreaMenuEdit(Update update, Language lan) {
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ENTER_AREA_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_NUMBER)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage giveHomePhotoMenuSend(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(SEND_PHOTO_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_AREA)), lan));
        return sendMessage;
    }

    public SendMessage giveHomePhotoMenuSendSimple(Update update, Language lan) {
        Message message = getMessage(update);
        homeService.deleteHomeAttachment(message.getChatId().toString());
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(SEND_PHOTO_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_AREA)), lan));
        return sendMessage;
    }

    public SendMessage giveHomePhotoMenuSendAgain(Update update, Language lan, BotState state) {
        saveHomePhoto(update, state);
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "✅");
        sendMessage.setReplyMarkup(KeyboardService.createReplyMarkup(List.of(List.of(BACK, NEXT_ACTION)), lan));
        sendMessage.setReplyToMessageId(message.getMessageId());
        return sendMessage;
    }

    public EditMessageText giveHomePhotoMenuEdit(Update update, Language lan) {
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(SEND_PHOTO_MENU_TEXT, lan));
        editMessageText.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_AREA)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;
    }

    public SendMessage giveHomePriceMenuSend(Update update, Language lan, BotState state) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ENTER_PRICE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PHOTO)), lan));
        return sendMessage;
    }

    public EditMessageText giveHomePriceMenuEdit(Update update, Language lan) {
        Message message = getMessage(update);
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(ENTER_PRICE_MENU_TEXT, lan));
        editMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PHOTO)), lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        return editMessage;
    }

    public SendMessage giveHomePriceMenuSend(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(getWord(ENTER_PRICE_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PHOTO)), lan));
        sendMessage.setChatId(message.getChatId().toString());
        return sendMessage;
    }

    public SendMessage giveHomeDescription(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(WRITE_DESCRIPTION_MENU_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_GIVE_HOME_PRICE)), lan));
        return sendMessage;
    }

    public SendMessage successfullySaved(Update update, Language lan) {
        Message message = getMessage(update);
        return new SendMessage(message.getChatId().toString(), getWord(SUCCESSFULLY_SAVED, lan));
    }

    public EditMessageText getShowMenuEdit(Update update, Language lan) {
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(SEARCH, SHOW_ALL),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);

        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(CHOOSE_ACTION, lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage getShowMenuSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
                List.of(SEARCH, SHOW_ALL),
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan);

        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHOOSE_ACTION, lan));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public HomePageableDTO showAllHomes(Update update, Language lan) {
        InlineKeyboardMarkup markup;
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1;
        CallbackQuery query = update.getCallbackQuery();
        Message message = query.getMessage();
        String data = query.getData();
        User user = userService.getByChatId(message.getChatId().toString());
        int page = user.getCrtPage() + (data.equals(PREV) ? -1 : data.startsWith(NEXT) ? 1 : 0);
        if (page < 0) return new HomePageableDTO(null, FIRST_PAGE_NOTIFICATION);
        Page<Home> allHome = homeService.getAllActiveHomes(page, pageableSize);
        if (allHome.isEmpty()) {
            return new HomePageableDTO(null, page == 0 ? null : LAST_PAGE_NOTIFICATION);
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
            sendPhoto = new SendPhoto(message.getChatId().toString(), new InputFile(home.getAttachments().get(0).getFileId()));
            sendPhoto.setCaption(getCaptionByHome(home, lan));
            sendPhoto.setReplyMarkup(markup);
            Like like = likeService.getLikeByHomeAndUser(home, user);
            row1.add(KeyboardService.getInlineButton(PHONE + home.getId(),
                    interestService.getVisible(home, user) ? userService.getById(home.getUser().getId()).getPhoneNumber()
                            : getWord(GET_PHONE_NUMBER, lan)));
            row1.add(KeyboardService.getInlineButton(PHOTOS + home.getId(), getWord(HOME_PHOTOS, lan), home.getDetailsPath()));
            row1.add(KeyboardService
                    .getInlineButton(LIKE + like.getId(),
                            home.getLikes()
                                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE)));

        }
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(KeyboardService.getInlineButton(PREV, PREV));
        row2.add(KeyboardService.getInlineButton(BACK_TO_SHOW_MENU_SEND, getWord(BACK, lan)));
        row2.add(KeyboardService.getInlineButton(BACK_TO_MAIN_MENU_SEND, getWord(MENU, lan)));
        row2.add(KeyboardService.getInlineButton(NEXT, NEXT));
        rowList.add(row2);
        sendMessageList.add(sendPhoto);
        return new HomePageableDTO(sendMessageList, null);
    }

    public AnswerCallbackQuery homeNotFound(Update update, Language lan) {
        return getAnswerCallbackQuery(update.getCallbackQuery(), HOMES_NOT_FOUND, lan);
    }

    public SendMessage searchedHomeNotFound(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(HOMES_NOT_FOUND, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(
                List.of(List.of(
                        BACK_TO_CHOOSE_MAX_PRICE_EDIT,
                        MENU
                )),
                lan
        ));
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
        Like like = likeService.getLikeByHomeAndUser(home, user);


        InlineKeyboardButton likeButton = KeyboardService.getInlineButton(LIKE + like.getId().toString(),
                home.getLikes()
                        + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
        row1.add(KeyboardService.getInlineButton(PHONE + home.getId().toString(),
                interestService.changeVisible(home, user) ?
                        userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan)));
        row1.add(KeyboardService.getInlineButton(PHOTOS + home.getId(), getWord(HOME_PHOTOS, lan), home.getDetailsPath()));
        row2.add(KeyboardService.getInlineButton(backName, getWord(BACK, lan)));
        row2.add(KeyboardService.getInlineButton(BACK_TO_MAIN_MENU_SEND, getWord(MENU, lan)));

        if (state.equals(BotState.SHOW_HOME_PHONE_MENU_MY_ACCOMMODATIONS)) {
            row1.add(likeButton);
            row2.add(1, KeyboardService.getInlineButton(DELETE + home.getId(), DELETE));
        } else if (state.equals(BotState.SHOW_HOME_PHONE_MENU_ALL)) {
            row1.add(likeButton);
            row2.add(0, KeyboardService.getInlineButton(PREV, PREV));
            row2.add(3, KeyboardService.getInlineButton(NEXT, NEXT));
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
                ADMIN_HOMES_INFO_NUMBER_OF_INTERESTED + home.getInterests() + "\n" +
                getWord(STATUS, lan) + "\t" + getWord(home.getStatus().equals(HomeStatus.SELL) ? SELL : RENT, lan) + "\n" +
                getWord(ADMIN_HOMES_INFO_REGION, lan) + getWord(home.getRegion().name(), lan) + "\n" +
                getWord(ADMIN_HOMES_INFO_DISTRICT, lan) + getWord(home.getDistrict().name(), lan) + "\n" +
                getWord(ADDRESS, lan) + home.getAddress() + "\n" +
                getWord(NUMBER_OF_ROOMS, lan) + home.getNumberOfRooms() + "\n" +
                getWord(AREA, lan) + home.getArea() + " m²" + "\n" +
                getWord(DATE, lan) + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(home.getCreatedDate()) + "\n" +
                getWord(ADMIN_HOMES_INFO_PRICE, lan) + home.getPrice() + " $" + "\n" +
                getWord(DESCRIPTION, lan) + home.getDescription();
    }

    public EditMessageCaption changeHomeLike(Update update, Language lan, String backName, BotState state) {
        Message message = update.getCallbackQuery().getMessage();

        Like like = likeService.changeLike(UUID.fromString(update.getCallbackQuery().getData()));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        markup.setKeyboard(rowList);
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        InlineKeyboardButton phoneButton = KeyboardService.getInlineButton(PHONE + like.getHome().getId().toString(), interestService.getVisible(like.getHome(), like.getUser()) ?
                userService.getById(like.getHome().getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
        InlineKeyboardButton likeButton = KeyboardService.getInlineButton(LIKE + like.getId().toString(), like.getHome().getLikes()
                + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));


        row2.add(KeyboardService.getInlineButton(backName, getWord(BACK, lan)));
        row2.add(KeyboardService.getInlineButton(BACK_TO_MAIN_MENU_SEND, getWord(MENU, lan)));
        row1.add(phoneButton);
        row1.add(KeyboardService.getInlineButton(PHOTOS + like.getHome().getId(), getWord(HOME_PHOTOS, lan), like.getHome().getDetailsPath()));
        if (state.equals(BotState.CHANGE_HOME_LIKE_MENU_MY_ACCOMMODATIONS)) {
            row1.add(likeButton);
            row2.add(1, KeyboardService.getInlineButton(DELETE + like.getHome().getId(), DELETE));
        } else if (state.equals(BotState.CHANGE_HOME_LIKE_MENU_ALL)) {
            row1.add(likeButton);
            row2.add(0, KeyboardService.getInlineButton(PREV, PREV));
            row2.add(3, KeyboardService.getInlineButton(NEXT, NEXT));
        } else
            row2.add(1, likeButton);


        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setMessageId(message.getMessageId());
        editMessageCaption.setChatId(message.getChatId().toString());
        editMessageCaption.setCaption(getCaptionByHome(like.getHome(), lan));
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
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(rows, lan);
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
        if (data.equals(FLAT) || data.equals(HOUSE))
            return state.equals(BotState.CHOOSE_HOME_TYPE) ? BotState.CHOOSE_HOME_NUMBER_EDIT : BotState.GIVE_HOME_NUMBER;
        if (data.startsWith(LIKE)) {
            update.getCallbackQuery().setData(data.substring(LIKE.length()));
            return state.equals(BotState.SHOW_OPTIONS) ? BotState.CHANGE_HOME_LIKE_MENU_ALL : state.equals(BotState.SHOW_SORTED_OPTIONS) ?
                    BotState.CHANGE_HOME_LIKE_MENU_SEARCH : state.equals(BotState.MY_FAVOURITES) ? BotState.CHANGE_HOME_LIKE_MENU_FAVOURITES :
                    state.equals(BotState.MY_HOMES) ? BotState.CHANGE_HOME_LIKE_MENU_MY_ACCOMMODATIONS : BotState.CHANGE_HOME_LIKE_MENU_ALL;
        }

        if (data.startsWith(PHONE)) {
            update.getCallbackQuery().setData(data.substring(PHONE.length()));
            return state.equals(BotState.SHOW_OPTIONS) ? SHOW_HOME_PHONE_MENU_ALL : state.equals(BotState.SHOW_SORTED_OPTIONS) ?
                    BotState.SHOW_HOME_PHONE_MENU_SEARCH : state.equals(BotState.MY_FAVOURITES) ? BotState.SHOW_HOME_PHONE_MENU_FAVOURITES :
                    state.equals(BotState.MY_HOMES) ? BotState.SHOW_HOME_PHONE_MENU_MY_ACCOMMODATIONS : SHOW_HOME_PHONE_MENU_ALL;
        }

        if (data.startsWith(PHOTOS)) {
            update.getCallbackQuery().setData(data.substring(PHOTOS.length()));
            return BotState.SHOW_HOME_PHOTOS;
        }

        return data.startsWith(DELETE) ? BotState.DELETE_ACCOMMODATION : state.equals(BotState.CHOOSE_DISTRICT) ? BotState.CHOOSE_HOME_STATUS :
                BotState.GIVE_ADDRESS;
    }

    public BotState getStateBySkip(Update update, BotState state) {
        switch (state) {
            case CHOOSE_REGION: {
                saveSearchRegion(update);
                state = BotState.CHOOSE_HOME_STATUS;
            }
            break;
            case CHOOSE_DISTRICT:
                state = BotState.CHOOSE_HOME_STATUS;
                break;
            case CHOOSE_HOME_STATUS:
                state = BotState.CHOOSE_HOME_TYPE;
                break;
            case CHOOSE_HOME_TYPE:
                state = BotState.CHOOSE_HOME_NUMBER_EDIT;
                break;
            case CHOOSE_HOME_NUMBER_EDIT:
            case CHOOSE_HOME_NUMBER_SEND:
                state = BotState.CHOOSE_HOME_MIN_PRICE_EDIT;
                break;
            case CHOOSE_HOME_MIN_PRICE_SEND:
            case CHOOSE_HOME_MIN_PRICE_EDIT:
                state = BotState.CHOOSE_HOME_MAX_PRICE_EDIT;
                break;
            case CHOOSE_HOME_MAX_PRICE_SEND:
            case CHOOSE_HOME_MAX_PRICE_EDIT:
                state = BotState.SHOW_SORTED_OPTIONS;
                break;
            default:
                state = BotState.ERROR;
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
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(rows, lan);
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


        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        CallbackQuery query = update.getCallbackQuery();
        if (update.hasCallbackQuery()) {
            if (query.getData().contains("BACK"))
                saveSearchType(update);
            else
                saveSearchStatus(update);
        }

        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(NUMBER_OF_ROOMS, lan));
        editMessageText.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_TYPE, SKIP)), lan));
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setChatId(message.getChatId().toString());
        return editMessageText;

    }

    public SendMessage chooseHomeNumberSend(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(NUMBER_OF_ROOMS, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_TYPE, SKIP)), lan));
        return sendMessage;
    }

    public SendMessage chooseMinPriceMenuSend(Update update, Language lan) {
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(MIN_PRICE, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_NUMBER, SKIP)), lan));
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
        Message message = getMessage(update);
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(MIN_PRICE, lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_CHOOSE_HOME_NUMBER, SKIP)), lan));
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
        Message message = getMessage(update);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(MAX_PRICE, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_CHOOSE_MIN_PRICE, SKIP)), lan));
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
        Message message = getMessage(update);
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(getWord(MAX_PRICE, lan));
        editMessage.setChatId(message.getChatId().toString());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(BACK_TO_CHOOSE_MIN_PRICE, SKIP)), lan));
        return editMessage;
    }

    public List<SendPhoto> showSortedOptionsSend(Update update, Language lan) {
        InlineKeyboardMarkup markup = null;
        List<List<InlineKeyboardButton>> rowList;
        List<InlineKeyboardButton> row1;
        List<InlineKeyboardButton> row2 = null;
        Message message = getMessage(update);
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
            sendPhoto.setPhoto(new InputFile(home.getAttachments().get(0).getFileId()));
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            Like like = likeService.getLikeByHomeAndUser(home, user);

            InlineKeyboardButton phoneButton =
                    new InlineKeyboardButton(interestService.getVisible(home, user) ?
                            userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
            phoneButton.setCallbackData(PHONE + home.getId().toString());


            InlineKeyboardButton likeButton = new InlineKeyboardButton(home.getLikes()
                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
            likeButton.setCallbackData(LIKE + like.getId().toString());
            row1.add(phoneButton);
            row1.add(KeyboardService.getInlineButton(PHOTOS + home.getId(), getWord(HOME_PHOTOS, lan), home.getDetailsPath()));
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
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);
        InlineKeyboardMarkup markup = KeyboardService.createInlineMarkup(List.of(
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
        Message message = getMessage(update);

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
            sendPhoto.setPhoto(new InputFile(home.getAttachments().get(0).getFileId()));
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            Like like = likeService.getLikeByHomeAndUser(home, user);

            InlineKeyboardButton phoneButton =
                    new InlineKeyboardButton(interestService.getVisible(home, user) ?
                            userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
            phoneButton.setCallbackData(PHONE + home.getId().toString());


            InlineKeyboardButton likeButton = new InlineKeyboardButton(home.getLikes()
                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
            likeButton.setCallbackData(LIKE + like.getId().toString());
            row1.add(phoneButton);
            row1.add(KeyboardService.getInlineButton(PHOTOS + home.getId(), getWord(HOME_PHOTOS, lan), home.getDetailsPath()));
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
        Message message = getMessage(update);

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
            sendPhoto.setPhoto(new InputFile(home.getAttachments().get(0).getFileId()));
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            Like like = likeService.getLikeByHomeAndUser(home, user);

            InlineKeyboardButton phoneButton =
                    new InlineKeyboardButton(interestService.getVisible(home, user) ?
                            userService.getById(home.getUser().getId()).getPhoneNumber() : getWord(GET_PHONE_NUMBER, lan));
            phoneButton.setCallbackData(PHONE + home.getId().toString());

            InlineKeyboardButton likeButton = new InlineKeyboardButton(home.getLikes()
                    + " " + (like.isActive() ? LIKE_ACTIVE : LIKE_NOT_ACTIVE));
            likeButton.setCallbackData(LIKE + like.getId().toString());

            InlineKeyboardButton deleteButton = new InlineKeyboardButton(DELETE);
            deleteButton.setCallbackData(DELETE + home.getId());


            row1.add(phoneButton);
            row1.add(KeyboardService.getInlineButton(PHOTOS + home.getId(), getWord(HOME_PHOTOS, lan), home.getDetailsPath()));
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
        Message message = getMessage(update);
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
        new Thread(() -> emailComponent.sendCode(user.getPhoneNumber(), user.getCode())).start();
        userService.save(user);

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(CHECK_USER_BY_ADMIN_TEXT, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(
                List.of(BACK_TO_MAIN_MENU_EDIT)
        ), lan));
        return sendMessage;
    }

    public SendMessage banUserMenu(Update update, Language lan) {
        Message message = getMessage(update);
        return new SendMessage(message.getChatId().toString(), getWord(BAN_MENU_TEXT, lan));
    }

    private Message getMessage(Update update) {
        return update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
    }

    public SendMediaGroup showHomePhotos(Update update, Language lan) {
        CallbackQuery query = update.getCallbackQuery();
        Home home = homeService.getById(UUID.fromString(query.getData()));
        List<InputMedia> mediaPhotos = home.getAttachments()
                .stream()
                .map(item -> new InputMediaPhoto(item.getFileId())).collect(Collectors.toList());
        if (mediaPhotos.size() != 1) mediaPhotos.get(0).setCaption(getCaptionByHome(home, lan));
        return new SendMediaGroup(query.getMessage().getChatId().toString(), mediaPhotos);
    }

    public SendMessage sendLimitationError(Update update, Language lan) {
        SendMessage sendMessage=new SendMessage(getMessage(update).getChatId().toString(), getWord(LIMITATION_ERROR, lan));
        sendMessage.setReplyMarkup(KeyboardService.createInlineMarkup(List.of(List.of(MENU)), lan));
        return sendMessage;
    }
}
