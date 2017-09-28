package juanguerra.menu_restaurante.gui_fx;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.controlsfx.control.Notifications;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import juanguerra.menu_restaurante.modelo.AlimentoPedido;
import juanguerra.menu_restaurante.modelo.Cola;
import juanguerra.menu_restaurante.modelo.Pedido;

// Clase para manejar los eventos en la interfaz grafica de Pedidos
public class Controlador_Pedidos {

	@FXML	private ListView<Pedido> listaPedidos;
	@FXML	private TextArea listaElementosPedido;
	@FXML	private Button botonDespachar;
	
	private Cola<Pedido> colaPedidos;
	
	private Scene escenaNuevoPedido;
	private Controlador_NuevoPedido nuevoPedidoController;
	
	@FXML	// accion a realizar al pulsar el boton Nuevo
	private void onBotonNuevoClicked(ActionEvent event) {
		nuevoPedidoController.reiniciar();
		nuevoPedidoController.iniciarTransaccion();
		Stage ventana = new Stage();
		ventana.setScene(escenaNuevoPedido);
		ventana.setResizable(false);
		ventana.initModality(Modality.APPLICATION_MODAL);
		ventana.centerOnScreen();
		ventana.setTitle("Nuevo pedido");
		ventana.getIcons().add(new Image(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/iconos/ordering-icon.png").toString()));
		ventana.setOnCloseRequest(w->{
			try {
				nuevoPedidoController.cancelarTransaccion();
			}catch(Exception ex) {
				
			}
		});
		ventana.showAndWait();
		cargarPedidos();
		colaPedidos = new Cola<>(listaPedidos.getItems());
	}
	
	@FXML	// accion a realizar al pulsar el boton Despachar
	private void onBotonDespacharClicked(ActionEvent event) {
		if(!colaPedidos.estaVacia()) {
			Notifications notificacionPedidoTomado = Notifications.create();
			notificacionPedidoTomado.title("Se despachó un pedido!");
			notificacionPedidoTomado.graphic(new ImageView(new Image(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/iconos/basket-accept-icon.png").toString())));
			notificacionPedidoTomado.position(Pos.TOP_RIGHT);
			notificacionPedidoTomado.hideAfter(Duration.seconds(3));			
			Pedido pedido = colaPedidos.desencolar();
			EntityManager manager = Main.emf.createEntityManager();
			manager.getTransaction().begin();;
			pedido = manager.merge(pedido);
			pedido.entregar();
			notificacionPedidoTomado.text(pedido.toString());
			manager.getTransaction().commit();
			manager.close();
			listaPedidos.getItems().remove(pedido);
			if(colaPedidos.getNumeroElementos() > 0)
				botonDespachar.setDisable(false);
			else
				botonDespachar.setDisable(true);
			listaElementosPedido.setText("");
			notificacionPedidoTomado.show();
		}
	}
	
	@FXML
	private void initialize() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_NuevoPedido.fxml"));
			escenaNuevoPedido = new Scene(loader.load(), 650, 450);
			nuevoPedidoController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listaPedidos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listaPedidos.getSelectionModel().selectedItemProperty().addListener( (observable,oldValue,newValue) -> actualizarListaElementosPedido(newValue));
		
		cargarPedidos();
		
		colaPedidos = new Cola<>(listaPedidos.getItems());
		
	}
	
	// método para cargar los pedidos
	@SuppressWarnings("unchecked")
	public void cargarPedidos() {
		EntityManager manager = Main.emf.createEntityManager();
		
		Query consultaPedidos = manager.createQuery( "SELECT p FROM Pedido p WHERE p.estado = 'TOMADO' ORDER BY p.numero ASC");// consulta para obtener todos los menús
		listaPedidos.setItems(FXCollections.observableArrayList((List<Pedido>) consultaPedidos.getResultList()));
		listaElementosPedido.setText("");
		if(listaPedidos.getItems().isEmpty())
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
			listaElementosPedido.setText("");
			List<AlimentoPedido> alimentosPedido = pedido.getAlimentosPedidos();
			alimentosPedido.forEach(p->listaElementosPedido.appendText(p.toString() + "\n"));
			manager.close();
		}
	}
	
}
