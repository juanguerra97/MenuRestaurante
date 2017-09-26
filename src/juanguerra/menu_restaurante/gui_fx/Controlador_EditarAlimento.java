package juanguerra.menu_restaurante.gui_fx;

import javax.persistence.EntityManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import juanguerra.menu_restaurante.modelo.Alimento;
import juanguerra.menu_restaurante.modelo.Precio;

// Clase para manejar los eventos en la interfaz de edición de un alimento
public class Controlador_EditarAlimento {

	@FXML	private TextField fieldNombre;
	@FXML	private TextField fieldPrecio;
	
	private Alimento alimento;// alimento a editar
	
	private Alert alertaErrores;// ventana para mostrar los errores
	
	@FXML	// método con la acción a realizar al  pulsar el botón de guardar
	private void onBotonGuardarClicked(ActionEvent event) {
		EntityManager manager = Main.emf.createEntityManager();
		manager.getTransaction().begin();// se inicia un transacción
		try {
			alimento = manager.merge(alimento);
			Precio precio = alimento.getPrecios().get(0);
			alimento.setNombre(fieldNombre.getText());
			precio = manager.merge(precio);
			try {
				double p = Double.parseDouble(fieldPrecio.getText());
				if(p != precio.getPrecio())
					precio.setPrecio(p);
			}catch(NumberFormatException ex) {
				fieldPrecio.setText("" + precio.getPrecio());
			}
			manager.getTransaction().commit();// se confirma la transacción
			Alert alertaExito = new Alert(AlertType.INFORMATION, "", new ButtonType("Volver"));
			alertaExito.setTitle("Mensaje");
			alertaExito.setHeaderText("Se actualizó un registro");	
			alertaExito.setContentText("Se actualizó el alimento: " + alimento.getNombre());
			alertaExito.showAndWait();
			borrar();
		}catch(IllegalArgumentException ex) {
			manager.getTransaction().rollback();// no se  realiza la transacción
			alertaErrores.setTitle("ERROR");
			alertaErrores.setHeaderText("Ha ocurrido un error con los datos ingresados");
			alertaErrores.setContentText(ex.getMessage());
			alertaErrores.showAndWait();
		}catch(Exception ex) {
			manager.getTransaction().rollback();// no se realiza la transaccion
			alertaErrores.setTitle("ERROR");
			alertaErrores.setHeaderText("Error de datos");
			alertaErrores.setContentText("NO se puede almacenar los datos\nEs posible que el nombre ya exista");
			alertaErrores.showAndWait();
		}
		manager.close();
	}
	
	@FXML
	private void initialize() {
		alertaErrores = new Alert(AlertType.ERROR, "", new ButtonType("Entendido"));
		alimento = null;
		fieldNombre.setOnKeyTyped(k->{
			String key = k.getCharacter();
			if(!Character.isLetter(key.charAt(0)) && key.charAt(0) != ' ')
				k.consume();
		});
		fieldPrecio.setOnKeyTyped(k->{
			String texto = fieldPrecio.getText() + k.getCharacter();
			try {
				Double.parseDouble(texto);
			}catch(NumberFormatException ex) {
				k.consume();
			}
		});
	}
	
	// establece el alimento a editar
	public void setAlimento(Alimento alimento) {
		this.alimento = alimento;
		if(alimento != null) {
			EntityManager manager = Main.emf.createEntityManager();
			alimento = manager.merge(alimento);
			fieldNombre.setText(alimento.getNombre());
			fieldPrecio.setText(""+alimento.getPrecios().get(0).getPrecio());
			fieldNombre.selectAll();
			fieldNombre.requestFocus();
			manager.close();
		}
	}
	
	// borra el contenido de los campos de texto
	public void borrar() {
		fieldNombre.clear();
		fieldPrecio.clear();
	}
	
}
