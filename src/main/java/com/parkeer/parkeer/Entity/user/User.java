package com.parkeer.parkeer.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkeer.parkeer.dto.user.UserDTO;
import com.parkeer.parkeer.entity.vehicle.Vehicle;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

import java.util.Collections;
import java.util.List;

import static com.parkeer.parkeer.config.SecurityConfig.passwordEncoder;
import static java.util.Collections.emptyList;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "tb_user")
public class User {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String addressNumber;
    private String zipCode;
    private String phone;
    private String cpf;
    private String email;
    @JsonIgnore
    private String password;

    public User(UserDTO userDTO) {
        this.name = userDTO.name();
        this.address = userDTO.address();
        this.addressNumber = userDTO.addressNumber();
        this.zipCode = userDTO.zipCode();
        this.phone = userDTO.phone();
        this.cpf = userDTO.cpf();
        this.email = userDTO.email();
        this.password = passwordEncoder().encode(userDTO.password());
    }

    public User(Long id, String name, String address, String addressNumber,
                String phone, String cpf, String email, String password
    ) {
        this.name = name;
        this.address = address;
        this.addressNumber = addressNumber;
        this.phone = phone;
        this.cpf = cpf;
        this.email = email;
        this.password = passwordEncoder().encode(password);
    }

    public void setPassword(String password) {
        this.password = passwordEncoder().encode(password);
    }
}
