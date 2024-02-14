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
    @Column(name = "moodys_rating")
    private String moodysRating;
    @NotBlank(message = "Sand Rating is mandatory")
    @Column(name = "sand_p_rating")
    private String sandPRating;
    @NotBlank(message = "Fitch Rating is mandatory")
    @Column(name = "fitch_rating")
    private String fitchRating;
    @NotNull(message = "Order Number Rating is mandatory")
    @Min(value = 1, message = "Order Number Rating is mandatory")
    @Column(name = "order_number")
    private Integer orderNumber;
}
