package com.mediconnect.patient.domain;

import java.time.LocalDate; import java.util.UUID;
import com.mediconnect.auth.domain.UserEntity; import com.mediconnect.shared.audit.AuditableEntity; import jakarta.persistence.*;
@Entity @Table(name="patients")
public class PatientEntity extends AuditableEntity {
    @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
    @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false,unique=true) private UserEntity user;
    @Column(nullable=false,length=80) private String firstName; @Column(nullable=false,length=80) private String lastName; @Column(nullable=false) private LocalDate birthDate; @Column(length=30) private String phone; @Column(length=255) private String address;
    protected PatientEntity(){}
    public PatientEntity(UserEntity user,String firstName,String lastName,LocalDate birthDate,String phone,String address){this.user=user;this.firstName=firstName;this.lastName=lastName;this.birthDate=birthDate;this.phone=phone;this.address=address;}
    public UUID getId(){return id;} public UserEntity getUser(){return user;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public LocalDate getBirthDate(){return birthDate;} public String getPhone(){return phone;} public String getAddress(){return address;}
    public void updateProfile(String firstName,String lastName,LocalDate birthDate,String phone,String address){this.firstName=firstName;this.lastName=lastName;this.birthDate=birthDate;this.phone=phone;this.address=address;}
}
