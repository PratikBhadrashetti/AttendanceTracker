package com.attendance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;

/**
 * @author harsha.patil
 *
 */

@Transactional
@Repository
public interface AttendanceRepository extends CrudRepository<Attendance, Integer>, Serializable {
	public List<Attendance> findByEmployee(Employee emp);

	public Attendance findByEmployeeAndMonthAndYear(Employee emp, String month, String year);
	
	

}