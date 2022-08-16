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

import java.io.*;
import java.nio.file.Files;
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

    public void updateFileIdByPath(String path,String fileId){
        attachmentRepository.updateFileId(path,fileId);
    }
}
