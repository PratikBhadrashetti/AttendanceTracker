package com.attendance.model;

/**
 * @author harsha.patil
 *
 */

public class ExcelData {

	private byte[] excelData;
	private String reportFilename;

	public byte[] getExcelData() {
		return excelData;
	}

	public void setExcelData(byte[] excelData) {
		this.excelData = excelData;
	}

	public String getReportFilename() {
		return reportFilename;
	}

	public void setReportFilename(String reportFilename) {
		this.reportFilename = reportFilename;
	}

}
