package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Line> lines;
}
