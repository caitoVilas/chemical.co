package com.ch.userservice.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/*
 * UserApp entity representing a user in the system.
 * Implements UserDetails for Spring Security integration.
 * Includes fields for user information and account status.
 * Uses Lombok annotations for boilerplate code reduction.
 *
 * @author caito
 *
 */
@Entity
@Table(name = "users")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter@Builder
public class UserApp implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles.isEmpty()) return null;
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getAuthority())).toList();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
