package uz.pdp.rentseekerlongpolling.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.rentseekerlongpolling.payload.FileDTO;
import uz.pdp.rentseekerlongpolling.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static uz.pdp.rentseekerlongpolling.util.Url.BASE_FILE;

@RestController
@RequestMapping(BASE_FILE)
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;


//    @GetMapping("/{fileUniqueId}")
//    public void downloadFile(@PathVariable String fileUniqueId, HttpServletResponse response) throws IOException {
//        FileDTO fileDTO = attachmentService.getFile(fileUniqueId);
//        if(fileDTO==null)
//            return;
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileUniqueId + "\"");
//        response.setContentType(fileDTO.getContentType());
//        FileCopyUtils.copy(new FileInputStream(fileDTO.getFile()), response.getOutputStream());
//    }

    @GetMapping(path = "/{fileUniqueId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileUniqueId) throws IOException {
        FileDTO fileDTO = attachmentService.getFile(fileUniqueId);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileUniqueId);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(fileDTO.getFile().getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(fileDTO.getFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
