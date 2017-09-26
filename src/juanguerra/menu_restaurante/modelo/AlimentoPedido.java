package juanguerra.menu_restaurante.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "AlimentoPedido")
public class AlimentoPedido implements Serializable {
	
	/*
	 * Clase para almacenar los datos de un alimento en un pedido
	 * */
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numeroPedido")
    private Pedido pedido;// número del pedido
    
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_precio")
    private Precio precio;
    
    @Column(name = "cantidad")
    private Integer cantidad;// cantidad pedida
    
    @Column(name = "total")
    private Double total;// costo total por la cantidad pedida de acuerdo a la medida
    
    // Constructor predeterminado
    public AlimentoPedido(){
        this.pedido = null;
        precio = null;
        this.cantidad = null;
        this.total = null;
    }
    
    // Constructor que recibe todos los atributos del alimento pedido
    public AlimentoPedido(Pedido pedido, Precio precio, Integer cantidad, double total) {
        setPedido(pedido);
        setPrecio(precio);
        setCantidad(cantidad);
        setTotal(total);
    }
    
    // Constructor que no recibe el total(lo establece en null)
    public AlimentoPedido(Pedido pedido, Precio precio, Integer cantidad) throws IllegalArgumentException {
        setPedido(pedido);
        setPrecio(precio);
        setCantidad(cantidad);
        this.total = null;
    }
    
    public Pedido getPedido() {return pedido;}
    
    // establece el numero del pedido al que pertenece el alimento pedido
    public void setPedido(Pedido pedido) throws IllegalArgumentException {
        if(pedido != null)
            this.pedido = pedido;
        else
            throw new IllegalArgumentException("El pedido es inválido no puede ser null");
    }

    
    public Precio getPrecio() {return precio;}
    
    public void setPrecio(Precio precio) throws IllegalArgumentException {
    	if(precio != null)
    		this.precio = precio;
    	else
    		throw new IllegalArgumentException("EL precio no puede se nulo");
    }

    public Integer getCantidad() {return cantidad;}
    
    // establece la cantidad pedida del alimento con la medida especificada
    public void setCantidad(int cantidad) throws IllegalArgumentException {
        if(cantidad > 0)
            this.cantidad = cantidad;
        else
            throw new IllegalArgumentException("La cantidad pedida es inválida");
    }

    public Double getTotal() {return total;}
    
    // establece el  costo total
    public void setTotal(double total) throws IllegalArgumentException {
        if(total > 0)
            this.total = total;
        else
            throw new IllegalArgumentException("El costo total del alimento pedido es inválido");
    }
    
    // calcula el total
    public void calcularTotal() {
    	total = precio.getPrecio() * cantidad;
    }
    
    // aumenta la cantidad pedida
    public void aumentarCantidad(int aumento){
        if(aumento > 0)
            cantidad += aumento;
    }
    
    // disminuye la cantidad pedida
    public void reducirCantidad(int decremento){
        if(decremento > 0 && decremento < cantidad)
            cantidad -= decremento;
    }
    
    @Override
    public String toString(){return "Pedido: " + pedido + ", " +  precio.toString() + " x " + cantidad;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
		result = prime * result + ((precio == null) ? 0 : precio.hashCode());
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
		AlimentoPedido other = (AlimentoPedido) obj;
		if (pedido == null) {
			if (other.pedido != null)
				return false;
		} else if (!pedido.equals(other.pedido))
			return false;
		if (precio == null) {
			if (other.precio != null)
				return false;
		} else if (!precio.equals(other.precio))
			return false;
		return true;
	}
    
    
}

