package juanguerra.menu_restaurante.gui_fx;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.controlsfx.control.Notifications;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import juanguerra.menu_restaurante.modelo.Alimento;
import juanguerra.menu_restaurante.modelo.AlimentoPedido;
import juanguerra.menu_restaurante.modelo.Menu;
import juanguerra.menu_restaurante.modelo.Pedido;
import juanguerra.menu_restaurante.modelo.Precio;

// Clase para manejar los eventos en la interfaz gráfica de ingreso de nuevo pedido
public class Controlador_NuevoPedido {
	
	@FXML	private ChoiceBox<Menu> choiceBoxMenu;
	@FXML	private Spinner<Integer> spinnerCantidad;
	@FXML	private ListView<Alimento> listaAlimentos;
	@FXML	private ListView<AlimentoPedido> listaElementosPedido;
	@FXML	private Button botonAnadir;
	@FXML	private Button botonEnviar;
	@FXML	private Button botonMas;
	@FXML	private Button botonMenos;
	@FXML	private Button botonQuitar;
	
	private EntityManager manager;// entity manager
	private Pedido pedido;// pedido que se está ingresando
	
	private AudioClip notificacion;
	
	@FXML	// evento del boton añadir
	private void onBotonAnadirClicked(ActionEvent event) {
		Alimento a = listaAlimentos.getSelectionModel().getSelectedItem();// se obtiene el alimento seleccionado
		if( a != null) {
			a = manager.merge(a);
			List<Precio> precios = a.getPrecios();
			if(!precios.isEmpty()) {
				Precio p = precios.get(0);
				int cantidad = spinnerCantidad.getValue();
				AlimentoPedido ap = new AlimentoPedido(pedido, p, cantidad);
				ObservableList<AlimentoPedido> elementos = listaElementosPedido.getItems();
				int i = elementos.indexOf(ap);
				if(i >= 0) {
					AlimentoPedido alp = elementos.get(i);
					alp.aumentarCantidad(cantidad);
					elementos.set(i, alp);
				}else 
					elementos.add(ap);
				if(!elementos.isEmpty())
					botonEnviar.setDisable(false);
				else
					botonEnviar.setDisable(true);
			}
			spinnerCantidad.getEditor().textProperty().set("1");
			spinnerCantidad.getValueFactory().setValue(1);
			listaAlimentos.getSelectionModel().clearSelection();
		}
	}
	
