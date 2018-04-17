/**
 * 
 */
package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
//import com.gcit.lms.jdbc.JDBCDemo;

/**
 * @author Aaron
 *
 */
@RestController
public class BorrowerService extends BaseController {

	@Autowired
	 AuthorDAO adao;
	
	@Autowired
	 BookDAO bookdao;
	
	@Autowired
	GenreDAO genredao;
	
	@Autowired
	PublisherDAO publisherdao;
	
	@Autowired
	BookCopiesDAO bookCopiesdao;
	
	@Autowired
	BorrowerDAO borrowerdao;
	
	@Autowired
	BranchDAO branchdao;
	
	@Autowired
	BookLoanDAO bookloandao;
	
	
	/*@Transactional
	public List<Branch> readBranch(String branchname) throws SQLException {

		try {
			return branchdao.readBranches(branchname);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	/*@Transactional
	public List<Book> readBooksCheckOut(String branchId) throws SQLException {
		
		try {

			return bookdao.readBooksByBranch(Integer.parseInt(branchId));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	
	@RequestMapping(value = "checkOutBook", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void checkOutBook(@RequestBody BookLoan bookLoan) throws SQLException {

		try {
			bookloandao.creatBookLoan(bookLoan); // book,branch
			bookCopiesdao.checkOutBookCopies(bookLoan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*@Transactional
	public Borrower checkBorrowerByCardNo(Borrower borrower) throws ClassNotFoundException, SQLException {
		try {

			return borrowerdao.readBorrowersByCardNo(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	@RequestMapping(value = "returnBook", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void returnBook(@RequestBody BookLoan bookLoan) throws SQLException {
		
		try {
			bookloandao.updateBookLoanDateIn(bookLoan);
			bookCopiesdao.returnBookCopies(bookLoan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "checkBorrowedBooksByCardNo/{cardNo}", method = RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Book> checkBorrowedBooksByCardNo (@PathVariable String cardNo) throws SQLException {
			List<Book> books = new ArrayList<>();
		try {

			books = bookdao.readBooksByBorrower(Integer.parseInt(cardNo));
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "checkBooksByBranchByCard", method = RequestMethod.POST,produces="application/json")
	@Transactional
	public List<Book> checkBorrowedBooksByCardNo (@RequestBody BookLoan bookLoan) throws SQLException {
			List<Book> books = new ArrayList<>();
		try {
				books =bookdao.readBookByBranchByCardNo(bookLoan);
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
