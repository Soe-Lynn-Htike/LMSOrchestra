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
public class Borrower  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6648198243048795980L;
	private Integer cardNo;
	private String name;
	private String address;
	private String phone;
	private List<BookLoan> bookLoans;
	/*private Branch branch;*/
	public Integer getCardNo() {
		return cardNo;
	}
	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<BookLoan> getBookLoans() {
		return bookLoans;
	}
	public void setBookLoans(List<BookLoan> bookLoans) {
		this.bookLoans = bookLoans;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardNo == null) ? 0 : cardNo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Borrower other = (Borrower) obj;
		if (cardNo == null) {
			if (other.cardNo != null)
				return false;
		} else if (!cardNo.equals(other.cardNo))
			return false;
		return true;
	}
	

}
