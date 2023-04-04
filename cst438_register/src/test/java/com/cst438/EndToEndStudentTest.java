package com.cst438;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:\\Users\\rshum\\Documents\\CSUMB\\CST438\\Project\\selenium\\chromedriver.exe";
	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_NAME = "Ian Cognito";
	public static final String TEST_USER_EMAIL = "icognito@csumb.edu";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	StudentRepository studentRepository;

	@Test
	public void addStudentTest() throws Exception {

		Student student = null;

		do {
			student = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (student != null) {
				studentRepository.delete(student);
			}
		} while (student != null);

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// Click Add Student Button
			driver.findElement(By.xpath("//button[@id='addStudentButton']")).click();
			Thread.sleep(SLEEP_DURATION);

			// Fill out student name and email. Then submit form.
			driver.findElement(By.xpath("//input[@name='studentName']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@name='studentEmail']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[@id='submitStudentButton']")).click();
			Thread.sleep(SLEEP_DURATION);

			// Check to see that added student is present in local database.
			student = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertNotNull(student, "Student was not inserted into database");
			assertTrue(student.getName().equals(TEST_USER_NAME), "Student was inserted into database but name is incorrect.");

			List<WebElement> paragraphs = driver.findElements(By.xpath("//p"));
			boolean emailFound = false;
			boolean nameFound = false;

			String emailMessage = String.format("Email: %s", TEST_USER_EMAIL);
			String nameMessage = String.format("Name: %s", TEST_USER_NAME);

			// Find add student confirmation message
			for(WebElement paragraph : paragraphs) {

				String formattedParagraph = paragraph.getText().toUpperCase();
				if (formattedParagraph.contains(emailMessage.toUpperCase())) {
					emailFound = true;
				}
				if (formattedParagraph.contains(nameMessage.toUpperCase())) {
					nameFound = true;
				}

				if (nameFound && emailFound) {
					break;
				}
			}

			// Check that name and email on confirmation message match the ones entered into form fields
			assertTrue(emailFound, "Student added to database but email not displayed on confirmation page");
			assertTrue(nameFound, "Student added to database but name not displayed on confirmation page");

		} catch (Exception ex) {
			throw ex;
		} finally {
			student = studentRepository.findByEmail(TEST_USER_EMAIL);

			if (student != null) {
				studentRepository.delete(student);
			}

			driver.quit();
		}
	}
}