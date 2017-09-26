package juanguerra.menu_restaurante.modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Menu")
public class Menu implements Serializable {
    
	/*
	 * Clase para crear los menús del restaurante
	 * */
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "nombre")
    private String nombre;// nombre del menú(debe ser único)
    
    @Column(name = "disponible")
    public Boolean disponible;// indica si el menú se encuentra disponible
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<Alimento> alimentos;	// alimentos que pertenecen al menú, relación uno a varios con la clase Alimento
    
    // Constructor predeterminado
    public Menu(){
        this.nombre = null;
        this.disponible = null;
    }
    
    // Constructor que recibe el nombre y el estado
    public Menu(String nombre, boolean disponible) throws IllegalArgumentException {
        setNombre(nombre);
        this.disponible = disponible;
    }
    
    // constructor que recibe solo el nombre
    public Menu(String nombre) throws IllegalArgumentException {
        this(nombre, true);// llamada al segundo constructor
    }

    public String getNombre() {return nombre;}
    
    // establece el nombre
    public void setNombre(String nombre) throws IllegalArgumentException {
        if(nombre !=  null){
            nombre = nombre.trim().toUpperCase();
            if(!nombre.isEmpty())
                this.nombre = nombre;
            else
                throw new IllegalArgumentException("El nombre no puede quedar vacío");
        }else
            throw new IllegalArgumentException("El nombre no puede ser null");
    }
    
    public List<Alimento> getAlimentos(){return alimentos;}
    
    // establece los alimentos del menú
    public void setAlimentos(List<Alimento> alimentos) throws IllegalArgumentException {
    	if(alimentos != null)
    		this.alimentos = alimentos;
    	else
    		throw new IllegalArgumentException("La lista de alimentos es inválida");
    }
    
    @Override
    public String toString(){return nombre;}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.nombre);
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
        final Menu other = (Menu) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }
    
}
