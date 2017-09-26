package juanguerra.menu_restaurante.modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Alimento")
public class Alimento implements Serializable {
	
	/*
	 * Clase para crear los alimentos
	 * */
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "nombre")
    private String nombre;// nombre del alimento(debe ser único)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nombreMenu")
    private Menu menu;// menú al que pertenece el alimento
    
    @Column(name = "disponible")
    public Boolean disponible;// indica si el alimento se encuentra disponible para la venta
    
    @OneToMany(mappedBy = "alimento", cascade = CascadeType.ALL)
    private List<Precio> precios;	// precios del alimento, relación uno a varios con la clase Precio
    
    // Constructor predeterminado
    public Alimento(){
        this.nombre = null;
        this.menu = null;
        this.disponible = null;
    }
    
    // COnstructor que recibe el nombre, menu y si el alimento se encuentra disponible
    public Alimento(String nombre, Menu menu, boolean disponible) throws IllegalArgumentException {
        setNombre(nombre);
        setMenu(menu);
        this.disponible = disponible;
    }
    
    // Constructor que solo recibe el nombre y menú del alimento
    public Alimento(String nombre, Menu menu) throws IllegalArgumentException {
        this(nombre, menu, false);// llamada al segundo constructor
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
    
    public Menu getMenu(){return menu;}
    
     // establece el menú del alimento
    public void setMenu(Menu menu) throws IllegalArgumentException {
        if(menu !=  null)
        	this.menu = menu;
        else
            throw new IllegalArgumentException("El nombre del menú no puede ser null");
    }
    
    public List<Precio> getPrecios(){return precios;}
    
    // establece los precios del alimento
    public void setPrecios(List<Precio> precios) throws IllegalArgumentException {
    	if(precios != null)
    		this.precios = precios;
    	else
    		throw new IllegalArgumentException("La lista de precios es inválida");
    }
    
    @Override
    public String toString(){return nombre;}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nombre);
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
        final Alimento other = (Alimento) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }
    
}
