package com.bartu.onlyhocams.service;


import com.bartu.onlyhocams.api.request.PostRequest;
import com.bartu.onlyhocams.api.request.UserRequest;
import com.bartu.onlyhocams.api.response.JwtResponse;
import com.bartu.onlyhocams.api.response.StatusDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OHServices {
    JwtResponse register(UserRequest userRequest);

    JwtResponse login(UserRequest userRequest);

    StatusDTO createCategory(String name);

    StatusDTO updateCategory(String name, Long id);

    StatusDTO sharePost(PostRequest request);

    StatusDTO deleteCategory(Long id);

    StatusDTO deletePost(Long id);

    StatusDTO likePost(Long id);

    StatusDTO dislikePost(Long id);

    StatusDTO addComment(Long id, String content);

    StatusDTO deleteComment(Long id);
}
