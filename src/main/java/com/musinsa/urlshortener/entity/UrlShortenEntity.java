package com.musinsa.urlshortener.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "url_shorten")
public class UrlShortenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_count")
    private int requestCount;

    @Column(name = "origin_url")
    private String originUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date", updatable = false)
    private Date lastModifiedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate= new Date();
        this.preUpdate();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedDate = new Date();
    }

}
