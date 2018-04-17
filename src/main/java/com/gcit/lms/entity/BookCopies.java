/**
 * 
 */
package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron
 *
 */
public class BookCopies implements Serializable {
	
	private Integer bookId;
	private Integer branchId;
	private Integer noOfCopies;
	private Book book;
	private Branch branch;
	
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
	public Integer getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	
	
	

}
