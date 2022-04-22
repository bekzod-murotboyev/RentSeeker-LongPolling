package uz.pdp.rentseekerlongpolling.service;

import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.rentseekerlongpolling.entity.Attachment;
import uz.pdp.rentseekerlongpolling.repository.AttachmentRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static uz.pdp.rentseekerlongpolling.util.Url.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public List<Attachment> saveAll(List<Attachment> attachments){
        return attachmentRepository.saveAll(attachments);
    }

    @SneakyThrows
    public List<Attachment> saveToDatabase(MultipartHttpServletRequest request) {
        List<Attachment> attachments = new ArrayList<>();
        request.getFileNames().forEachRemaining(item -> {
            MultipartFile file = request.getFile(item);
            String[] split = Objects.requireNonNull(Objects.requireNonNull(file).getOriginalFilename()).split("\\.");
            String fileUniqueId = UUID.randomUUID() + "." + split[split.length - 1];
            String path = GLOBAL + BASE_FILE + "/" + fileUniqueId;
            attachments.add(new Attachment(
                    path,
                    fileUniqueId,
                    100,
                    100,
                    file.getSize(),
                    path,
//                        Base64.getEncoder().encodeToString(file.getBytes()),
                    file.getContentType()
            ));
        });
        return attachments;
    }

    public List<Attachment> saveToFile(MultipartHttpServletRequest request) {
        List<Attachment> attachments = new ArrayList<>();
        request.getFileNames().forEachRemaining(item -> {
            try {
                MultipartFile file = request.getFile(item);
                String[] split = Objects.requireNonNull(Objects.requireNonNull(file).getOriginalFilename()).split("\\.");
                String fileUniqueId = UUID.randomUUID() + "." + split[split.length - 1];
                String path = GLOBAL + BASE_FILE + "/" + fileUniqueId;
                attachments.add(new Attachment(
                        path,
                        fileUniqueId,
                        100,
                        100,
                        file.getSize(),
                        path,
                        file.getContentType()
                ));
                writeFile(fileUniqueId, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return attachments;
    }

    public void writeFile(String name, byte[] bytes) {
        File file = new File(DIRECTORY + name);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            file.createNewFile();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Resource getFile(String fileUniqueId) {
        return new FileSystemResource(Paths.get(DIRECTORY, fileUniqueId));
    }
}
