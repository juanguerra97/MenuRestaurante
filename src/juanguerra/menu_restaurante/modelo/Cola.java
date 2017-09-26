package juanguerra.menu_restaurante.modelo;

import java.util.List;

// Clase para representar la estructura de datos Cola
public class Cola<T> {
	
	private Nodo primero;// primer nodo de la cola
	private Nodo ultimo;// último nodo de la cola
	private int numElementos;// contador del numero de elemntos en la cola
	
	// Constructor que crea una cola vacía
	public Cola() {
		primero = ultimo = null;
		numElementos = 0;
	}
	
	// constructor que recibe una lista con los elementos a encolar
	public Cola(List<T> listaElementos) {
		this();
		if(listaElementos != null) {
			for(int i = 0; i < listaElementos.size(); ++i)
				encolar(listaElementos.get(i));
		}
	}
	
	// método para insertar un elemento en la cola
	public void encolar(T dato) {
		if(dato != null) {
			Nodo nuevo = new Nodo(dato);// creacion del nodo con el dato a insertar
			if(numElementos == 0)// si la cola está vacía el nuevo nodo pasa a ser el primero y el último en la cola
				primero = ultimo = nuevo;
			else {
				ultimo.siguiente = nuevo;
				ultimo = nuevo;
			}
			++numElementos;
		}
	}
	
	// método para eliminar un elemento de la cola, devuelve el dato del primer elemento
	public T desencolar() {
		if(numElementos > 0) {
			T dato = primero.dato;
			if(numElementos == 1)
				primero = ultimo = null;
			else
				primero = primero.siguiente;
			--numElementos;
			return dato;
		}
		return null;
	}
	
	// método que devuelve el primer elemento en la cola
	public T getPrimero() {
		if(numElementos > 0)
			return primero.dato;
		return null;
	}
	
	// método que devuelve el último elemento en la cola
	public T getUltimo() {
		if(numElementos > 0)
			return ultimo.dato;
		return null;
	}
	
	// método para vaciar la cola
	public void vaciar() {
		while(desencolar() != null);
	}
	
	public int getNumeroElementos() {return numElementos;}
	
	public boolean estaVacia() {return numElementos == 0;}

	// clase interna para crear los nodos
	private class Nodo{
		
		public T dato;
		public Nodo siguiente;
		
		// COnstructor
		public Nodo(T dato, Nodo siguiente) {
			this.dato = dato;
			this.siguiente = siguiente;
		}
		
		// COnstructor
		public Nodo(T dato) {
			this(dato, null);
		}
		
	}
	
}
