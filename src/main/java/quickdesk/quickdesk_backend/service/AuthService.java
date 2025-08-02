//package quickdesk.quickdesk_backend.service;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import quickdesk.quickdesk_backend.dto.AuthResponse;
//import quickdesk.quickdesk_backend.dto.LoginRequest;
//import quickdesk.quickdesk_backend.dto.RegisterRequest;
//import quickdesk.quickdesk_backend.jwt.JwtService;
//import quickdesk.quickdesk_backend.model.User;
//import quickdesk.quickdesk_backend.repository.UserRepository;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    // ✅ Manual constructor (no Lombok)
//    public AuthService(
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder,
//            JwtService jwtService,
//            AuthenticationManager authenticationManager
//    ) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//        this.authenticationManager = authenticationManager;
//    }
//
//    public AuthResponse register(RegisterRequest request) {
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPaasword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(request.getRole());
//
//        userRepository.save(user);
//
//        String token = jwtService.generateToken(user);
//        return new AuthResponse(token);
//    }
//
//    public AuthResponse login(LoginRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String token = jwtService.generateToken(user);
//        return new AuthResponse(token);
//    }
//}
package quickdesk.quickdesk_backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quickdesk.quickdesk_backend.dto.AuthResponse;
import quickdesk.quickdesk_backend.dto.LoginRequest;
import quickdesk.quickdesk_backend.dto.RegisterRequest;
import quickdesk.quickdesk_backend.jwt.JwtService;
import quickdesk.quickdesk_backend.model.Role;
import quickdesk.quickdesk_backend.model.User;
import quickdesk.quickdesk_backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPaasword(passwordEncoder.encode(request.getPassword()));

        // Set role safely, default to USER if null
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
