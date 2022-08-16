package uz.pdp.rentseekerlongpolling.service;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.rentseekerlongpolling.entity.Attachment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static uz.pdp.rentseekerlongpolling.util.firebase.FireBaseUtils.BUCKET_NAME;
import static uz.pdp.rentseekerlongpolling.util.firebase.FireBaseUtils.DOWNLOAD_URL;


@Service
public class FirebaseService {


    public List<Attachment> upload(MultipartHttpServletRequest request) {
        return request
                .getFileMap()
                .values()
                .stream()
                .map(file -> {
                    try {
                        String fileUniqueId = UUID.randomUUID() + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
                        String path = upload(file, fileUniqueId);
                        return new Attachment(path, fileUniqueId, ((int) file.getSize()), ((int) file.getSize()), file.getSize(), path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }


    public String uploadByPath(String path, String fileUniqueId) throws IOException {
        StorageOptions
                .newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService()
                .createFrom(getBlobInfo(fileUniqueId, null), Paths.get(path));

        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileUniqueId, StandardCharsets.UTF_8));
    }

    public String upload(MultipartFile file, String fileUniqueId) throws IOException {
        StorageOptions
                .newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService()
                .create(getBlobInfo(fileUniqueId, file.getContentType()), file.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileUniqueId, StandardCharsets.UTF_8));
    }

    public String uploadByInputStream(InputStream inputStream, String fileUniqueId) throws IOException {
        StorageOptions
                .newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService()
                .createFrom(getBlobInfo(fileUniqueId, "image/jpeg"),
                        inputStream,
                        Storage.BlobWriteOption.detectContentType());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileUniqueId, StandardCharsets.UTF_8));
    }

    private BlobInfo getBlobInfo(String fileUniqueId, String contentType) {
        return BlobInfo
                .newBuilder(BlobId.of(BUCKET_NAME, fileUniqueId))
                .setContentType(Objects.isNull(contentType) ? "media" : contentType)
                .build();
    }

    private Credentials getCredentials() throws IOException {
        return GoogleCredentials.fromStream(IOUtils.toInputStream("{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"picture-5939\",\n" +
                "  \"private_key_id\": \"90b64129a30d05f32ae23e4ec1919dca7115fcac\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDRKmyKK/iRjqn5\\nOMgRi4ekaG1ycjSuZI6oD3BA/0g4GSn6B07GrtGS2a0kDrVtXvYlOQ8HRH/563fJ\\nW/32idlSs3k51Ihri826Ouv0/2B8oUu4rOkPjtihTdUSjAZIgK3l1DQKaotq++vu\\nDNZyjeOmXVVcr1CTgqv5yP4B5qfN1uWf9ssNwkYoPwVgV6+1rBkJ7x+JeQCnFs57\\n4+b+zOpOdchnthP8cuKQmy8q3zLJ8GgsQntKyDproRQVKSy+6BJ6V+MQnUPpiCAe\\nN5JSU82Q+hBJHf5dKbz5/BAX4Xy3rWmxut68oBkBIYlu0uXX9Jou0HimlwaA6ZAW\\nNOkqLCN3AgMBAAECggEAA//ApX+jt/y3STmJsbQuOrl4ZjjCwxbPF218IvH+inwI\\nrmwXWHsdLKaWxdq2I0ougJWvsN7hZ4DWAHaOy3jlO8yMo8tpdEzu+QGJY1zCln9q\\n+DPuHMsJX0Qir8EXmalEKeG9wPk6gygf4aRDMVt/H74nFKeRbdCQwyzRlm23hZru\\nUpHQbBYfe9PkaJyLle3FUaDQoewzBGiOIEn9gJwxhWH+tgLBt4/joTK5B+yerZL5\\nMOZa8gJULgsKTVZ7okCJthTfE0z1lNPwfegrFIEEbmDqBcMDKWlvNU3d4v6u0B1g\\nTfK17uHpssHtGE1Lwelq3RSe4Mg1OeEmvsC4spswYQKBgQD/l5UHAcoxCBHzc9Ao\\nEMv8tI1UVKE3cvbo8mTN1TQjg6x9bKwirK1BSyy3dUBqwL1UzCa3Q7ho7DNtMIAs\\nrGqC+7MMchzGY3EyXsVwa3QBrOvmk/pyykzuks34Qs+DTTd5fdIkdKjnwyPbDCEz\\nxcC9ss96frr53RPJidy+xP+E0QKBgQDRf+AH5dsoi5Dt3M6W3tOY4YLWs4ycvoeT\\npAhv3xX91oflB2f7Daf3RIrR4dKNkFHFJpX5e3ZX24bqcUTk9ANkDfNC3j1L64Zq\\nnMhakAXPsRyWQyB/Biz5KDzDr1xh3ASqQzquGwyGtf9Yh2PXatZVVX6m1U0H7Emy\\nIweTV+rVxwKBgQCmGX4xxYn+fz4bNmJKB1Bpc3R9H1p9zGqFTDESSsHNap7IC715\\n8znMuuvedYZdWdAwsVNfudS36vgot2I0pf+6C5R6PBZTHwDLOSeaPVpwVE4h1Hzn\\n6GxChojwE4sDy0SH4aqVInM52mkMyz8whA0TuoZ3FrCv/GoFiWA5YZCkYQKBgFvh\\nsJjmJ7tdkr+v0u96ZOUtKpwNfUMVkRKgo46lEdkVaN565BDIiX06E4Q/fX/W5vYH\\nmUkk699WiZJzARtFJeOQ9rrCC7IrZvWFM538gn/3lqkUYcE0LS9NhXDxjRviSi1+\\ndNL9S/4c/hOOtaw50Cz25Sg4bqNl72BwWpc1vOnrAoGAa4Mq8ODLSAcnvXVvTyd6\\nkqW3vraeRGZ9sHOAm5cWXqn/oIfAttN0G17A6Z3XX6mLROisYbSbDmClVG30y0ag\\ny+B5CQCuieZtPI1PuESgKyQMvTRVW0Hw5UApsXlB5DQlR7dXXEe2DXs80BFSyads\\nOk4crVgCY47biw1aKpLu9xs=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"firebase-adminsdk-srguy@picture-5939.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"114382733695975006568\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-srguy%40picture-5939.iam.gserviceaccount.com\"\n" +
                "}\n", StandardCharsets.UTF_8));
    }

    public Object delete(String path) {
        try {
            String fileUniqueId = this.getFileNameFromPath(path);
            this.deleteFile(fileUniqueId);
            return ResponseEntity.ok();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    private boolean deleteFile(String fileUniqueId) throws IOException {
        return StorageOptions
                .newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService()
                .delete(BlobId.of(BUCKET_NAME, fileUniqueId));
    }

    private String getExtension(String fileUniqueId) {
        return fileUniqueId.substring(fileUniqueId.lastIndexOf("."));
    }

    private String getFileNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("?"));
    }

}
