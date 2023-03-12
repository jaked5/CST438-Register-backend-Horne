package com.cst438.domain;

public class StudentDTO {
    public int student_id;
    public String studentName;
    public String studentEmail;
    public String studentStatus;
    public int studentStatusCode;

    // empty constructor of student
    public StudentDTO() {
        this.studentName = null;
        this.studentEmail = null;
        this.studentStatus = null;
        this.studentStatusCode = 0;
    }

    //  constructor of student with name and email
    public StudentDTO(String studentName, String studentEmail) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public StudentDTO(String studentName, String studentEmail, String studentStatus, int studentCode) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentStatus = studentStatus;
        this.studentStatusCode = studentCode;
    }

    @Override
    public String toString() {
        return "StudentDTO [studentName= " + studentName + ", studentEmail= " + studentEmail + ", studentStatus= "
                + studentStatus + ", studentCode= " + studentStatusCode + "]";
    }

    // method to check if variables are equal to each other
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        StudentDTO tempStudent = (StudentDTO) obj;
        if (student_id != tempStudent .student_id)
            return false;

        if (studentEmail == null) {
            if (tempStudent.studentEmail != null)
                return false;
        } else if (!studentEmail.equals(tempStudent.studentEmail))
            return false;

        if (studentName == null) {
            if (tempStudent.studentName != null)
                return false;
        } else if (!studentName.equals(tempStudent.studentName))
            return false;

        if (studentStatus == null) {
            if (tempStudent.studentStatus != null)
                return false;
        } else if (!studentStatus.equals(tempStudent.studentStatus))
            return false;

        if (studentStatusCode != tempStudent.studentStatusCode)
            return false;

        return true;
    }
}