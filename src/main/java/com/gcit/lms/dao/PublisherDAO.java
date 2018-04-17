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

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Publisher;

/**
 * @author Aaron
 *
 */
@Component
public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>> {

	
	
	public void createPublisher(Publisher p) throws ClassNotFoundException, SQLException {
		
		jdbcTemplate.update("insert into tbl_publisher(publisherName,publisherAddress,publisherPhone) values(?,?,?)",new Object[] {p.getPublisherName(),p.getPublisherAddress(),p.getPublisherPhone()});
	}
	
	
	public void updatePublisher(Publisher p) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_publisher set publisherName =? , publisherAddress =?, publisherPhone =? where publisherId =?", new Object[] {p.getPublisherName(),p.getPublisherAddress(),p.getPublisherPhone(),p.getPublisherId()});
	}

	public void deletePublisher(Publisher p) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete from tbl_publisher where publiserId = ?",new Object[] {p.getPublisherId()});
	}
	
	public Publisher getPublisherbyBookId(Book book) {
		List<Publisher> publishers = new ArrayList<>();
		publishers =jdbcTemplate.query("select * from tbl_publisher where publisherId IN (select pubId  from tbl_book where bookId=?)",new Object[] {book.getBookId()},this);
		if(publishers!=null) {
			return publishers.get(0);
		}
		else {
			return null;
		}
	}
	
	public Publisher getPublisherById(Integer id) {
		List<Publisher> publishers = new ArrayList<>();
		publishers =jdbcTemplate.query("select * from tbl_publisher where publisherId=?",new Object[] {id},this);
		if(publishers!=null) {
			return publishers.get(0);
		}
		else {
			return null;
		}
	}
	
	
	public List<Publisher> readPublishers(String publisherName) throws ClassNotFoundException,SQLException {
		if (publisherName != null && !publisherName.isEmpty()) {
			publisherName = "%" + publisherName + "%";
			return jdbcTemplate.query("select * from tbl_publisher where publisherName like ?", new Object[] { publisherName },this);
		} else {
			return jdbcTemplate.query("select * from tbl_publisher", this);
		}

	}
	
	public List<Publisher> readPublishersWithoutBook()throws ClassNotFoundException,SQLException {
		return jdbcTemplate.query("select * from tbl_publisher tp where not Exists (select null from tbl_book b where b.pubId=tp.publisherId)",this);
	}
	
	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		 List<Publisher> publishers = new ArrayList<>();
		 while(rs.next()) {
			 Publisher p = new Publisher();
			 p.setPublisherId(rs.getInt("publisherId"));
			 p.setPublisherName(rs.getString("publisherName"));
		   	 p.setPublisherAddress(rs.getString("publisherAddress"));
		     p.setPublisherPhone(rs.getString("publisherPhone"));
		     publishers.add(p); 
		 }
		 return publishers;
	}


	

	

	
}
