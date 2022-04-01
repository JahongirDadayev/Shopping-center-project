package com.example.restful.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DbProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    private DbCategory dbCategory;

    @OneToMany
    private List<DbAttachment> dbAttachments;

    private String qrcode;

    @ManyToOne
    private DbMeasurement dbMeasurement;
}
