package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.AuthenticationRequest;
import dev.abhiroopsantra.schoolmgmtapi.dto.AuthenticationResponse;
import dev.abhiroopsantra.schoolmgmtapi.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    // TODO: Use standardized HTTP status codes
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                                            HttpServletResponse response)
            throws IOException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    ));
        } catch (BadCredentialsException ex) {
//            throw new BadCredentialsException("Incorrect username or password");
            return new ResponseEntity<>(
                    new AuthenticationResponse(null, "1", "Incorrect username or password"),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception ex) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not created");
            return new ResponseEntity<>(
                    new AuthenticationResponse(null, "2", "An error occurred while authenticating"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new ResponseEntity<>(
                new AuthenticationResponse(jwt, "0", "Success"),
                HttpStatus.OK
        );
    }
}
