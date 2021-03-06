package com.nikolay.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The type Employee.
 *
 * @author Mikalai_Kazak @epam.com 10.12.2018
 */
public class Employee {

  private Long id;

  private Long departmentId;

  private String fullName;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthday;

  private BigDecimal salary;

  /**
   * Instantiates a new Employee.
   */
  public Employee() {
  }

  /**
   * Instantiates a new Employee.
   *
   * @param id the id
   * @param departmentId the department id
   * @param fullName the full name
   * @param birthday the birthday
   * @param salary the salary
   */
  public Employee(Long id, Long departmentId, String fullName, LocalDate birthday,
      BigDecimal salary) {
    this.id = id;
    this.departmentId = departmentId;
    this.fullName = fullName;
    this.birthday = birthday;
    this.salary = salary;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }
}


