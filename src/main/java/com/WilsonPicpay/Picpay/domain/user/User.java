package com.WilsonPicpay.Picpay.domain.user;

import com.WilsonPicpay.Picpay.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(name = "cpf", unique = true)
    private String document;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO user) {
        this.firstName =   user.firstName();
        this.lastName = user.lastName();
        this.balance = user.balance();
        this.userType = user.userType();
        this.email = user.email();
        this.password = user.password();
        this.document = user.document();
    }
}
