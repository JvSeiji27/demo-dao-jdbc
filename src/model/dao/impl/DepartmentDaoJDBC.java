package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.util.List;
import model.dao.DepartmentDao;
import model.entities.Department;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

public class DepartmentDaoJDBC implements DepartmentDao{

    private Connection conn;
    
    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("insert into department (Name) values (?);", Statement.RETURN_GENERATED_KEYS);
          
            st.setString(1, obj.getName());
            int rows = st.executeUpdate();
            
            if(rows > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                    DB.closeResultSet(rs);
                }
            }else{
                throw new DbException("Unexpected error!");
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
    public void update(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("update department set name = ? where id = ?");
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.execute();
        }
        catch(SQLException e){
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
            st = conn.prepareStatement("delete from department where Id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
            if(rows == 0){
                System.out.println("Nothing delete!");
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
    public Department findById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("select * from department where Id = ?;");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                DB.closeResultSet(rs);
                return obj;
            }
            else{
                return null;
            }
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
           
            DB.closeStatement(st);
        }
            
        
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("select * from department;");
            ResultSet rs = st.executeQuery();
            List <Department> departments = new ArrayList<>();
            while(rs.next()){
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                departments.add(obj);
            }
            return departments;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }
    
}
