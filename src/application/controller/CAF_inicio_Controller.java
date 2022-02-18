package application.controller;

import application.Main;
import application.ModifyScenes;
import application.dao.MainDao;
import application.model.Usuario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CAF_inicio_Controller {
	
    private @FXML MenuBar menu;
    
    private @FXML Menu menum;
    
    
    private @FXML Button btnlogar;
    private @FXML TextField txtuser = new TextField();
    private @FXML TextField txtpw = new TextField();
    
    private @FXML Pane panelogin;
    private Stage stage = Main.Stagemain;
    private MainDao dao = new MainDao();
    
    String msg_a = "Voc� n�o tem permiss�o de acesso.";
    
    @FXML void initialize() {
    	txtuser.setText("andre.cavalcante");;
    	txtpw.setText("truco");
    	
    	menum.setStyle("-fx-border-radius:5");
    }
    
    @FXML void btnlogar(ActionEvent event) {
    	if (
    			txtuser.getText().isEmpty() || 
    			txtpw.getText().isEmpty()
    		) {
    	
    			Main.dialogBox("Digite um Usu�rio e Senha!", 1);

    		} else {
    			
    			//verifica��o na funcaoo do banco de dados
    			if (dao.UserConfirm(
    					txtuser.getText(), 
    					txtpw.getText())
    			) {
    			
    				//Caso login esteja ok, ajusta o frame para modo logado
    				panelogin.setVisible(false);
    				panelogin.setDisable(true);
    				menu.setDisable(false);
    				//Futuro controle de acesso
    				txtuser.setText("");
    				txtpw.setText("");
    			} else {
    				Main.dialogBox("Nome de Usu�rio ou Senha incorreto!", 1);
    			}
    		}
    }
    @FXML void openmenuref(ActionEvent event) {
    	if (Main.user.getNvacesso() <= 2) {

			Main.modify.modify("view/CAF_refeitorio.fxml", "Controle de Acesso - Refeit�rio");
			
			stage.setOnCloseRequest(e -> e.consume());

		} else {
			Main.dialogBox(msg_a, 1);
		}
    }

    
    @FXML void openmenufun(ActionEvent event) {
    	if(Main.user.getNvacesso() <= 1) {
    		Main.modify.modify("view/CAF_funcionarios.fxml", "Controle de Acesso - Cadastro de Funcion�rios");
    		
    		stage.setOnCloseRequest(e -> e.consume());
    	} else {
			Main.dialogBox(msg_a, 1);
		}
    }

    
    @FXML void openmenuadi(ActionEvent event) {
    	if(Main.user.getNvacesso() <= 1) {
    		Main.modify.modify("view/CAF_adicionais.fxml", "Controle de Acesso - Adicionais");
    		
    		stage.setOnCloseRequest(e -> e.consume());
    	} else {
			Main.dialogBox(msg_a, 1);
		}
    }

    
    @FXML void openmenurel(ActionEvent event) {

    }
    
    
    @FXML void Logout(ActionEvent event) {
    	for(int i=0; i<ModifyScenes.listS.size(); i++) {
			Stage s = ModifyScenes.listS.get(i);
			s.close();
			ModifyScenes.listS.remove(i);
		}
		Main.Stagemain.setOnCloseRequest(e -> Platform.exit());
		ModifyScenes.close();
		panelogin.setVisible(true);
		panelogin.setDisable(false);
		menu.setDisable(true);
		Main.user = new Usuario();
    }
}
