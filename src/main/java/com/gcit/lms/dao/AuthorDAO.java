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


/**
 * @author Aaron
 *
 */
@Component
public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>> {
	
	public void createAuthor(Author author) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("INSERT INTO tbl_author(authorName) VALUES(?)", new Object[] { author.getAuthorName() });
	}
	public Integer createAuthorWithPK(Author author) throws ClassNotFoundException, SQLException {

	
		/*String insertSql = "insert into tbl_author (authorName) values(?)";
		// this is the key holder
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		// the name of the generated column (you can track more than one column)
		String id_column = "authorId";

		// the update method takes an implementation of PreparedStatementCreator which
		// could be a lambda
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(insertSql, new String[] { id_column });
			ps.setString(1, author.getAuthorName());
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
		                connection.prepareStatement("insert into tbl_author (authorName) values(?)", 
		                    Statement.RETURN_GENERATED_KEYS);
		            ps.setString(1, author.getAuthorName());
		           
		            return ps;
		        }
		    }, keyHolder);
		    Integer id = keyHolder.getKey().intValue();
		   return id; 
		
		/*final KeyHolder holder = new GeneratedKeyHolder();
		int status = jdbcTemplate.update("insert into tbl_author (authorName) values(?)", author.getAuthorName(), holder, new String[]{"authorId"});
		
		return status;*/

	}
	/*public void saveBookAuthor(Author author, Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("INSERT INTO tbl_book_authors VALUES (?, ?)",
				new Object[] { book.getBookId(), author.getAuthorId() });
	}*/
	public void saveAuthorBook(Author author) throws ClassNotFoundException, SQLException {
		for (Book book : author.getBooks()) {
			jdbcTemplate.update("insert into tbl_book_authors VALUES (?, ?)",
					new Object[] { book.getBookId(), author.getAuthorId() });
		}
	}
	public void deleteAuthorBook(Author author) throws ClassNotFoundException, SQLException {
		for (Book book : author.getBooks()) {
			jdbcTemplate.update("delete from tbl_book_authors where bookId=? and authorId=?",
					new Object[] { book.getBookId(), author.getAuthorId() });
		}
	}
	public void updateAuthor(Author author) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("UPDATE  tbl_author SET authorName=? WHERE authorId=?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });

	}
	
	public List<Author> readAuthorsByBookId(Book book){
		
		return jdbcTemplate.query("select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId=?)",new Object[] {book.getBookId()},this);
	}
	public void deleteAuthor(Author author) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("DELETE  FROM tbl_author WHERE authorId =?", new Object[] { author.getAuthorId() });

	}

	public List<Author> readAuthors() throws ClassNotFoundException, SQLException {
		/*
		 * if(authorName !=null && !authorName.isEmpty()){ authorName =
		 * "%"+authorName+"%"; return
		 * readAll("select * from tbl_author where authorName like ?", new
		 * Object[]{authorName}); }else{ return readAll("select * from tbl_author",
		 * null); }
		 */
		return jdbcTemplate.query("select * from tbl_author", this);
	}
	
	public List<Author> readAuthorsByName(String searchString) throws ClassNotFoundException, SQLException {
		
		searchString = "%"+searchString+ "%";
		return jdbcTemplate.query("select * from tbl_author where authorName like ?",new Object[] {searchString}, this);
	}
	
	public Author readAuthorsById(Integer id) throws ClassNotFoundException, SQLException {

		List<Author> authors= jdbcTemplate.query("select * from tbl_author where authorId=? ",new Object[] { id }, this);
		if(authors != null && !authors.isEmpty()) {
		 return	authors.get(0);
		}
		else {
			return null;
		}
	
	}


	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author author = new Author();
			author.setAuthorId(rs.getInt("authorId"));
			author.setAuthorName(rs.getString("authorName"));
			authors.add(author);
		}
		return authors;
	}

}
