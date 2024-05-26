package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.UserDTO;
import com.bartu.onlyhocams.entity.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private BigDecimal credit;

    public UserDTO toDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.getId());
        userDTO.setEmail(this.getEmail());
        userDTO.setName(this.getName());
        userDTO.setUsername(this.getUsername());
        userDTO.setRole(this.getRole());
        userDTO.setRoleStr(this.getRole().getDescription());
        return userDTO;
    }
}
