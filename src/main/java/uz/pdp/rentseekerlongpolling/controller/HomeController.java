package uz.pdp.rentseekerlongpolling.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.rentseekerlongpolling.payload.SearchDTO;
import uz.pdp.rentseekerlongpolling.payload.home.HomeEditDTO;
import uz.pdp.rentseekerlongpolling.payload.response.ApiResponse;
import uz.pdp.rentseekerlongpolling.service.HomeService;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "3") Integer size) {
        ApiResponse response = homeService.getAllHome(page, size);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        ApiResponse response = homeService.getHomeById(id);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveHomes(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "3") Integer size) {
        ApiResponse response = homeService.getActiveHomes(page, size);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchHomes(@RequestBody SearchDTO searchDTO) {
        ApiResponse response = homeService.searchHome(searchDTO);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getByUserPhoneNumber(@RequestParam String phone, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "3") Integer size) {
        ApiResponse response = homeService.getByUserPhone(phone, page, size);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<?> getByUserInactiveHome(@RequestParam UUID userId) {
        ApiResponse response = homeService.getNoActiveHomeByUserId(userId);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }

    @GetMapping("/favourite")
    public ResponseEntity<ApiResponse> getFavouriteHomes(@RequestParam UUID userId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "3") Integer size) {
        ApiResponse response = homeService.getFavouriteHomesByUserId(userId, page, size);
        return ResponseEntity.status(response.isSuccess() ? OK : NOT_FOUND).body(response);
    }

    @PostMapping
    public ResponseEntity<?> addHome(MultipartHttpServletRequest request) throws JsonProcessingException {
        ApiResponse response = homeService.addHome(request);
        return ResponseEntity.status(response.isSuccess() ? CREATED : CONFLICT).body(response);
    }

    @PutMapping
    public ResponseEntity<?> editHome(@RequestParam UUID id, @Valid @RequestBody HomeEditDTO homeEditDTO) {
        ApiResponse response = homeService.editHome(id, homeEditDTO);
        return ResponseEntity.status(response.isSuccess() ? ACCEPTED : CONFLICT).body(response);
    }


    @PutMapping("/like")
    public ResponseEntity<ApiResponse> changeHomeLike(@RequestParam UUID homeId, @RequestParam UUID userId) {
        ApiResponse response = homeService.editHomeLike(homeId, userId);
        return ResponseEntity.status(response.isSuccess() ? ACCEPTED : CONFLICT).body(response);
    }

    @PutMapping("/likeById")
    public ResponseEntity<ApiResponse> changeHomeLike(@RequestParam UUID likeId) {
        ApiResponse response = homeService.editHomeLike(likeId);
        return ResponseEntity.status(response.isSuccess() ? ACCEPTED : CONFLICT).body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteHome(@RequestParam UUID id) {
        ApiResponse response = homeService.delete(id);
        return ResponseEntity.status(response.isSuccess() ? NO_CONTENT : NOT_FOUND).body(response);
    }
}
