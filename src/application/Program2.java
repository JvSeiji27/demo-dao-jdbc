package application;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {
    public static void main(String[] args){
        DepartmentDao dep = DaoFactory.createDepartment();
        
        System.out.println("Test 1 -- FindById");
        
        Department depar = dep.findById(2);
        System.out.println(depar);
        
        System.out.println("Test 2 -- FindAll");
        
        List <Department> list = dep.findAll();
        for(Department d : list){
            System.out.println(d);
        }
        
        System.out.println("Test 3 -- Insert");
        Department dep3 = new Department(null, "FullStack");
        dep.insert(dep3);
        System.out.println("Inserted!");
        System.out.println("NewId: " + dep3.getId());
    
        System.out.println("Test 4 -- Update");
        Department j = dep.findById(9);
        j.setName("Software Developer");
        dep.update(j);
        System.out.println("Updated!!!");
        
        System.out.println("Test 5 - Delete");
        dep.deleteById(7);
        
    }
}
