package com.attendance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.entity.Client;
import com.attendance.entity.Employee;

/**
 * @author harsha.patil
 *
 */

@Transactional
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer>, Serializable {
	
	Employee findById(int id);
	public Employee findByName(String name);
	public List<Employee> findByClientAndManagerid(Client client, String managerid);

	
}
