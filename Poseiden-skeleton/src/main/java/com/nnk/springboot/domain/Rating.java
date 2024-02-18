package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = "Moodys Rating is mandatory")
    @Positive(message = "Order Number Rating is mandatory")
    @Column(name = "moodys_rating")
    private Integer moodysRating;
    @NotNull(message = "Sand Rating is mandatory")
    @Positive(message = "Order Number Rating is mandatory")
    @Column(name = "sand_p_rating")
    private Integer sandPRating;
    @NotNull(message = "Fitch Rating is mandatory")
    @Positive(message = "Order Number Rating is mandatory")
    @Column(name = "fitch_rating")
    private Integer fitchRating;
    @NotNull(message = "Order Number Rating is mandatory")
    @Positive(message = "Order Number Rating is mandatory")
    @Column(name = "order_number")
    private Integer orderNumber;
}
