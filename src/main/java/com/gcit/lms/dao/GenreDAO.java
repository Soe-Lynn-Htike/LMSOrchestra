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
import com.gcit.lms.entity.Genre;

/**
 * @author Aaron
 *
 */
@Component
public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>> {

	
	
	public void creatGenre(Genre g) throws ClassNotFoundException, SQLException { 
		jdbcTemplate.update("insert into tbl_genre(genre_name) values(?)", new Object[] {g.getGenre_id()});
	}
	public Integer createGenreWithPK(Genre genre) throws ClassNotFoundException, SQLException {
		 KeyHolder keyHolder = new GeneratedKeyHolder();
		    jdbcTemplate.update(new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection)
		                throws SQLException {
		            PreparedStatement ps = 
		                connection.prepareStatement("insert into tbl_genre (genre_name) values(?)", 
		                    Statement.RETURN_GENERATED_KEYS);
		            ps.setString(1, genre.getGenre_name());
		           
		            return ps;
		        }
		    }, keyHolder);
		    Integer id = keyHolder.getKey().intValue();
		   return id;
	}
	public void updateGenre(Genre g) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_genre set genre_name= ? where genre_id =?", new Object[] {g.getGenre_name(),g.getGenre_id()});
	}
	public void deleteGenre(Genre g) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete  from tbl_genre where genre_id=?",new Object[] {g.getGenre_id()});
	}
	public void saveGenreBook(Genre genre) throws ClassNotFoundException, SQLException {
		for (Book book : genre.getBooks()) {
			jdbcTemplate.update("insert into tbl_book_genres VALUES (?, ?)",
					new Object[] {genre.getGenre_id(), book.getBookId()});
		}
	}
	public void deleteGenreBook(Genre genre) throws ClassNotFoundException, SQLException {
		for (Book book : genre.getBooks()) {
			jdbcTemplate.update("delete from tbl_book_genres where genre_id=? and bookId=?",
					new Object[] { genre.getGenre_id(), book.getBookId() });
		}
	}
	public List<Genre> getGenresByBookId(Book book){
		return jdbcTemplate.query("select * from tbl_genre where genre_id IN (select genre_id from tbl_book_genres where bookId=?);",new Object[] {book.getBookId()} ,this);
	}
	public List<Genre> readGenres(String genreName) throws ClassNotFoundException,SQLException {
		if(genreName !=null && !genreName.isEmpty()){
			genreName = "%"+genreName+"%";
			return jdbcTemplate.query("select * from tbl_genre where genre_name like ?", new Object[]{genreName},this);
		}else{
			return jdbcTemplate.query("select * from tbl_genre", this);
		}
		
	}
	public Genre readGenreById(Integer id) throws ClassNotFoundException,SQLException {
		List<Genre> genres = new ArrayList<>();
		genres = jdbcTemplate.query("select * from tbl_genre where genre_id=?", new Object[] {id},this);
		if(genres !=null) {
			return genres.get(0);
		}
		else {
			return null;
		}
		
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Genre> genres = new ArrayList<>();
		while(rs.next()) {
			Genre g = new Genre();
			g.setGenre_id(rs.getInt("genre_id"));
			g.setGenre_name(rs.getString("genre_name"));
			genres.add(g);
		}
		return genres;
	}
	

}
