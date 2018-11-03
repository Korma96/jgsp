package com.mjvs.jgsp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Student extends Passenger {
    @Column(name = "index_number", unique = true, nullable = false)
    private String indexNumber;

    @Column(name = "faculty", unique = false, nullable = false)
    private String faculty;

    public Student(String username, String password, String firstName, String lastName, String email, String address, String indexNumber, String faculty) {
        super(username, password, firstName, lastName, email, address, PassengerType.STUDENT);

        this.indexNumber = indexNumber;
        this.faculty = faculty;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
