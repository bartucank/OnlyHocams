package com.bartu.onlyhocams.service;


import com.bartu.onlyhocams.api.request.NoteRequest;
import com.bartu.onlyhocams.api.request.PostRequest;
import com.bartu.onlyhocams.api.request.UserRequest;
import com.bartu.onlyhocams.api.response.JwtResponse;
import com.bartu.onlyhocams.api.response.StatusDTO;
import com.bartu.onlyhocams.dto.CategoryDTO;
import com.bartu.onlyhocams.dto.NoteDTO;
import com.bartu.onlyhocams.dto.PostDTO;
import com.bartu.onlyhocams.dto.UserDTO;
import com.bartu.onlyhocams.entity.enums.Type;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    UserDTO getUserDetails();

    StatusDTO uploadDocument(MultipartFile file) throws IOException;


    List<PostDTO> getPostsByCategoryId(int lim, int off, Long categoryId,String keyword);

    StatusDTO shareNote(NoteRequest request);

    StatusDTO deleteNote(Long id);

    List<NoteDTO> getNotes(int limit, int offset);

    NoteDTO getNoteFullDetail(Long id);

    StatusDTO reviewNote(Long id, String review, Type type);

    List<CategoryDTO> getCategories();

    StatusDTO purchaseNote(Long id);
}
