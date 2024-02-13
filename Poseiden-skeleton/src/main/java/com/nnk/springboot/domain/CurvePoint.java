package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "curve_point")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer curveId;
    private Timestamp asOfDate;
    @NotNull(message = "Term is mandatory")
    @DecimalMin(value= "0.1", message = "Term is mandatory")
    private Double term;
    @NotNull(message = "Value is mandatory")
    @DecimalMin(value= "0.1", message = "Value is mandatory")
    private Double value;
    private Timestamp creationDate;
}
