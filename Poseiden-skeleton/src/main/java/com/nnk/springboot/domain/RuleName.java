package com.nnk.springboot.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotBlank(message = "Json is mandatory")
    private String json;
    @NotBlank(message = "Template is mandatory")
    private String template;
    @NotBlank(message = "SQL String is mandatory")
    @Column(name = "sql_str")
    private String sqlStr;
    @NotBlank(message = "SQL Part is mandatory")
    @Column(name = "sql_part")
    private String sqlPart;
}
