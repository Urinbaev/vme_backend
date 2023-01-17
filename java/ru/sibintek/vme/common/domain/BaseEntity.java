package ru.sibintek.vme.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static ru.sibintek.vme.common.domain.EntityStatus.ACTIVE;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @Setter(onMethod = @__({@JsonIgnore}))
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @CreatedBy
    @Column(updatable = false)
    public String createdBy;

    @JsonIgnore
    @LastModifiedBy
    @Column
    public String lastModifiedBy;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false)
    private Date createdAt;

    @JsonIgnore
    @LastModifiedDate
    @Column
    private Date updatedAt;

    @JsonIgnore
    @Column
    @Enumerated(EnumType.STRING)
    private EntityStatus status = ACTIVE;

}
