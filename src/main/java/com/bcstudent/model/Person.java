package com.bcstudent.model;

/**
 * Abstract base class representing a person in the system
 * Contains common fields and methods for both Students and Counselors
 */
public abstract class Person {
    private String name;
    private ContactInfo contactInfo;

    public Person(String name, ContactInfo contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", contactInfo=" + contactInfo +
                '}';
    }
}
