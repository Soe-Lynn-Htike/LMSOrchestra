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
public class Genre implements Serializable {
    
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1302364695191320628L;
	private Integer genre_id;
    private String  genre_name;
    private List<Book> books;
	
    public Integer getGenre_id() {
		return genre_id;
	}
	public void setGenre_id(Integer genre_id) {
		this.genre_id = genre_id;
	}
	public String getGenre_name() {
		return genre_name;
	}
	public void setGenre_name(String genre_name) {
		this.genre_name = genre_name;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre_id == null) ? 0 : genre_id.hashCode());
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
		Genre other = (Genre) obj;
		if (genre_id == null) {
			if (other.genre_id != null)
				return false;
		} else if (!genre_id.equals(other.genre_id))
			return false;
		return true;
	}
	 
}
