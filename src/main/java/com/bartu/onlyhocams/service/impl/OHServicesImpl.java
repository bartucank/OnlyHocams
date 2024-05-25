package com.bartu.onlyhocams.service.impl;


import com.bartu.onlyhocams.api.request.PostRequest;
import com.bartu.onlyhocams.api.request.UserRequest;
import com.bartu.onlyhocams.api.response.JwtResponse;
import com.bartu.onlyhocams.api.response.StatusDTO;
import com.bartu.onlyhocams.entity.Category;
import com.bartu.onlyhocams.entity.LikeLog;
import com.bartu.onlyhocams.entity.Post;
import com.bartu.onlyhocams.entity.User;
import com.bartu.onlyhocams.entity.enums.Role;
import com.bartu.onlyhocams.entity.enums.Type;
import com.bartu.onlyhocams.exception.ExceptionCode;
import com.bartu.onlyhocams.exception.OhException;
import com.bartu.onlyhocams.repository.*;
import com.bartu.onlyhocams.security.JwtProvider;
import com.bartu.onlyhocams.security.JwtUserDetails;
import com.bartu.onlyhocams.service.OHServices;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OHServicesImpl implements OHServices {
    private CategoryRepository categoryRepository;
    private DocumentRepository documentRepository;
    private LikeLogRepository likeLogRepository;
    private NoteRepository noteRepository;
    private PostRepository postRepository;
    private ReviewRepository repository;
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public OHServicesImpl(CategoryRepository categoryRepository, DocumentRepository documentRepository, LikeLogRepository likeLogRepository, NoteRepository noteRepository, PostRepository postRepository, ReviewRepository repository, UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
        this.likeLogRepository = likeLogRepository;
        this.noteRepository = noteRepository;
        this.postRepository = postRepository;
        this.repository = repository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtResponse register(UserRequest userRequest) {
        if(Objects.isNull(userRequest)){
            throw new OhException(ExceptionCode.INVALID_REQUEST);
        }
        if(Objects.isNull(userRequest.getEmail())){
            throw new OhException(ExceptionCode.EMAIL_CANNOT_BE_NULL);
        }
        if(Objects.isNull(userRequest.getName())){
            throw new OhException(ExceptionCode.NAME_CANNOT_BE_NULL);
        }
        if(Objects.isNull(userRequest.getUsername())){
            throw new OhException(ExceptionCode.USERNAME_CANNOT_BE_NULL);
        }
        if(Objects.isNull(userRequest.getPassword())){
            throw new OhException(ExceptionCode.PASS_CANNOT_BE_NULL);
        }

        //Check if user exists;
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            throw new OhException(ExceptionCode.THIS_USERNAME_ALREADY_EXISTS);
        }

        //Check if phone exists;
        if (userRepository.findByEmail(userRequest.getEmail()) != null) {
            throw new OhException(ExceptionCode.THIS_EMAIL_ALREADY_EXISTS);
        }
        userRepository.insertUser(userRequest.getName(),userRequest.getUsername(),userRequest.getEmail(),passwordEncoder.encode(userRequest.getPassword()),Role.USER.name(), BigDecimal.TEN);
        return login(userRequest);
    }

    @Override
    public JwtResponse login(UserRequest userRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(),
                        userRequest.getPassword());
        Authentication authentication = authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateJwtToken(authentication);
        JwtUserDetails jwtUser = (JwtUserDetails) authentication.getPrincipal();
        User user = userRepository.getById(jwtUser.getId());
        JwtResponse authenticationResponse = new JwtResponse();
        authenticationResponse.setJwt(token);
        authenticationResponse.setRole(user.getRole());
        return authenticationResponse;
    }

    @Override
    public StatusDTO createCategory(String name){
        if(Objects.isNull(name) || name.isEmpty()){
            throw new OhException(ExceptionCode.CONTENT_CANNOT_BE_EMPTY);
        }
        categoryRepository.insertCategory(name);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }




    @Override
    public StatusDTO updateCategory(String name, Long id){
        if(Objects.isNull(name) || name.isEmpty()){
            throw new OhException(ExceptionCode.CONTENT_CANNOT_BE_EMPTY);
        }
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.CATEGORY_NULL);
        }
        Category category = categoryRepository.getById(id);
        if(Objects.isNull(category)){
            throw new OhException(ExceptionCode.CATEGORY_NULL);
        }
        categoryRepository.updateCategory(name,id);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }


    @Override
    public StatusDTO deleteCategory(Long id) {
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.CATEGORY_NULL);
        }
        Category category = categoryRepository.getById(id);
        if(Objects.isNull(category)){
            throw new OhException(ExceptionCode.CATEGORY_NULL);
        }
        List<Long> postIds = postRepository.getByCategoryId(id);
        likeLogRepository.bulkDeleteByPostIds(postIds);
        documentRepository.bulkDeleteByPostIds(postIds);
        postRepository.deletePostByCategoryId(id);
        categoryRepository.deleteCategory(id);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }
    @Override
    public StatusDTO sharePost(PostRequest request){
        if(Objects.isNull(request)){
            throw new OhException(ExceptionCode.INVALID_REQUEST);
        }
        if(Objects.isNull(request.getContent())){
            throw new OhException(ExceptionCode.CONTENT_CANNOT_BE_EMPTY);
        }
        if(Objects.isNull(request.getCategory())){
            throw new OhException(ExceptionCode.CATEGORY_NULL);
        }
        if(Objects.isNull(categoryRepository.getById(request.getCategory()))){
            throw new OhException(ExceptionCode.CATEGORY_NULL);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        Long postId = postRepository.insertPost(request.getContent(), LocalDateTime.now(),jwtUser.getId(),request.getCategory());
        if(Objects.nonNull(request.getDocumentIds())){
            documentRepository.linkDocumentToPost(postId,request.getDocumentIds());
        }
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO deletePost(Long id){
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Post post = postRepository.getById(id);
        if(Objects.isNull(post)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!jwtUser.getId().equals(post.getUser().getId()) && !jwtUser.getRole().equals(Role.ADMIN)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        likeLogRepository.bulkDeleteByPostIds(List.of(id));
        documentRepository.bulkDeleteByPostIds(List.of(id));
        postRepository.deleteById(id);

        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO likePost(Long id){
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Post post = postRepository.getById(id);
        if(Objects.isNull(post)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        LikeLog likeLog = likeLogRepository.getLikeLogByPostAndUser(jwtUser.getId(),id);
        if(Objects.nonNull(likeLog)){
            likeLogRepository.deleteById(likeLog.getId());
        }
        likeLogRepository.likeDislikePost(jwtUser.getId(),id, Type.LIKE.name());
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }


    @Override
    public StatusDTO dislikePost(Long id){
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Post post = postRepository.getById(id);
        if(Objects.isNull(post)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        LikeLog likeLog = likeLogRepository.getLikeLogByPostAndUser(jwtUser.getId(),id);
        if(Objects.nonNull(likeLog)){
            likeLogRepository.deleteById(likeLog.getId());
        }
        likeLogRepository.likeDislikePost(jwtUser.getId(),id, Type.DISLIKE.name());
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO addComment(Long id, String content){
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Post post = postRepository.getById(id);
        if(Objects.isNull(post)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        likeLogRepository.addComment(jwtUser.getId(),id, content);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO deleteComment(Long id) {

        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.POST_NOT_FOUND);
        }
        LikeLog likeLog = likeLogRepository.getById(id);
        if(Objects.isNull(likeLog)){
            throw new OhException(ExceptionCode.COMMENT_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!likeLog.getUser().getId().equals(jwtUser.getId()) && !likeLog.getUser().getRole().equals(Role.USER)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        likeLogRepository.deleteById(id);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }


}
