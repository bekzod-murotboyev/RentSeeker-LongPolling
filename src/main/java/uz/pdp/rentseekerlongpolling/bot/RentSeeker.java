package uz.pdp.rentseekerlongpolling.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.rentseekerlongpolling.payload.LanStateDTO;
import uz.pdp.rentseekerlongpolling.payload.home.HomePageableDTO;
import uz.pdp.rentseekerlongpolling.service.AdminBotService;
import uz.pdp.rentseekerlongpolling.service.BotService;
import uz.pdp.rentseekerlongpolling.service.LanguageService;
import uz.pdp.rentseekerlongpolling.service.UserService;
import uz.pdp.rentseekerlongpolling.util.enums.BotState;
import uz.pdp.rentseekerlongpolling.util.enums.Language;
import uz.pdp.rentseekerlongpolling.util.enums.Role;
import uz.pdp.rentseekerlongpolling.util.security.BaseData;

import java.util.List;

import static uz.pdp.rentseekerlongpolling.util.constant.Constant.CHANGE_LANGUAGE;
import static uz.pdp.rentseekerlongpolling.util.constant.Constant.MY_FAVOURITES;
import static uz.pdp.rentseekerlongpolling.util.constant.Constant.*;
import static uz.pdp.rentseekerlongpolling.util.enums.BotState.*;
import static uz.pdp.rentseekerlongpolling.util.security.BaseData.*;

@Component
@RequiredArgsConstructor
public class RentSeeker extends TelegramLongPollingBot {


    private final UserService userService;

    private final BotService botService;

    private final AdminBotService adminBotService;

    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (!update.hasCallbackQuery() && !update.hasMessage())
            return;
        LanStateDTO data = userService.getAndCheck(update);
        if (!data.isActive()) {
            execute(botService.banUserMenu(update, data.getLanguage()));
            return;
        }


