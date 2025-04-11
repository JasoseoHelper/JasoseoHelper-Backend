package com.jasoseohelper.question.entity;

import com.jasoseohelper.resume.entity.Resume;
import com.jasoseohelper.user.entity.User;
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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid;

    @CreationTimestamp
    @Column(nullable=false, updatable = false)
    private Timestamp c_date;

    @Column
    private Long version;

    @ManyToOne
    @JoinColumn(name="rid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Resume resume;

    @ManyToOne
    @JoinColumn(name="uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void updateVersion(Long versionId){
        this.version = versionId;
    }
}


