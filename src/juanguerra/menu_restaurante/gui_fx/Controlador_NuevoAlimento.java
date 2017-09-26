package juanguerra.menu_restaurante.gui_fx;

import javax.persistence.EntityManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import juanguerra.menu_restaurante.modelo.Alimento;
import juanguerra.menu_restaurante.modelo.Menu;
import juanguerra.menu_restaurante.modelo.Precio;

// Clase para manejar los eventos de la interfaz gráfica para ingresar un nuevo alimento
public class Controlador_NuevoAlimento {
	
	public static final String NOMBRE_UNIDAD_DEFAULT = "UNIDAD";
	
	@FXML	TextField fieldMenu;
	@FXML	TextField fieldAlimento;
	@FXML	TextField fieldPrecioUnitario;

	private Menu menu;// menu al que pertenece el nuevo alimento a ingresar
	
	private Alert alertaErrores;// ventana para mostrar los errores
	
	@FXML	// método con la acción a realizar al pulsar el botón Guardar
	private void onBotonGuardarClicked(ActionEvent event) {
		try {
			Alimento nuevo = new Alimento(fieldAlimento.getText(), menu, true);
			try {
				double precio = Double.parseDouble(fieldPrecioUnitario.getText());
				Precio pNuevo = new Precio(nuevo, NOMBRE_UNIDAD_DEFAULT, precio);
				EntityManager manager = Main.emf.createEntityManager();
				manager.getTransaction().begin();// se inicia una transacción
				try {
					manager.persist(nuevo);
					manager.persist(pNuevo);
					manager.getTransaction().commit();// se confirma la transacción
					Alert alertaExito = new Alert(AlertType.INFORMATION, "", new ButtonType("Volver"));
					alertaExito.setTitle("Mensaje");
					alertaExito.setHeaderText("Se ingresó un nuevo registro");	
					alertaExito.setContentText("Se ingresó el alimento: " + nuevo.getNombre());
					alertaExito.showAndWait();
					fieldAlimento.clear();
					fieldPrecioUnitario.clear();
					fieldAlimento.requestFocus();
				}catch(Exception ex) {
					manager.getTransaction().rollback();// no se realiza la transaccion
					alertaErrores.setTitle("ERROR");
					alertaErrores.setHeaderText("Error de datos");
					alertaErrores.setContentText("NO se puede almacenar el nuevo alimento!\nEs posible que ya exista");
					alertaErrores.showAndWait();
				}
			}catch(NumberFormatException ex) {
				alertaErrores.setTitle("ERROR");
				alertaErrores.setHeaderText("Ha omitido un dato");
				alertaErrores.setContentText("Ingrese el precio!");
				alertaErrores.showAndWait();
			}
		}catch(IllegalArgumentException ex) {
			alertaErrores.setTitle("ERROR");
			alertaErrores.setHeaderText("Ha ocurrido un error con los datos ingresados");
			alertaErrores.setContentText(ex.getMessage());
			alertaErrores.showAndWait();
		}
	}
	
	@FXML
	private void initialize() {
		alertaErrores = new Alert(AlertType.ERROR, "", new ButtonType("Entendido"));
		borrar();
		fieldAlimento.setOnKeyTyped(k->{
			String key = k.getCharacter();
			if(!Character.isLetter(key.charAt(0)) && key.charAt(0) != ' ')
				k.consume();
		});
		fieldPrecioUnitario.setOnKeyTyped(k->{
			String texto = fieldPrecioUnitario.getText() + k.getCharacter();
			try {
				Double.parseDouble(texto);
			}catch(NumberFormatException ex) {
				k.consume();
			}
		});
	}
	
	// establece el menu al que pertenece el alimento
	public void setMenu(Menu menu) {
		this.menu = menu;
		if(menu != null)
			fieldMenu.setText(menu.getNombre());
	}
	
	// método para borrar el contenido de los campos de texto
	public void borrar() {
		this.menu = null;
		fieldMenu.clear();
		fieldAlimento.clear();
		fieldPrecioUnitario.clear();
	}
	
}
