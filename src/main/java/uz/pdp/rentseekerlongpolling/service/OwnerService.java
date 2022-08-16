package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.rentseekerlongpolling.entity.Owner;
import uz.pdp.rentseekerlongpolling.payload.auth.AuthDTO;
import uz.pdp.rentseekerlongpolling.payload.response.ApiResponse;
import uz.pdp.rentseekerlongpolling.repository.OwnerRepository;
import uz.pdp.rentseekerlongpolling.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class OwnerService {


    private final OwnerRepository ownerRepository;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public ResponseEntity<ApiResponse> login(AuthDTO authDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword()));
        return ResponseEntity
                .ok(new ApiResponse(true, "TOKEN", jwtProvider.generateToken(authDTO.getUsername())));
    }
}
