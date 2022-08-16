package uz.pdp.rentseekerlongpolling.util.firebase;


public interface FireBaseUtils {
    String BUCKET_NAME = "picture-5939.appspot.com";

    String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";
}
