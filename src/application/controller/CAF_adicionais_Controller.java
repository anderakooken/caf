package application.controller;

import java.util.List;

import application.Main;
import application.dao.AdicionaisDao;
import application.model.Funcionario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class CAF_adicionais_Controller {
	
    

    
    private @FXML TextField txtfuncao;
    private @FXML TextField txtquant;
    private @FXML TextField txtnome;
    private @FXML TextField txtsetor;
    private @FXML TextField txtmatricula;

    
    private @FXML TableView<Funcionario> table;
    private @FXML TableColumn<Funcionario, String> cfuncao;
    private @FXML TableColumn<Funcionario, String> csetor;
    private @FXML TableColumn<Funcionario, String> cnome;
    private @FXML TableColumn<Funcionario, String> cquant;
    private @FXML TableColumn<Funcionario, String> cmatricula;
    
    

    AdicionaisDao dao = new AdicionaisDao();
    Funcionario f = new Funcionario();
    private @FXML void initialize(){
    	initTable();
    	setClick();
    	
    }
    
    @FXML void adicionar(ActionEvent event) {
    	if(txtmatricula.getText()==null || txtmatricula.getText().isEmpty() ||
        		txtquant.getText()==null || txtquant.getText().isEmpty()) {
    		Main.dialogBox("Preencha todos os campos!", 1);
    		return;
    	}
    	if(!(txtquant.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtquant.getText().contains(".") || txtquant.getText().contains(",")) {
    		Main.dialogBox("Digite uma quantidade válida!", 1);
    		return;
    	}
    	if(!(txtmatricula.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtmatricula.getText().contains(".") || txtmatricula.getText().contains(",")) {
    		Main.dialogBox("Digite uma matricula válida!", 1);
    		return;
    	}
    	
    	setFun();
		if(Main.ConfirmationDialog("Cadastrar", "Você tem certeza disso?").get() == ButtonType.OK) {
			List<Funcionario> flist = dao.listAdcionais("WHERE emp_matricula=" + txtmatricula.getText());
			if(flist.size()>0) {
				Main.dialogBox("Não se pode cadastrar a mesma matricula duas vezes!", 1);
				txtnome.setText(flist.get(0).get("nome"));
				txtsetor.setText(flist.get(0).get("setor"));
				txtfuncao.setText(flist.get(0).get("funcao"));
				txtquant.setText(flist.get(0).get("quantidade"));
				return;
			}
			dao.CE(f, 1);
			initTable();
			limpar();
		}
    }
    		
    private @FXML void editar(ActionEvent event) {
    	if(txtmatricula.getText()==null || txtmatricula.getText().isEmpty() ||
        		txtquant.getText()==null || txtquant.getText().isEmpty()) {
        		Main.dialogBox("Preencha todos os campos!", 1);
        		return;
        	}
    	if(!(txtquant.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtquant.getText().contains(".") || txtquant.getText().contains(",")) {
    		Main.dialogBox("Digite uma quantidade válida!", 1);
    		return;
    	}
    	if(!(txtmatricula.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtmatricula.getText().contains(".") || txtmatricula.getText().contains(",")) {
    		Main.dialogBox("Digite uma matricula válida!", 1);
    		return;
    	}
        	setFun();
    		if(Main.ConfirmationDialog("Editar", "Você tem certeza disso?").get() == ButtonType.OK) {
    			dao.CE(f, 2);
    			limpar();
    			initTable();
    		}
        	
    }
    
    private void initTable() {
    	cmatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
    	csetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
    	cquant.setCellValueFactory(new PropertyValueFactory<>("quant"));
    	cnome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	cfuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
    	
    	table.setItems(FXCollections.observableArrayList(dao.listAdcionais(null)));
    }
    
    private @FXML void TableClick() {
    	int i;
    	i= table.getSelectionModel().getSelectedIndex();
    	if(i>=0) {
    		Funcionario fun = table.getItems().get(i);
    		
    		txtnome.setText(fun.get("nome"));
    		txtmatricula.setText(fun.get("matricula"));
    		txtfuncao.setText(fun.get("funcao"));
    		txtsetor.setText(fun.get("setor"));
    		txtquant.setText(fun.get("quantidade"));
    	}
    }
    private void limpar() {
    	txtnome.setText(null);
    	txtmatricula.setText(null);
    	txtsetor.setText(null);
    	txtfuncao.setText(null);
    	txtquant.setText(null);
    }
    private void setClick() {
    	txtmatricula.setOnKeyPressed(e->{
    		if(e.getCode() == KeyCode.ENTER) {
    			adicionar(null);
    		}
    	});
    	txtquant.setOnKeyPressed(e->{
    		if(e.getCode() == KeyCode.ENTER) {
    			adicionar(null);
    		}
    	});
    }
    
    private void setFun() {
    	f.set("matricula", txtmatricula.getText());
    	f.set("quantidade", txtquant.getText());
    }
}
