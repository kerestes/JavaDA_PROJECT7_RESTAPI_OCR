package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Moodys Rating is mandatory")
    private String moodysRating;
    @NotBlank(message = "Sand Rating is mandatory")
    private String sandPRating;
    @NotBlank(message = "Fitch Rating is mandatory")
    private String fitchRating;
    @NotNull(message = "Order Number Rating is mandatory")
    @Min(value = 1, message = "Order Number Rating is mandatory")
    private Integer orderNumber;
}
