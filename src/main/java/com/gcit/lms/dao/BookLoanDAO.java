/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.Connection;
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
public class BookLoanDAO extends BaseDAO<BookLoan> implements ResultSetExtractor<List<BookLoan>> {

	
	public void creatBookLoan(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_book_loans(bookId,branchId,cardNo,dateOut,dueDate) values(?,?,?,curdate(),date_add(curdate(), INTERVAL 1 WEEK))",
				new Object[] {bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo()});
	}
	
	public void updateBookLoan(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) VALUES (?,?,?,?,?,?)",
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo(),bookLoan.getDateOut(), bookLoan.getDueDate(), bookLoan.getDateIn()});
	}
	public void overrideDueDate(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_book_loans set dueDate = date_add(dueDate, INTERVAL 1 week) WHERE bookId = ? and branchId = ? and cardNo = ? and dateIn is NULL",
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() });
	}
	public void updateBookLoanDueDate(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dueDate = ? WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] {bookLoan.getDueDate(), bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() });
	}
	public List<BookLoan> getBookLoansByCardNo(Borrower borrower)throws ClassNotFoundException, SQLException{
		return jdbcTemplate.query("select * from tbl_book_loans where cardNo IN(select cardNo from tbl_borrower where cardNo=?)", new Object[]{borrower.getCardNo()},this);
	}
	public void updateBookLoanDateIn(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dateIn = curdate() WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() });
	}
	public void deleteBookLoan(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_loans WHERE bookId = ? and branchId = ? and cardNo = ?", new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() });
	}
	public List<BookLoan> readBookLoans() throws ClassNotFoundException, SQLException{
		
		return jdbcTemplate.query("select * from tbl_book_loans", this);
	}
	@Override
	public List<BookLoan> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<BookLoan> bookLoans = new ArrayList<>();
		if(rs.next()) {
			while (rs.next()) {
				BookLoan a = new BookLoan();
				a.setBookId(rs.getInt("bookId"));
				a.setBranchId(rs.getInt("branchId"));
				a.setCardNo(rs.getInt("cardNo"));
				a.setDateOut(rs.getString("dateOut"));
				a.setDueDate(rs.getString("dueDate"));
				a.setDateIn(rs.getString("dateIn"));

				bookLoans.add(a);
			}
			return bookLoans;
		}
		return null;
	}

	

}
