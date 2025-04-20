package com.jasoseohelper.question.entity;

import com.jasoseohelper.redis.entity.RedisTempVersion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vid;

    @CreationTimestamp
    @Column(nullable=false)
    private Timestamp m_date;

    @Column(nullable=false)
    private String title;

    @Column
    private String guide;

    @Column(nullable=false)
    private String content;

    @Column
    private String feedback;

    @ManyToOne
    @JoinColumn(name="qid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    public void updateFrom(RedisTempVersion temp) {
        this.title = temp.getTitle();
        this.guide = temp.getGuide();
        this.content = temp.getContent();
        this.feedback = temp.getFeedback();
    }
}
