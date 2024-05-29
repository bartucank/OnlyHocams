package com.bartu.onlyhocams.service.impl;


import com.bartu.onlyhocams.api.request.NoteRequest;
import com.bartu.onlyhocams.api.request.PostRequest;
import com.bartu.onlyhocams.api.request.UserRequest;
import com.bartu.onlyhocams.api.response.JwtResponse;
import com.bartu.onlyhocams.api.response.StatusDTO;
import com.bartu.onlyhocams.dto.*;
import com.bartu.onlyhocams.entity.*;
import com.bartu.onlyhocams.entity.enums.Role;
import com.bartu.onlyhocams.entity.enums.Status;
import com.bartu.onlyhocams.entity.enums.Type;
import com.bartu.onlyhocams.exception.ExceptionCode;
import com.bartu.onlyhocams.exception.OhException;
import com.bartu.onlyhocams.repository.*;
import com.bartu.onlyhocams.security.JwtProvider;
import com.bartu.onlyhocams.security.JwtUserDetails;
import com.bartu.onlyhocams.service.DocumentUtil;
import com.bartu.onlyhocams.service.OHServices;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OHServicesImpl implements OHServices {
    private CategoryRepository categoryRepository;
    private DocumentRepository documentRepository;
    private LikeLogRepository likeLogRepository;
    private NoteRepository noteRepository;
    private PostRepository postRepository;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private NotePurhcaseHistoryRepository notePurhcaseHistoryRepository;

    public OHServicesImpl(CategoryRepository categoryRepository, DocumentRepository documentRepository, LikeLogRepository likeLogRepository, NoteRepository noteRepository, PostRepository postRepository, ReviewRepository reviewRepository, UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, NotePurhcaseHistoryRepository notePurhcaseHistoryRepository) {
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
        this.likeLogRepository = likeLogRepository;
        this.noteRepository = noteRepository;
        this.postRepository = postRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.notePurhcaseHistoryRepository = notePurhcaseHistoryRepository;
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
//        Long postId = postRepository.insertPost(request.getContent(), LocalDateTime.now(),jwtUser.getId(),request.getCategory());
        Post post = new Post();
        post.setContent(request.getContent());
        post.setPublishDate(LocalDateTime.now());
        post.setUser(jwtUser);
        post.setCategory(categoryRepository.getById(request.getCategory()));
        post = postRepository.save(post);
        if(Objects.nonNull(request.getDocumentIds())){
            documentRepository.linkDocumentToPost(post.getId(),request.getDocumentIds());
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

    @Override
    public UserDTO getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        Long id = jwtUser.getId();
        User user = userRepository.getById(id);
        if(Objects.isNull(user)){
            throw new OhException(ExceptionCode.USER_NOT_FOUND);
        }
        return user.toDTO();
    }

    @Override
    public StatusDTO uploadDocument(MultipartFile file) throws IOException {
        if(Objects.isNull(file)){
            throw new OhException(ExceptionCode.INVALID_REQUEST);
        }
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setData(DocumentUtil.compressImage(file.getBytes()));
        document = documentRepository.save(document);
return StatusDTO.builder().statusCode("S").msg("Success").additionalInformation(document.getId()).build();
    }

    @Override
    public List<PostDTO> getPostsByCategoryId(int lim, int off,Long categoryId,String keyword){
        List<Post> posts = new ArrayList<>();
        if(Objects.nonNull(keyword) && !keyword.isEmpty()){
            keyword = "%"+keyword.toLowerCase()+"%";
            posts =  postRepository.getPostsByKeyword(lim,off,categoryId,keyword);
        }else{
            posts =  postRepository.getPosts(lim,off,categoryId);
        }
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = post.toDTO();
            List<LikeLog> actions = likeLogRepository.getLikes(post.getId());
            for (LikeLog action : actions) {
                if(Objects.isNull(action.getType())){
                    postDTO.getComments().add(action.toCommentDTO());
                }else{
                    postDTO.getActions().add(action.toActionDTO());
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    User jwtUser = (User) auth.getPrincipal();
                    if(action.getUser().getId().equals(jwtUser.getId()) && action.getType().equals(Type.LIKE)){
                        postDTO.setIsLiked(true);
                    }
                    if(action.getUser().getId().equals(jwtUser.getId()) && action.getType().equals(Type.DISLIKE)) {
                        postDTO.setIsDisliked(true);
                    }
                }
            }
            List<Document> documents = documentRepository.getDocumentsByPostId(post.getId());
            for (Document document : documents) {
                postDTO.getDocuments().add(document.toDTO());
            }
            postDTOS.add(postDTO);
        }

        return postDTOS;
    }

    @Override
    public List<CategoryDTO> getCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(Category::toDTO).collect(Collectors.toList());
    }

    @Override
    public StatusDTO shareNote(NoteRequest request) {
        if(Objects.isNull(request)){
            throw new OhException(ExceptionCode.INVALID_REQUEST);
        }
        if(Objects.isNull(request.getContent())){
            throw new OhException(ExceptionCode.CONTENT_CANNOT_BE_EMPTY);
        }
        if(Objects.isNull(request.getTitle())){
            throw new OhException(ExceptionCode.TITLE_CANNOT_BE_EMPTY);
        }
        if(Objects.isNull(request.getDocumentId())){
            throw new OhException(ExceptionCode.DOCUMENT_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();

        Note note = new Note();
        note.setContent(request.getContent());
        note.setUser(jwtUser);
        note.setTitle(request.getTitle());
        note.setPublishDate(LocalDateTime.now());
        note.setStatus(Status.WAITING_APPROVE);
        note = noteRepository.save(note);
        documentRepository.linkDocumentToNote(note.getId(),request.getDocumentId());


        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }



    @Override
    public StatusDTO deleteNote(Long id) {
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }
        Note note = noteRepository.getById(id);
        if(Objects.isNull(note)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!jwtUser.getId().equals(note.getUser().getId()) && !jwtUser.getRole().equals(Role.ADMIN)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        reviewRepository.bulkDeleteByNoteIds(List.of(id));
        documentRepository.bulkDeleteByNoteIds(List.of(id));
        noteRepository.deleteById(id);

        return StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO approveNote(Long id){
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }
        Note note = noteRepository.getById(id);
        if(Objects.isNull(note)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!jwtUser.getRole().equals(Role.ADMIN)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        noteRepository.approveNoteById(id);
        return StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public List<NoteDTO> getNotes(int limit, int offset,String keyword,Boolean waiting,Boolean owned) {
        List<Note> notes = new ArrayList<>();
        if(Objects.nonNull(keyword) && !keyword.isEmpty()){
            keyword = "%"+keyword.toLowerCase()+"%";
            notes =  noteRepository.getNotesByKeyword(limit,offset,keyword);
        }else{
            notes =  noteRepository.getNotes(limit,offset);
        }
        if(Objects.nonNull(waiting) && waiting){
            notes = noteRepository.getWaitingNotes(limit,offset);
        }
        if(Objects.nonNull(owned) && owned){
            notes = noteRepository.getOwnNotes(limit,offset,((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        }
        List<NoteDTO> noteDTOS = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        for (Note note : notes) {
            NoteDTO noteDTO = note.toDTO();
            if(!CollectionUtils.isEmpty(notePurhcaseHistoryRepository.existsByNoteAndUser(note.getId(),jwtUser.getId())) || jwtUser.getRole().equals(Role.ADMIN)) {
                noteDTO.setIsPurchased(true);
            }else{
                noteDTO.setIsPurchased(false);
            }
            noteDTOS.add(noteDTO);
        }
        return noteDTOS;
    }

    @Override
    public NoteDTO getNoteFullDetail(Long id){
        Note note = noteRepository.getById(id);
        NoteDTO noteDTO = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(Objects.nonNull(note)){
            noteDTO = note.toDTO();
            List<Review> reviews = reviewRepository.getByNoteId(note.getId());
            for (Review review : reviews) {
                ReviewDTO reviewDTO = review.toDTO();
                if(review.getUser().getId().equals(jwtUser.getId()) || jwtUser.getRole().equals(Role.ADMIN)){
                    reviewDTO.setCanDelete(true);
                }
                noteDTO.getReviews().add(reviewDTO);
            }
            if(!CollectionUtils.isEmpty(notePurhcaseHistoryRepository.existsByNoteAndUser(id,jwtUser.getId())) || jwtUser.getRole().equals(Role.ADMIN)){
                noteDTO.setIsPurchased(true);
                Document document = documentRepository.getDocumentByNoteId(note.getId());
                noteDTO.setDocument(document.toDTO());
            }else{
                noteDTO.setIsPurchased(false);
            }
        }
        return noteDTO;
    }
    @Override
    public StatusDTO reviewNote(Long id, String content, Type type) {
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }
        Note note = noteRepository.getById(id);
        if(Objects.isNull(note)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }
        if(Objects.isNull(content)){
            throw new OhException(ExceptionCode.CONTENT_CANNOT_BE_EMPTY);
        }
        if(Objects.isNull(type)){
            throw new OhException(ExceptionCode.TYPE_CANNOT_BE_EMPTY);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        Review review = reviewRepository.getByNoteIdAndUserId(id,jwtUser.getId());
        if(Objects.isNull(review)){
            review = new Review();
        }
        review.setContent(content);
        review.setType(type);
        review.setUser(jwtUser);
        review.setNote(note);
        review = reviewRepository.save(review);
        return StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO purchaseNote(Long id) {
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }
        Note note = noteRepository.getById(id);
        if(Objects.isNull(note)){
            throw new OhException(ExceptionCode.NOTE_NOT_FOUND);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        User user = userRepository.getById(jwtUser.getId());
        if(user.getCredit().compareTo(BigDecimal.ONE)<0){
            throw new OhException(ExceptionCode.INSUFFICIENT_BALANCE);
        }
        NotePurhcaseHistory notePurhcaseHistory = new NotePurhcaseHistory();
        notePurhcaseHistory.setNote(note);
        notePurhcaseHistory.setUser(user);
        notePurhcaseHistoryRepository.save(notePurhcaseHistory);
        userRepository.updateBalance(user.getId(),user.getCredit().subtract(BigDecimal.ONE));
        return StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO deleteUser(Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!jwtUser.getRole().equals(Role.ADMIN)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        User user = userRepository.getById(id);
        if(Objects.isNull(user)){
            throw new OhException(ExceptionCode.USER_NOT_FOUND);
        }

        List<Long> postIds = postRepository.getPostsByUserId(id);
        likeLogRepository.bulkDeleteByPostIds(postIds);
        documentRepository.bulkDeleteByPostIds(postIds);
        postRepository.deletePostsByUserId(id);

        List<Long> noteIds = noteRepository.getNotesByUserId(id);
        reviewRepository.bulkDeleteByNoteIds(noteIds);
        documentRepository.bulkDeleteByNoteIds(noteIds);
        noteRepository.deleteNotesByUserId(id);

        userRepository.deleteById(id);
        return StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO deleteReview(Long id) {
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.REVIEW_NOT_FOUND);
        }
        Review review = reviewRepository.getById(id);
        if(Objects.isNull(review)){
            throw new OhException(ExceptionCode.REVIEW_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!jwtUser.getId().equals(review.getUser().getId()) && !jwtUser.getRole().equals(Role.ADMIN)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        reviewRepository.deleteById(id);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public StatusDTO deleteLikeLog(Long id) {
        if(Objects.isNull(id)){
            throw new OhException(ExceptionCode.COMMENT_NOT_FOUND);
        }
        LikeLog likeLog = likeLogRepository.getById(id);
        if(Objects.isNull(likeLog)){
            throw new OhException(ExceptionCode.COMMENT_NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User jwtUser = (User) auth.getPrincipal();
        if(!jwtUser.getId().equals(likeLog.getUser().getId()) && !jwtUser.getRole().equals(Role.ADMIN)){
            throw new OhException(ExceptionCode.USER_UNAUTHORIZED);
        }
        likeLogRepository.deleteById(id);
        return  StatusDTO.builder().statusCode("S").msg("Success").build();
    }

    @Override
    public List<UserDTO> getUsers(){
        List<User> users = userRepository.findAllUsers();
        return users.stream().map(User::toDTO).collect(Collectors.toList());
    }
}
