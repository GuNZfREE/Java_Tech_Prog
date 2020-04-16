package ru.Bank.Controller.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.Bank.Entity.User;
import ru.Bank.Helper.Status.ErrorStatus;
import ru.Bank.Model.Request.UserLoginRequest;
import ru.Bank.Model.Request.UserSignUpRequest;
import ru.Bank.Model.Response.ReturnResponse.ErrorReturnResponse;
import ru.Bank.Model.Response.ReturnResponse.SecurityReturnResponse;
import ru.Bank.Model.Response.SecurityResponse;
import ru.Bank.Repository.UserRepository;
import ru.Bank.Security.JwtToken;
import ru.Bank.Service.JwtUserDetailsService;
import ru.Bank.Service.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/api/security")
public class SecurityController extends AbstractController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private UserService userService;
    private JwtUserDetailsService jwtUserDetailsService;
    private JwtToken jwtToken;

    @Autowired
    public SecurityController(AuthenticationManager authenticationManager, UserRepository userRepository,
                              UserService userService, JwtUserDetailsService jwtUserDetailsService, JwtToken jwtToken) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtToken = jwtToken;
    }

    @ApiOperation(value = "login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = SecurityReturnResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginRequest userLogin) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getLoginOrPhone(),
                    userLogin.getPassword()));
        } catch (BadCredentialsException e) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.LOGIN_INVALID);
        }
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(userLogin.getLoginOrPhone());
        if (userDetails == null) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.LOGIN_INVALID);
        }
        final String token = jwtToken.generateToken(userDetails);
        User user = this.userService.getUser(userDetails.getUsername());
        return this.successResponse(new SecurityResponse("Bearer " + token, user),
                HttpStatus.OK, ErrorStatus.OK);
    }

    @ApiOperation(value = "signup", response = SecurityResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully", response = SecurityResponse.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorReturnResponse.class),
    })
    @PostMapping("/signup")
    public ResponseEntity create(@Valid @RequestBody UserSignUpRequest userSignUp) throws  ValidationException {
        if (userRepository.existsUserByLogin(userSignUp.getLogin())) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.LOGIN_ALREADY_EXIST);
        }
        if (userRepository.existsUserByPhone(userSignUp.getPhone())) {
            return this.errorResponse(HttpStatus.BAD_REQUEST, ErrorStatus.PHONE_ALREADY_EXIST);
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(userSignUp.getPassword());
        User newUser = new User(userSignUp.getLogin(), encodedPassword, userSignUp.getAddress(), userSignUp.getPhone());
        userRepository.save(newUser);
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(newUser.getLogin());
        final String token = jwtToken.generateToken(userDetails);
        User user = this.userService.getUser(userDetails.getUsername());
        return this.successResponse(new SecurityResponse("Bearer " + token, user),
                HttpStatus.CREATED, ErrorStatus.OK);
    }
}
