package com.task_manager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "random_four_digits")
public class RandomFourDigits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer num;

    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "sent_to_user_id")
    private User sentTo;

    @Column(name = "token", unique = true)
    private String token;
}
