package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.DocumentDTO;
import com.bartu.onlyhocams.entity.enums.Status;
import com.bartu.onlyhocams.service.DocumentUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    
    private String fileName;
    private String fileType;


    private byte[] data;


    @ManyToOne
    @JoinColumn(name = "post_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_DOCUMENT_POST"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "note_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_DOCUMENT_NOTE"))
    private Note note;

    public DocumentDTO toDTO(){
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(this.id);
        documentDTO.setFileName(this.fileName);
        documentDTO.setFileType(this.fileType);
        documentDTO.setData(DocumentUtil.decompressImage(this.data));
        return documentDTO;
    }
}
