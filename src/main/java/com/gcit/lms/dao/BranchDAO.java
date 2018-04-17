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

import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;

/**
 * @author Aaron
 *
 */
@Component
public class BranchDAO extends BaseDAO<Branch> implements ResultSetExtractor<List<Branch>> {

	
	
	public void createBranch(Branch b) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_library_branch (branchName,branchAddress) values(?,?)", new Object[] {b.getBranchName(),b.getBranchAddress()});
	}
	public Integer createBranchWithPK(Branch branch) throws ClassNotFoundException, SQLException {
		 KeyHolder keyHolder = new GeneratedKeyHolder();
		    jdbcTemplate.update(new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection)
		                throws SQLException {
		            PreparedStatement ps = 
		                connection.prepareStatement("insert into tbl_library_branch (branchName,branchAddress) values(?,?)", 
		                    Statement.RETURN_GENERATED_KEYS);
		            ps.setString(1,branch.getBranchName());
		            ps.setString(2, branch.getBranchAddress());
		           
		            return ps;
		        }
		    }, keyHolder);
		    Integer id = keyHolder.getKey().intValue();
		   return id;
	}
	public void updateBranch(Branch b) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_library_branch SET branchName = ? , branchAddress=? where branchId =?",new Object[] {b.getBranchName(),b.getBranchAddress(),b.getBranchId()});
	}
	public void updateBranchByName(Branch b) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_library_branch SET branchName = ?  where branchId =?",new Object[] {b.getBranchName(),b.getBranchId()});
	}
	public void updateBranchByAddress(Branch b) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_library_branch SET branchAddress=?  where branchId =?",new Object[] {b.getBranchAddress(),b.getBranchId()});
	}
	public void deleteBranch(Branch b) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete  from tbl_library_branch where branchId =?",new Object[] {b.getBranchId()});
	}
	public Branch readBranchByBookCopiesBranchId(Integer branchId) {
		List<Branch> branches = jdbcTemplate.query("select * from tbl_library_branch where branchId IN(select branchId from tbl_book_copies where branchId=?);",new Object[] {branchId},this);
		if(branches !=null) {
			return branches.get(0);
		}else {
			return null;
		}
	}
	public List<Branch> readBranchByBorrower(Integer cardNo)throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("select * from tbl_library_branch where branchId IN(select branchId from tbl_book_loans where cardNo=? and dateIn IS  NULL)",new Object[] {cardNo}, this);
	}
	public Branch readBranchById(Integer branchId) {
		List<Branch> branches = jdbcTemplate.query("select * from tbl_library_branch where branchId=?",new Object[] {branchId},this);
		if(branches !=null) {
			return branches.get(0);
		}else {
			return null;
		}
	}
	public List<Branch> readBranches(String branchName) throws ClassNotFoundException, SQLException{
	
		if (branchName != null && !branchName.isEmpty()) {
			branchName = "%" + branchName + "%";
			return jdbcTemplate.query("select * from tbl_library_branch WHERE branchName like ?", new Object[] { branchName },this);
		} else {
			return jdbcTemplate.query("select * from tbl_library_branch", this);
		}
	}

	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Branch> branches = new ArrayList<>();
		while(rs.next()) {
			Branch b = new Branch();
			b.setBranchId(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
			branches.add(b);
		}
		return branches;
	}
}
