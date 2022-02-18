package application;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import application.model.Combobox;
import application.model.Usuario;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class Main extends Application {
	public static Stage Stagemain;
	public static ModifyScenes modify = new ModifyScenes();
	public static Usuario user = new Usuario();
	public void start(Stage primaryStage) {
		try {
			Stagemain = primaryStage;
			Pane FXMLlogin = FXMLLoader.load(getClass().getResource("view/CAF_inicio.fxml"));
			Scene MainScene = new Scene(FXMLlogin);
			Stagemain.setOnCloseRequest(e -> Platform.exit());
			Stagemain.setTitle("CAF - Controle de Acesso");
			Stagemain.setResizable(false);
			Stagemain.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/CAF.png")));
			Stagemain.setScene(MainScene);
			Stagemain.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//Funcao para o dialog warning e information
		public static void dialogBox(String text, int i){
			Stage stage;
			if(i==1) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Atenção");
					stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/Atencao.png")));
					alert.setContentText(text);
					alert.show();
					
			} else if(i==2) {
				Alert alert = new Alert(AlertType.INFORMATION);
				
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Atenção!");
				stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/information.png")));
				alert.setContentText(text);
				alert.show();
			}
		}
		//Funcao para o dialog confirmation.
		public static Optional<ButtonType> ConfirmationDialog(String Header, String text){
			Stage stage;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/confirm.png")));
			alert.setHeaderText(Header);
			alert.setContentText(text);

			Optional<ButtonType> result = alert.showAndWait();
			
			
			return result;
			
		}
		
		public static void imprimir(String tipo) {
			switch(tipo.toLowerCase()) {
			case "relatorio hoje":
				Desktop dk = Desktop.getDesktop();
				try(FileWriter fw = new FileWriter("C:\\CAF\\impressao.txt",false);
						BufferedWriter bf = new BufferedWriter(fw);
						PrintWriter print = new PrintWriter(bf);) {
					
					print.print("");
					
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//File file = new File("C:\\teste.pdf");
				try {
					dk.print(new File("C:\\impressao.txt"));
					//dk.open(file);
					//System.out.println("teste");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		public static int selectCB(String item, ComboBox<Combobox> cb) {
	    	int i = -1;
	    	if(!(item == null || item.isEmpty())) {
		    	for(i=0; i<=cb.getItems().size(); i++) {
					cb.selectionModelProperty().getValue().select(i);
					if(item.toUpperCase().equals(cb.getValue().getNome().toUpperCase())) {
						i = cb.getItems().size();
					}
				}
	    	}
	    	return i;
	    }
}
