package com.bartu.onlyhocams.api.controller;



import com.bartu.onlyhocams.api.request.NoteRequest;
import com.bartu.onlyhocams.api.request.PostRequest;
import com.bartu.onlyhocams.api.request.UserRequest;
import com.bartu.onlyhocams.api.response.JwtResponse;
import com.bartu.onlyhocams.api.response.StatusDTO;
import com.bartu.onlyhocams.api.service.ApiResponse;
import com.bartu.onlyhocams.api.service.ResponseService;
import com.bartu.onlyhocams.dto.CategoryDTO;
import com.bartu.onlyhocams.dto.NoteDTO;
import com.bartu.onlyhocams.dto.PostDTO;
import com.bartu.onlyhocams.dto.UserDTO;
import com.bartu.onlyhocams.entity.enums.Type;
import com.bartu.onlyhocams.service.OHServices;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value ="/api/user", produces = "application/json;charset=UTF-8")
public class UserController {

    private OHServices service;
    private ResponseService responseService;

    public UserController(OHServices service, ResponseService responseService) {
        this.service = service;
        this.responseService = responseService;
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<StatusDTO>> sharePost(@RequestBody PostRequest request){
        return responseService.createResponse(service.sharePost(request));
    }

    @DeleteMapping("/post")
    public ResponseEntity<ApiResponse<StatusDTO>> deletePost(@RequestParam("id")Long id){
        return responseService.createResponse(service.deletePost(id));
    }

    @PostMapping("/post/like")
    public ResponseEntity<ApiResponse<StatusDTO>> likePost(@RequestParam("id")Long id){
        return responseService.createResponse(service.likePost(id));
    }

    @PostMapping("/post/dislike")
    public ResponseEntity<ApiResponse<StatusDTO>> dislikePost(@RequestParam("id")Long id){
        return responseService.createResponse(service.dislikePost(id));
    }

    @PostMapping("/post/comment")
    public ResponseEntity<ApiResponse<StatusDTO>> addComment(@RequestParam("id")Long id, @RequestParam("comment")String comment){
        return responseService.createResponse(service.addComment(id,comment));
    }

    @DeleteMapping("/post/comment")
    public ResponseEntity<ApiResponse<StatusDTO>> deleteComment(@RequestParam("id")Long id){
        return responseService.createResponse(service.deleteComment(id));
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDetails(){
        return responseService.createResponse(service.getUserDetails());
    }

    @GetMapping("/post")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getPosts(@RequestParam("limit")int limit, @RequestParam("offset")int offset,
                                                               @RequestParam(value = "categoryId",required = true)Long categoryId,
                                                               @RequestParam(value = "key",required = false)String key){
        return responseService.createResponse(service.getPostsByCategoryId(limit,offset,categoryId,key));
    }

    @PostMapping(value="/document",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StatusDTO>> uploadDocument(@RequestParam("document") MultipartFile file) throws IOException {
        return responseService.createResponse(service.uploadDocument(file));
    }

    @PostMapping("/note")
    public ResponseEntity<ApiResponse<StatusDTO>> shareNote(@RequestBody NoteRequest request){
        return responseService.createResponse(service.shareNote(request));
    }

    @DeleteMapping("/note")
    public ResponseEntity<ApiResponse<StatusDTO>> deleteNote(@RequestParam("id")Long id){
        return responseService.createResponse(service.deleteNote(id));
    }

    @GetMapping("/note")
    public ResponseEntity<ApiResponse<List<NoteDTO>>> getNotes(@RequestParam("limit")int limit,
                                                               @RequestParam("offset")int offset,
                                                               @RequestParam(value = "key",required = false) String key,
                                                               @RequestParam(value = "waiting",required = false) Boolean waiting,
                                                               @RequestParam(value = "owned",required = false) Boolean owned
    ) {
        return responseService.createResponse(service.getNotes(limit,offset,key,waiting,owned));
    }

    @PostMapping("/note/review")
    public ResponseEntity<ApiResponse<StatusDTO>> reviewNote(@RequestParam("id")Long id, @RequestParam("content")String content, @RequestParam("type")Type type){
        return responseService.createResponse(service.reviewNote(id,content,type));
    }
    @DeleteMapping("/note/review")
    public ResponseEntity<ApiResponse<StatusDTO>> reviewNote(@RequestParam("id")Long id){
        return responseService.createResponse(service.deleteReview(id));
    }



    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getCategories(){
        return responseService.createResponse(service.getCategories());
    }

    @PostMapping("/note/purchase")
    public ResponseEntity<ApiResponse<StatusDTO>> purchaseNote(@RequestParam("id")Long id){
        return responseService.createResponse(service.purchaseNote(id));
    }

    @GetMapping("/note/detail")
    public ResponseEntity<ApiResponse<NoteDTO>> getNoteFullDetail(@RequestParam("id")Long id){
        return responseService.createResponse(service.getNoteFullDetail(id));
    }
}
