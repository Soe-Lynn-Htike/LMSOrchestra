/**
 * 
 */
package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponentsBuilder;

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
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

/**
 * @author Aaron
 *
 */
@RestController
public class AdminService extends BaseController {

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

	
	@Autowired
	RestTemplate restTemplate;


	@RequestMapping(value="authorObject", method=RequestMethod.GET, produces="application/json" )
	public Author authorObject() throws SQLException {
		return new Author();
	}
	
	// author delete
	@RequestMapping(value = "author/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> deleteAuthor(@PathVariable String Id) throws SQLException {
				Author author = new Author();
		try {
			author = adao.readAuthorsById(Integer.parseInt(Id));
			if (author == null) {
	            return new ResponseEntity<Object>(new CustomErrorType("Unable to delete. Author with id " + Id + " not found."),
	                    HttpStatus.NOT_FOUND);
	        }else {
	        	adao.deleteAuthor(author);
	        	return new ResponseEntity<Author>(HttpStatus.NO_CONTENT);
	        }
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	// author create
	@RequestMapping(value = "author", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> creatAuthor(@RequestBody Author author, UriComponentsBuilder ucBuilder)
			throws SQLException {
		try {
			Integer authorId = adao.createAuthorWithPK(author);
			author.setAuthorId(authorId);
			adao.saveAuthorBook(author);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/lms/author/{id}").buildAndExpand(author.getAuthorId()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// get author list
	@RequestMapping(value = "authors", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Author>> readAllAuthors() {

		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/authors";
		ResponseEntity<Author[]> response = restTemplate.getForEntity(baseURL, Author[].class);
		if(response.getBody() == null) {
			ArrayList<Author> arrayList = new ArrayList(Arrays.asList(response.getBody()));

			return new ResponseEntity<List<Author>>(HttpStatus.NO_CONTENT);
		}else {
			ArrayList<Author> arrayList = new ArrayList(Arrays.asList(response.getBody()));

			return new ResponseEntity<List<Author>>(arrayList, HttpStatus.OK);
		}
		
	}
	
	
	// get particular author
	@RequestMapping(value = "author/{Id}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<?> readAuthorsById(@PathVariable String Id) {
		
		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/";
		Author author = new Author();
		author = restTemplate.getForObject(baseURL + "author/" + Id, Author.class);
		if(author == null) {
			return new ResponseEntity<Object>(new CustomErrorType("Publisher with id " + Id + " not found"),
					HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Author>(author, HttpStatus.OK);
		}
		
	}
	
	
	
	// publisher update
	@RequestMapping(value = "author/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateAuthor(@PathVariable String id, @RequestBody Author author) {
		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/";

		HttpEntity<Author> entity = new HttpEntity<Author>(author);
		ResponseEntity<Author> checkauthor = restTemplate.exchange(baseURL, HttpMethod.PUT, entity, Author.class);
		if (checkauthor == null) {
			return new ResponseEntity<Object>(new CustomErrorType("Publisher with id " + id + " cannot update"),
					HttpStatus.NOT_FOUND);
		} else {
			return checkauthor;
		}

	}
	
	@RequestMapping(value = "updateAuthorBook", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateAuthorBook(@RequestBody Author author) throws SQLException {

		try {
				adao.deleteAuthorBook(author);
				System.out.print("sdf");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user
		}
	}
	
	
	// get authors by name
	@RequestMapping(value = "authorsname/{searchname}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Author>> searchAuthorsByName(@PathVariable String searchname) {
		List<Author> authors = new ArrayList<>();
		try {
			authors = adao.readAuthorsByName(searchname);
			for (Author a : authors) {
				a.setBooks(bookdao.readBooksByAuthorId(a));
			}
			return new ResponseEntity<List<Author>>(authors, HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<List<Author>>(HttpStatus.NOT_FOUND);
	}
	
	

	@RequestMapping(value="bookObject", method=RequestMethod.GET, produces="application/json" )
	public Book initBook() throws SQLException {
		return new Book();
	}
	
	
	// get book list
	@RequestMapping(value = "books", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Book>> readBooks() throws SQLException {
		List<Book> books = new ArrayList<>();
		try {

			books = bookdao.readBooks("");
			for (Book b : books) {
				b.setAuthors(adao.readAuthorsByBookId(b));
				b.setGenres(genredao.getGenresByBookId(b));
				b.setPublisher(publisherdao.getPublisherbyBookId(b));
				b.setBookcopies(bookCopiesdao.getBookCopiesByBookId(b));
			}
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	
	// delete book 
	@RequestMapping(value = "book/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> deleteBook(@PathVariable String Id) throws SQLException {
		Book book = new Book();
		try {
			book = bookdao.getBookByPK(Integer.parseInt(Id));
			if (book == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to delete. Book with id " + Id + " not found."),
						HttpStatus.NOT_FOUND);
			} else {
				bookdao.deleteBook(book);
				return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// update book
	@RequestMapping(value = "book/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateBook(@PathVariable String Id, @RequestBody Book book) throws SQLException {
		Book checkbook = new Book();
		try {
			checkbook = bookdao.getBookByPK(Integer.parseInt(Id));
			if (checkbook == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to update. Book with id " + Id + " not found."),
						HttpStatus.NOT_FOUND);
			} else {
				bookdao.updateBook(book);
				bookdao.saveBookAuthor(book);
				bookdao.saveBookGenre(book);
				return new ResponseEntity<Book>(book, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// create book
	@RequestMapping(value = "book", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> creatAuthor(@RequestBody Book book, UriComponentsBuilder ucBuilder)
			throws SQLException {
		try {
			Integer bookId = bookdao.createBookWithPK(book);
			book.setBookId(bookId);
			bookdao.saveBookAuthor(book);
			bookdao.saveBookGenre(book);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/lms/book/{id}").buildAndExpand(book.getBookId()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	// read book by title
	@RequestMapping(value = "bookstitle/{searchTitle}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Book>> readBooksByTitle(@PathVariable String searchTitle) throws SQLException {
		List<Book> books = new ArrayList<>();
		try {
			books = bookdao.readBooks(searchTitle);
			for (Book book : books) {
				book.setAuthors(adao.readAuthorsByBookId(book));
				book.setGenres(genredao.getGenresByBookId(book));
				book.setPublisher(publisherdao.getPublisherbyBookId(book));
				book.setBookcopies(bookCopiesdao.getBookCopiesByBookId(book));
			}
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	// read book by id
	@RequestMapping(value = "book/{Id}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<?> readBookById(@PathVariable String Id) throws SQLException {
		Book book = new Book();
		try {
			book = bookdao.getBookByPK(Integer.parseInt(Id));
			if(book == null) {
				return new ResponseEntity<Object>(new CustomErrorType("Book with id " + Id + " not found"),
						HttpStatus.NOT_FOUND);
			}else {
				book.setAuthors(adao.readAuthorsByBookId(book));
				book.setGenres(genredao.getGenresByBookId(book));
				book.setPublisher(publisherdao.getPublisherbyBookId(book));
				book.setBookcopies(bookCopiesdao.getBookCopiesByBookId(book));
			}
			return new ResponseEntity<Book>(book, HttpStatus.OK);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	
	
	@RequestMapping(value = "updateBookAuthorGenre", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateBookAuthorGenre(@RequestBody Book book) throws SQLException {

		try {
				bookdao.deleteAuthorGenre(book);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user
		}
	}

	
	
	@RequestMapping(value="publisherObject", method=RequestMethod.GET, produces="application/json" )
	public Publisher initPublisher() throws SQLException {
		return new Publisher();
	}
	

	// create publisher
	@RequestMapping(value = "publisher", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updatePublisher(@RequestBody Publisher publisher) throws SQLException {
		
		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/publisher";
		HttpEntity<Publisher> entity = new HttpEntity<Publisher>(publisher);
		ResponseEntity<Publisher> responseEntity = restTemplate.postForEntity(baseURL, entity, Publisher.class);
		if(responseEntity == null) {
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return responseEntity;
		}
	}
	
	@RequestMapping(value = "publisher/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updatePublisher(@PathVariable String id, @RequestBody Publisher publisher) {

		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/publisher/" +id;
		HttpEntity<Publisher> entity = new HttpEntity<Publisher>(publisher);
	    ResponseEntity<Publisher> p = restTemplate.exchange(baseURL, HttpMethod.PUT, entity, Publisher.class);
	    if(p== null) {
	    	return new ResponseEntity<Object>(new CustomErrorType("Publisher with id " + id + " cannot update"),
					HttpStatus.NOT_FOUND);
	    }else {
	    	return p;
	    }
	    
	}
	
	
	// delete publisher
	@RequestMapping(value = "publisher/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> deletePublisher(@PathVariable String id) throws SQLException {
		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/publisher/" + id;
		restTemplate.delete(baseURL);
		return new ResponseEntity<Publisher>(HttpStatus.NO_CONTENT);
	}
	
	
	/*// update publisher
	@RequestMapping(value = "publisher/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updatePublisher(@PathVariable String id, @RequestBody Publisher publisher)
			throws SQLException {
		Publisher checkpublisher = new Publisher();
		try {
			checkpublisher = publisherdao.getPublisherById(Integer.parseInt(id));
			if (checkpublisher == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to update. Publisher with id " + id + " not found."),
						HttpStatus.NOT_FOUND);
			} else {
				publisherdao.updatePublisher(publisher);
				return new ResponseEntity<Publisher>(publisher, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	// update publisher
	/*@RequestMapping(value = "publisher/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ModelAndView updatePublisher(@PathVariable String id, @RequestBody Publisher publisher) {

		
		String url = "http://localhost:8002/lms/publisher/" + id;
		return new ModelAndView("forward:" + url,"Publishser",publisher);

	}*/
	
	
	
	
	
	
	// get publisher list
	@RequestMapping(value = "publishers", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Publisher>> readPublisher() throws SQLException {
		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/publishers";
		ResponseEntity<Publisher[]> response = restTemplate.getForEntity(baseURL, Publisher[].class);
		if(response.getBody() == null) {
			ArrayList<Publisher> arrayList = new ArrayList(Arrays.asList(response.getBody()));

			return new ResponseEntity<List<Publisher>>(HttpStatus.NO_CONTENT);
		}else {
			ArrayList<Publisher> arrayList = new ArrayList(Arrays.asList(response.getBody()));

			return new ResponseEntity<List<Publisher>>(arrayList, HttpStatus.OK);
		}
	
	}
	
	@RequestMapping(value = "publishers/withoutbooks", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Publisher>> readPublishersWithoutBook() throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		try {
			publishers = publisherdao.readPublishersWithoutBook();
			return new ResponseEntity<List<Publisher>>(publishers,HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Publisher>>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	// get publisher by name
	@RequestMapping(value = "publishername/{searchPublisher}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Publisher>> readPublisher(@PathVariable String searchPublisher) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		try {
			publishers = publisherdao.readPublishers(searchPublisher);
			for (Publisher p : publishers) {
				p.setBooks(bookdao.readBooksByPublisherId(p));
			}
			return new ResponseEntity<List<Publisher>>(publishers,HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Publisher>>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	// get certain publisher
	@RequestMapping(value = "publisher/{Id}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<?> readPublisherById(@PathVariable String Id) throws SQLException {
		String baseURL = "http://ec2-54-84-251-9.compute-1.amazonaws.com:8015/lms/";
		Publisher publisher = new Publisher();
		publisher = restTemplate.getForObject(baseURL + "publisher/" + Id, Publisher.class);
		if(publisher == null) {
			return new ResponseEntity<Object>(new CustomErrorType("Publisher with id " + Id + " not found"),
					HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Publisher>(publisher, HttpStatus.OK);
		}
	}

	@RequestMapping(value="genreObject", method=RequestMethod.GET, produces="application/json" )
	public Genre initGenre() throws SQLException {
		return new Genre();
	}
	
	/*// update Genre
	@RequestMapping(value = "updateGenre", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateGenre(@RequestBody Genre genre) throws SQLException {
		try {
			if (genre.getGenre_id() != null && genre.getGenre_name() != null) {
				genredao.updateGenre(genre);
				genredao.saveGenreBook(genre);
			} else if (genre.getGenre_id() == null && genre.getGenre_name() != null) {
				Integer genreId = genredao.createGenreWithPK(genre);
				genre.setGenre_id(genreId);
				genredao.saveGenreBook(genre);
			} else {
				genredao.deleteGenre(genre);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	// create genre
	@RequestMapping(value = "genre", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> createGenre(@RequestBody Genre genre, UriComponentsBuilder ucBuilder) throws SQLException {
		try {
				Integer genreId = genredao.createGenreWithPK(genre);
				genre.setGenre_id(genreId);
				genredao.saveGenreBook(genre);
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(ucBuilder.path("/lms/genre/{id}").buildAndExpand(genre.getGenre_id()).toUri());
				return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// update genre
	@RequestMapping(value = "genre/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateGenre(@PathVariable String id, @RequestBody Genre genre) throws SQLException {
		Genre checkgenre = new Genre();
		try {
			checkgenre = genredao.readGenreById(Integer.parseInt(id));
			if (checkgenre == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to Update.Genre with id " + id + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				genredao.updateGenre(genre);
				genredao.saveGenreBook(genre);
				return new ResponseEntity<Genre>(genre, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	// delete genre
	@RequestMapping(value = "genre/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> deleteGenre(@PathVariable String id) throws SQLException {
		Genre genre = new Genre();
		try {
			genre = genredao.readGenreById(Integer.parseInt(id));
			if (genre == null) {
				return new ResponseEntity<Object>(new CustomErrorType("Unable to delete.Genre with id " + id + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				genredao.deleteGenre(genre);
				return new ResponseEntity<Genre>(HttpStatus.NO_CONTENT);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// read Genres
	@Transactional
	@RequestMapping(value = "genres", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Genre>> readGenres() throws SQLException {
		List<Genre> genres = new ArrayList<>();
		try {
			genres = genredao.readGenres("");
			for (Genre g : genres) {
				g.setBooks(bookdao.readBooksByGenreId(g));
			}
			return new ResponseEntity<List<Genre>>(genres, HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Genre>>(HttpStatus.NO_CONTENT);
		}

	}

	// read genre by name
	@RequestMapping(value = "genrenames/{searchGenre}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Genre>> readGenreByName(@PathVariable String searchGenre) throws SQLException {
		List<Genre> genres = new ArrayList<>();
		try {
			genres = genredao.readGenres(searchGenre);
			for (Genre g : genres) {
				g.setBooks(bookdao.readBooksByGenreId(g));
			}
			return new ResponseEntity<List<Genre>>(genres, HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Genre>>(HttpStatus.NO_CONTENT);
		}

	}
	
	
	// get particular genre
	@RequestMapping(value = "genre/{Id}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<?> readGenreById(@PathVariable String Id) throws SQLException {
		Genre genre = new Genre();
		try {
			genre = genredao.readGenreById(Integer.parseInt(Id));
			if (genre == null) {
				return new ResponseEntity<Object>(new CustomErrorType("Genre with id " + Id + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				genre.setBooks(bookdao.readBooksByGenreId(genre));
				return new ResponseEntity<Genre>(genre, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "updateGenreBook", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateGenreBook(@RequestBody Genre genre) throws SQLException {
		try {
			genredao.deleteGenreBook(genre);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user
		}
	}
	
	
	
	@RequestMapping(value="borrowerObject", method=RequestMethod.GET, produces="application/json" )
	public Borrower initBorrower() throws SQLException {
		return new Borrower();
	}

	
	//create borrower
	@RequestMapping(value = "borrower/", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> createBorrower(@RequestBody Borrower borrower,UriComponentsBuilder ucBuilder) throws SQLException {
		try {
				// create with PK
				borrowerdao.createBorrower(borrower);
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(ucBuilder.path("/lms/borrower/{id}").buildAndExpand(borrower.getCardNo()).toUri());
				return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// update borrower
	@RequestMapping(value = "borrower/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateBorrower(@PathVariable String id, @RequestBody Borrower borrower)
			throws SQLException {
		Borrower checkborrower = new Borrower();
		try {

			checkborrower = borrowerdao.readBorrowersByCardNo(Integer.parseInt(id));
			if (checkborrower == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to update.Borrower with cardNo " + id + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				borrowerdao.updateBorrower(borrower);
				return new ResponseEntity<Borrower>(borrower, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// delete borrower
	@RequestMapping(value = "borrower/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateBorrower(@PathVariable String id) throws SQLException {
		Borrower borrower = new Borrower();
		try {
			borrower = borrowerdao.readBorrowersByCardNo(Integer.parseInt(id));
			if (borrower == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to delete.Borrower with cardNo " + id + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				borrowerdao.deleteBorrower(borrower);
				return new ResponseEntity<Borrower>(HttpStatus.NO_CONTENT);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// read Borrowers
	@RequestMapping(value = "borrowers", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Borrower>> readBorrower() throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		try {
			borrowers = borrowerdao.readBorrowers("");
			for (Borrower b : borrowers) {
				b.setBookLoans(bookloandao.getBookLoansByCardNo(b));
			}
			return new ResponseEntity<List<Borrower>>(borrowers,HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Borrower>>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	// read borrower
	@RequestMapping(value = "borrower/{cardNo}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<?> readBorrowerByCardNo(@PathVariable String cardNo) throws SQLException {
		Borrower borrower = new Borrower();
		try {
			borrower = borrowerdao.readBorrowersByCardNo(Integer.parseInt(cardNo));
			if (borrower == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Borrower with card Number " + cardNo + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				borrower.setBookLoans(bookloandao.getBookLoansByCardNo(borrower));
				return new ResponseEntity<Borrower>(borrower, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Borrower>>(HttpStatus.NO_CONTENT);
		}

	}

	
	// get borrowers name
	@RequestMapping(value = "borrowersname/{searchName}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Borrower>> readBorrowerByName(@PathVariable String searchName) throws SQLException {
		List<Borrower> borrwers = new ArrayList<>();
		try {
			borrwers = borrowerdao.readBorrowers(searchName);
			for (Borrower b : borrwers) {
				b.setBookLoans(bookloandao.getBookLoansByCardNo(b));
			}
			return new ResponseEntity<List<Borrower>>(borrwers,HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Borrower>>(HttpStatus.NO_CONTENT);
		}
		
	}

	
	
	@RequestMapping(value="branchObject", method=RequestMethod.GET, produces="application/json" )
	public Branch initBranch() throws SQLException {
		return new Branch();
	}
	
	
	
	// create branch
	@RequestMapping(value = "branch", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> createBranch(@RequestBody Branch branch, UriComponentsBuilder ucBuilder)
			throws SQLException {
		List<Book> books = new ArrayList<>();
		try {

			Integer branchId = branchdao.createBranchWithPK(branch);
			books = bookdao.readBooks("");
			bookCopiesdao.createDefaultBookCopies(branchId, books);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/lms/branch/{id}").buildAndExpand(branch.getBranchId()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	// update branch
	@RequestMapping(value = "branch/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateBranch(@PathVariable String id, @RequestBody Branch branch) throws SQLException {
		Branch checkbranch = new Branch();
		try {
			checkbranch = branchdao.readBranchById(Integer.parseInt(id));
			if (checkbranch == null) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Cannot update.Branch with id " + id + " not found"), HttpStatus.NOT_FOUND);
			} else {
				branchdao.updateBranch(branch);
				return new ResponseEntity<Branch>(branch, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	// delete branch
	@RequestMapping(value = "branch/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> deleteBranch(@PathVariable String id) throws SQLException {
				Branch branch = new Branch();
		try {
				branch = branchdao.readBranchById(Integer.parseInt(id));
				if(branch == null) {
					return new ResponseEntity<Object>(
							new CustomErrorType("Cannot delete.Branch with id " + id + " not found"),
							HttpStatus.NOT_FOUND);
				}else {
					branchdao.deleteBranch(branch);
					return new ResponseEntity<Branch>(HttpStatus.NO_CONTENT);
				}
				
		

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// read Branch
	@RequestMapping(value = "branches", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Branch>> readBranches() throws SQLException {

		List<Branch> branches = new ArrayList<>();
		try {
			branches = branchdao.readBranches("");
			for (Branch branch : branches) {
				branch.setBookcopies(bookCopiesdao.getBookCopiesByBranch(branch));
			}
			return new ResponseEntity<>(branches,HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Branch>>(HttpStatus.NO_CONTENT);
		}
		
	}

	// get branches name
	@RequestMapping(value = "branchesname/{searchBranchName}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<List<Branch>> readBranchByName(@PathVariable String searchBranchName) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		try {
			branches = branchdao.readBranches(searchBranchName);
			for (Branch branch : branches) {
				branch.setBookcopies(bookCopiesdao.getBookCopiesByBranch(branch));
			}
			return new ResponseEntity<List<Branch>>(branches,HttpStatus.OK);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<Branch>>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	
	// get particular branch
	@RequestMapping(value = "branch/{id}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public ResponseEntity<?> readBranchById(@PathVariable String Id) throws SQLException {
		Branch branch = new Branch();
		try {

			branch = branchdao.readBranchById(Integer.parseInt(Id));
			if (branch == null) {
				return new ResponseEntity<Object>(new CustomErrorType("Branch with id " + Id + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				branch.setBookcopies(bookCopiesdao.getBookCopiesByBranch(branch));
				return new ResponseEntity<Branch>(branch, HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Book Loan
	@RequestMapping(value = "updateBookLoan", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateBookLoan(@RequestBody BookLoan bookLoan) throws SQLException {

		try {

			if (bookLoan.getBookId() != null && bookLoan.getBranchId() != null && bookLoan.getCardNo() != null
					&& bookLoan.getDateIn() == null) {
				bookloandao.creatBookLoan(bookLoan);
			} else if (bookLoan.getBookId() == null && bookLoan.getBranchId() == null && bookLoan.getCardNo() == null
					&& bookLoan.getDateIn() != null) {
				bookloandao.updateBookLoan(bookLoan);
			} else {
				bookloandao.deleteBookLoan(bookLoan);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	@RequestMapping(value = "readBranchByBorrower/{cardNo}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public List<Branch> readBranchByBorrower(@PathVariable String cardNo) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		try {
			branches = branchdao.readBranchByBorrower(Integer.parseInt(cardNo));
			
			return branches;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "readBookByBorrower/{cardNo}", method = RequestMethod.GET, produces = "application/json")
	@Transactional
	public List<Book> readBookByBorrower(@PathVariable String cardNo) throws SQLException {
		List<Book> books = new ArrayList<>();
		try {
			books = bookdao.readBooksByBorrower(Integer.parseInt(cardNo));
			
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="initBookLoan", method=RequestMethod.GET, produces="application/json" )
	public BookLoan initBookLoan() throws SQLException {
		return new BookLoan();
	}
	
	
	@RequestMapping(value = "getBooksByBranchByCardNo", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public List<Book> getBooksByBranchByCardNo(@RequestBody BookLoan bookLoan) throws SQLException {
		List<Book> books = new ArrayList<>();
		try {
			books = bookdao.readBookByBranchByCardNo(bookLoan);
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*// override Book Loan Due Date
	@RequestMapping(value = "overrideBookLoanDueDate", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void overrideBookLoanDueDate(@RequestBody BookLoan bookLoan) throws SQLException {

		try {

			bookloandao.overrideDueDate(bookLoan);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}
	}*/
	
	
	// override duedate
	@RequestMapping(value = "duedate/{cardNo}", method = RequestMethod.PUT, consumes = "application/json")
	@Transactional
	public ResponseEntity<?> overrideBookLoanDueDate(@PathVariable String cardNo, @RequestBody BookLoan bookLoan)
			throws SQLException {
		List<BookLoan> checkbookloan = new ArrayList<>();
		Borrower borrower = new Borrower();
		borrower.setCardNo(Integer.parseInt(cardNo));
		try {
			checkbookloan = bookloandao.getBookLoansByCardNo(borrower);
			if (checkbookloan.isEmpty()) {
				return new ResponseEntity<Object>(
						new CustomErrorType("Unable to find borrwer with" + cardNo + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				bookloandao.overrideDueDate(bookLoan);
				return new ResponseEntity<Object>(new CustomErrorType("Due date is successfully"), HttpStatus.OK);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(new CustomErrorType("Something wrong with the server!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "updateBookLoanDueDate", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void updateBookLoanDueDate(@RequestBody BookLoan bookLoan) throws SQLException {

		try {
			bookloandao.updateBookLoanDueDate(bookLoan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
