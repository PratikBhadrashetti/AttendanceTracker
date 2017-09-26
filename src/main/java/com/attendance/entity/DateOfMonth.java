package com.attendance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

/**
 * @author harsha.patil
 *
 */

@Component
@Entity(name = "date")
public class DateOfMonth {

	public DateOfMonth() {

	}

	public DateOfMonth(String date_1, String date_2, String date_3, String date_4, String date_5, String date_6,
			String date_7, String date_8, String date_9, String date_10, String date_11, String date_12, String date_13,
			String date_14, String date_15, String date_16, String date_17, String date_18, String date_19,
			String date_20, String date_21, String date_22, String date_23, String date_24, String date_25,
			String date_26, String date_27, String date_28, String date_29, String date_30, String date_31) {
		super();
		Date_1 = date_1;
		Date_2 = date_2;
		Date_3 = date_3;
		Date_4 = date_4;
		Date_5 = date_5;
		Date_6 = date_6;
		Date_7 = date_7;
		Date_8 = date_8;
		Date_9 = date_9;
		Date_10 = date_10;
		Date_11 = date_11;
		Date_12 = date_12;
		Date_13 = date_13;
		Date_14 = date_14;
		Date_15 = date_15;
		Date_16 = date_16;
		Date_17 = date_17;
		Date_18 = date_18;
		Date_19 = date_19;
		Date_20 = date_20;
		Date_21 = date_21;
		Date_22 = date_22;
		Date_23 = date_23;
		Date_24 = date_24;
		Date_25 = date_25;
		Date_26 = date_26;
		Date_27 = date_27;
		Date_28 = date_28;
		Date_29 = date_29;
		Date_30 = date_30;
		Date_31 = date_31;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String Date_1;
	@Column
	private String Date_2;
	@Column
	private String Date_3;
	@Column
	private String Date_4;
	@Column
	private String Date_5;
	@Column
	private String Date_6;
	@Column
	private String Date_7;
	@Column
	private String Date_8;
	@Column
	private String Date_9;
	@Column
	private String Date_10;
	@Column
	private String Date_11;
	@Column
	private String Date_12;
	@Column
	private String Date_13;
	@Column
	private String Date_14;
	@Column
	private String Date_15;
	@Column
	private String Date_16;
	@Column
	private String Date_17;
	@Column
	private String Date_18;
	@Column
	private String Date_19;
	@Column
	private String Date_20;
	@Column
	private String Date_21;
	@Column
	private String Date_22;
	@Column
	private String Date_23;
	@Column
	private String Date_24;
	@Column
	private String Date_25;
	@Column
	private String Date_26;
	@Column
	private String Date_27;
	@Column
	private String Date_28;
	@Column
	private String Date_29;
	@Column
	private String Date_30;
	@Column
	private String Date_31;

	@Column

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate_1() {
		return Date_1;
	}

	public void setDate_1(String date_1) {
		Date_1 = date_1;
	}

	public String getDate_2() {
		return Date_2;
	}

	public void setDate_2(String date_2) {
		Date_2 = date_2;
	}

	public String getDate_3() {
		return Date_3;
	}

	public void setDate_3(String date_3) {
		Date_3 = date_3;
	}

	public String getDate_4() {
		return Date_4;
	}

	public void setDate_4(String date_4) {
		Date_4 = date_4;
	}

	public String getDate_5() {
		return Date_5;
	}

	public void setDate_5(String date_5) {
		Date_5 = date_5;
	}

	public String getDate_6() {
		return Date_6;
	}

	public void setDate_6(String date_6) {
		Date_6 = date_6;
	}

	public String getDate_7() {
		return Date_7;
	}

	public void setDate_7(String date_7) {
		Date_7 = date_7;
	}

	public String getDate_8() {
		return Date_8;
	}

	public void setDate_8(String date_8) {
		Date_8 = date_8;
	}

	public String getDate_9() {
		return Date_9;
	}

	public void setDate_9(String date_9) {
		Date_9 = date_9;
	}

	public String getDate_10() {
		return Date_10;
	}

	public void setDate_10(String date_10) {
		Date_10 = date_10;
	}

	public String getDate_11() {
		return Date_11;
	}

	public void setDate_11(String date_11) {
		Date_11 = date_11;
	}

	public String getDate_12() {
		return Date_12;
	}

	public void setDate_12(String date_12) {
		Date_12 = date_12;
	}

	public String getDate_13() {
		return Date_13;
	}

	public void setDate_13(String date_13) {
		Date_13 = date_13;
	}

	public String getDate_14() {
		return Date_14;
	}

	public void setDate_14(String date_14) {
		Date_14 = date_14;
	}

	public String getDate_15() {
		return Date_15;
	}

	public void setDate_15(String date_15) {
		Date_15 = date_15;
	}

	public String getDate_16() {
		return Date_16;
	}

	public void setDate_16(String date_16) {
		Date_16 = date_16;
	}

	public String getDate_17() {
		return Date_17;
	}

	public void setDate_17(String date_17) {
		Date_17 = date_17;
	}

	public String getDate_18() {
		return Date_18;
	}

	public void setDate_18(String date_18) {
		Date_18 = date_18;
	}

	public String getDate_19() {
		return Date_19;
	}

	public void setDate_19(String date_19) {
		Date_19 = date_19;
	}

	public String getDate_20() {
		return Date_20;
	}

	public void setDate_20(String date_20) {
		Date_20 = date_20;
	}

	public String getDate_21() {
		return Date_21;
	}

	public void setDate_21(String date_21) {
		Date_21 = date_21;
	}

	public String getDate_22() {
		return Date_22;
	}

	public void setDate_22(String date_22) {
		Date_22 = date_22;
	}

	public String getDate_23() {
		return Date_23;
	}

	public void setDate_23(String date_23) {
		Date_23 = date_23;
	}

	public String getDate_24() {
		return Date_24;
	}

	public void setDate_24(String date_24) {
		Date_24 = date_24;
	}

	public String getDate_25() {
		return Date_25;
	}

	public void setDate_25(String date_25) {
		Date_25 = date_25;
	}

	public String getDate_26() {
		return Date_26;
	}

	public void setDate_26(String date_26) {
		Date_26 = date_26;
	}

	public String getDate_27() {
		return Date_27;
	}

	public void setDate_27(String date_27) {
		Date_27 = date_27;
	}

	public String getDate_28() {
		return Date_28;
	}

	public void setDate_28(String date_28) {
		Date_28 = date_28;
	}

	public String getDate_29() {
		return Date_29;
	}

	public void setDate_29(String date_29) {
		Date_29 = date_29;
	}

	public String getDate_30() {
		return Date_30;
	}

	public void setDate_30(String date_30) {
		Date_30 = date_30;
	}

	public String getDate_31() {
		return Date_31;
	}

	public void setDate_31(String date_31) {
		Date_31 = date_31;
	}

}
