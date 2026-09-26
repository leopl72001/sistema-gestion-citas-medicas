package com.mediconnect.auth.domain;

import java.util.UUID;
import com.mediconnect.shared.audit.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name="app_users")
public class UserEntity extends AuditableEntity {
    @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
    @Column(nullable=false,unique=true,length=180) private String email;
    @Column(nullable=false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private Role role;
    @Column(nullable=false) private boolean enabled=true;
    protected UserEntity(){}
    public UserEntity(String email,String passwordHash,Role role){this.email=email;this.passwordHash=passwordHash;this.role=role;this.enabled=true;}
    public UUID getId(){return id;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public Role getRole(){return role;} public boolean isEnabled(){return enabled;}
    public void setEmail(String email){this.email=email;} public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;} public void setRole(Role role){this.role=role;} public void setEnabled(boolean enabled){this.enabled=enabled;}
}
