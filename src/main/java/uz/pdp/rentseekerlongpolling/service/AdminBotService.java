package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.util.constant.Constant;
import uz.pdp.rentseekerlongpolling.util.enums.HomeStatus;
import uz.pdp.rentseekerlongpolling.util.enums.HomeType;
import uz.pdp.rentseekerlongpolling.util.enums.Language;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.pdp.rentseekerlongpolling.util.Url.*;

@Service
@RequiredArgsConstructor
public class AdminBotService extends LanguageService implements Constant {


    private final UserService userService;

    private final HomeService homeService;

    public EditMessageText setAdminMenuEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_SHOW_USERS, ADMIN_SHOW_HOMES),
                List.of(BACK_TO_ADMIN_MENU)
        ), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ADMIN_USERS_SHOW_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage setAdminMenuSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_SHOW_USERS, ADMIN_SHOW_HOMES)), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ADMIN_USERS_SHOW_MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText setAdminShowUsersEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_EXCEL_FILE, SEARCH),
                List.of(BACK_TO_ADMIN_MAIN_MENU)), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ADMIN_USERS_SHOW_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage setAdminShowUsersSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_EXCEL_FILE, SEARCH),
                List.of(BACK_TO_ADMIN_MAIN_MENU)), lan);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText setAdminChooseUsersEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup=new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList=new ArrayList<>();
        markup.setKeyboard(rowList);
        List<InlineKeyboardButton> row1=new ArrayList<>();
        List<InlineKeyboardButton> row2=new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        InlineKeyboardButton activeButton=new InlineKeyboardButton(getWord(ADMIN_ACTIVE_USERS,lan));
        activeButton.setCallbackData(ADMIN_ACTIVE_USERS);
        activeButton.setUrl(GLOBAL+BASE_USER+USER_ACTIVE);
        InlineKeyboardButton inactiveButton=new InlineKeyboardButton(getWord(ADMIN_DEACTIVATED_USERS,lan));
        inactiveButton.setCallbackData(ADMIN_DEACTIVATED_USERS);
        inactiveButton.setUrl(GLOBAL+BASE_USER+USER_INACTIVE);
        InlineKeyboardButton backButton=new InlineKeyboardButton(getWord(BACK,lan));
        backButton.setCallbackData(BACK_TO_ADMIN_USERS_SHOW);
        row1.add(activeButton);
        row1.add(inactiveButton);
        row2.add(backButton);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ADMIN_USERS_SHOW_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage setAdminChooseUsersSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_ACTIVE_USERS, ADMIN_DEACTIVATED_USERS),
                List.of(BACK_TO_ADMIN_USERS_SHOW)), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ADMIN_CHOOSE_USERS_MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public EditMessageText setAdminUserEnterPhoneEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ADMIN_ENTER_PHONE_NUMBER_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton back = new InlineKeyboardButton(getWord(BACK, lan));
        back.setCallbackData(BACK_TO_ADMIN_USERS_SHOW);
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(back)));
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }

    public SendMessage setAdminUserEnterPhoneNumberSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ADMIN_ENTER_PHONE_NUMBER_MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        return sendMessage;
    }

    public EditMessageText setAdminHomeFilterEdit(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_HOMES_IN_WEEK, ADMIN_HOMES_IN_DAY),
                List.of(BACK_TO_ADMIN_MAIN_MENU)), lan);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(getWord(ADMIN_HOMES_FILTER_MENU_TEXT, lan));
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public SendMessage setAdminHomeFilterSend(Update update, Language lan) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(
                List.of(ADMIN_HOMES_IN_WEEK, ADMIN_HOMES_IN_DAY),
                List.of(BACK_TO_ADMIN_MAIN_MENU)), lan);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), getWord(ADMIN_HOMES_FILTER_MENU_TEXT, lan));
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    private String getCaptionByHome(Home home, Language lan) {
        return getWord(HOUSE_TYPE, lan) + "\t" + getWord(home.getHomeType().equals(HomeType.HOUSE) ? HOUSE : FLAT, lan) + "\t\t\t\t\t\t\t\t\t\t" +
                getWord(ADMIN_HOMES_INFO_NUMBER_OF_INTERESTED, lan) + home.getInterests() + "\n" +
                getWord(STATUS, lan) + "\t" + getWord(home.getStatus().equals(HomeStatus.SELL) ? SELL : RENT, lan) + "\n" +
                getWord(ADMIN_HOMES_INFO_REGION, lan) + home.getRegion() + "\n" +
                getWord(ADMIN_HOMES_INFO_DISTRICT, lan) + (home.getDistrict() != null ? home.getDistrict() : " ") + "\n" +
                getWord(ADDRESS, lan) + home.getAddress() + "\n" +
                getWord(NUMBER_OF_ROOMS, lan) + home.getNumberOfRooms() + "\n" +
                getWord(AREA, lan) + home.getArea() + " m²" + "\n" +
                getWord(DATE, lan) + DateTimeFormatter.ofPattern("dd/MM/yyyy").format((home.getCreatedDate())) + "\n" +
                getWord(ADMIN_HOMES_INFO_PRICE, lan) + home.getPrice() + " $" + "\n" +
                getWord(ADMIN_USER_INFO_PHONE_NUMBER, lan) + homeService.getHomeOwnerPhoneNumber(home.getUser().getId()) + "\n" +
                getWord(DESCRIPTION, lan) + home.getDescription();
    }


    public List<SendPhoto> showHomes(Update update, Language lan, String searchType) {
        InlineKeyboardMarkup markup = null;
        List<List<InlineKeyboardButton>> rowList;
        List<InlineKeyboardButton> row1;
        List<InlineKeyboardButton> row2 = null;
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        List<Home> allHome;
        if (searchType.equals(ADMIN_HOMES_IN_WEEK)) {
            allHome = homeService.getWeekHomes();
        } else
            allHome = homeService.getDayHomes();

        if (allHome.isEmpty())
            return null;

        List<SendPhoto> sendMessageList = new ArrayList<>();
        SendPhoto sendPhoto = null;

        for (Home home : allHome) {
            if (sendPhoto != null)
                sendMessageList.add(sendPhoto);
            markup = new InlineKeyboardMarkup();
            rowList = new ArrayList<>();
            markup.setKeyboard(rowList);
            row1 = new ArrayList<>();
            row2 = new ArrayList<>();
            rowList.add(row1);
            rowList.add(row2);

            sendPhoto = new SendPhoto(message.getChatId().toString(),new InputFile(home.getFileId()));
            sendPhoto.setCaption(getCaptionByHome(home, lan));

            InlineKeyboardButton banButton = new InlineKeyboardButton(DELETE);
            banButton.setCallbackData(home.getId().toString());
            row1.add(banButton);
            sendPhoto.setReplyMarkup(markup);
        }

        InlineKeyboardButton back = new InlineKeyboardButton(getWord(BACK, lan));
        back.setCallbackData(BACK_TO_ADMIN_HOMES_FILTER_SEND);
        row2.add(0, back);
        sendPhoto.setReplyMarkup(markup);
        sendMessageList.add(sendPhoto);
        return sendMessageList;
    }

    public SendMessage sendAdminUserInfo(Update update, Language lan) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        User user = userService.getSearchUser(message.getText());
        sendMessage.setText(getCaptionByUser(user, lan));

        InlineKeyboardButton buttonBan = new InlineKeyboardButton(!user.isActive() ? BAN : REMOVE_BAN);
        buttonBan.setCallbackData(user.getPhoneNumber());
        InlineKeyboardButton buttonBack = new InlineKeyboardButton(getWord(BACK, lan));
        buttonBack.setCallbackData(BACK_TO_ADMIN_USERS_SHOW);
        List<List<InlineKeyboardButton>> lines = List.of(List.of(buttonBan), List.of(buttonBack));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(lines);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public String getCaptionByUser(User user, Language lan) {
        return getWord(ADMIN_USER_INFO_NAME, lan) + " :    " + user.getName() + "\n" +
                getWord(ADMIN_USER_INFO_USERNAME, lan) + " :    " + user.getUsername() + "\n" +
                getWord(ADMIN_USER_INFO_PHONE_NUMBER, lan) + " :    " + user.getPhoneNumber() + "\n" +
                getWord(ADMIN_USER_INFO_LANGUAGE, lan) + " :    " + user.getLanguage() + "\n" +
                getWord(ROLE, lan) + " :    " + user.getRole() + "\n" +
                getWord(SITUATION, lan) + "     " + (user.isActive() ? getWord(ACTIVE, lan) : getWord(DE_ACTIVE, lan));
    }

    public EditMessageText editAdminUserInfo(Update update, Language lan) {

        Message message = update.getCallbackQuery().getMessage();
        String text = update.getCallbackQuery().getData();
        EditMessageText editMessage = new EditMessageText();
        editMessage.setMessageId(message.getMessageId());
        editMessage.setChatId(message.getChatId().toString());
        User user = userService.banOrUnbanUser(text);
        editMessage.setText(getCaptionByUser(user, lan));

        InlineKeyboardButton buttonBan = new InlineKeyboardButton(!user.isActive() ? BAN : REMOVE_BAN);
        buttonBan.setCallbackData(user.getPhoneNumber());
        InlineKeyboardButton buttonBack = new InlineKeyboardButton(getWord(BACK, lan));
        buttonBack.setCallbackData(BACK_TO_ADMIN_USERS_SHOW);
        List<List<InlineKeyboardButton>> lines = List.of(List.of(buttonBan), List.of(buttonBack));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(lines);

        editMessage.setReplyMarkup(inlineKeyboardMarkup);
        return editMessage;
    }

    public DeleteMessage sendAdminDeleteHome(Update update, Language lan) {
        Message message = update.getCallbackQuery().getMessage();
        DeleteMessage deleteMessage = new DeleteMessage();
        String homeId = update.getCallbackQuery().getData();
        homeService.deleteHome(UUID.fromString(homeId));
        deleteMessage.setMessageId(message.getMessageId());
        deleteMessage.setChatId(message.getChatId().toString());
        return deleteMessage;
    }


}
