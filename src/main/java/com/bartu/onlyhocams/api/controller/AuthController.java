package com.bartu.onlyhocams.api.controller;



import com.bartu.onlyhocams.api.request.UserRequest;
import com.bartu.onlyhocams.api.response.JwtResponse;
import com.bartu.onlyhocams.api.service.ApiResponse;
import com.bartu.onlyhocams.api.service.ResponseService;
import com.bartu.onlyhocams.service.OHServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/api/auth", produces = "application/json;charset=UTF-8")
public class AuthController {

    private OHServices service;
    private ResponseService responseService;

    public AuthController(OHServices service, ResponseService responseService) {
        this.service = service;
        this.responseService = responseService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(@RequestBody UserRequest userRequest){
        return responseService.createResponse(service.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody UserRequest userRequest){
        return responseService.createResponse(service.login(userRequest));
    }


}
