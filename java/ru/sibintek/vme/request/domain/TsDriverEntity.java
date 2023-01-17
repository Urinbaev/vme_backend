package ru.sibintek.vme.request.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sibintek.vme.vehicle.domain.Vehicle;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ref_requests_vehicles", schema = "vme")
public class TsDriverEntity {

    @EmbeddedId
    private RequestTsKey id;

    @JsonBackReference("request-ref")
    @MapsId("requestId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "request_id", insertable = false, updatable = false)
    private RequestBaseEntity request;

    @JsonBackReference(("vehicle-ref"))
    @MapsId("vehicleId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    private Vehicle vehicle;
}
