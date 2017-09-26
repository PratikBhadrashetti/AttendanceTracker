package com.attendance.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.exception.AttendanceException;
import com.attendance.model.ExcelData;
import com.attendance.model.Report;
import com.attendance.repository.ClientRepository;
import com.attendance.service.ReportService;
import com.attendance.utils.DateUtils;

/**
 * @author harsha.patil
 *
 */

@RestController
@RequestMapping(value = "/report")
public class ReportController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	ReportService reportService;

	@Resource
	Environment environment;

	@Autowired
	DateUtils dateUtils;

	@Resource
	ClientRepository clientRepository;

	private String excelReportFilePath;

	private List<String> headerList;

	@RequestMapping(value = "/getreport", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadRep(@RequestBody Report report) throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : downloadReport::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");

		int monthNum = 0;
		int yearNum = 0;
		try {

			String filePath = environment.getProperty("report.filepath");
			String mediaType = "application/vnd.ms-excel";
			int colCnt = Integer.valueOf(environment.getRequiredProperty("report.template.columncount"));
			int rowCnt = Integer.valueOf(environment.getRequiredProperty("report.template.rowcount"));

			if ((report.getMonth().isEmpty() || report.getMonth() == null)
					&& (report.getYear().isEmpty() || report.getYear() == null)) {
				monthNum = dateUtils.getCurrentMonthNumber();
				yearNum = dateUtils.getCurrentYearInInt();
				headerList = reportService.getHeaders(monthNum);
			} else {
				monthNum = dateUtils.getMonthNumber(report.getMonth());
				yearNum = dateUtils.getYearNumber(report.getYear());
				headerList = reportService.getHeaders(monthNum);
			}

			if ((report.getMonth().isEmpty() || report.getMonth() == null)
					&& (report.getYear().isEmpty() || report.getYear() == null)) {
				try {
					excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
							clientRepository.findById(report.getClient_id()).getName(), dateUtils.getCurrentMonth(),
							dateUtils.getCurrentYear());
					reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
							clientRepository.findById(report.getClient_id()).getName(), report.getMonth(),
							report.getYear());
					reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ExcelData excelData = reportService.getExcelFile(report, filePath);
			String fileName = excelData.getReportFilename();
			byte[] fileData = excelData.getExcelData();

			HttpHeaders headers = new HttpHeaders();
			headers.set("charset", "utf-8");
			headers.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
			headers.set(HttpHeaders.EXPIRES, "0");
			headers.set(HttpHeaders.PRAGMA, "no-cache");
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.valueOf(mediaType));
			headers.setContentLength(fileData.length);

			return new ResponseEntity<byte[]>(fileData, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : downloadReport::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing downloadReport",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/getrep", method = RequestMethod.POST)
	public ResponseEntity<String> downloadReport(@RequestBody Report report) throws AttendanceException {

		logger.debug("************************************************************************");
		logger.debug("Entry : downloadReport::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");

		int monthNum = 0;
		int yearNum = 0;
		try {

			String filePath = environment.getProperty("report.filepath");
			String mediaType = "application/vnd.ms-excel";
			int colCnt = Integer.valueOf(environment.getRequiredProperty("report.template.columncount"));
			int rowCnt = Integer.valueOf(environment.getRequiredProperty("report.template.rowcount"));

			if ((report.getMonth().isEmpty() || report.getMonth() == null)
					&& (report.getYear().isEmpty() || report.getYear() == null)) {
				monthNum = dateUtils.getCurrentMonthNumber();
				yearNum = dateUtils.getCurrentYearInInt();
				headerList = reportService.getHeaders(monthNum);
			} else {
				monthNum = dateUtils.getMonthNumber(report.getMonth());
				yearNum = dateUtils.getYearNumber(report.getYear());
				headerList = reportService.getHeaders(monthNum);
			}

			if ((report.getMonth().isEmpty() || report.getMonth() == null)
					&& (report.getYear().isEmpty() || report.getYear() == null)) {
				try {
					excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
							clientRepository.findById(report.getClient_id()).getName(), dateUtils.getCurrentMonth(),
							dateUtils.getCurrentYear());
					reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
							clientRepository.findById(report.getClient_id()).getName(), report.getMonth(),
							report.getYear());
					reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ExcelData excelData = reportService.getExcelFile(report, filePath);
			String fileName = excelData.getReportFilename();

			FileInputStream inp = new FileInputStream(new File(filePath + fileName));
			Workbook workbook = WorkbookFactory.create(inp);

			// Get the first Sheet.
			Sheet sheet = workbook.getSheet("IncedoAttendanceTracker");

			// Start constructing JSON.
			JSONObject json = new JSONObject();

			// Iterate through the rows.
			JSONArray rows = new JSONArray();
			for (Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext();) {
				Row row = rowsIT.next();
				JSONObject jRow = new JSONObject();

				// Iterate through the cells.
				JSONArray cells = new JSONArray();
				for (Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext();) {
					Cell cell = cellsIT.next();
					cells.put(cell.getStringCellValue());
				}
				jRow.put("cell", cells);
				rows.put(jRow);
			}

			// Create the JSON.
			json.put("rows", rows);

			// Get the JSON text.
			// return json.toString();
			return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : downloadReport::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		return null;

	}

	@RequestMapping(value = "/getreportfile", method = RequestMethod.POST)
	public ResponseEntity<Void> downloadReportFile(@RequestBody Report report, HttpServletResponse response)
			throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : downloadReport::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");

		int monthNum = 0;
		int yearNum = 0;
		try {

			String filePath = environment.getProperty("report.filepath");
			String mediaType = "application/vnd.ms-excel";
			int colCnt = Integer.valueOf(environment.getRequiredProperty("report.template.columncount"));
			int rowCnt = Integer.valueOf(environment.getRequiredProperty("report.template.rowcount"));

			if ((report.getMonth().isEmpty() || report.getMonth() == null)
					&& (report.getYear().isEmpty() || report.getYear() == null)) {
				monthNum = dateUtils.getCurrentMonthNumber();
				yearNum = dateUtils.getCurrentYearInInt();
				headerList = reportService.getHeaders(monthNum);
			} else {
				monthNum = dateUtils.getMonthNumber(report.getMonth());
				yearNum = dateUtils.getYearNumber(report.getYear());
				headerList = reportService.getHeaders(monthNum);
			}

			if ((report.getMonth().isEmpty() || report.getMonth() == null)
					&& (report.getYear().isEmpty() || report.getYear() == null)) {
				try {
					excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
							clientRepository.findById(report.getClient_id()).getName(), dateUtils.getCurrentMonth(),
							dateUtils.getCurrentYear());
					reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
							clientRepository.findById(report.getClient_id()).getName(), report.getMonth(),
							report.getYear());
					reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ExcelData excelData = reportService.getExcelFile(report, filePath);
			String fileName = excelData.getReportFilename();
			//

			InputStream is = new FileInputStream(new File(filePath + fileName));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : downloadReport::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing downloadReport",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/download/{year}/{month}/{clientid}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable("year") String year,
			@PathVariable("month") String month, @PathVariable("clientid") int clientid) throws IOException {

		logger.debug("************************************************************************");
		logger.debug("Entry : downloadReport::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");

		int monthNum = 0;
		int yearNum = 0;
		try {

			String filePath = environment.getProperty("report.filepath");
			String mediaType = "application/vnd.ms-excel";
			int colCnt = Integer.valueOf(environment.getRequiredProperty("report.template.columncount"));
			int rowCnt = Integer.valueOf(environment.getRequiredProperty("report.template.rowcount"));

			monthNum = dateUtils.getMonthNumber(month);
			yearNum = dateUtils.getYearNumber(year);
			headerList = reportService.getHeaders(monthNum);

			Report report = new Report(clientid, month, year);

			try {
				excelReportFilePath = reportService.writeHeadersToExcel(headerList, colCnt, rowCnt,
						clientRepository.findById(clientid).getName(), month, year);
				reportService.prepareAttendanceReport(report, monthNum, yearNum, excelReportFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}

			ExcelData excelData = reportService.getExcelFile(report, filePath);
			String fileName = excelData.getReportFilename();

			File file = null;
			file = new File(filePath + fileName);

			if (!file.exists()) {
				String errorMessage = "Sorry. The file you are looking for does not exist";
				System.out.println(errorMessage);
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
				outputStream.close();
				return;
			}

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				System.out.println("mimetype is not detectable, will take default");
				mimeType = "application/octet-stream";
			}

			System.out.println("mimetype : " + mimeType);

			response.setContentType(mimeType);

			/*
			 * "Content-Disposition : inline" will show viewable types [like
			 * images/text/pdf/anything viewable by browser] right on browser
			 * while others(zip e.g) will be directly downloaded [may provide
			 * save as popup, based on your browser setting.]
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			/*
			 * "Content-Disposition : attachment" will be directly download, may
			 * provide save as popup, based on your browser setting
			 */
			// response.setHeader("Content-Disposition",
			// String.format("attachment; filename=\"%s\"", file.getName()));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			// Copy bytes from source to destination(outputstream in this
			// example), closes both streams.
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : downloadReport::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");

		}
	}
}