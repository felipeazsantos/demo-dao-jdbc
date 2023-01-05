package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT seller.*, department.Name as DepName "
						 + "FROM seller INNER JOIN department "
						 + "ON seller.DepartmentId = department.Id "
						 + "WHERE seller.Id = ?";
			
			st = conn.prepareStatement(sql);
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, dep);
				
				return seller;
			} 
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		return new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		// TODO Auto-generated method stub
		return new Seller(
				rs.getInt("Id"), 
				rs.getString("Name"), 
				rs.getString("Email"), 
				rs.getDate("BirthDate"), 
				rs.getDouble("BaseSalary"),
				dep);
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
						 + "ON seller.DepartmentId = department.Id "
						 + "ORDER BY Name";
			
			st = conn.prepareStatement(sql);
			
			
			rs = st.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> mapDep = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = mapDep.get(rs.getInt("DepartmentId"));
								
				if (dep == null) {
					dep = instantiateDepartment(rs);
					mapDep.put(rs.getInt("DepartmentId"), dep);
					
				}
				
				Seller seller = instantiateSeller(rs, dep);
				
				sellers.add(seller);
				
			} 
			return sellers;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department dep) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT seller.*, department.Name as DepName "
						 + "FROM seller INNER JOIN department "
						 + "ON seller.DepartmentId = department.Id "
						 + "WHERE departmentId = ? "
						 + "ORDER BY Name";
			
			st = conn.prepareStatement(sql);
			
			st.setInt(1, dep.getId());
			
			rs = st.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			
			while (rs.next()) {
				Seller seller = instantiateSeller(rs, dep);
				
				sellers.add(seller);
				
			} 
			return sellers;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
