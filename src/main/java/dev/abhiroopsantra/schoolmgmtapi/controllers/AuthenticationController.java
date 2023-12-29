package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.Auth.AuthenticationRequest;
import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.repositories.UserRepository;
import dev.abhiroopsantra.schoolmgmtapi.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public AuthenticationController(
            AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil,
            UserRepository userRepository, ModelMapper modelMapper
                                   ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService    = userDetailsService;
        this.jwtUtil               = jwtUtil;
        this.userRepository        = userRepository;
        this.modelMapper           = modelMapper;
    }


    @PostMapping("/login") public ResponseEntity<ApiResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest
                                                                                       ) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                                                                       authenticationRequest.getPassword()
            ));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            Optional<User>    user        = userRepository.findFirstByEmail(authenticationRequest.getEmail());

            HashMap<String, Object> responseData = new HashMap<>();

            if (user.isPresent()) {
                final String jwt = jwtUtil.generateToken(user.get());

                responseData.put("token", jwt);
                responseData.put("user", modelMapper.map(user.get(), UserDto.class));
            } else {
                responseData.put("user", null);
            }

            return new ResponseEntity<>(new ApiResponse(responseData, "0", "Success"), HttpStatus.OK);

        }
        catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ApiResponse(null, "1", "Incorrect username or password"),
                                        HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", "An error occurred while authenticating"),
                                        HttpStatus.INTERNAL_SERVER_ERROR
            );
        }


    }
}
