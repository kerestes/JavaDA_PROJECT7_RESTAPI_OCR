package com.nnk.springboot.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rule_name")
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String json;
    private String template;
    private String sqlStr;
    private String sqlPart;
}
