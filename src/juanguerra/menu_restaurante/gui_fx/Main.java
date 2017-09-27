package juanguerra.menu_restaurante.gui_fx;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static EntityManagerFactory emf;
	
	public static Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		Main.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/juanguerra/menu_restaurante/gui_fx/GUI_Inicio.fxml"));
		Scene escena = new Scene(root, 600, 450);
		stage.setScene(escena);
		stage.setMinWidth(600);
		stage.setMinHeight(400);
		stage.setOnCloseRequest(e->{
			if(emf != null) {
				try {emf.close();}catch(Exception ex) {ex.printStackTrace();}
			}
		});
		stage.setMaximized(true);
		stage.show();

	}
	
	public static void main(String args[]) {
		emf = null;
		try {
			emf = Persistence.createEntityManagerFactory("MenuRestaurante");
			launch(args);
		}catch(Exception ex) {
			Platform.exit();
			ex.printStackTrace();
		}		
	}

}
