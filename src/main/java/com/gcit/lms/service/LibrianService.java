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
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Branch;
//import com.gcit.lms.jdbc.JDBCDemo;
import com.gcit.lms.entity.Genre;

/**
 * @author Aaron
 *
 */
@RestController
public class LibrianService extends BaseController {

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
	
	/*@RequestMapping(value = "readBranches", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public List<Branch> readBranches() throws SQLException {

		List<Branch> branches = new ArrayList<>();
		try {
			branches = branchdao.readBranches("");
			for (Branch branch : branches) {
				branch.setBookcopies(bookCopiesdao.getBookCopiesByBranch(branch));
			}
			return branches;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value = "updateBranch", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateBranch(@RequestBody Branch branch) throws SQLException {
		try {
			if (branch.getBranchId() != null && branch.getBranchName() != null && branch.getBranchAddress() != null) {
				branchdao.updateBranch(branch);
			} else if (branch.getBranchId() == null && branch.getBranchName() != null
					&& branch.getBranchAddress() != null) {
				branchdao.createBranch(branch);
			} else {
				branchdao.deleteBranch(branch);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@RequestMapping(value="initBookCopies", method=RequestMethod.GET, produces="application/json" )
	public BookCopies initBookCopies() throws SQLException {
		return new BookCopies();
	}
	
	@RequestMapping(value = "readBooksByBranch/{searchBranch}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public List<Book> readBooksByTitle(@PathVariable String searchBranch) throws SQLException {
		List<Book> books = new ArrayList<>();
		try {
			 books = bookdao.readBooksByBranch(Integer.parseInt(searchBranch));
			for (Book book : books) {
				book.setAuthors(adao.readAuthorsByBookId(book));
				book.setGenres(genredao.getGenresByBookId(book));
				book.setPublisher(publisherdao.getPublisherbyBookId(book));
				book.setBookcopies(bookCopiesdao.getBookCopiesByBookId(book));
			}
			return books;

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value = "updateNoOfCopies", method = RequestMethod.POST, produces = "application/json")
	@Transactional
	public void updateNoOfCopies(@RequestBody BookCopies bookcopies) throws SQLException {
		
		try {
			bookCopiesdao.updateBookCopies(bookcopies);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "showNoOfCopies", method = RequestMethod.POST, produces = "application/json")
	@Transactional
	public BookCopies showNoOfCopies(@RequestBody BookCopies bookCopies) throws SQLException {
			BookCopies bookcopy = new BookCopies();
		try {
			  bookcopy = bookCopiesdao.getBookCopiesbyBranchbyBook(bookCopies);
			  bookcopy.setBook(bookdao.readBooksByBookCopies(bookcopy.getBookId()));
			  bookcopy.setBranch(branchdao.readBranchByBookCopiesBranchId(bookcopy.getBranchId()));
			  return bookcopy ;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
