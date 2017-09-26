package juanguerra.menu_restaurante.gui_fx;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import juanguerra.menu_restaurante.modelo.Alimento;
import juanguerra.menu_restaurante.modelo.Menu;

// Clase para el manejo de los eventos en la interaz gráfica para administrar los alimentos del restaurante
public class Controlador_AdministracionMenu {
	
	@FXML	private ChoiceBox<Menu> choiceBoxMenu;// caja para seleccinar el menú
	@FXML	private ListView<Alimento> listaAlimentos;
	@FXML	private Button botonNuevo;
	@FXML	private Button botonEditar;
	@FXML	private Button botonEliminar;
	
	private Parent guiNuevo;
	private Controlador_NuevoAlimento guiNuevoController;
	
	private Parent guiEdicion;
	private Controlador_EditarAlimento guiEdicionController;
	
	@FXML	// método con la accion a ejecutar al pulsar el boton Nuevo
	private void onBotonNuevoClicked(ActionEvent event) {
		Menu menu = choiceBoxMenu.getSelectionModel().getSelectedItem();
		if(menu != null) {
			guiNuevoController.borrar();
			guiNuevoController.setMenu(menu);
			Scene s = new Scene(guiNuevo, 400, 200);
			Stage stage = new Stage();
			stage.setScene(s);
			stage.setResizable(false);
			stage.showAndWait();
			actualizarListaAlimentos(menu);
		}
	}
	
	@FXML	// método con la acción a ejecutar al pulsar el boton Editar
	private void onBotonEditarClicked(ActionEvent event) {
		Alimento alimento = listaAlimentos.getSelectionModel().getSelectedItem();
		if(alimento != null) {
			guiEdicionController.borrar();
			guiEdicionController.setAlimento(alimento);
			Scene s = new Scene(guiEdicion, 400, 200);
			Stage stage = new Stage();
			stage.setScene(s);
			stage.setResizable(false);
			stage.showAndWait();
			actualizarListaAlimentos(choiceBoxMenu.getSelectionModel().getSelectedItem());
		}
	}
	
	@FXML	// método con la acción a ejecutar al pulsar el boton Eliminar
	private void onBotonEliminarClicked(ActionEvent event) {
		Alimento alimento = listaAlimentos.getSelectionModel().getSelectedItem();
		if(alimento != null) {
			EntityManager manager = Main.emf.createEntityManager();
			manager.getTransaction().begin();
			alimento = manager.merge(alimento);
			manager.remove(alimento);	
			manager.getTransaction().commit();
			listaAlimentos.getItems().remove(alimento);
			listaAlimentos.getSelectionModel().clearSelection();
			manager.close();
		}
	}
	
	@FXML
	private void initialize() {
		
		
		try {
			FXMLLoader loaderGUINuevo = new FXMLLoader(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_NuevoAlimento.fxml"));
			guiNuevo = loaderGUINuevo.load();
			guiNuevoController = loaderGUINuevo.getController();
			
			FXMLLoader loaderGUIEdicion = new FXMLLoader(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_EditarAlimento.fxml"));
			guiEdicion = loaderGUIEdicion.load();
			guiEdicionController = loaderGUIEdicion.getController();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
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
			listaAlimentos.getSelectionModel().clearSelection();
			manager.close();
		}
	}
	
}
