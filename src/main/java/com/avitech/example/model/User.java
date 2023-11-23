package com.avitech.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="SUSERS")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Column(name="USER_ID", nullable=false, unique=true)
    private Long id;

    @Column(name="USER_GUID")
    private String guid;

    @Column(name="USER_NAME")
    private String name;

}
