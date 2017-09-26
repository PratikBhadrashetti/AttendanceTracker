package com.attendance.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.attendance.constants.AttendanceConstants;
import com.attendance.constants.AttendanceStatus;
import com.attendance.entity.Attendance;
import com.attendance.entity.Client;
import com.attendance.entity.DateOfMonth;
import com.attendance.entity.Employee;
import com.attendance.entity.User;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.ClientRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.UserRepository;
import com.attendance.utils.DateUtils;
import com.attendance.utils.EmailUtil;

@Component
@Transactional
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	Employee employee;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	Client client;

	@Autowired
	Attendance attendence;

	@Autowired
	User user;

	@Autowired
	DateOfMonth date;

	@Resource
	ClientRepository clientRepository;

	@Resource
	AttendanceRepository attendenceRepository;

	@Resource
	EmployeeRepository employeeRepository;

	@Resource
	UserRepository userRepository;

	@Resource
	Environment environment;

	public String insertData(DateOfMonth dateObjForDb, String splitArrayFromSubject[], String mailId, String managerId) {
	
		String empName = splitArrayFromSubject[0];
		String empId = splitArrayFromSubject[1];
		int pKey = Integer.parseInt(empId);
		String clientName = splitArrayFromSubject[2];
		String month1 = splitArrayFromSubject[3];
		String year1 = splitArrayFromSubject[4];
		logger.info("Inserting Attendence for the Employee " + empName + " for the Month " + month1);
		employee.setId(pKey);
		employee.setEmpid(empId);
		if (clientRepository.findByName(clientName) == null) {
			employee.setClient(client);
		} else {
			client = clientRepository.findByName(clientName);
			employee.setClient(client);
		}

		employee.setName(empName);
		
		employee.setSubmitStatus(AttendanceStatus.Submitted+"");
		employee.setEmailid(mailId);
		employee.setManagerid(managerId);
		attendence.setDate(dateObjForDb);
		attendence.setEmployee(employee);
		attendence.setMonth(month1);
		attendence.setSubmitStatus(AttendanceStatus.Submitted+"");
		attendence.setYear(year1);
		Set<Attendance> empAttendances = new HashSet<Attendance>();

		empAttendances.add(attendence);
		employee.setEmpAttendances(empAttendances);
		List<Employee> empList = new ArrayList<Employee>();
		empList.add(employee);

		client.setEmployees(empList);
		clientRepository.save(client);
		logger.info("Insertion Successful");
		return "success";
	}

	public String insertManagersAndClients(int mngrCount) {

		int count = 1;
		for (int i = 0; i <= mngrCount - 1; i++) {
			List<Client> list = new ArrayList<>();
			Random rand = new Random();
			String name = "manager" + count;
			String manager = environment.getRequiredProperty(name);
			String managerDetails[] = manager.split(",");
			User userFromProperty = new User();
			User userFromDb = userRepository.findByUsername(managerDetails[0]);
			if (userFromDb == null) {
				userFromProperty.setId(rand.nextInt(50) + 1);
				userFromProperty.setPassword(managerDetails[2]);
				userFromProperty.setUsermail(managerDetails[1]);
				userFromProperty.setUsername(managerDetails[0]);
			} else {
				userFromProperty = userRepository.findByUsername(managerDetails[0]);
			}

			String clientArray[] = environment.getRequiredProperty(managerDetails[0] + ".Clients").split(",");
			logger.info("Manager Name " + managerDetails[0] + " and Clients " + Arrays.asList(clientArray));
			logger.debug(clientArray.length + " Length of client");

			for (int cl = 0; cl < clientArray.length; cl++) {
				if (clientRepository.findByName(clientArray[cl]) == null) {
					logger.debug("###########  Need to insert data " + clientArray[cl]);
					Client newClientForDB = new Client();
					Random rand1 = new Random();
					newClientForDB.setId(rand1.nextInt(50) + 1);
					newClientForDB.setLocation("Bangalore");
					newClientForDB.setName(clientArray[cl]);
					newClientForDB.setUser(userFromProperty);
					list.add(newClientForDB);
				} else {
					Client newClientForDBNewOne = clientRepository.findByName(clientArray[cl]);
					//newClientForDBNewOne.setUser(userFromProperty);
					logger.debug("###########  From Already inserted data " + newClientForDBNewOne.getName());
					list.add(newClientForDBNewOne);
				}

			}

			userFromProperty.setClients(list);
			userRepository.save(userFromProperty);

			count = count + 1;
		}

		return "success";
	}

