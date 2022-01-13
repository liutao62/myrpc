package org.hut.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Hello implements Serializable {
    private String name;
    private String age;
}
