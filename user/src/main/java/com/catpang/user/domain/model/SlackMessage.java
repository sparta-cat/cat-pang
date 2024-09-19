package com.catpang.user.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_slack_messages")
public class SlackMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slack_message_id")
    private UUID id;

    private String receiver;

    private String message;

    private LocalDateTime createdAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;
}
