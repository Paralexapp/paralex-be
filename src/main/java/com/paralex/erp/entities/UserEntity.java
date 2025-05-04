package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paralex.erp.enums.RegistrationLevel;
import com.paralex.erp.enums.UserType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")  // MongoDB-specific annotation
public class UserEntity implements UserDetails {

    @Id
    private String id;  // MongoDB uses String or ObjectId for the primary key

    private String name;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private String customerCode;

    private String walletId;

    private String businessId;

    private String email;

    private boolean isAccountBlocked;

    @JsonIgnore
    private String password;

    private UserType userType;

    private RegistrationLevel registrationLevel;

    @DBRef(lazy = true)
    @JsonIgnore
    private Otp otp;  // Using @DBRef for references to other documents

    private String phoneNumber;

    private String photoUrl;

    private String aboutMe;

    private LocalDateTime time;

    @DBRef
    private BailBondEntity bailBond;  // Reference to another document

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    private boolean enabled;

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
