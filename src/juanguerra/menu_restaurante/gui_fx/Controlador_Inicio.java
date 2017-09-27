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
	
	private Parent guiPedidos;

	@FXML	// accion a realizar al seleccionar la opcion Administrar en el menu Inicio
	private void onOpcionAdministrar(ActionEvent event) {
		if(!panel.getCenter().equals(guiAdmon)) {
			panel.setCenter(guiAdmon);
			Main.stage.setTitle("Administración");
		}
	}
	
	@FXML	// accion a realizar al seleccionar la opcion Pedidos en el menu Inicio
	private void onOpcionPedidos(ActionEvent event) {
		if(!panel.getCenter().equals(guiPedidos)) {
			panel.setCenter(guiPedidos);
			Main.stage.setTitle("Pedidos");
		}
	}
	
	@FXML
	private void initialize() {
		try {
			guiAdmon = FXMLLoader.load(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_AdministracionMenu.fxml"));
			
			guiPedidos = FXMLLoader.load(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_Pedidos.fxml"));
			
			panel.setCenter(guiPedidos);
			Main.stage.setTitle("Pedidos");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
