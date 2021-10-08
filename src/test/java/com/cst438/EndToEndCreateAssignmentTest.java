package com.cst438;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

public class EndToEndCreateAssignmentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/justinmello/Desktop/chromedriver";

	public static final String URL = "https://mello-cst438-grade-fe.herokuapp.com/";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	public static final String TEST_ASSIGNMENT_NAME = "New Test Assignment";
	public static final String TEST_ASSIGNMENT_DATE = "11-5-2021";
	public static final int TEST_ASSIGNMENT_ID = 1;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void createAssignmentTest() throws Exception {
		 

		//Deletes existing students in the database.
        Assignment x = null;
          do {
              x = assignmentRepository.findById(TEST_ASSIGNMENT_ID);
              if (x != null)
                  assignmentRepository.delete(x);
          } while (x != null);
		


		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);

		try {
			
			// Locate and click New Assignment button
			driver.findElement(By.xpath("//a[last()]")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Locate text fields and enter the data
			WebElement we = driver.findElement(By.xpath("//input[@name='courseName']"));
			we.sendKeys("40443");
			we = driver.findElement(By.xpath("//input[@name='assignmentName']"));
			we.sendKeys(TEST_ASSIGNMENT_NAME);
			we = driver.findElement(By.xpath("//input[@name='dueDate']"));
			we.sendKeys(TEST_ASSIGNMENT_DATE);
			
			
			driver.findElement(By.xpath("//input[@name='submit']")).click();
			Thread.sleep(SLEEP_DURATION);

			//Verify
			Assignment a = assignmentRepository.findById(TEST_ASSIGNMENT_ID);
			assertNotNull(a); 

		} catch (Exception ex) {
			throw ex;
		} finally {

			//Clean up
			Assignment a = assignmentRepository.findById(TEST_ASSIGNMENT_ID);
			if(a !=null)
				assignmentRepository.delete(a);
	         
			driver.quit();
		}

	}
}
