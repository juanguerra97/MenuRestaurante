package juanguerra.menu_restaurante.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Pedido")
public class Pedido implements Serializable {
	
	/*
	 * Clase para almacenar los datos de un pedido
	 * 
	 * */
    
    private static final long serialVersionUID = 1L;
    
    public static final String TIPOS_PEDIDO[] = {"PARA LLEVAR","MESA","DOMICILIO"};// tipos de pedido
    public static final String ESTADOS_PEDIDO[] = {"CREADO","TOMADO","ENTREGADO"};// estados de un pedido
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "numero")
    private Long numero;// número de pedido, debe ser único
    
    @Column(name = "tipo")
    private String tipo;// tipo de pedido
    
    @Column(name = "estado")
    private String estado;// estado del pedido
    
    @Column(name = "horaPedido")
    private LocalDateTime horaPedido;// fecha y hora en que se tomó el pedido
    
    @Column(name = "horaEntrega")
    private LocalDateTime horaEntrega;// fecha y hora en que se entregó el pedido
    
    @Column(name = "total")
    private Double total;// costo total del pedido
    
    @Column(name = "numeroMesa")
    private Integer numeroMesa;// número de mesa del pedido(si el tipo del pedido = MESA)
    
    @Column(name = "direccion")
    private String direccion;// direccion donde se debe entregar el pedido(si el tipo del pedido = PARA LLEVAR)
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<AlimentoPedido> alimentosPedidos;// alimentos pedidos en la orden, relación uno a varios con la clase AlimentoPedido
    
    // COnstructor predeterminado
    public Pedido(){
        this.numero = null;
        this.tipo = null;
        this.estado = null;
        this.horaPedido = null;
        this.horaEntrega = null;
        this.total = null;
    }
    
    // COnstructor
    public Pedido(Long numero, String tipo, String estado, LocalDateTime horaPedido) throws IllegalArgumentException {
        this();// llamada al constructor predeterminado
        setNumero(numero);
        setTipo(tipo);
        setEstado(estado);
        setHoraPedido(horaPedido);
    }
    
    // COnstructor
    public Pedido(Long numero, String tipo, String estado, LocalDateTime horaPedido, int numeroMesa) throws IllegalArgumentException {
        this();// llamada al constructor predeterminado
        setNumero(numero);
        setTipo(tipo);
        setEstado(estado);
        setHoraPedido(horaPedido);
        setNumeroMesa(numeroMesa);
    }
    
    // COnstructor
    public Pedido(Long numero, String tipo, String estado, LocalDateTime horaPedido, String direccion) throws IllegalArgumentException {
        this();// llamada al constructor predeterminado
        setNumero(numero);
        setTipo(tipo);
        setEstado(estado);
        setHoraPedido(horaPedido);
        setDireccion(direccion);
    }
    
    // COnstructor que recibe todos los atributos para un pedido de llevar
    public Pedido(Long numero, String tipo, String estado, LocalDateTime horaPedido, LocalDateTime horaEntrega, double total) throws IllegalArgumentException {
        this(numero, tipo, estado, horaPedido);// llamada al segundo constructor
        setHoraEntrega(horaEntrega);
        setTotal(total);
    }
    
    // COnstructor
    public Pedido(String tipo, String estado, LocalDateTime horaPedido) throws IllegalArgumentException {
    	this();
    	setTipo(tipo);
        setEstado(estado);
        setHoraPedido(horaPedido);
    }
    
    // COnstructor
    public Pedido(Long numero, String tipo, String estado) throws IllegalArgumentException {
        this(numero, tipo, estado, LocalDateTime.now());// llamada al segundo constructor
    }
    
    // COnstructor
    public Pedido(Long numero, String tipo, String estado, int numeroMesa) throws IllegalArgumentException {
        this(numero, tipo, estado, LocalDateTime.now(), numeroMesa);// llamada al tercer constructor
    }
    
    // COnstructor
    public Pedido(Long numero, String tipo, String estado, String direccion) throws IllegalArgumentException {
        this(numero, tipo, estado, LocalDateTime.now(), direccion);// llamada al cuarto constructor
    }
    
    public Long getNumero() {return numero;}

    // establece el numero del pedido
    public void setNumero(long numero) throws IllegalArgumentException {
        if(numero > 0)
            this.numero = numero;
        else
            throw new IllegalArgumentException("El número del pedido es inválido");
    }

    public String getTipo() {return tipo;}
    
    // establece el tipo del pedido
    public void setTipo(String tipo) throws IllegalArgumentException {
        if(tipo !=  null){
            tipo = tipo.trim().toUpperCase();
            if(!tipo.isEmpty()){
                boolean valido = false;
                for(int i = 0; i < TIPOS_PEDIDO.length; ++i)
                    if(TIPOS_PEDIDO[i].equals(tipo)){
                        valido = true;
                        break;
                    }
                if(valido)
                    this.tipo = tipo;
                else
                    throw new IllegalArgumentException("El tipo de pedido no es válido");
            }else
                throw new IllegalArgumentException("El tipo del pedido no puede quedar vacío");
        }else
            throw new IllegalArgumentException("El tipo del pedido no puede ser null");
    }

    public String getEstado() {return estado;}
    
    // establece el estado del pedido
    public void setEstado(String estado) throws IllegalArgumentException {
        if(estado !=  null){
            estado = estado.trim().toUpperCase();
            if(!estado.isEmpty()){
                boolean valido = false;
                for(int i = 0; i < ESTADOS_PEDIDO.length; ++i)
                    if(ESTADOS_PEDIDO[i].equals(estado)){
                        valido = true;
                        break;
                    }
                if(valido)
                    this.estado = estado;
                else
                    throw new IllegalArgumentException("El estado del pedido no es válido");
            }else
                throw new IllegalArgumentException("El estado del pedido no puede quedar vacío");
        }else
            throw new IllegalArgumentException("El estado del pedido no puede ser null");
    }

    public LocalDateTime getHoraPedido() {return horaPedido;}
    
    // establece la fecha y hora del pedido
    public void setHoraPedido(LocalDateTime horaPedido) throws IllegalArgumentException {
        if(horaPedido != null)
            this.horaPedido = horaPedido;
        else
            throw new IllegalArgumentException("La hora en que se pidió el pedido no puede ser null");
    }

    public LocalDateTime getHoraEntrega() {return horaEntrega;}
    
    // establece la hora en que se entregó el pedido
    public void setHoraEntrega(LocalDateTime horaEntrega) throws IllegalArgumentException {
        if(horaEntrega != null)
            this.horaEntrega = horaEntrega;
        else
            throw new IllegalArgumentException("La hora en que se entregó el pedido no puede ser null");
    }

    public Double getTotal() {return total;}
    
    // establece el costo total del pedido
    public void setTotal(double total) throws IllegalArgumentException {
        if(total > 0)
            this.total = total;
        else
            throw new IllegalArgumentException("El total es inválido");
    }

    public Integer getNumeroMesa() {return numeroMesa;}
    
    // establece el número de mesa
    public void setNumeroMesa(int numeroMesa) throws IllegalArgumentException {
        if(numeroMesa > 0)
            this.numeroMesa = numeroMesa;
        else
            throw new IllegalArgumentException("El número de mesa es inválido");
    }

    public String getDireccion() {return direccion;}
    
    // establece la dirección
    public void setDireccion(String direccion) throws IllegalArgumentException {
        if(direccion !=  null){
            direccion = direccion.trim().toUpperCase();
            if(!direccion.isEmpty())
                this.direccion = direccion;
            else
                throw new IllegalArgumentException("La dirección no puede quedar vacía");
        }else
            throw new IllegalArgumentException("La dirección no puede ser null");
    }
    
    public List<AlimentoPedido> getAlimentosPedidos(){return alimentosPedidos;}
    
    // método para entregar el pedido
    public void entregar() {
    	horaEntrega = LocalDateTime.now();
    	estado = "ENTREGADO";
    	calcularTotal();
    }
    
    // calcula el total
    public void calcularTotal() {
    	if(alimentosPedidos != null) {
    		double t = 0.0d;
    		for(int i = 0; i < alimentosPedidos.size(); ++i) {
    			AlimentoPedido ap = alimentosPedidos.get(i);
    			t += ap.getPrecio().getPrecio() * ap.getCantidad();
    		}    			
    		this.total = t;
    	}else
    		total = 0.0d;
    }
    
    @Override
    public String toString(){return "#" + numero + ", Hora: " + horaPedido.toLocalTime();}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.numero);
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
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        return true;
    }
    
}