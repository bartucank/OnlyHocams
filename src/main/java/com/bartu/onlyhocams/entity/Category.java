package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.CategoryDTO;
import com.bartu.onlyhocams.entity.enums.Type;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    private String name;


    public CategoryDTO toDTO() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(this.id);
        categoryDTO.setName(this.name);
        return categoryDTO;
    }
}
