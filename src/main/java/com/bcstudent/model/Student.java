package com.bcstudent.model;

/**
 * Class representing a Student in the system
 * Extends Person and adds student-specific fields
 */
public class Student extends Person {
    private String studentNumber;
    private String program;

    public Student(String name, ContactInfo contactInfo, String studentNumber, String program) {
        super(name, contactInfo);
        this.studentNumber = studentNumber;
        this.program = program;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentNumber='" + studentNumber + '\'' +
                ", program='" + program + '\'' +
                ", " + super.toString() +
                '}';
    }
}
