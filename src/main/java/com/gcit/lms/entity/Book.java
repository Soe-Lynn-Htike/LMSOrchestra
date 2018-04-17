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
public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7401794194536968695L;
	private Integer bookId;
	private String  title;
	private Integer publisherId;
	private List<Author> authors;
	private Publisher publisher;
	private List<Genre> genres;
	private List<BookCopies> bookcopies;
	
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public List<BookCopies> getBookcopies() {
		return bookcopies;
	}
	public void setBookcopies(List<BookCopies> bookcopies) {
		this.bookcopies = bookcopies;
	}
	public Integer getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
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
		Book other = (Book) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		return true;
	}
	
}
