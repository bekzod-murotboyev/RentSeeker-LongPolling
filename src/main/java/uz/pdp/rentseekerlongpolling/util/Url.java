package uz.pdp.rentseekerlongpolling.util;


import uz.pdp.rentseekerlongpolling.util.security.BaseData;

public interface Url {
    String BASE_USER="api/user";
    String BASE_FILE="api/attachment";
    String DIRECTORY="../";

    String USER_ACTIVE="/active";
    String USER_INACTIVE="/inactive";
    String GLOBAL="http://rent-seeker.us-east-1.elasticbeanstalk.com/";





    String TOKEN="bot"+ BaseData.TOKEN;

    String TELEGRAM_BASE="https://api.telegram.org/";
    String TELEGRAM_GET_FILE_PATH=TOKEN+"/getFile";
    String TELEGRAM_GET_FILE="file/"+TOKEN+"/";

    String SEND_MESSAGE=TOKEN+"/sendMessage";
    String SEND_PHOTO=TOKEN+"/sendPhoto";
    String EDIT_MESSAGE_TEXT=TOKEN+"/editMessageText";
    String DELETE_MESSAGE=TOKEN+"/deleteMessage";
    String EDIT_MESSAGE_CAPTION=TOKEN+"/editMessageCaption";




//    String GLOBAL="http://localhost:8080/";

}
