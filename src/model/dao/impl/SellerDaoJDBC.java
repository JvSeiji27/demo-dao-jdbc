package model.dao.impl;

import java.sql.Date;
import db.DB;
import db.DbException;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

    private Connection conn;
    
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into seller (Name, Email, "
                    +"BirthDate, BaseSalary, DepartmentId) "
                    + "values "
                    + "(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            
            int rowsAffected = st.executeUpdate();
            
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
                
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("update seller set name = ? "
                    +", Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                    + "where id = ?");
                    
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
            
            st.executeUpdate();
            
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
       PreparedStatement st = null;
       try{
           st = conn.prepareStatement("delete from seller "
                   + "where Id = ?;");
           st.setInt(1, id);
           int rows = st.executeUpdate();
           if(rows == 0){
               throw new DbException("Invalid Id!");
           }
         
                  
       }
       catch(SQLException e){
           throw new DbException(e.getMessage());
       }
       finally{
           DB.closeStatement(st);
       }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
    
        try{
            st = conn.prepareStatement("select seller.*, department.Name as DepName " +
            "from seller inner join " +
            "department on seller.DepartmentId = department.Id " +
            "where seller.Id = ?");
            
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs, dep);
               return obj;
            }
            return null;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    private Department instantiateDepartment (ResultSet rs) throws SQLException{
        Department dep =  new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }
    
    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
               Seller obj = new Seller();
               obj.setId(rs.getInt("Id"));
               obj.setName(rs.getString("Name"));
               obj.setEmail(rs.getNString("Email"));
               obj.setBirthDate(rs.getDate("BirthDate"));
               obj.setBaseSalary(rs.getDouble("BaseSalary"));
               obj.setDepartment(dep);
               return obj;
    }
    

    @Override
    public List<Seller> findAll() {
        List <Seller> sellers = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
    
        try{
            st = conn.prepareStatement("select seller.*, department.Name as DepName " 
                    +"from seller inner join department "
                    +"on department.Id = seller.DepartmentId " 
                    +"order by name;");

            
            
            rs = st.executeQuery();
            
            Map<Integer, Department> map = new HashMap<>();
            
           
                while(rs.next()){  
                    Department dep = map.get(rs.getInt("DepartmentId"));
                
                    if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(dep.getId(), dep);
                }
                    Seller obj = instantiateSeller(rs, dep);
                    sellers.add(obj);
                    
                }
                return sellers;
            }

        
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        List <Seller> sellers = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
    
        try{
            st = conn.prepareStatement("select seller.*, department.Name as DepName " 
                    +"from seller inner join department "
                    +"on department.Id = seller.DepartmentId " 
                    +"where DepartmentId = ? "
                    +"order by name;");

            
            st.setInt(1, department.getId());
            rs = st.executeQuery();
            
            Map<Integer, Department> map = new HashMap<>();
            
           
                while(rs.next()){  
                    Department dep = map.get(rs.getInt("DepartmentId"));
                
                    if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(dep.getId(), dep);
                }
                    Seller obj = instantiateSeller(rs, dep);
                    sellers.add(obj);
                    
                }
                return sellers;
            }

        
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    
    }
    
}
