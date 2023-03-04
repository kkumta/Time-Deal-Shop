package com.kkumta.timedeal.domain;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class User {
    
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;
    
    @Column(nullable = false)
    @Size(min = 2, max = 20)
    private String name;
    
    @Column(nullable = false)
    @Size(min = 5, max = 50)
    private String email;
    
    @Column(nullable = false)
    @Size(min = 8, max = 50)
    private String password;
    
    @Column(nullable = false)
    private String userType;
    
    @Column(nullable = false)
    @Size(max = 20)
    private String phoneNumber;
    
    @Column(nullable = false)
    @Size(max = 100)
    private String address;
    
    @Builder
    public User(String name, String email, String password, String userType, String phoneNumber,
                String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}