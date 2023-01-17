package ru.sibintek.vme.request.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestTsKey implements Serializable {

    @Column(nullable = false)
    private Long requestId;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private Long driverId;
}
