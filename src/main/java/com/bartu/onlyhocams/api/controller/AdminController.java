package com.bartu.onlyhocams.api.controller;


import com.bartu.onlyhocams.api.request.PostRequest;
import com.bartu.onlyhocams.api.response.StatusDTO;
import com.bartu.onlyhocams.api.service.ApiResponse;
import com.bartu.onlyhocams.api.service.ResponseService;
import com.bartu.onlyhocams.dto.UserDTO;
import com.bartu.onlyhocams.service.OHServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin", produces = "application/json;charset=UTF-8")
public class AdminController {

    private OHServices service;
    private ResponseService responseService;

    public AdminController(OHServices service, ResponseService responseService) {
        this.service = service;
        this.responseService = responseService;
    }

    @PostMapping("/category")
    public ResponseEntity<ApiResponse<StatusDTO>> createCategory(@RequestParam("name") String name) {
        return responseService.createResponse(service.createCategory(name));
    }

    @PutMapping("/category")
    public ResponseEntity<ApiResponse<StatusDTO>> updateCategory(@RequestParam("name") String name,
                                                                 @RequestParam("id") Long id) {
        return responseService.createResponse(service.updateCategory(name,id));
    }

    @DeleteMapping("/category")
    public ResponseEntity<ApiResponse<StatusDTO>> deleteCategory(@RequestParam("id") Long id) {
        return responseService.createResponse(service.deleteCategory(id));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsers() {
        return responseService.createResponse(service.getUsers());
    }


}
