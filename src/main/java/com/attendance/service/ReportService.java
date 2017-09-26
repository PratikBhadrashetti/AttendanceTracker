package com.attendance.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.attendance.controller.ReportController;
import com.attendance.entity.Attendance;
import com.attendance.entity.Client;
import com.attendance.entity.DateOfMonth;
import com.attendance.entity.Employee;
import com.attendance.model.ExcelData;
import com.attendance.model.Report;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.ClientRepository;
import com.attendance.utils.DateUtils;
import com.attendance.utils.ReportUtil;

/**
 * @author harsha.patil
 *
 */

@Component
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Resource
	ClientRepository clientRepository;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	ReportUtil reportUtil;

	@Resource
	AttendanceRepository attendanceRepository;

	@Resource
	Environment environment;

	public ExcelData getExcelFile(Report report, String filePath) {

		logger.debug("************************************************************************");
		logger.debug("Entry : getExcelFile::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");

		ExcelData excelData = new ExcelData();
		byte[] data = null;
		String reportFilename = "";
		String fileUrl = "";
		Path path = null;

		int clientId = report.getClient_id();
		Client client = clientRepository.findById(clientId);
		String clientName = client.getName();

		if (report.getMonth().isEmpty() || report.getYear().isEmpty()) {

			String month = dateUtils.getCurrentMonth();
			String year = dateUtils.getCurrentYear();

			reportFilename = clientName + "_" + month + "_" + year;
			fileUrl = filePath + reportFilename;
			try {
				path = Paths.get(fileUrl);
				data = Files.readAllBytes(path);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

		} else {

			String month = report.getMonth();
			String year = report.getYear();

			reportFilename = clientName + "_" + month + "_" + year + ".xlsx";

			// reportFilename = "Datami_August_2017.xlsx";
			fileUrl = filePath + reportFilename;
			try {
				path = Paths.get(fileUrl);
				data = Files.readAllBytes(path);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);

			}
		}

		excelData.setExcelData(data);
		excelData.setReportFilename(reportFilename);

		logger.debug("************************************************************************");
		logger.debug("Exit : getExcelFile::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");

		return excelData;

	}

	public void prepareAttendanceReport(Report report, int month, int year, String excelReportFilePath)
			throws IOException {

		// String excelTemplateFilePath =
		// environment.getProperty("report.template.path");
		// String excelReportFilePath =
		// environment.getProperty("report.filepath") + "_" + report.getMonth()
		// + "_"
		// + report.getYear() + ".xslx";
		//
		// File source = new File(excelTemplateFilePath);
		// File dest = new File(excelReportFilePath);
		//
		// reportUtil.copyFileUsingStream(source, dest);

		try {
			int index = 3;
			// HSSFWorkbook wb = new HSSFWorkbook();
			// HSSFSheet sheet = wb.createSheet("Excel Sheet");

			int noOfDaysInMonth = dateUtils.getMaxDaysInMonth(month, year);

			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(new File(excelReportFilePath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			XSSFWorkbook wb = null;
			try {
				wb = new XSSFWorkbook(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			XSSFSheet sheet = wb.getSheet("IncedoAttendanceTracker");

			Client client = clientRepository.findById(report.getClient_id());
			List<Employee> employees = client.getEmployees();

			if (!employees.isEmpty()) {

				for (Employee employee : employees) {

					// Iterable<Attendance> attendances =
					// employee.getEmpAttendances();
//					Iterable<Attendance> attendances = attendanceRepository.findByEmployeeAndMonthAndYear(employee,
//							dateUtils.getMonthName(month), String.valueOf(year));
					
					List<Attendance> attendances = new ArrayList<Attendance>();
					attendances.add(attendanceRepository.findByEmployeeAndMonthAndYear(employee,
							dateUtils.getMonthName(month), String.valueOf(year)));
					for (Attendance attendance : attendances) {

						logger.info("******************************************************************");
						logger.info(attendance.getMonth() + " Month");
						logger.info(attendance.getYear() + " Year");

						Employee e = attendance.getEmployee();
						DateOfMonth date = attendance.getDate();

						logger.info(e.getName() + " EmpName");
						logger.info(date.getDate_1());
						// HSSFRow row = sheet.createRow((short) index);

						// Row row = sheet.getRow(3);

						Row row = sheet.createRow((short) index);

						if (date != null) {

							if (noOfDaysInMonth == 28) {

								row.createCell((short) 1).setCellValue(e.getName());
								row.createCell((short) 2).setCellValue(String.valueOf(e.getId()));
								row.createCell((short) 3).setCellValue(e.getClient().getName());
								row.createCell((short) 4).setCellValue(String.valueOf(attendance.getYear()));
								row.createCell((short) 5).setCellValue(attendance.getMonth());

								row.createCell((short) 6).setCellValue(date.getDate_1());
								row.createCell((short) 7).setCellValue(date.getDate_2());
								row.createCell((short) 8).setCellValue(date.getDate_3());
								row.createCell((short) 9).setCellValue(date.getDate_4());
								row.createCell((short) 10).setCellValue(date.getDate_5());
								row.createCell((short) 11).setCellValue(date.getDate_6());
								row.createCell((short) 12).setCellValue(date.getDate_7());
								row.createCell((short) 13).setCellValue(date.getDate_8());
								row.createCell((short) 14).setCellValue(date.getDate_9());
								row.createCell((short) 15).setCellValue(date.getDate_10());
								row.createCell((short) 16).setCellValue(date.getDate_11());
								row.createCell((short) 17).setCellValue(date.getDate_12());
								row.createCell((short) 18).setCellValue(date.getDate_13());
								row.createCell((short) 19).setCellValue(date.getDate_14());
								row.createCell((short) 20).setCellValue(date.getDate_15());
								row.createCell((short) 21).setCellValue(date.getDate_16());
								row.createCell((short) 22).setCellValue(date.getDate_17());
								row.createCell((short) 23).setCellValue(date.getDate_18());
								row.createCell((short) 24).setCellValue(date.getDate_19());
								row.createCell((short) 25).setCellValue(date.getDate_20());
								row.createCell((short) 26).setCellValue(date.getDate_21());
								row.createCell((short) 27).setCellValue(date.getDate_22());
								row.createCell((short) 28).setCellValue(date.getDate_23());
								row.createCell((short) 29).setCellValue(date.getDate_24());
								row.createCell((short) 30).setCellValue(date.getDate_25());
								row.createCell((short) 31).setCellValue(date.getDate_26());
								row.createCell((short) 32).setCellValue(date.getDate_27());
								row.createCell((short) 33).setCellValue(date.getDate_28());

								index++;

							} else if (noOfDaysInMonth == 30) {

								row.createCell((short) 1).setCellValue(e.getName());
								row.createCell((short) 2).setCellValue(String.valueOf(e.getId()));
								row.createCell((short) 3).setCellValue(e.getClient().getName());
								row.createCell((short) 4).setCellValue(String.valueOf(attendance.getYear()));
								row.createCell((short) 5).setCellValue(attendance.getMonth());

								row.createCell((short) 6).setCellValue(date.getDate_1());
								row.createCell((short) 7).setCellValue(date.getDate_2());
								row.createCell((short) 8).setCellValue(date.getDate_3());
								row.createCell((short) 9).setCellValue(date.getDate_4());
								row.createCell((short) 10).setCellValue(date.getDate_5());
								row.createCell((short) 11).setCellValue(date.getDate_6());
								row.createCell((short) 12).setCellValue(date.getDate_7());
								row.createCell((short) 13).setCellValue(date.getDate_8());
								row.createCell((short) 14).setCellValue(date.getDate_9());
								row.createCell((short) 15).setCellValue(date.getDate_10());
								row.createCell((short) 16).setCellValue(date.getDate_11());
								row.createCell((short) 17).setCellValue(date.getDate_12());
								row.createCell((short) 18).setCellValue(date.getDate_13());
								row.createCell((short) 19).setCellValue(date.getDate_14());
								row.createCell((short) 20).setCellValue(date.getDate_15());
								row.createCell((short) 21).setCellValue(date.getDate_16());
								row.createCell((short) 22).setCellValue(date.getDate_17());
								row.createCell((short) 23).setCellValue(date.getDate_18());
								row.createCell((short) 24).setCellValue(date.getDate_19());
								row.createCell((short) 25).setCellValue(date.getDate_20());
								row.createCell((short) 26).setCellValue(date.getDate_21());
								row.createCell((short) 27).setCellValue(date.getDate_22());
								row.createCell((short) 28).setCellValue(date.getDate_23());
								row.createCell((short) 29).setCellValue(date.getDate_24());
								row.createCell((short) 30).setCellValue(date.getDate_25());
								row.createCell((short) 31).setCellValue(date.getDate_26());
								row.createCell((short) 32).setCellValue(date.getDate_27());
								row.createCell((short) 33).setCellValue(date.getDate_28());
								row.createCell((short) 34).setCellValue(date.getDate_29());
								row.createCell((short) 35).setCellValue(date.getDate_30());

								index++;

							} else if (noOfDaysInMonth == 31) {

								row.createCell((short) 1).setCellValue(e.getName());
								row.createCell((short) 2).setCellValue(String.valueOf(e.getId()));
								row.createCell((short) 3).setCellValue(e.getClient().getName());
								row.createCell((short) 4).setCellValue(String.valueOf(attendance.getYear()));
								row.createCell((short) 5).setCellValue(attendance.getMonth());

								row.createCell((short) 6).setCellValue(date.getDate_1());
								row.createCell((short) 7).setCellValue(date.getDate_2());
								row.createCell((short) 8).setCellValue(date.getDate_3());
								row.createCell((short) 9).setCellValue(date.getDate_4());
								row.createCell((short) 10).setCellValue(date.getDate_5());
								row.createCell((short) 11).setCellValue(date.getDate_6());
								row.createCell((short) 12).setCellValue(date.getDate_7());
								row.createCell((short) 13).setCellValue(date.getDate_8());
								row.createCell((short) 14).setCellValue(date.getDate_9());
								row.createCell((short) 15).setCellValue(date.getDate_10());
								row.createCell((short) 16).setCellValue(date.getDate_11());
								row.createCell((short) 17).setCellValue(date.getDate_12());
								row.createCell((short) 18).setCellValue(date.getDate_13());
								row.createCell((short) 19).setCellValue(date.getDate_14());
								row.createCell((short) 20).setCellValue(date.getDate_15());
								row.createCell((short) 21).setCellValue(date.getDate_16());
								row.createCell((short) 22).setCellValue(date.getDate_17());
								row.createCell((short) 23).setCellValue(date.getDate_18());
								row.createCell((short) 24).setCellValue(date.getDate_19());
								row.createCell((short) 25).setCellValue(date.getDate_20());
								row.createCell((short) 26).setCellValue(date.getDate_21());
								row.createCell((short) 27).setCellValue(date.getDate_22());
								row.createCell((short) 28).setCellValue(date.getDate_23());
								row.createCell((short) 29).setCellValue(date.getDate_24());
								row.createCell((short) 30).setCellValue(date.getDate_25());
								row.createCell((short) 31).setCellValue(date.getDate_26());
								row.createCell((short) 32).setCellValue(date.getDate_27());
								row.createCell((short) 33).setCellValue(date.getDate_28());
								row.createCell((short) 34).setCellValue(date.getDate_29());
								row.createCell((short) 35).setCellValue(date.getDate_30());
								row.createCell((short) 36).setCellValue(date.getDate_31());

								index++;

							}
						} else {

							row.createCell((short) 1).setCellValue("NO DATA FOUND");
						}

						// index++;

						logger.info("******************************************************************");

					}
				}
			} else {

				Row row = sheet.createRow((short) index);
				row.createCell((short) 1).setCellValue("NO DATA FOUND");
			}

			FileOutputStream fileOut = new FileOutputStream(new File(excelReportFilePath));
			wb.write(fileOut);
			fileOut.close();
			System.out.println("Data is saved in excel file.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getHeaders(int monthNumber) {

		List<String> headerList = new ArrayList<String>();

		String employeeName = environment.getRequiredProperty("report.employeename");
		String employeeId = environment.getRequiredProperty("report.employeeid");
		String clientName = environment.getRequiredProperty("report.clientname");
		String month = environment.getRequiredProperty("report.month");
		String year = environment.getRequiredProperty("report.year");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, monthNumber);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat df = new SimpleDateFormat("dd-EEE");

		headerList.add(employeeName);
		headerList.add(employeeId);
		headerList.add(clientName);
		headerList.add(year);
		headerList.add(month);

		for (int i = 0; i < maxDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			headerList.add(df.format(cal.getTime()));

		}
		return headerList;

	}

	public String writeHeadersToExcel(List<String> headerList, int columnCount, int rowCount, String clientName,
			String month, String year) throws IOException {
		// String excelFilePath =
		// environment.getProperty("report.template.path");
		String excelFilePath = environment.getProperty("report.filepath") + clientName + "_" + month + "_" + year
				+ ".xlsx";
		// FileInputStream inputStream = null;
		// try {
		// inputStream = new FileInputStream(new File(excelFilePath));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// XSSFWorkbook workbook = null;
		// try {
		// workbook = new XSSFWorkbook(inputStream);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// XSSFSheet sheet = workbook.getSheet("Sheet1");

		XSSFWorkbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		XSSFSheet sheet = workbook.createSheet("IncedoAttendanceTracker");

		Row rowHeader = sheet.createRow((short) 0);

		// Create a cell and put a value in it.
		rowHeader.createCell(5).setCellValue(createHelper.createRichTextString("INCEDO ATTENDANCE TRACKER"));
		sheet.autoSizeColumn(5);

		int colCount = columnCount;
		Row row = sheet.createRow(rowCount);
		// Row row = sheet.getRow(rowCount);

		XSSFCellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBold(true);
		style.setFillBackgroundColor(IndexedColors.RED.getIndex());
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 127, 80)));
		style.setFont(font);

		int autoSizeColCnt = colCount;

		for (String str : headerList) {
			Cell cell = row.createCell(++colCount);
			cell.setCellValue(str);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(autoSizeColCnt++);
		}

		try (FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath))) {
			workbook.write(outputStream);
			workbook.close();
		}
		return excelFilePath;

	}

}
