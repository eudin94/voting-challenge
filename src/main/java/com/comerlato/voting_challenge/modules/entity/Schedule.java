package com.comerlato.voting_challenge.modules.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer durationInSeconds;
    private String subject;
    private Boolean closed;
}
