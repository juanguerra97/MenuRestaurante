package juanguerra.menu_restaurante.modelo;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Precio")
public class Precio implements Serializable {
    
	/*
	 * Clase para crear los precios de un alimento
	 * */
	
    private static final long serialVersionUID = 1L;
    
    public static final NumberFormat FORMATO_MONEDA = NumberFormat.getCurrencyInstance();// objeto para dar formato de moneda
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nombreAlimento")
    private Alimento alimento;// nombre del alimento del que el objeto es precio, relacion varios a uno con la clase Alimento
    
    @Column(name = "nombreMedida")
    private String nombreMedida;// nombre de la medida a la que se aplica este precio(Plato, Vaso, Litro, Combo, etc.)
    
    @Column(name = "precio")
    private Double precio;// precio por unidad de medida
    
    // Constructor predeterminado
    public Precio(){
    	this.id = null;
        this.alimento = null;
        this.nombreMedida = null;
        this.precio = null;
    }
    
    // Constructor que recibe todos los atributos
    public Precio(long id, Alimento alimento, String nombreMedida, double precio) throws IllegalArgumentException {
    	setId(id);
        setAlimento(alimento);
        setNombreMedida(nombreMedida);
        setPrecio(precio);
    }
    
    // Constructor que no recibe el id
    public Precio(Alimento alimento, String nombreMedida, double precio) throws IllegalArgumentException {
        this.id = null;
    	setAlimento(alimento);
        setNombreMedida(nombreMedida);
        setPrecio(precio);
    }
    
    public Long getId() {return id;}
    
    // establece el id
    public void setId(long id) throws IllegalArgumentException {
    	if(id > 0)
    		this.id = id;
    	else
    		throw new IllegalArgumentException("ID inválido");
    }
    
    public Alimento getAlimento() {return alimento;}
    
    // establece el alimento del que este objeto es precio
    public void setAlimento(Alimento alimento) throws IllegalArgumentException {
        if(alimento !=  null)
        	this.alimento = alimento;
        else
            throw new IllegalArgumentException("El alimento no puede ser null");
    }

    public String getNombreMedida() {return nombreMedida;}

    // establece el nombre de la medida
    public void setNombreMedida(String nombreMedida) throws IllegalArgumentException {
        if(nombreMedida !=  null){
            nombreMedida = nombreMedida.trim().toUpperCase();
            if(!nombreMedida.isEmpty())
                this.nombreMedida = nombreMedida;
            else
                throw new IllegalArgumentException("El nombre de la medida no puede quedar vacío");
        }else
            throw new IllegalArgumentException("El nombre de la medida no puede ser null");
    }

    public Double getPrecio() {return precio;}
    
    // establece el precio por unidad de medida
    public void setPrecio(double precio) throws IllegalArgumentException {
        if(precio > 0)
            this.precio = precio;
        else throw new IllegalArgumentException("El precio es inválido");
    }
    
    @Override
    public String toString(){return nombreMedida + " de " + alimento + " " + FORMATO_MONEDA.format(precio);}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alimento == null) ? 0 : alimento.hashCode());
		result = prime * result + ((nombreMedida == null) ? 0 : nombreMedida.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Precio other = (Precio) obj;
		if (alimento == null) {
			if (other.alimento != null)
				return false;
		} else if (!alimento.equals(other.alimento))
			return false;
		if (nombreMedida == null) {
			if (other.nombreMedida != null)
				return false;
		} else if (!nombreMedida.equals(other.nombreMedida))
			return false;
		return true;
	}
    
}
