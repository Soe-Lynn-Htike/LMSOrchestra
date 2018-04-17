/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;


/**
 * @author Aaron
 *
 */
@Component
public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>> {

	
	public void createBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_borrower (name,address,phone) VALUES (?,?,?)", new Object[] { borrower.getName(),borrower.getAddress(),borrower.getPhone() });
	}
	public void updateBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_borrower set name=?, address=?, phone=? where cardNo=?",new Object[] {borrower.getName(),borrower.getAddress(),borrower.getPhone(),borrower.getCardNo()});
	}
	public void deleteBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete  from tbl_borrower where cardNo=?",new Object[] {borrower.getCardNo()});
	}
	
	public Borrower readBorrowersByCardNo(Integer cardNo) throws ClassNotFoundException, SQLException{
		List<Borrower> borrowers = new ArrayList<>();

		borrowers = jdbcTemplate.query("select * from tbl_borrower where cardNo=?", new Object[] { cardNo }, this);
		if (!borrowers.isEmpty()) {
			return borrowers.get(0);
		}else if(borrowers.isEmpty()) {
			return new Borrower();
		}
		return null;
		
	}
	public List<Borrower> readBorrowers(String searchBorrower) throws ClassNotFoundException, SQLException{
		
		if(searchBorrower!=null && !searchBorrower.isEmpty()){
			searchBorrower = "%"+searchBorrower+"%";
			return jdbcTemplate.query("select * from tbl_borrower where name like ?", new Object[]{searchBorrower},this);
		}else{
			return jdbcTemplate.query("select * from tbl_borrower", this);
		}
		
	}
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Borrower> borrowers = new ArrayList<>();

			while (rs.next()) {
				Borrower a = new Borrower();
				a.setCardNo(rs.getInt("cardNo"));
				a.setName(rs.getString("name"));
				a.setAddress(rs.getString("address"));
				a.setPhone(rs.getString("phone"));
				borrowers.add(a);
			}
			return borrowers;


		// check error for cardno one
		
	}
}
