package xyz.cupscoffee.hackathondwi.user.adapter.persistence.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User entity.
 *
 * <p>
 * This class represents a user in the database. It is the entity
 * representation of the
 * {@link xyz.cupscoffee.hackathondwi.user.domain.model.User User}
 * class.
 * </p>
 *
 * <p>
 * Implements the {@link UserDetails} interface of Spring Security.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code", name = "uk_user_code")
})
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String code;
    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;
    @Column(columnDefinition = "CHAR(60)", nullable = false)
    private String password;

    @Override
    public String getUsername() {
        return code;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
