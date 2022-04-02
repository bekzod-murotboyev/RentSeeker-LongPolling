package uz.pdp.rentseekerlongpolling.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.rentseekerlongpolling.payload.telegram.simple_telegram.FileDataDTO;
import uz.pdp.rentseekerlongpolling.util.security.BaseData;

@FeignClient(url = BaseData.TELEGRAM_GET_FILE_PATH,name = "TelegramFileFeign")
public interface TelegramFileFeign {

    @GetMapping
    FileDataDTO getFilePath(@RequestParam(name = "file_id") String fileId);

}
