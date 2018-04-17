/**
 * 
 */
package com.gcit.lms.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * @author Aaron
 *
 */
public abstract class BaseDAO<T> {

	@Autowired  // inject
	public JdbcTemplate jdbcTemplate;
}
