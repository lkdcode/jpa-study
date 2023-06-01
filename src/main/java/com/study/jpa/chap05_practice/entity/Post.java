package com.study.jpa.chap05_practice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = {"hashTags"})
@EqualsAndHashCode(of = {"id"}) // pk 추가
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "post_no")
    private Long id;

    @Column(nullable = false) // NOT NULL
    private String writer; // 작성자

    @Column(nullable = false) // NOT NULL
    private String title; // 제목
    private String content; // 게시글 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate; // 작성 시간

    @UpdateTimestamp
    private LocalDateTime updateDate; // 수정 시간

    @OneToMany(mappedBy = "post") // 상대편의 맵핑 이름(필드이름)
    private List<HashTag> hashTags = new ArrayList<>();
}
