package com.bartu.onlyhocams.dto;

import com.bartu.onlyhocams.entity.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDTO {
    private Long id;

    private String title;
    private LocalDateTime publishDate;
    private String formattedDate;
    private UserDTO user;
    private DocumentDTO document;
    private List<ReviewDTO> reviews;
    private Boolean isPurchased;
    private Status status;
}
