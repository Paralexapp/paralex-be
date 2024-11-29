package com.paralex.erp.entities;

import com.paralex.erp.enums.RegistrationLevel;
import com.paralex.erp.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
@DynamicUpdate
@DynamicInsert
@Component
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    @Column(name = "customerCode", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String customerCode;


    @Column(name = "email", unique = true, nullable = false, insertable = true, updatable = true)
    private String email;

    private String password;

    private UserType userType;

    private RegistrationLevel registrationLevel;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "otp_id", referencedColumnName = "id")
    private Otp otp;

    @Column(name = "phoneNumber", unique = true, nullable = true, insertable = true, updatable = true)
    @Setter
    private String phoneNumber;

    @Column(name = "photoUrl", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String photoUrl;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean enabled;
    @Override
    public boolean isEnabled() {

        return enabled;
    }

}
