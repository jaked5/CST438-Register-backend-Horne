package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.cst438.controller.ScheduleController;
import com.cst438.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.StudentController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {

    static final String URL = "http://localhost:8080";
    public static final int TEST_STUDENT_ID = 13579;
    public static final String TEST_STUDENT_NAME = "JAKE";
    public static final String TEST_STUDENT_EMAIL = "jahorne@csumb.edu";
    public static final String STUDENT_ENDPOINT = "/student";
    public static final String HOLD_ENDPOINT = "/hold";
    public static final String RELEASE_ENDPOINT = "/release";
    private static Student student;

    @MockBean
    StudentRepository studentRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void addStudentExist() throws Exception {

        MockHttpServletResponse response;

        Student student = new Student();
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setEmail(TEST_STUDENT_EMAIL);

        List<Student> students = new java.util.ArrayList<>();
        students.add(student);

        // given  -- stubs for database repositories that return test data
        given(studentRepository.save(any(Student.class))).willReturn(student);
        given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
        given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);

        // create the DTO (data transfer object) for the course to add.  primary key course_id is 0.
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.studentEmail = TEST_STUDENT_EMAIL;
        studentDTO.studentName = TEST_STUDENT_NAME;

        // then do an http post request with body of courseDTO as JSON
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify that return status = OK (value 200)
        assertEquals(200, response.getStatus());

        // verify that returned data has same student id
        StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
        assertEquals( TEST_STUDENT_ID , result.student_id);

        // verify that repository save method was called.
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void addStudentNoExist() throws Exception {

        MockHttpServletResponse response;

        Student student = new Student();
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setEmail(TEST_STUDENT_EMAIL);

        List<Student> students = new java.util.ArrayList<>();
        students.add(student);

        // given  -- stubs for database repositories that return test data
        given(studentRepository.save(any(Student.class))).willReturn(student);
        given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
        given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);

        // create the DTO (data transfer object) for the course to add.  primary key course_id is 0.
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.studentEmail = TEST_STUDENT_EMAIL;
        studentDTO.studentName = TEST_STUDENT_NAME;

        // then do an http post request with body of studentDTO as JSON
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(asJsonString(studentDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verify that return status = NOT OK (value 400)
        assertEquals(400, response.getStatus());

        // verify that repository save method was not used since no values.
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    public void holdExist() throws Exception {

        MockHttpServletResponse response;

        Student student = new Student();
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setEmail(TEST_STUDENT_EMAIL);

        List<Student> students = new java.util.ArrayList<>();
        students.add(student);

        // given  -- stubs for database repositories that return test data
        given(studentRepository.save(any(Student.class))).willReturn(student);
        given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
        given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);

        // create the DTO (data transfer object) for the course to add.  primary key course_id is 0.
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.studentEmail = TEST_STUDENT_EMAIL;
        studentDTO.studentName = TEST_STUDENT_NAME;

        // then do an http post request with body of studentDTO as JSON
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .put(STUDENT_ENDPOINT+ HOLD_ENDPOINT + "/" + TEST_STUDENT_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(asJsonString(student)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // verifies that student has hold status
        verify(studentRepository, times(1)).findById(TEST_STUDENT_ID);

    }

    @Test
    void releaseExist() throws Exception {
        MockHttpServletResponse response;

        Student student = new Student();
        student.setStudent_id(TEST_STUDENT_ID);
        student.setName(TEST_STUDENT_NAME);
        student.setEmail(TEST_STUDENT_EMAIL);

        List<Student> students = new java.util.ArrayList<>();
        students.add(student);

        // given  -- stubs for database repositories that return test data
        given(studentRepository.save(any(Student.class))).willReturn(student);
        given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
        given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);

        // create the DTO (data transfer object) for the course to add.  primary key course_id is 0.
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.studentEmail = TEST_STUDENT_EMAIL;
        studentDTO.studentName = TEST_STUDENT_NAME;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(
                STUDENT_ENDPOINT+ RELEASE_ENDPOINT + "/" + TEST_STUDENT_ID);
        mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

        verify(studentRepository, times(1)).findById(TEST_STUDENT_ID);
    }

    private static String asJsonString(final Object obj) {
        try {

            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T  fromJsonString(String str, Class<T> valueType ) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
