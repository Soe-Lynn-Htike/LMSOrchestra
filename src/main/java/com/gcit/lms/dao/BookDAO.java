/**
 * 
 */
package com.gcit.lms.dao;


import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;


/**
 * @Book Aaron
 *
 */
@Component
public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>> {
	
	public void createBook(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("insert  into tbl_book (title) values(?)", new Object[] { book.getTitle() });
	}

	public Integer createBookWithPK(Book book) throws ClassNotFoundException, SQLException {

		/*// return createWithPK("insert int tbl_book (title) values(?)", new Object[]
		// {book.getTitle()});
		String insertSql = "insert into tbl_book (title) values(?)";
		// this is the key holder
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		// the name of the generated column (you can track more than one column)
		String id_column = "bookId";

		// the update method takes an implementation of PreparedStatementCreator which
		// could be a lambda
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(insertSql, new String[] { id_column });
			ps.setString(1, book.getTitle());
			return ps;
		}, keyHolder);

		// after the update executed we can now get the value of the generated ID
		BigDecimal id = (BigDecimal) keyHolder.getKeys().get(id_column);
		return id.intValue();*/
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	        public PreparedStatement createPreparedStatement(Connection connection)
	                throws SQLException {
	            PreparedStatement ps = 
	                connection.prepareStatement("insert into tbl_book (title,pubId) values(?,?)", 
	                    Statement.RETURN_GENERATED_KEYS);
	            ps.setString(1, book.getTitle());
	            ps.setInt(2, book.getPublisherId());
	            return ps;
	        }
	    }, keyHolder);
	    Integer id = keyHolder.getKey().intValue();
	   return id; 

	}

	public void saveBookAuthor(Book book) throws ClassNotFoundException, SQLException {
		for (Author a : book.getAuthors()) {
			jdbcTemplate.update("insert into tbl_book_authors VALUES (?, ?)",
					new Object[] { book.getBookId(), a.getAuthorId() });
		}
	}

	public void saveBookGenre(Book book) throws ClassNotFoundException, SQLException {
		for (Genre genre : book.getGenres()) {
			jdbcTemplate.update("insert into tbl_book_genres VALUES (?, ?)",
					new Object[] { genre.getGenre_id(), book.getBookId() });
		}
	}
	
	public void deleteAuthorGenre(Book book) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		for (Author author : book.getAuthors()) {
			jdbcTemplate.update("delete from tbl_book_authors where bookId=? and authorId=?",
					new Object[] { book.getBookId(), author.getAuthorId() });
		}
		for (Genre genre : book.getGenres()) {
			jdbcTemplate.update("delete from tbl_book_genres where genre_id=? and bookId=?",
					new Object[] { genre.getGenre_id(), book.getBookId() });
		}

	}
	public void updateBook(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("update  tbl_book set title=? where bookId=?",
				new Object[] { book.getTitle(), book.getBookId() });
	}
	public void updateBookByPublisherId(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("update  tbl_book set pubId=? where bookId=?",
				new Object[] { book.getPublisherId(), book.getBookId() });
	}
	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("delete tbl_book from where bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readBooksByBorrower(Integer cardNo)throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query(
				"select * from tbl_book where bookId IN (select bookId from tbl_book_loans where cardNo =? and  dateIn IS NULL)",
				new Object[] { cardNo }, this);
	}


	public List<Book> readBooks(String bookTitle) throws ClassNotFoundException, SQLException {
		if(bookTitle !=null && !bookTitle.isEmpty()) {
			bookTitle = "%" + bookTitle + "%";
			return jdbcTemplate.query("select * from tbl_book where title like ?", new Object[] { bookTitle }, this);
		}else {
			return jdbcTemplate.query("select * from tbl_book", this);
		}
		
	}
	
	public List<Book> readBooksByAuthorId(Author author) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book where bookId IN(select bookId from tbl_book_authors where authorId=? )",
				new Object[] { author.getAuthorId() }, this);
	}

	public List<Book> readBooksByGenreId(Genre genre) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book where bookId IN(select bookId from tbl_book_genres where genre_id=?)",
				new Object[] { genre.getGenre_id() }, this);
	}
	
	
	public List<Book> readBooksByPublisherId(Publisher publisher) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book where pubId IN(select publisherId from tbl_publisher where publisherId=?)",
				new Object[] { publisher.getPublisherId() }, this);
	}
	
	public List<Book> readBooksByBranch(Integer searchBranch) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book b where Exists(select null from tbl_book_copies bc where b.bookId = bc.bookId and bc.noOfCopies >1 and bc.branchId=? )",
				new Object[] { searchBranch }, this);
	}
	public Book readBooksByBookCopies(Integer searchBranch) throws ClassNotFoundException, SQLException {

		List<Book> books= jdbcTemplate.query(
				"select * from tbl_book where bookId IN(select bookId from tbl_book_copies where bookId=?);",
				new Object[] { searchBranch }, this);
		if (books != null) {
			return books.get(0);
		}

		return null;
	}
	
	public List<Book> readBookByBranchByCardNo(BookLoan bookLoan)throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("select  b.* from tbl_book b inner join tbl_book_loans tbl on b.bookId = tbl.bookId inner join tbl_library_branch tlb on tlb.branchId = tbl.branchId inner join tbl_borrower tb on tb.cardNo = tbl.cardNo where tlb.branchId = ? and tb.cardNo=? and tbl.dateIn IS NULL",new Object[] {bookLoan.getBranchId(),bookLoan.getCardNo()} ,this);
	}
	public Book getBookByPK(Integer id)throws ClassNotFoundException, SQLException {

		List<Book> books = jdbcTemplate.query("select * from tbl_book where bookId=?",
				new Object[] { id }, this);
		if (books != null && !books.isEmpty()) {
			return books.get(0);
		}

		return null;

	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			book.setPublisherId(rs.getInt("pubId"));
			books.add(book);
		}
		return books;
	}

	

}
