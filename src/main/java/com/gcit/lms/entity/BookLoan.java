/**
 * 
 */
package com.gcit.lms.entity;

import java.io.Serializable;

/**
 * @author Aaron
 *
 */
public class BookLoan implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4414030124140146821L;
	private Integer bookId;
	private Integer branchId;
	private Integer cardNo;
	private String dateOut;
	private String dueDate;
	private String dateIn;
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public Integer getCardNo() {
		return cardNo;
	}
	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}
	public String getDateOut() {
		return dateOut;
	}
	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getDateIn() {
		return dateIn;
	}
	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}
	
}
