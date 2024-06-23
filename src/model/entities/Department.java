package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable{
    //O serializable, faz com que os objetos sejam transformados em sequência de bits;
    //Útil para serem usados em arquivos, tráfego de rede e etc.
    private String name;
    private Integer id;
    
    public Department(){
        
    }
    
    public Department(Integer id, String name){
        this.name = name;
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Department other = (Department) obj;
        return Objects.equals(this.name, other.name);
    }
    
    public String toString(){
        return "Department - id: " + id + ", name: " + name;
    }
    
    
    
}
