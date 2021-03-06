package com.nikolay.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nikolay.dao.EmployeeDao;
import com.nikolay.model.Employee;
import com.nikolay.model.dto.ResponseEmployeeDto;
import com.nikolay.service.exception.OperationFailedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/test-service-mock.xml"})
public class TestEmployeeService {

  public static final Logger LOGGER = LogManager.getLogger();

  private static final long CORRECT_AMOUNT_EMPLOYEES = 2L;
  private static final long CORRECT_EMPLOYEE_ID = 1L;
  private static final long CORRECT_EMPLOYEE_DEPARTMENT_ID = 3L;
  private static final String CORRECT_EMPLOYEE_DEPARTMENT_NAME = "Engineering";
  private static final String CORRECT_EMPLOYEE_FULL_NAME = "Clem Hudspith";
  private static final LocalDate CORRECT_EMPLOYEE_BIRTHDAY = LocalDate.of(1982, 4, 2);
  private static final BigDecimal CORRECT_EMPLOYEE_SALARY = BigDecimal.valueOf(810);
  private static final long NEW_EMPLOYEE_ID = 3L;
  private static final LocalDate DATE = LocalDate.of(1982, 4, 2);
  private static final LocalDate DATE_FROM = LocalDate.of(1982, 4, 2);
  private static final LocalDate DATE_TO = LocalDate.of(1991, 7, 20);
  private static final Long INCORRECT_EMPLOYEE_ID = -20L;

  @Autowired
  EmployeeDao employeeDaoMock;

  @Autowired
  EmployeeService employeeService;

  private Employee saveEmployee;
  private ResponseEmployeeDto responseEmployeeDto;
  private List<ResponseEmployeeDto> employees;

  @Before
  public void setUp() {

    responseEmployeeDto = new ResponseEmployeeDto(
        CORRECT_EMPLOYEE_ID,
        CORRECT_EMPLOYEE_DEPARTMENT_ID,
        CORRECT_EMPLOYEE_DEPARTMENT_NAME,
        CORRECT_EMPLOYEE_FULL_NAME,
        CORRECT_EMPLOYEE_BIRTHDAY,
        CORRECT_EMPLOYEE_SALARY
    );

    saveEmployee = new Employee(
        CORRECT_EMPLOYEE_ID,
        CORRECT_EMPLOYEE_DEPARTMENT_ID,
        CORRECT_EMPLOYEE_FULL_NAME,
        CORRECT_EMPLOYEE_BIRTHDAY,
        CORRECT_EMPLOYEE_SALARY
    );

    employees = Arrays.asList(responseEmployeeDto, responseEmployeeDto);

    LOGGER.error("execute: setUp()");
  }

  @After
  public void afterTest() {
    verifyNoMoreInteractions(employeeDaoMock);
    reset(employeeDaoMock);
    LOGGER.error("execute: afterTest()");
  }

  @Test
  public void testGetEmployeeById() {
    LOGGER.debug("test Service: run testGetEmployeeById()");
    when(employeeDaoMock.getEmployeeById(CORRECT_EMPLOYEE_ID)).thenReturn(responseEmployeeDto);

    ResponseEmployeeDto newEmployee = employeeService.getEmployeeById(CORRECT_EMPLOYEE_ID);

    verify(employeeDaoMock).getEmployeeById(CORRECT_EMPLOYEE_ID);
    assertNotNull(newEmployee);
    assertEquals(CORRECT_EMPLOYEE_ID, newEmployee.getId().longValue());
    assertEquals(CORRECT_EMPLOYEE_FULL_NAME, newEmployee.getFullName());
    assertEquals(CORRECT_EMPLOYEE_DEPARTMENT_ID, newEmployee.getDepartmentId().longValue());
    assertEquals(CORRECT_EMPLOYEE_DEPARTMENT_NAME, newEmployee.getDepartmentName());
    assertEquals(CORRECT_EMPLOYEE_SALARY, newEmployee.getSalary());
    assertEquals(CORRECT_EMPLOYEE_BIRTHDAY, newEmployee.getBirthday());
  }

  @Test
  public void testSaveEmployee() {
    LOGGER.debug("test Service: run testSaveEmployee()");
    when(employeeDaoMock.saveEmployee(saveEmployee)).thenReturn(NEW_EMPLOYEE_ID);

    Long employeeId = employeeService.saveEmployee(saveEmployee);

    verify(employeeDaoMock).saveEmployee(saveEmployee);
    assertNotNull(employeeId);
    assertEquals(NEW_EMPLOYEE_ID, employeeId.longValue());
  }

  @Test
  public void testUpdateEmployee() {
    LOGGER.debug("test Service: run testUpdateEmployee()");
    when(employeeDaoMock.updateEmployee(saveEmployee)).thenReturn(true);

    employeeService.updateEmployee(saveEmployee);

    verify(employeeDaoMock).updateEmployee(saveEmployee);
  }

  @Test
  public void testDeleteEmployee() {
    LOGGER.debug("test Service: run testDeleteEmployee()");
    when(employeeDaoMock.deleteEmployee(CORRECT_EMPLOYEE_ID)).thenReturn(true);

    employeeService.deleteEmployee(CORRECT_EMPLOYEE_ID);

    verify(employeeDaoMock).deleteEmployee(CORRECT_EMPLOYEE_ID);
  }

  @Test
  public void testGetAllEmployee() {
    LOGGER.debug("test Service: run testGetAllEmployee()");
    when(employeeDaoMock.getAllEmployees()).thenReturn(employees);

    List<ResponseEmployeeDto> employees = employeeService.getAllEmployees();

    verify(employeeDaoMock).getAllEmployees();
    assertNotNull(employees);
    assertEquals(CORRECT_AMOUNT_EMPLOYEES, employees.size());
  }


  @Test
  public void testGetEmployeeByDateOfBirthday() {
    LOGGER.debug("test Service: run testGetEmployeeByDateOfBirthday()");
    when(employeeDaoMock.getEmployeesByDateOfBirthday(DATE))
        .thenReturn(Collections.singletonList(responseEmployeeDto));

    List<ResponseEmployeeDto> employees = employeeService.getEmployeesByDateOfBirthday(DATE);

    assertNotNull(employees);
    verify(employeeDaoMock).getEmployeesByDateOfBirthday(DATE);
  }

  @Test
  public void testGetEmployeeBetweenDatesOfBirthday() {
    LOGGER.debug("test Service: run testGetEmployeeBetweenDatesOfBirthday()");
    when(employeeDaoMock.getEmployeesBetweenDatesOfBirthday(DATE_FROM, DATE_TO))
        .thenReturn(employees);

    List<ResponseEmployeeDto> employees = employeeService
        .getEmployeesBetweenDatesOfBirthday(DATE_FROM, DATE_TO);

    verify(employeeDaoMock).getEmployeesBetweenDatesOfBirthday(DATE_FROM, DATE_TO);
    assertNotNull(employees);
    assertEquals(CORRECT_AMOUNT_EMPLOYEES, employees.size());
  }

  @Test(expected = OperationFailedException.class)
  public void testGetEmployeeByIdException() {
    LOGGER.debug("test Service: run testGetEmployeeByIdException()");
    when(employeeDaoMock.getEmployeeById(INCORRECT_EMPLOYEE_ID))
        .thenThrow(OperationFailedException.class);

    ResponseEmployeeDto employee = employeeService.getEmployeeById(INCORRECT_EMPLOYEE_ID);

    assertNull(employee);
    verifyZeroInteractions(employeeDaoMock.getEmployeeById(CORRECT_EMPLOYEE_ID));
  }
}

