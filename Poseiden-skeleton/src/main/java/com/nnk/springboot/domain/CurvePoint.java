package com.nnk.springboot.domain;

import jakarta.persistence.*;
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
    private Double term;
    private Double value;
    private Timestamp creationDate;
}
