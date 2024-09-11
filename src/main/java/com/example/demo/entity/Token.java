package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "token")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "is_logged_out")
    private String loggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
