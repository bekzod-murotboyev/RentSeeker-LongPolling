package uz.pdp.rentseekerlongpolling.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.payload.SearchDTO;
import uz.pdp.rentseekerlongpolling.service.HomeService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "3") Integer size){
        return ResponseEntity.ok(homeService.getAllHome(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id){
        Home home = homeService.getById(id);
        return ResponseEntity.status(home==null?404:200).body(home);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveHomes(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "3") Integer size){
        return ResponseEntity.ok(homeService.getAllActiveHomes(page,size));
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchHomes(@RequestBody SearchDTO searchDTO){
        return ResponseEntity.ok(homeService.searchHome(searchDTO));
    }

    @GetMapping
    public ResponseEntity<?> getByUserPhoneNumber(@RequestParam String phone,@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "3") Integer size){
        return ResponseEntity.ok(homeService.getByUserPhone(phone,page,size));
    }





}
