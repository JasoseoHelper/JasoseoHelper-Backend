package com.jasoseohelper.resume.entity;

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
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    @Column(nullable=false)
    private String resume_name;

    @CreationTimestamp
    @Column(nullable=false, updatable = false)
    private Timestamp c_date;

    @Column(nullable=false)
    private Timestamp d_date;

    @CreationTimestamp
    @Column(nullable=false)
    private Timestamp m_date;

    @ManyToOne
    @JoinColumn(name="uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
