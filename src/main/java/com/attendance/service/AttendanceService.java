package com.attendance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.attendance.entity.Attendance;
import com.attendance.entity.DateOfMonth;

public class AttendanceService {

	@Autowired
	DateOfMonth dateOfMonth;

	@Autowired
	Attendance attendance;

	// 1,3,7,9,12 abscent
	public void storeAttendance(List<Integer> abscentList) {

		Map<Integer, String> dayMap = new HashMap<Integer, String>();
		dayMap.put(1, "date_1");
		dayMap.put(2, "date_2");
		dayMap.put(3, "date_3");
		dayMap.put(4, "date_4");
		dayMap.put(5, "date_5");
		dayMap.put(6, "date_6");
		dayMap.put(7, "date_7");
		dayMap.put(8, "date_8");
		dayMap.put(9, "date_9");
		dayMap.put(10, "date_10");
		dayMap.put(11, "date_11");
		dayMap.put(12, "date_12");
		dayMap.put(13, "date_13");
		dayMap.put(14, "date_14");
		dayMap.put(15, "date_15");
		dayMap.put(16, "date_16");
		dayMap.put(17, "date_17");

	}

}
