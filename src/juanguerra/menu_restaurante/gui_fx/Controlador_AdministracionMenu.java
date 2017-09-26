package juanguerra.menu_restaurante.gui_fx;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import juanguerra.menu_restaurante.modelo.Alimento;
import juanguerra.menu_restaurante.modelo.Menu;

// Clase para el manejo de los eventos en la interaz gráfica para administrar los alimentos del restaurante
public class Controlador_AdministracionMenu {
	
	@FXML	private ChoiceBox<Menu> choiceBoxMenu;// caja para seleccinar el menú
	@FXML	private ListView<Alimento> listaAlimentos;
	@FXML	private Button botonNuevo;
	@FXML	private Button botonEditar;
	@FXML	private Button botonEliminar;
	
	@FXML	// método con la accion a ejecutar al pulsar el boton Nuevo
	private void onBotonNuevoClicked(ActionEvent event) {
		
	}
	
	@FXML	// método con la acción a ejecutar al pulsar el boton Editar
	private void onBotonEditarClicked(ActionEvent event) {
		Alimento alimento = listaAlimentos.getSelectionModel().getSelectedItem();
		if(alimento != null) {
			
		}
	}
	
	@FXML	// método con la acción a ejecutar al pulsar el boton Eliminar
	private void onBotonEliminarClicked(ActionEvent event) {
		Alimento alimento = listaAlimentos.getSelectionModel().getSelectedItem();
		if(alimento != null) {
			EntityManager manager = Main.emf.createEntityManager();			
			manager.remove(alimento);	
			listaAlimentos.getItems().remove(alimento);
			listaAlimentos.getSelectionModel().clearSelection();
			manager.close();
		}
	}
	
	@FXML
	private void initialize() {
		
		// evento para actualizar la lista de alimentos cuando se cambie el menú seleccionado
		choiceBoxMenu.getSelectionModel().selectedItemProperty().addListener( (observable,oldValue,newValue) -> actualizarListaAlimentos(newValue));
		
		listaAlimentos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listaAlimentos.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
			if(newValue == null) {
				botonNuevo.setDisable(false);
				botonEditar.setDisable(true);
				botonEliminar.setDisable(true);
			}else {
				botonNuevo.setDisable(true);
				botonEditar.setDisable(false);
				botonEliminar.setDisable(false);
			}
		});
		
		reiniciar();
		
	}
	
	// método para reiniciar la interfaz gráfica
	@SuppressWarnings("unchecked")
	public void reiniciar() {
		EntityManager manager = Main.emf.createEntityManager();
		
		Query consultaMenus = manager.createQuery( "SELECT m FROM Menu m");// consulta para obtener todos los menús
		choiceBoxMenu.setItems(FXCollections.observableArrayList((List<Menu>) consultaMenus.getResultList()));
		choiceBoxMenu.getSelectionModel().select(0);;
		
		manager.close();
		
		actualizarListaAlimentos(choiceBoxMenu.getSelectionModel().getSelectedItem());
	}
	
	// método que actualiza la lista de alimentos de acuerdo al menú seleccionado
	private void actualizarListaAlimentos(Menu menu) {
		if(menu != null) {
			EntityManager manager = Main.emf.createEntityManager();
			menu = manager.merge(menu);
			listaAlimentos.setItems(FXCollections.observableArrayList(menu.getAlimentos()));
			manager.close();
		}
	}
	
}