/*	private DateOfMonth getDateValue(List<String> l, String month, int year) {

		// With fixed month and year
		logger.info("###################################### Final List "+l);
		logger.info("Year And Month: " + year + " " + dateUtils.getMonthNumber(month));
		List<String> listOfWeekOffs = GetWeekoffList(year, dateUtils.getMonthNumber(month));
		logger.info("Weekoff List:"+listOfWeekOffs);
		// getting from mail
		// List<String> listOfWeekOffs = GetWeekoffList(year,
		// Calendar.getInstance().get(Calendar.MONTH));
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		// date.setId(n);
		for (int i = 1; i <= 31; i++) {
			
			switch (i) {
			case 1:
				if (l.contains(String.valueOf(i)) && i == 1) {
					date.setDate_1(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_1(AttendanceConstants.PRESENT);
				}
				break;
			case 2:
				if (l.contains(String.valueOf(i)) && i == 2) {
					date.setDate_2(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_2(AttendanceConstants.PRESENT);
				}
				break;
			case 3:
				if (l.contains(String.valueOf(i)) && i == 3) {
					date.setDate_3(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_3(AttendanceConstants.PRESENT);
				}
				break;
			case 4:
				if (l.contains(String.valueOf(i)) && i == 4) {
					date.setDate_4(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_4(AttendanceConstants.PRESENT);
				}
				break;
			case 5:
				if (l.contains(String.valueOf(i)) && i == 5) {
					date.setDate_5(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_5(AttendanceConstants.PRESENT);
				}
				break;
			case 6:
				if (l.contains(String.valueOf(i)) && i == 6) {
					date.setDate_6(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_6(AttendanceConstants.PRESENT);
				}
				break;
			case 7:
				if (l.contains(String.valueOf(i)) && i == 7) {
					date.setDate_7(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_7(AttendanceConstants.PRESENT);
				}
				break;
			case 8:
				if (l.contains(String.valueOf(i)) && i == 8) {
					date.setDate_8(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_8(AttendanceConstants.PRESENT);
				}
				break;
			case 9:
				if (l.contains(String.valueOf(i)) && i == 9) {
					date.setDate_9(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_9(AttendanceConstants.PRESENT);
				}
				break;
			case 10:
				if (l.contains(String.valueOf(i)) && i == 10) {
					date.setDate_10(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_10(AttendanceConstants.PRESENT);
				}
				break;
			case 11:
				if (l.contains(String.valueOf(i)) && i == 11) {
					date.setDate_11(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_11(AttendanceConstants.PRESENT);
				}
				break;
			case 12:
				if (l.contains(String.valueOf(i)) && i == 12) {
					date.setDate_12(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_12(AttendanceConstants.PRESENT);
				}
				break;
			case 13:
				if (l.contains(String.valueOf(i)) && i == 13) {
					date.setDate_13(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_13(AttendanceConstants.PRESENT);
				}
				break;
			case 14:
				if (l.contains(String.valueOf(i)) && i == 14) {
					date.setDate_14(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_14(AttendanceConstants.PRESENT);
				}
				break;
			case 15:
				if (l.contains(String.valueOf(i)) && i == 15) {
					date.setDate_15(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_15(AttendanceConstants.PRESENT);
				}
				break;
			case 16:
				if (l.contains(String.valueOf(i)) && i == 16) {
					date.setDate_16(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_16(AttendanceConstants.PRESENT);
				}
				break;
			case 17:
				if (l.contains(String.valueOf(i)) && i == 17) {
					date.setDate_17(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_17(AttendanceConstants.PRESENT);
				}
				break;
			case 18:
				if (l.contains(String.valueOf(i)) && i == 18) {
					date.setDate_18(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_18(AttendanceConstants.PRESENT);
				}
				break;
			case 19:
				if (l.contains(String.valueOf(i)) && i == 19) {
					date.setDate_19(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_19(AttendanceConstants.PRESENT);
				}
				break;
			case 20:
				if (l.contains(String.valueOf(i)) && i == 20) {
					date.setDate_20(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_20(AttendanceConstants.PRESENT);
				}
				break;
			case 21:
				if (l.contains(String.valueOf(i)) && i == 21) {
					date.setDate_21(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_21(AttendanceConstants.PRESENT);
				}
				break;
			case 22:
				if (l.contains(String.valueOf(i)) && i == 22) {
					date.setDate_22(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_22(AttendanceConstants.PRESENT);
				}
				break;
			case 23:
				if (l.contains(String.valueOf(i)) && i == 23) {
					date.setDate_23(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_23(AttendanceConstants.PRESENT);
				}
				break;
			case 24:
				if (l.contains(String.valueOf(i)) && i == 24) {
					date.setDate_24(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_24(AttendanceConstants.PRESENT);
				}
				break;
			case 25:
				if (l.contains(String.valueOf(i)) && i == 25) {
					date.setDate_25(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_25(AttendanceConstants.PRESENT);
				}
				break;
			case 26:
				if (l.contains(String.valueOf(i)) && i == 26) {
					date.setDate_26(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_26(AttendanceConstants.PRESENT);
				}
				break;
			case 27:
				if (l.contains(String.valueOf(i)) && i == 27) {
					date.setDate_27(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_27(AttendanceConstants.PRESENT);
				}
				break;
			case 28:
				if (l.contains(String.valueOf(i)) && i == 28) {
					date.setDate_28(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_28(AttendanceConstants.PRESENT);
				}
				break;
			case 29:
				if (l.contains(String.valueOf(i)) && i == 29) {
					date.setDate_29(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_29(AttendanceConstants.PRESENT);
				}
				break;
			case 30:
				if (l.contains(String.valueOf(i)) && i == 30) {
					date.setDate_30(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_30(AttendanceConstants.PRESENT);
				}
				break;
			case 31:
				if (l.contains(String.valueOf(i)) && i == 31) {
					date.setDate_31(AttendanceConstants.ABSCENT);
				} else {
					date.setDate_31(AttendanceConstants.PRESENT);
				}
				break;
			}

			if (listOfWeekOffs.contains(i)) {
				switch (i) {
				case 1:
					if (listOfWeekOffs.contains(i) && i == 1) {
						date.setDate_1(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 2:
					if (listOfWeekOffs.contains(i) && i == 2) {
						date.setDate_2(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 3:
					if (listOfWeekOffs.contains(String.valueOf(i)) && i == 3) {
						date.setDate_3(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 4:
					if (listOfWeekOffs.contains(i) && i == 4) {
						date.setDate_4(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 5:
					if (listOfWeekOffs.contains(i) && i == 5) {
						date.setDate_5(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 6:
					if (listOfWeekOffs.contains(i) && i == 6) {
						date.setDate_6(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 7:
					if (listOfWeekOffs.contains(i) && i == 7) {
						date.setDate_7(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 8:
					if (listOfWeekOffs.contains(i) && i == 8) {
						date.setDate_8(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 9:
					if (listOfWeekOffs.contains(i) && i == 9) {
						date.setDate_9(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 10:
					if (listOfWeekOffs.contains(i) && i == 10) {
						date.setDate_10(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 11:
					if (listOfWeekOffs.contains(i) && i == 11) {
						date.setDate_11(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 12:
					if (listOfWeekOffs.contains(i) && i == 12) {
						date.setDate_12(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 13:
					if (listOfWeekOffs.contains(i) && i == 13) {
						date.setDate_13(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 14:
					if (listOfWeekOffs.contains(i) && i == 14) {
						date.setDate_14(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 15:
					if (listOfWeekOffs.contains(i) && i == 15) {
						date.setDate_15(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 16:
					if (listOfWeekOffs.contains(i) && i == 16) {
						date.setDate_16(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 17:
					if (listOfWeekOffs.contains(i) && i == 17) {
						date.setDate_17(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 18:
					if (listOfWeekOffs.contains(i) && i == 18) {
						date.setDate_18(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 19:
					if (listOfWeekOffs.contains(i) && i == 19) {
						date.setDate_19(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 20:
					if (listOfWeekOffs.contains(i) && i == 20) {
						date.setDate_20(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 21:
					if (listOfWeekOffs.contains(i) && i == 21) {
						date.setDate_21(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 22:
					if (listOfWeekOffs.contains(i) && i == 22) {
						date.setDate_22(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 23:
					if (listOfWeekOffs.contains(i) && i == 23) {
						date.setDate_23(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 24:
					if (listOfWeekOffs.contains(i) && i == 24) {
						date.setDate_24(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 25:
					if (listOfWeekOffs.contains(i) && i == 25) {
						date.setDate_25(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 26:
					if (listOfWeekOffs.contains(i) && i == 26) {
						date.setDate_26(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 27:
					if (listOfWeekOffs.contains(i) && i == 27) {
						date.setDate_27(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 28:
					if (listOfWeekOffs.contains(i) && i == 28) {
						date.setDate_28(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 29:
					if (listOfWeekOffs.contains(i) && i == 29) {
						date.setDate_29(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 30:
					if (listOfWeekOffs.contains(i) && i == 30) {
						date.setDate_30(AttendanceConstants.WEEK_OFF);
					}

					break;
				case 31:
					if (listOfWeekOffs.contains(i) && i == 31) {
						date.setDate_31(AttendanceConstants.WEEK_OFF);
					}

					break;
				}
			}

		}

		return date;
	}
*/
	public String checkSubmitStatus(int empId, String month, String year) {

		Calendar cal = Calendar.getInstance();
		Employee em = employeeRepository.findOne(empId);
		Attendance at = attendenceRepository.findByEmployeeAndMonthAndYear(em, month, year);
		if (em == null || at == null) {
			return "NoRecord";
		} else {
			if (at.getSubmitStatus().equalsIgnoreCase(AttendanceStatus.Submitted+"") && at.getMonth().equalsIgnoreCase(month)
					&& at.getYear().equalsIgnoreCase(year)) {
				logger.info("Already submitted for EMP ID " +empId );
				return "Submitted";
			} else {
				logger.info("Need to submit now for EMP ID  " + empId);
				return "NotSubmitted";
			}
		}
	}

	public static List<String> GetWeekoffList(int year, int month) {
		// int year = 2010;
		// int month = Calendar.JANUARY;
		Calendar cal = new GregorianCalendar(year, month, 1);
		List list = new ArrayList<String>();
		do {
			// get the day of the week for the current day
			int day = cal.get(Calendar.DAY_OF_WEEK);
			// check if it is a Saturday or Sunday
			if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
				// print the day - but you could add them to a list or whatever
				// System.out.println(cal.get(Calendar.DAY_OF_MONTH));
				list.add(cal.get(Calendar.DAY_OF_MONTH));

			}
			// advance to the next day
			cal.add(Calendar.DAY_OF_YEAR, 1);
		} while (cal.get(Calendar.MONTH) == month);
		// stop when we reach the start of the next month

		return list;
	}

	public boolean sendEmailToClientsideEmp(int employeeId, String reminderText) {
		Employee emp = employeeRepository.findById(employeeId);
		List<String> emailIds = new ArrayList<String>();
		//Ninganagouda_602513_Affirmed_July_2017
		String subject = emp.getName()+"_"+emp.getEmpid()+"_"+emp.getClient().getName()+"_"+dateUtils.getCurrentMonth()+"_"+dateUtils.getCurrentYear();
		if (emp != null) {
			String emailId = emp.getEmailid();
			emailIds.add(emailId);
			emailUtil.sendEmail(emailIds, reminderText,subject);
			return true;
		}
		return false;
	}

	public boolean sendEmailToAllClientsideEmps(int clientId, String reminderText) {
		Client client = clientRepository.findById(clientId);
		if (client != null) {
			List<Employee> emps = client.getEmployees();
			for (Employee emp : emps) {
				String subject = emp.getName()+"_"+emp.getEmpid()+"_"+emp.getClient().getName()+"_"+dateUtils.getCurrentMonth()+"_"+dateUtils.getCurrentYear();
				List<String> emailIds = new ArrayList<String>();
				emailIds.add(emp.getEmailid());
				emailUtil.sendEmail(emailIds, reminderText,subject);
			}
			//emailUtil.sendEmail(emailIds, reminderText);
			return true;
		}
		return false;
	}

	public void CheckSubmitStatusForCurrMonth() {
		
		Iterable<Employee> empList = employeeRepository.findAll();
		for (Employee employee : empList) {
			
			Attendance attend = attendenceRepository.findByEmployeeAndMonthAndYear(employee, dateUtils.getCurrentMonth(), dateUtils.getCurrentYear());
			if(attend==null)
			{
				Attendance a = new Attendance();
				a.setEmployee(employee);
				a.setMonth(dateUtils.getCurrentMonth());
				a.setYear(dateUtils.getCurrentYear());
				a.setDate(new DateOfMonth());
				a.setSubmitStatus(AttendanceStatus.NotSubmitted+"");
				
				employee.setSubmitStatus(AttendanceStatus.NotSubmitted+"");
				
				employeeRepository.save(employee);
				attendenceRepository.save(a);
			}
			
		}
		
	}
	
	public String insertDataWhenNotSubmit(DateOfMonth dateObjectForDbFromMail , String splitArrayFromSubject[], String mailId, String month, String year,
			String managerId) {
		
		
		String empId = splitArrayFromSubject[1];
		int pKey = Integer.parseInt(empId);
		List<String> dateListFromMailNew = new ArrayList<String>();
		
		Employee empFromDB1 = employeeRepository.findById(pKey);
		logger.info("For the employee "+empFromDB1.getName());
		logger.info("*************************************** Dates For NotSubmitted Update "+dateListFromMailNew);
				
		Attendance attendForSubmit = attendenceRepository.findByEmployeeAndMonthAndYear(empFromDB1, month, year+"");
		
		DateOfMonth dateOfMonth = attendForSubmit.getDate();
		dateOfMonth = dateObjectForDbFromMail;
		attendForSubmit.setEmployee(empFromDB1);
		attendForSubmit.setDate(dateOfMonth);
		attendForSubmit.setSubmitStatus(AttendanceStatus.Submitted+"");
		attendForSubmit.setMonth(month);
		attendForSubmit.setYear(year);
		
		empFromDB1.setSubmitStatus(AttendanceStatus.Submitted+"");
		employeeRepository.save(empFromDB1);
		attendenceRepository.save(attendForSubmit);
		
		
		logger.info("Inserted Succeesful for the employee "+empFromDB1.getName());
		
		logger.info("Insertion Successful");
		return "success";
	}

	public void insertExcelData(String headerList, String valueList, ArrayList<String> dateList, String subject,String ManagerId,String fromMail)  {
		 
		 String split1 = subject.replace("Attendance details :", "");
		 String[] split2 = split1.split("_");
		 String[] mailIdFromOutlook = fromMail.replaceAll(">", "").split("<");
		 String headerArry[] = headerList.split("\\$");
		 String valueArry[] = valueList.split("\\$");
		 HashMap<String, String> map = new HashMap<>();
		 System.out.println("Lentgh of Both Array "+headerArry.length+" "+valueArry.length);
		 for(int i=0;i<headerArry.length;i++)
		 {
			 try{
				 map.put(headerArry[i].toString().trim(), valueArry[i].toString().trim());
			 }
			 catch(Exception e)
			 {
				 logger.error("Puttinh blnk space");
				 map.put(headerArry[i].toString().trim(), "");
			 }
			 
		 }
		DateOfMonth dateObjectForDbFromMail = GetMonthData(map, dateList);
		insertData(dateObjectForDbFromMail,split2,mailIdFromOutlook[1],ManagerId);
    }
	 
	public DateOfMonth GetMonthData( HashMap<String, String> map, ArrayList<String> dateList)
	 {
		 DateOfMonth dateObjectForDbFromMail = new DateOfMonth();
		 int i = 0;
		 try
		 {
			 for(int j=1;j <= 31;j++)
				{
					switch(j)
					{
					case 1:
					dateObjectForDbFromMail.setDate_1(map.get(dateList.get(i)));
					case 2:
					dateObjectForDbFromMail.setDate_2(map.get(dateList.get(i)));
					case 3:
					dateObjectForDbFromMail.setDate_3(map.get(dateList.get(i)));
					case 4:
					dateObjectForDbFromMail.setDate_4(map.get(dateList.get(i)));
					case 5:
					dateObjectForDbFromMail.setDate_5(map.get(dateList.get(i)));
					case 6:
					dateObjectForDbFromMail.setDate_6(map.get(dateList.get(i)));
					case 7:
					dateObjectForDbFromMail.setDate_7(map.get(dateList.get(i)));
					case 8:
					dateObjectForDbFromMail.setDate_8(map.get(dateList.get(i)));
					case 9:
					dateObjectForDbFromMail.setDate_9(map.get(dateList.get(i)));
					case 10:
					dateObjectForDbFromMail.setDate_10(map.get(dateList.get(i)));
					case 11:
					dateObjectForDbFromMail.setDate_11(map.get(dateList.get(i)));
					case 12:
					dateObjectForDbFromMail.setDate_12(map.get(dateList.get(i)));
					case 13:
					dateObjectForDbFromMail.setDate_13(map.get(dateList.get(i)));
					case 14:
					dateObjectForDbFromMail.setDate_14(map.get(dateList.get(i)));
					case 15:
					dateObjectForDbFromMail.setDate_15(map.get(dateList.get(i)));
					case 16:
					dateObjectForDbFromMail.setDate_16(map.get(dateList.get(i)));
					case 17:
					dateObjectForDbFromMail.setDate_17(map.get(dateList.get(i)));
					case 18:
					dateObjectForDbFromMail.setDate_18(map.get(dateList.get(i)));
					case 19:
					dateObjectForDbFromMail.setDate_19(map.get(dateList.get(i)));
					case 20:
					dateObjectForDbFromMail.setDate_20(map.get(dateList.get(i)));
					case 21:
					dateObjectForDbFromMail.setDate_21(map.get(dateList.get(i)));
					case 22:
					dateObjectForDbFromMail.setDate_22(map.get(dateList.get(i)));
					case 23:
					dateObjectForDbFromMail.setDate_23(map.get(dateList.get(i)));
					case 24:
					dateObjectForDbFromMail.setDate_24(map.get(dateList.get(i)));
					case 25:
					dateObjectForDbFromMail.setDate_25(map.get(dateList.get(i)));
					case 26:
					dateObjectForDbFromMail.setDate_26(map.get(dateList.get(i)));
					case 27:
					dateObjectForDbFromMail.setDate_27(map.get(dateList.get(i)));
					case 28:
					dateObjectForDbFromMail.setDate_28(map.get(dateList.get(i)));
					case 29:
					dateObjectForDbFromMail.setDate_29(map.get(dateList.get(i)));
					case 30:
					dateObjectForDbFromMail.setDate_30(map.get(dateList.get(i)));
					case 31:
					dateObjectForDbFromMail.setDate_31(map.get(dateList.get(i)));
}
					i = i+1;
					
				}
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 
		 return dateObjectForDbFromMail;
	 }
	 
}
