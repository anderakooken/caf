package application.controller;

import application.Main;
import application.dao.MainDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CAF_login_alterpw_Controller {
	
    private @FXML Label labelUser;

    
    private @FXML PasswordField txtpwatual;

    
    private @FXML PasswordField txtpwconfim;

    
    private @FXML PasswordField txtpwnova;

    private MainDao dao = new MainDao();
    private @FXML void initialize() {
    	labelUser.setText(Main.user.getNmuser());
    }
    
    
    @FXML void alterarSenha(ActionEvent event) {
    	if(txtpwatual.getText()==null || txtpwconfim.getText() == null || txtpwnova.getText() == null) {
    		Main.dialogBox("Preencha todos os campos para alterar a senha!", 1);
    		return;
    	}
    	if(!(txtpwatual.getText().equals(Main.user.getPwrc()))) {
    		Main.dialogBox("Senha atual errada, digite novamente!", 1);
    		return;
    	}
    	if(!(txtpwconfim.getText().equals(txtpwnova.getText()))){
    		Main.dialogBox("As senhas tem que ser iguais, digite novamente!", 1);
    		return;
    	}
    	
    	if(Main.ConfirmationDialog("Alteração de senha", "Você tem certeza disso?").get() == ButtonType.OK) {
    		dao.alterPw(Main.user.getMatricula(), txtpwnova.getText());
    		
    		Main.reiniciar();
    	}
    }
}
