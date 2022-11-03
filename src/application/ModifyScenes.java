package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ModifyScenes {
	static Stage stage;
	// i � a contagem de stages
	static int i=0; 
	//List para listar todos os stages
	public static List<Stage>listS = new ArrayList<>();
	//Funcao para abrir um novo stage/janela.
	public void modify(String scene, String title) {

		
				FXMLLoader fxml = new FXMLLoader();
				try {
					stage = new Stage();
					listS.add(stage);
					@SuppressWarnings("static-access")
					Pane root = fxml.load(getClass().getResource(scene));
					Scene nwscene = new Scene(root);
					stage.setScene(nwscene);
					//if(!(scene.equals("view/CAF_refeitorio.fxml"))) {
						stage.setResizable(false);
					//}
					Stage stageatual = stage;
					stage.getIcons().add(new Image(ModifyScenes.class.getResourceAsStream("view/Icons/java.png")));
					if(scene.equals("view/CAF_refeitorio.fxml")) {
						stage.setMaximized(true);
						stage.setResizable(true);
					}
					nwscene.setOnKeyPressed(e ->{
						if(e.getCode() == KeyCode.ESCAPE) {
							close();
							stageatual.close();
						}
					});
					
					stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent event) {
							close();
						}
					});
					stage.setTitle(title);
					i++;
					stage.show();
		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}

	public static void close() {
		if(listS.lastIndexOf(stage) != -1) {
			listS.remove(listS.indexOf(stage));
		}
		
		
		i--;
	}
}
