package com.mjvs.jgsp.model;

import javax.persistence.*;

@Entity
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "transport_type", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TransportType transportType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Stop stop;

    public Transport(TransportType transportType, Stop stop) {
        this.transportType = transportType;
        this.stop = stop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }
}