        BotState state = data.getState();
        Language lan = data.getLanguage();
        Role role = data.getRole();
        boolean isAdmin = data.isAdmin();

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(START))
            isAdmin = false;
        if (isAdmin) {
            onUpdateReceivedForAdmin(update, data);
            return;
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals(START)) {
                    if (!state.equals(BotState.CHOOSE_LANGUAGE)) {
                        state = role.equals(Role.ADMIN) ? BotState.ADMIN_MENU_SEND : BotState.MAIN_MENU_SEND;
                        execute(botService.removeKeyBoardMarkup(update));
                    }
                } else if (text.equals(TOKEN))
                    state = BotState.CHECK_USER_BY_ADMIN;
                else if (state.equals(BotState.CHECK_USER_BY_ADMIN) && botService.checkByCode(update)) {
                    state = BotState.ADMIN_MENU_SEND;
                    role = Role.ADMIN;
                } else if (state.equals(BotState.REGISTER))
                    state = BotState.SETTINGS_MENU_SEND;
                else if (state.equals(BotState.LOCATION_MENU))
                    state = BotState.WRITE_OR_SEND_LOCATION;
                else if (state.equals(BotState.GIVE_ADDRESS))
                    state = BotState.GIVE_HOME_TYPE_SEND;
                else if (state.equals(BotState.GIVE_HOME_NUMBER))
                    state = BotState.GIVE_HOME_AREA_SEND;
                else if (state.equals(BotState.GIVE_HOME_AREA_SEND) || state.equals(BotState.GIVE_HOME_AREA_EDIT))
                    state = BotState.GIVE_HOME_PHOTO_SEND;
                else if (state.equals(BotState.GIVE_HOME_PRICE_EDIT) || state.equals(GIVE_HOME_PRICE_SEND))
                    state = BotState.GIVE_HOME_DESCRIPTION;
                else if (state.equals(BotState.GIVE_HOME_DESCRIPTION))
                    state = BotState.SAVE_HOME_TO_STORE;
                else if (state.equals(BotState.CHOOSE_HOME_NUMBER_EDIT) || state.equals(BotState.CHOOSE_HOME_NUMBER_SEND))
                    state = BotState.CHOOSE_HOME_MIN_PRICE_SEND;
                else if (state.equals(BotState.CHOOSE_HOME_MIN_PRICE_SEND) || state.equals(BotState.CHOOSE_HOME_MIN_PRICE_EDIT))
                    state = BotState.CHOOSE_HOME_MAX_PRICE_SEND;
                else if (state.equals(BotState.CHOOSE_HOME_MAX_PRICE_SEND) || state.equals(BotState.CHOOSE_HOME_MAX_PRICE_EDIT))
                    state = BotState.SHOW_SORTED_OPTIONS;
                else if (state.equals(GIVE_HOME_PHOTO_SEND_AGAIN) && text.equals(LanguageService.getWord(BACK, lan)))
                    state = GIVE_HOME_PHOTO_SEND_SIMPLE;
                else if (state.equals(GIVE_HOME_PHOTO_SEND_AGAIN) && text.equals(LanguageService.getWord(NEXT_ACTION, lan)))
                    state = GIVE_HOME_PRICE_SEND;
                else {
                    execute(botService.setError(update));
                    return;
                }
            } else if (message.hasContact() && state.equals(BotState.REGISTER))
                state = BotState.SETTINGS_MENU_SEND;
            else if (message.hasLocation() && state.equals(BotState.LOCATION_MENU))
                state = BotState.GIVE_HOME_TYPE_SEND;
            else if (message.hasPhoto() &&
                    (state.equals(BotState.GIVE_HOME_PHOTO_SEND) ||
                            state.equals(GIVE_HOME_PHOTO_SEND_SIMPLE) ||
                            state.equals(BotState.GIVE_HOME_PHOTO_EDIT) ||
                            state.equals(BotState.GIVE_HOME_PHOTO_SEND_AGAIN)))
                state = GIVE_HOME_PHOTO_SEND_AGAIN;
            else {
                execute(botService.setError(update));
                return;
            }
        } else if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case UZ: {
                    lan = Language.UZ;
                    state = state.equals(BotState.CHOOSE_LANGUAGE) ? BotState.MAIN_MENU_EDIT : BotState.SETTINGS_MENU_EDIT;
                }
                break;
                case RU: {
                    lan = Language.RU;
                    state = state.equals(BotState.CHOOSE_LANGUAGE) ? BotState.MAIN_MENU_EDIT : BotState.SETTINGS_MENU_EDIT;
                }
                break;
                case EN: {
                    lan = Language.EN;
                    state = state.equals(BotState.CHOOSE_LANGUAGE) ? BotState.MAIN_MENU_EDIT : BotState.SETTINGS_MENU_EDIT;
                }
                break;
                case ADMIN:
                    state = BotState.ADMIN_PANEL;
                    break;
                case SETTINGS:
                case BACK_TO_SETTINGS_MENU_EDIT:
                    state = BotState.SETTINGS_MENU_EDIT;
                    break;
                case BACK_TO_ADMIN_MENU:
                    state = BotState.ADMIN_MENU_EDIT;
                    break;
                case BACK_TO_SETTINGS_MENU_SEND:
                    state = BotState.SETTINGS_MENU_SEND;
                    break;
                case BACK_TO_MAIN_MENU_SEND:
                    state = BotState.MAIN_MENU_SEND;
                    break;
                case BACK_TO_MAIN_MENU_EDIT:
                case USER:
                case MENU:
                    state = BotState.MAIN_MENU_EDIT;
                    break;
                case CHANGE_LANGUAGE:
                    state = BotState.CHANGE_LANGUAGE;
                    break;
                case REGISTRATION:
                    state = BotState.REGISTER;
                    break;
                case ADD_ACCOMMODATION:
                case BACK_TO_GIVE_HOME_STATUS:
                    state = BotState.GIVE_HOME_STATUS;
                    break;
                case BACK_TO_WRITE_SEND_LOCATION:
                case FOR_RENTING:
                case FOR_SELLING:
                    state = BotState.WRITE_OR_SEND_LOCATION;
                    break;
                case BACK_SEND_LOCATION:
                case SEND_LOCATION:
                    state = BotState.LOCATION_MENU;
                    break;
                case BACK_TO_GIVE_REGION:
                case WRITE_ADDRESS:
                    state = BotState.GIVE_REGION;
                    break;
                case BACK_TO_GIVE_DISTRICT:
                    state = BotState.GIVE_DISTRICT;
                    break;
                case BACK_TO_GIVE_ADDRESS:
                    state = BotState.GIVE_ADDRESS;
                    break;
                case BACK_TO_GIVE_HOME_TYPE:
                    state = BotState.GIVE_HOME_TYPE_EDIT;
                    break;
                case BACK_TO_GIVE_HOME_NUMBER:
                    state = BotState.GIVE_HOME_NUMBER;
                    break;
                case BACK_TO_GIVE_HOME_AREA:
                    state = BotState.GIVE_HOME_AREA_EDIT;
                    break;
                case BACK_TO_GIVE_HOME_PHOTO:
                    state = BotState.GIVE_HOME_PHOTO_EDIT;
                    break;
                case NEXT_ACTION:
                case BACK_TO_GIVE_HOME_PRICE:
                    state = BotState.GIVE_HOME_PRICE_EDIT;
                    break;
                case BACK_TO_SHOW_MENU_EDIT:
                case SHOW_ACCOMMODATIONS:
                    state = BotState.SHOW_MENU_EDIT;
                    break;
                case BACK_TO_SHOW_MENU_SEND:
                    state = BotState.SHOW_MENU_SEND;
                    break;
                case SHOW_ALL:
                case PREV:
                case NEXT:
                    state = BotState.SHOW_OPTIONS;
                    break;
                case BACK_TO_CHOOSE_REGION:
                case SEARCH:
                    state = BotState.CHOOSE_REGION;
                    break;
                case BACK_TO_CHOOSE_DISTRICT:
                    state = BotState.CHOOSE_DISTRICT;
                    break;
                case BACK_TO_CHOOSE_HOME_STATUS:
                    state = BotState.CHOOSE_HOME_STATUS;
                    break;
                case BACK_TO_CHOOSE_HOME_TYPE:
                case GET_RENTING:
                case FOR_BUY:
                    state = BotState.CHOOSE_HOME_TYPE;
                    break;
                case BACK_TO_CHOOSE_HOME_NUMBER:
                    state = BotState.CHOOSE_HOME_NUMBER_EDIT;
                    break;
                case BACK_TO_CHOOSE_MIN_PRICE:
                    state = BotState.CHOOSE_HOME_MIN_PRICE_EDIT;
                    break;
                case BACK_TO_CHOOSE_MAX_PRICE_SEND:
                    state = BotState.CHOOSE_HOME_MAX_PRICE_SEND;
                    break;
                case BACK_TO_CHOOSE_MAX_PRICE_EDIT:
                    state = BotState.CHOOSE_HOME_MAX_PRICE_EDIT;
                    break;
                case BACK_TO_MY_NOTES_MENU_EDIT:
                case MY_NOTES:
                    state = BotState.MY_NOTES_MENU_EDIT;
                    break;
                case BACK_TO_MY_NOTES_MENU_SEND:
                    state = BotState.MY_NOTES_MENU_SEND;
                    break;
                case MY_FAVOURITES:
                    state = BotState.MY_FAVOURITES;
                    break;
                case MY_ACCOMMODATIONS:
                    state = BotState.MY_HOMES;
                    break;
                case SKIP:
                    state = botService.getStateBySkip(update, state);
                    break;
                default:
                    state = botService.getState(update, state);
            }
        }


        switch (state) {
            case CHOOSE_LANGUAGE:
                execute(botService.chooseLanguage(update, lan));
                break;
            case CHECK_USER_BY_ADMIN:
                execute(botService.checkUserMenu(update, lan));
                break;
            case ADMIN_MENU_SEND: {
                execute(botService.getAdminMenuSend(update, lan));
                botService.changeIsAdminUser(update, false);
            }
            break;
            case ADMIN_MENU_EDIT: {
                execute(botService.getAdminMenuEdit(update, lan));
                botService.changeIsAdminUser(update, false);
            }
            break;
            case ADMIN_PANEL: {
                if (role.equals(Role.ADMIN)) {
                    onUpdateReceivedForAdmin(update, data);
                    isAdmin = true;
                }
            }
            break;
            case CHANGE_LANGUAGE:
                execute(botService.changeLanguage(update, lan));
                break;
            case MAIN_MENU_EDIT:
                execute(botService.setMenuEdit(update, lan, role));
                break;
            case MAIN_MENU_SEND:
                execute(botService.setMenuSend(update, lan, role));
                break;
            case SETTINGS_MENU_EDIT:
                execute(botService.getSettingMenuEdit(update, lan));
                break;
            case SETTINGS_MENU_SEND: {
                if (botService.saveContact(update, lan)) {
                    execute(botService.removeKeyBoardMarkup(update));
                    execute(botService.getSettingMenuSend(update, lan));
                } else {
                    state = BotState.REGISTER;
                    execute(botService.getRegister(update, lan));
                }
            }
            break;
            case REGISTER: {
                execute(botService.deleteMessage(update));
                execute(botService.getRegister(update, lan));
            }
            break;
            case GIVE_HOME_STATUS: {
                if (botService.checkByPhone(update))
                    execute(botService.setHomeStatusMenu(update, lan));
                else
                    execute(botService.setWarningRegister(update, lan));
            }
            break;
            case WRITE_OR_SEND_LOCATION: {
                if (update.hasCallbackQuery())
                    execute(botService.setWriteOrSendLocationMenuEdit(update, lan));
                else if (update.hasMessage()) {
                    if (update.getMessage().getText().equals(LanguageService.getWord(BACK, lan))) {
                        execute(botService.removeKeyBoardMarkup(update));
                        execute(botService.setWriteOrSendLocationMenuSend(update, lan));
                    } else {
                        execute(botService.getMenuLocation(update, lan));
                        state = BotState.LOCATION_MENU;
                    }

                }
            }
            break;
            case LOCATION_MENU: {
                execute(botService.deleteMessage(update));
                execute(botService.getMenuLocation(update, lan));
            }
            break;
            case GIVE_REGION:
                execute(botService.giveRegionMenu(update, lan));
                break;
            case GIVE_DISTRICT:
                execute(botService.giveDistrictMenu(update, lan, state));
                break;
            case GIVE_ADDRESS:
                execute(botService.giveAddressMenu(update, lan, state));
                break;
            case GIVE_HOME_TYPE_SEND: {
                if (botService.checkLocation(update)) {
                    execute(botService.removeKeyBoardMarkup(update));
                    execute(botService.giveHomeTypeMenuSend(update, lan, state));
                } else {
                    execute(botService.LocationNotFound(update, lan));
                    execute(botService.getMenuLocation(update, lan));
                    state = BotState.LOCATION_MENU;
                }
            }
            break;
            case GIVE_HOME_TYPE_EDIT:
                execute(botService.giveHomeTypeMenuEdit(update, lan));
                break;
            case GIVE_HOME_NUMBER:
                execute(botService.giveHomeNumberMenu(update, lan, state));
                break;
            case GIVE_HOME_AREA_SEND: {
                if (botService.saveNumberOfRoom(update, state))
                    execute(botService.giveHomeAreaMenuSend(update, lan));
                else {
                    execute(botService.giveHomeNumberMenuSend(update, lan, state));
                    state = BotState.GIVE_HOME_NUMBER;
                }
            }
            break;
            case GIVE_HOME_AREA_EDIT:
                execute(botService.giveHomeAreaMenuEdit(update, lan));
                break;
            case GIVE_HOME_PHOTO_SEND: {
                if (botService.saveHomeArea(update, state))
                    execute(botService.giveHomePhotoMenuSend(update, lan));
                else {
                    execute(botService.giveHomeAreaMenuSend(update, lan));
                    state = BotState.GIVE_HOME_AREA_SEND;
                }
            }
            break;
            case GIVE_HOME_PHOTO_SEND_SIMPLE: {
                execute(botService.removeKeyBoardMarkup(update));
                execute(botService.giveHomePhotoMenuSendSimple(update, lan));
            }
            break;
            case GIVE_HOME_PHOTO_EDIT:
                execute(botService.giveHomePhotoMenuEdit(update, lan));
                break;
            case GIVE_HOME_PHOTO_SEND_AGAIN:
                execute(botService.giveHomePhotoMenuSendAgain(update, lan, state));
                break;
            case GIVE_HOME_PRICE_EDIT:
                execute(botService.giveHomePriceMenuEdit(update, lan));
                break;
            case GIVE_HOME_PRICE_SEND:
                execute(botService.removeKeyBoardMarkup(update));
                execute(botService.giveHomePriceMenuSend(update, lan));
                break;
            case GIVE_HOME_DESCRIPTION: {
                if (botService.saveHomePrice(update, state))
                    execute(botService.giveHomeDescription(update, lan));
                else {
                    execute(botService.giveHomePriceMenuSend(update, lan, state));
                    state = GIVE_HOME_PRICE_SEND;
                }
            }
            break;
            case SAVE_HOME_TO_STORE: {
                if (botService.saveHomeDescription(update, state)) {
                    execute(botService.successfullySaved(update, lan));
                    execute(botService.setMenuSend(update, lan, role));
                } else
                    execute(botService.sendLimitationError(update, lan));
            }
            break;
            case SHOW_MENU_SEND:
                execute(botService.getShowMenuSend(update, lan));
                break;
            case SHOW_MENU_EDIT:
                execute(botService.getShowMenuEdit(update, lan));
                break;
            case SHOW_OPTIONS: {
                HomePageableDTO homePageableDTO = botService.showAllHomes(update, lan);
                if (homePageableDTO.getSendPhotos() == null) {
                    if (homePageableDTO.getMessage() == null)
                        execute(botService.homeNotFound(update, lan));
                    else
                        execute(botService.getAnswerCallbackQuery(update.getCallbackQuery(),
                                homePageableDTO.getMessage(), lan));
                    return;
                }
                if (update.getCallbackQuery().getData().equals(SHOW_ALL))
                    execute(botService.deleteMessage(update));
                for (SendPhoto sendPhoto : homePageableDTO.getSendPhotos())
                    execute(sendPhoto);
            }
            break;
            case SHOW_HOME_PHONE_MENU_ALL: {
                execute(botService.changeVisibleHomePhone(update, lan, BACK_TO_SHOW_MENU_SEND, state));
                state = BotState.SHOW_OPTIONS;
            }
            break;
            case SHOW_HOME_PHONE_MENU_FAVOURITES: {
                execute(botService.changeVisibleHomePhone(update, lan, BACK_TO_MY_NOTES_MENU_SEND, state));
                state = BotState.MY_FAVOURITES;
            }
            break;
            case SHOW_HOME_PHONE_MENU_MY_ACCOMMODATIONS: {
                execute(botService.changeVisibleHomePhone(update, lan, BACK_TO_MY_NOTES_MENU_SEND, state));
                state = BotState.MY_HOMES;
            }
            break;
            case SHOW_HOME_PHONE_MENU_SEARCH: {
                execute(botService.changeVisibleHomePhone(update, lan, BACK_TO_CHOOSE_MAX_PRICE_SEND, state));
                state = BotState.SHOW_SORTED_OPTIONS;
            }
            break;
            case CHANGE_HOME_LIKE_MENU_ALL: {
                execute(botService.changeHomeLike(update, lan, BACK_TO_SHOW_MENU_SEND, state));
                state = BotState.SHOW_OPTIONS;
            }
            break;
            case CHANGE_HOME_LIKE_MENU_FAVOURITES: {
                execute(botService.changeHomeLike(update, lan, BACK_TO_MY_NOTES_MENU_SEND, state));
                state = BotState.MY_FAVOURITES;
            }
            break;
            case CHANGE_HOME_LIKE_MENU_MY_ACCOMMODATIONS: {
                execute(botService.changeHomeLike(update, lan, BACK_TO_MY_NOTES_MENU_SEND, state));
                state = BotState.MY_HOMES;
            }
            break;
            case CHANGE_HOME_LIKE_MENU_SEARCH: {
                execute(botService.changeHomeLike(update, lan, BACK_TO_CHOOSE_MAX_PRICE_SEND, state));
                state = BotState.SHOW_SORTED_OPTIONS;
            }
            break;
            case CHOOSE_REGION:
                execute(botService.chooseRegionMenu(update, lan));
                break;
            case CHOOSE_DISTRICT:
                execute(botService.chooseDistrict(update, lan));
                break;
            case CHOOSE_HOME_STATUS:
                execute(botService.chooseHomeStatus(update, lan));
                break;
            case CHOOSE_HOME_TYPE:
                execute(botService.chooseHomeType(update, lan));
                break;
            case CHOOSE_HOME_NUMBER_EDIT:
                execute(botService.chooseHomeNumberEdit(update, lan));
                break;
            case CHOOSE_HOME_MIN_PRICE_SEND: {
                if (botService.saveSearchNumber(update)) {
                    execute(botService.chooseMinPriceMenuSend(update, lan));
                } else {
                    execute(botService.chooseHomeNumberSend(update, lan));
                    state = BotState.CHOOSE_HOME_NUMBER_SEND;
                }
            }
            break;
            case CHOOSE_HOME_MIN_PRICE_EDIT:
                execute(botService.chooseMinPriceMenuEdit(update, lan));
                break;
            case CHOOSE_HOME_MAX_PRICE_SEND: {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    if (botService.saveSearchMinPrice(update)) {
                        execute(botService.chooseMaxPriceMenuSend(update, lan));
                    } else {
                        execute(botService.chooseMinPriceMenuSend(update, lan));
                        state = BotState.CHOOSE_HOME_MIN_PRICE_SEND;
                    }
                } else
                    execute(botService.chooseMaxPriceMenuSend(update, lan));
            }
            break;
            case CHOOSE_HOME_MAX_PRICE_EDIT:
                execute(botService.chooseMaxPriceMenuEdit(update, lan));
                break;
            case SHOW_SORTED_OPTIONS: {
                if (botService.saveSearchMaxPrice(update)) {
                    execute(botService.deleteMessage(update));
                    List<SendPhoto> sendPhotos = botService.showSortedOptionsSend(update, lan);
                    if (sendPhotos == null) {
                        execute(botService.searchedHomeNotFound(update, lan));
                        return;
                    }
                    for (SendPhoto sendPhoto : sendPhotos)
                        execute(sendPhoto);
                } else {
                    execute(botService.chooseMaxPriceMenuSend(update, lan));
                    state = BotState.CHOOSE_HOME_MAX_PRICE_SEND;
                }
            }
            break;
            case MY_NOTES_MENU_EDIT:
                execute(botService.getMyNotesMenuEdit(update, lan));
                break;
            case MY_NOTES_MENU_SEND:
                execute(botService.getMyNotesMenuSend(update, lan));
                break;
            case MY_FAVOURITES: {
                List<SendPhoto> sendPhotos = botService.getMyFavouriteHomes(update, lan);
                if (sendPhotos == null) {
                    execute(botService.homeNotFound(update, lan));
                    return;
                }
                execute(botService.deleteMessage(update));
                for (SendPhoto sendPhoto : sendPhotos)
                    execute(sendPhoto);
            }
            break;
            case MY_HOMES: {
                List<SendPhoto> sendPhotos = botService.getMyHomes(update, lan);
                if (sendPhotos == null) {
                    execute(botService.homeNotFound(update, lan));
                    return;
                }
                execute(botService.deleteMessage(update));
                for (SendPhoto sendPhoto : sendPhotos)
                    execute(sendPhoto);
            }
            break;
            case DELETE_ACCOMMODATION:
                execute(botService.deleteAccommodation(update));
                break;
            case SHOW_HOME_PHOTOS: {
                SendMediaGroup sendMediaGroup = botService.showHomePhotos(update, lan);
                if (sendMediaGroup.getMedias().size() != 1)
                    execute(sendMediaGroup);
                else
                    execute(botService.itHasOnlyOnePic(update, lan));
            }
            break;
            default:
                execute(botService.setError(update));
        }
        userService.saveStateAndLan(update, new LanStateDTO(lan, state, role, isAdmin, data.isActive()));
    }


    @SneakyThrows
    public void onUpdateReceivedForAdmin(Update update, LanStateDTO data) {
        BotState state = data.getState();
        Language lan = data.getLanguage();
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals(BaseData.TOKEN)) {
                    state = ADMIN_MAIN_MENU_SEND;
                } else if (state == ADMIN_USER_ENTER_PHONE_NUMBER_EDIT || state == ADMIN_USER_ENTER_PHONE_NUMBER_SEND) {
                    if (userService.checkByPhoneNumber(text)) {
                        state = ADMIN_SEARCH_USER_INFO_SEND;
                    } else {
                        state = ADMIN_USER_ENTER_PHONE_NUMBER_SEND;
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {

            switch (update.getCallbackQuery().getData()) {
                case ADMIN:
                case BACK_TO_ADMIN_MAIN_MENU:
                    state = ADMIN_MAIN_MENU_EDIT;
                    break;
                case ADMIN_SHOW_USERS:
                case BACK_TO_ADMIN_USERS_SHOW:
                    state = ADMIN_USERS_SHOW;
                    break;
                case BACK_TO_ADMIN_CHOOSE_USER_TYPE:
                    state = ADMIN_CHOOSE_USER_TYPE_SEND;
                    break;
                case ADMIN_EXCEL_FILE:
                    state = ADMIN_CHOOSE_USER_TYPE_EDIT;
                    break;
                case ADMIN_ACTIVE_USERS:
                    state = ADMIN_GET_ACTIVE_EXCEL_FILE;
                    break;
                case ADMIN_DEACTIVATED_USERS:
                    state = ADMIN_GET_DEACTIVATED_EXCEL_FILE;
                    break;
                case SEARCH:
                    state = ADMIN_USER_ENTER_PHONE_NUMBER_EDIT;
                    break;
                case BACK_TO_ADMIN_HOMES_FILTER_SEND:
                    state = ADMIN_HOMES_FILTER_SEND;
                    break;
                case BACK_TO_ADMIN_HOMES_FILTER_EDIT:
                case ADMIN_SHOW_HOMES:
                    state = ADMIN_HOMES_FILTER_EDIT;
                    break;
                case ADMIN_HOMES_IN_WEEK:
                    state = ADMIN_WEEK_HOMES_INFO;
                    break;
                case ADMIN_HOMES_IN_DAY:
                    state = ADMIN_DAY_HOMES_INFO;
                    break;
                case BACK_TO_ADMIN_MENU:
                    state = ADMIN_MENU_EDIT;
                    break;
                default:
                    state = adminBotService.getState(update, state);
            }
        }
        switch (state) {
            case ADMIN_MENU_EDIT: {
                execute(botService.getAdminMenuEdit(update, lan));
                data.setAdmin(false);
            }
            break;
            case ADMIN_MAIN_MENU_SEND:
                execute(adminBotService.setAdminMenuSend(update, lan));
                break;
            case ADMIN_MAIN_MENU_EDIT:
                execute(adminBotService.setAdminMenuEdit(update, lan));
                break;
            case ADMIN_USERS_SHOW:
                execute(adminBotService.setAdminShowUsersEdit(update, lan));
                break;
            case ADMIN_CHOOSE_USER_TYPE_SEND:
                execute(adminBotService.setAdminChooseUsersSend(update, lan));
                break;
            case ADMIN_CHOOSE_USER_TYPE_EDIT:
                execute(adminBotService.setAdminChooseUsersEdit(update, lan));
                break;
            case ADMIN_USER_ENTER_PHONE_NUMBER_EDIT:
                execute(adminBotService.setAdminUserEnterPhoneEdit(update, lan));
                break;
            case ADMIN_USER_ENTER_PHONE_NUMBER_SEND:
                execute(adminBotService.setAdminUserEnterPhoneNumberSend(update, lan));
                break;
            case ADMIN_HOMES_FILTER_SEND:
                execute(adminBotService.setAdminHomeFilterSend(update, lan));
                break;
            case ADMIN_HOMES_FILTER_EDIT:
                execute(adminBotService.setAdminHomeFilterEdit(update, lan));
                break;
            case ADMIN_SEARCH_USER_INFO_SEND:
                execute(adminBotService.sendAdminUserInfo(update, lan));
                break;
            case ADMIN_SEARCH_USER_INFO_EDIT:
                execute(adminBotService.editAdminUserInfo(update, lan));
                break;
            case ADMIN_DAY_HOMES_INFO: {
                showHomes(update, lan, ADMIN_HOMES_IN_DAY);
                state = ADMIN_HOMES_INFO;
            }
            break;
            case ADMIN_WEEK_HOMES_INFO: {
                showHomes(update, lan, ADMIN_HOMES_IN_WEEK);
                state = ADMIN_HOMES_INFO;
            }
            break;
            case ADMIN_HOMES_INFO: {
                execute(adminBotService.sendAdminDeleteHome(update, lan));
                break;
            }
            case SHOW_HOME_PHOTOS: {
                SendMediaGroup sendMediaGroup = botService.showHomePhotos(update, lan);
                if (sendMediaGroup.getMedias().size() != 1)
                    execute(sendMediaGroup);
                else
                    execute(botService.itHasOnlyOnePic(update, lan));
            }
            break;
            case ERROR:
            default:
                execute(botService.setError(update));
        }
        userService.saveStateAndLan(update, new LanStateDTO(lan, state, data.getRole(), data.isAdmin(), data.isActive()));
    }

    private void showHomes(Update update, Language lan, String searchType) throws TelegramApiException {
        List<SendPhoto> sendPhotos = adminBotService.showHomes(update, lan, searchType);
        if (sendPhotos == null) {
            execute(botService.homeNotFound(update, lan));
            return;
        }
        for (SendPhoto sendPhoto : sendPhotos)
            execute(sendPhoto);

    }


    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
