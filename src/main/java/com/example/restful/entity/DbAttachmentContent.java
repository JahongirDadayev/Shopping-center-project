package com.example.restful.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DbAttachmentContent implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private byte[] bytes;

    @OneToOne(cascade = CascadeType.ALL)
    private DbAttachment dbAttachment;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
