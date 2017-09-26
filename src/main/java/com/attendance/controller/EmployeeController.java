package com.attendance.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.entity.Client;
import com.attendance.entity.Employee;
import com.attendance.exception.AttendanceException;
import com.attendance.repository.ClientRepository;
import com.attendance.repository.EmployeeRepository;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ClientRepository clientRepository;

	@RequestMapping(value = "/employees/{userid}/{clientid}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Employee>> getEmployees(@PathVariable("userid") String userid,
			@PathVariable("clientid") int clientid) throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : getEmployees::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");
		try {

			Client client = clientRepository.findById(clientid);
			List<Employee> emps = employeeRepository.findByClientAndManagerid(client, userid);

			return new ResponseEntity<Iterable<Employee>>(emps, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : getEmployees::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing getClients", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		HttpHeaders headers = new HttpHeaders();
		if (employee == null) {
			return new ResponseEntity<Employee>(HttpStatus.BAD_REQUEST);
		}
		employeeRepository.save(employee);
		headers.add("Employee Created  - ", String.valueOf(employee.getId()));
		return new ResponseEntity<Employee>(employee, headers, HttpStatus.CREATED);
	}

}