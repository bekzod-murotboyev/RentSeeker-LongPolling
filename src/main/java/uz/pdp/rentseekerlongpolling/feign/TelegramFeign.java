package uz.pdp.rentseekerlongpolling.feign;


import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import uz.pdp.rentseekerlongpolling.payload.telegram.HandleSendPhoto;
import uz.pdp.rentseekerlongpolling.payload.telegram.simple_telegram.FileDataDTO;
import uz.pdp.rentseekerlongpolling.payload.telegram.telegramFeign.Result;
import uz.pdp.rentseekerlongpolling.payload.telegram.telegramFeign.ResultDelete;

import static uz.pdp.rentseekerlongpolling.util.Url.*;

@FeignClient(url = TELEGRAM_BASE, name = "TelegramFeign")
public interface TelegramFeign {

    @GetMapping(TELEGRAM_GET_FILE_PATH)
    FileDataDTO getFilePath(@RequestParam(name = "file_id") String fileId);

    @GetMapping(TELEGRAM_GET_FILE + "{file_path}")
    Response getFile(@PathVariable(name = "file_path") String filePath);

    @PostMapping(SEND_MESSAGE)
    Result sendMessage(@RequestBody SendMessage sendMessage);

    @PostMapping(SEND_PHOTO)
    Result sendPhoto(@RequestBody HandleSendPhoto sendPhoto);

    @PostMapping(EDIT_MESSAGE_TEXT)
    Result editMessageText(@RequestBody EditMessageText editMessageText);

    @PostMapping(DELETE_MESSAGE)
    ResultDelete deleteMessage(@RequestBody DeleteMessage deleteMessage);

    @PostMapping(EDIT_MESSAGE_CAPTION)
    Result editMessageCaption(@RequestBody EditMessageCaption editMessageCaption);
}
