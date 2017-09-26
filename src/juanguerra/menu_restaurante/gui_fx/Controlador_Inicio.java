package juanguerra.menu_restaurante.gui_fx;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

// Clase para manejar los eventos en la interfaz de inicio
public class Controlador_Inicio {
	
	@FXML	private BorderPane panel;
	
	private Parent guiAdmon;
	private Controlador_AdministracionMenu guiAdmonController;
	
	private Parent guiPedidos;

	@FXML	// accion a realizar al seleccionar la opcion Administrar en el menu Inicio
	private void onOpcionAdministrar(ActionEvent event) {
		panel.setCenter(guiAdmon);
	}
	
	@FXML	// accion a realizar al seleccionar la opcion Pedidos en el menu Inicio
	private void onOpcionPedidos(ActionEvent event) {
		
	}
	
	@FXML
	private void initialize() {
		try {
			FXMLLoader loaderGUIAdmon = new FXMLLoader(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_AdministracionMenu.fxml"));
			guiAdmon = loaderGUIAdmon.load();
			guiAdmonController = loaderGUIAdmon.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
