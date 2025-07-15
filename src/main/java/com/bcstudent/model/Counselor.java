package com.bcstudent.model;

/**
 * Class representing a Counselor in the system
 * Extends Person and adds counselor-specific fields
 */
public class Counselor extends Person {
    private String specialization;
    private String officeLocation;

    public Counselor(String name, ContactInfo contactInfo, String specialization, String officeLocation) {
        super(name, contactInfo);
        this.specialization = specialization;
        this.officeLocation = officeLocation;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    @Override
    public String toString() {
        return "Counselor{" +
                "specialization='" + specialization + '\'' +
                ", officeLocation='" + officeLocation + '\'' +
                ", " + super.toString() +
                '}';
    }
}