	@FXML	// evento del boton Enviar
	private void onBotonEnviarClicked(ActionEvent event) {
		ObservableList<AlimentoPedido> elementos = listaElementosPedido.getItems();
		if(!elementos.isEmpty()) {
			pedido.setHoraPedido(LocalDateTime.now());
			pedido.setEstado("TOMADO");
			elementos.forEach(e->manager.persist(e));
			Stage v = (Stage) choiceBoxMenu.getScene().getWindow();
			Notifications notificacionPedidoTomado = Notifications.create();
			notificacionPedidoTomado.title("Nuevo pedido agregado!");
			notificacionPedidoTomado.graphic(new ImageView(new Image(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/iconos/check-icon.png").toString())));
			notificacionPedidoTomado.position(Pos.TOP_RIGHT);
			notificacionPedidoTomado.hideAfter(Duration.seconds(3));
			notificacionPedidoTomado.text(pedido.toString());
			confirmarTransaccion();
			v.close();
			notificacionPedidoTomado.show();
			notificacion.play();
		}
	}
	
	@FXML	// evento del boton mas
	private void onBotonMenosClicked(ActionEvent event) {
		AlimentoPedido ap = listaElementosPedido.getSelectionModel().getSelectedItem();
		if(ap != null) {
			ObservableList<AlimentoPedido> elementos = listaElementosPedido.getItems();
			int i = elementos.indexOf(ap);
			if( i >= 0) {
				ap.reducirCantidad(1);
				elementos.set(i, ap);
				if(ap.getCantidad() <= 1 && !botonMenos.isDisabled())
					botonMenos.setDisable(true);
			}
			listaElementosPedido.getSelectionModel().select(ap);
		}
	}
	
	@FXML	// evento del boton menos
	private void onBotonQuitarClicked(ActionEvent event) {
		AlimentoPedido ap = listaElementosPedido.getSelectionModel().getSelectedItem();
		if(ap != null) {
			listaElementosPedido.getItems().remove(ap);
			if(listaElementosPedido.getItems().isEmpty())
				botonEnviar.setDisable(true);
		}
	}
	
	@FXML	// evento del boton mas
	private void onBotonMasClicked(ActionEvent event) {
		AlimentoPedido ap = listaElementosPedido.getSelectionModel().getSelectedItem();
		if(ap != null) {
			ObservableList<AlimentoPedido> elementos = listaElementosPedido.getItems();
			int i = elementos.indexOf(ap);
			if( i >= 0) {
				if(ap.getCantidad() < 50) {
					ap.aumentarCantidad(1);
					elementos.set(i, ap);
				}
				if(ap.getCantidad() > 1 && ap.getCantidad() < 50 && botonMenos.isDisabled())
					botonMenos.setDisable(false);
			}
			listaElementosPedido.getSelectionModel().select(ap);
		}
	}
	
	// método para activar/desactivar los botones que modifican los elementos pedidos de un orden
	private void setDisabledBotonesElementosPedidos(boolean disabled) {
		botonMas.setDisable(disabled);
		botonMenos.setDisable(disabled);
		botonQuitar.setDisable(disabled);
	}

	@FXML
	private void initialize() {
		
		manager = null;
		pedido = null;
		
		SpinnerValueFactory.IntegerSpinnerValueFactory intFactorySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,50,1,1);
		spinnerCantidad.setValueFactory(intFactorySpinner);
		spinnerCantidad.setEditable(false);
		
		choiceBoxMenu.getSelectionModel().selectedItemProperty().addListener( (observable,oldValue,newValue) -> {
			actualizarAlimentosMenu(newValue);
			listaElementosPedido.getSelectionModel().clearSelection();
			});
		
		listaAlimentos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listaAlimentos.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
			if(newValue == null) {
				botonAnadir.setDisable(true);
				spinnerCantidad.setDisable(true);
			}else {
				spinnerCantidad.setDisable(false);
				botonAnadir.setDisable(false);
				listaElementosPedido.getSelectionModel().clearSelection();
			}
			spinnerCantidad.getEditor().textProperty().set("1");
			spinnerCantidad.getValueFactory().setValue(1);
		});
		
		listaElementosPedido.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listaElementosPedido.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
			if(newValue == null) {
				setDisabledBotonesElementosPedidos(true);
			}else {
				listaAlimentos.getSelectionModel().clearSelection();
				setDisabledBotonesElementosPedidos(false);
				if(newValue.getCantidad() <= 1)
					botonMenos.setDisable(true);
				if(newValue.getCantidad() >= 50)
					botonMas.setDisable(true);
			}
		});
		
		reiniciar();
		
		notificacion = new AudioClip(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/sonidos/job-done.mp3").toString());
		
	}
	
	// método para reiniciar la interfaz
	@SuppressWarnings("unchecked")
	public void reiniciar() {
		
		EntityManager manager = Main.emf.createEntityManager();
		Query consultaMenus = manager.createQuery( "SELECT m FROM Menu m");// consulta para obtener todos los menús
		choiceBoxMenu.setItems(FXCollections.observableArrayList((List<Menu>) consultaMenus.getResultList()));
		choiceBoxMenu.getSelectionModel().select(0);;
		manager.close();
		
		actualizarAlimentosMenu(choiceBoxMenu.getSelectionModel().getSelectedItem());// rellena la lista de alimentos de acuerdo al menu seleccionado
		listaElementosPedido.getItems().clear();
		setDisabledBotonesElementosPedidos(true);
		
		botonAnadir.setDisable(true);
		botonEnviar.setDisable(true);
		
		spinnerCantidad.getEditor().textProperty().set("1");
		spinnerCantidad.getValueFactory().setValue(1);
		spinnerCantidad.setDisable(true);
	}
	
	// método para actualizar la lista de alimentos de acuerdo al menú seleccionado
	private void actualizarAlimentosMenu(Menu menu) {
		if(menu != null) {
			EntityManager manager = Main.emf.createEntityManager();
			menu = manager.merge(menu);
			listaAlimentos.setItems(FXCollections.observableArrayList(menu.getAlimentos()));
			listaAlimentos.getSelectionModel().clearSelection();
			manager.close();
		}
	}
	
	// inicia la transaccion
	public void iniciarTransaccion() {
		if(manager == null) {
			manager = Main.emf.createEntityManager();
			manager.getTransaction().begin();
			pedido = new Pedido("PARA LLEVAR", "CREADO", LocalDateTime.now());
			manager.persist(pedido);
			pedido = manager.merge(pedido);
		}
	}
	
	// cancela la transaccion
	public void cancelarTransaccion() {
		if(manager != null) {
			manager.getTransaction().rollback();
			manager.close();
			manager = null;
			pedido = null;
		}
	}
	
	// confirma la transaccion
	public void confirmarTransaccion() {
		if(manager != null) {
			manager.getTransaction().commit();
			manager.close();
			manager = null;
			pedido = null;
		}
	}
	
}
