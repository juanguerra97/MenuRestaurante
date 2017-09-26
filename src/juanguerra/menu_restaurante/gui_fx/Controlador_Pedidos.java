package juanguerra.menu_restaurante.gui_fx;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import juanguerra.menu_restaurante.modelo.AlimentoPedido;
import juanguerra.menu_restaurante.modelo.Cola;
import juanguerra.menu_restaurante.modelo.Pedido;

// Clase para manejar los eventos en la interfaz grafica de Pedidos
public class Controlador_Pedidos {

	@FXML	private ListView<Pedido> listaPedidos;
	@FXML	private ListView<AlimentoPedido> listaElementosPedido;
	@FXML	private Button botonDespachar;
	
	private Cola<Pedido> colaPedidos;
	
	@FXML	// accion a realizar al pulsar el boton Nuevo
	private void onBotonNuevoClicked(ActionEvent event) {
		
	}
	
	@FXML	// accion a realizar al pulsar el boton Despachar
	private void onBotonDespacharClicked(ActionEvent event) {
		if(!colaPedidos.estaVacia()) {
			Pedido pedido = colaPedidos.desencolar();
			EntityManager manager = Main.emf.createEntityManager();
			manager.getTransaction().begin();;
			pedido = manager.merge(pedido);
			pedido.entregar();
			manager.getTransaction().commit();
			manager.close();
			listaPedidos.getItems().remove(pedido);
			if(colaPedidos.getNumeroElementos() > 0)
				botonDespachar.setDisable(false);
			else
				botonDespachar.setDisable(true);
		}
	}
	
	@FXML
	private void initialize() {
		listaPedidos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listaPedidos.getSelectionModel().selectedItemProperty().addListener( (observable,oldValue,newValue) -> actualizarListaElementosPedido(newValue));
		
		listaElementosPedido.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		cargarPedidos();
		
		colaPedidos = new Cola<>(listaPedidos.getItems());
		
	}
	
	// método para cargar los pedidos
	@SuppressWarnings("unchecked")
	public void cargarPedidos() {
		EntityManager manager = Main.emf.createEntityManager();
		
		Query consultaPedidos = manager.createQuery( "SELECT p FROM Pedido p WHERE p.estado = 'TOMADO' ORDER BY p.numero ASC");// consulta para obtener todos los menús
		listaPedidos.setItems(FXCollections.observableArrayList((List<Pedido>) consultaPedidos.getResultList()));
		listaElementosPedido.setItems(FXCollections.observableArrayList());
		if(listaElementosPedido.getItems().isEmpty())
			botonDespachar.setDisable(true);
		else
			botonDespachar.setDisable(false);
		
		manager.close();
	}
	
	// metodo para actualizar la lista de elementos pedidos de un pedido
	private void actualizarListaElementosPedido(Pedido pedido) {
		if(pedido != null) {
			EntityManager manager = Main.emf.createEntityManager();
			pedido = manager.merge(pedido);
			listaElementosPedido.setItems(FXCollections.observableArrayList(pedido.getAlimentosPedidos()));
			manager.close();
		}
	}
	
}
