package com.nikolay.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.nikolay.dao.DepartmentDao;
import com.nikolay.model.Department;
import com.nikolay.model.dto.ResponseDepartmentDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/test-dao-rest.xml"})
public class TestDepartmentRestDao {

  private static final Logger LOGGER = LogManager.getLogger();

  @Value("${department.endpoint}")
  private String url;

  @Value("${department.endpoint.with.id}")
  private String urlWithIdParam;

  @Autowired
  private DepartmentDao departmentRestDao;

  @Autowired
  private RestTemplate mockRestTemplate;

  private Department dep1;
  private ResponseDepartmentDto dep2;

  @Before
  public void setUp() {
    dep1 = new Department(1L, "New Department");
    dep2 = new ResponseDepartmentDto(1L, "Services", BigDecimal.valueOf(3249));
    LOGGER.error("execute: beforeTest()");
  }

  @Test
  public void testGetDepartmentById() {
    LOGGER.debug("test TestDepartmentRestDao: run testGetDepartmentById()");
    when(mockRestTemplate.getForObject(urlWithIdParam, ResponseDepartmentDto.class, 1L))
        .thenReturn(dep2);
    ResponseDepartmentDto department = departmentRestDao.getDepartmentById(1L);
    assertNotNull(department);
    assertEquals(1L, department.getId().longValue());
    verify(mockRestTemplate, times(1))
        .getForObject(urlWithIdParam, ResponseDepartmentDto.class, 1L);
  }

  @Test
  public void testGetAllDepartment() {
    LOGGER.debug("test TestDepartmentRestDao: run testGetAllDepartment()");
    when(mockRestTemplate.getForObject(url, ResponseDepartmentDto[].class)).thenReturn(
        new ResponseDepartmentDto[]{dep2, dep2});
    List<ResponseDepartmentDto> departmentList = departmentRestDao.getAllDepartments();
    assertNotNull(departmentList);
    assertEquals(2, departmentList.size());
    verify(mockRestTemplate, times(1)).getForObject(url, ResponseDepartmentDto[].class);
  }

  @Test
  public void testDeleteDepartment() {
    LOGGER.debug("test TestDepartmentRestDao: run testDeleteDepartment()");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
    when(mockRestTemplate.exchange(urlWithIdParam, HttpMethod.DELETE, entity, Boolean.class, 1L))
        .thenReturn(responseEntity);
    assertTrue(departmentRestDao.deleteDepartment(1L));
    verify(mockRestTemplate).exchange(urlWithIdParam, HttpMethod.DELETE, entity, Boolean.class, 1L);
  }

  @Test
  public void testUpdateDepartment() {
    LOGGER.debug("test TestDepartmentRestDao: run testUpdateDepartment()");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<?> entity = new HttpEntity<>(dep1, headers);
    ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
    when(mockRestTemplate.exchange(urlWithIdParam, HttpMethod.PUT, entity, Boolean.class, 1L))
        .thenReturn(responseEntity);
    assertTrue(departmentRestDao.updateDepartment(dep1));
    verify(mockRestTemplate).exchange(urlWithIdParam, HttpMethod.PUT, entity, Boolean.class, 1L);
  }

  @Test
  public void testSaveDepartment() {
    LOGGER.debug("test TestDepartmentRestDao: run testSaveDepartment()");
    when(mockRestTemplate.postForEntity(url, dep1, Long.class))
        .thenReturn(new ResponseEntity<>(1L, HttpStatus.FOUND));
    Long departmentId = departmentRestDao.saveDepartment(dep1);
    assertNotNull(departmentId);
    assertEquals(1L, departmentId.longValue());
    verify(mockRestTemplate, times(1)).postForEntity(url, dep1, Long.class);
  }

}
