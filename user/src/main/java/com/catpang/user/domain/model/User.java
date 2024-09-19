package com.catpang.user.domain.model;

import com.catpang.core.domain.model.RoleEnum;
import com.catpang.core.domain.model.auditing.Timestamped;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted is FALSE")
@Table(name = "p_users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(unique = true, nullable = false)
    private String slackId;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlackMessage> messages = new ArrayList<>();

    /**
     * 슬랙 메시지 보냈을 때 사용되는 메서드
     */
    public void addSlackMessage(SlackMessage message) {
        messages.add(message);
        message.setSender(this);
    }

}
