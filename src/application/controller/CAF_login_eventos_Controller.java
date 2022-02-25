package application.controller;

import application.Main;
import application.dao.EventoDao;
import application.model.Evento;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CAF_login_eventos_Controller {
	
    private @FXML TextField txtid;  
    private @FXML TextField txtfim;  
    private @FXML TextField txtvrunit;  
    private @FXML TextField txtinicio;  
    private @FXML TextField txtevento;

    
    private @FXML TableView<Evento> table;
    private @FXML TableColumn<Evento, String> cid;
	private @FXML TableColumn<Evento, String> cvrunit;
	private @FXML TableColumn<Evento, String> chrini;
    private @FXML TableColumn<Evento, String> chrfim;
    private @FXML TableColumn<Evento, String> cevento;

    
    private EventoDao dao = new EventoDao();
    private Evento evento = new Evento();
    private @FXML void initialize() {
    	initTable();
    }
    
    private @FXML void Adicionar(ActionEvent event) {
    	if(txtfim.getText() == null || txtfim.getText().isEmpty() || txtvrunit.getText() == null || 
    		txtvrunit.getText().isEmpty() || txtinicio.getText() == null || txtinicio.getText().isEmpty() ||
    		txtevento.getText() == null || txtevento.getText().isEmpty()) {
    		Main.dialogBox("Preencha todos os campos para cadastrar um evento!", 1);
    		return;
    	}
    	else if(txtid.getText() == null || txtid.getText().isEmpty()) {
    		setEvent();
    		if(Main.ConfirmationDialog("Cadastrando um Evento!", "Você tem certeza disso?").get() == ButtonType.OK) {
    			dao.CED(1, evento);
    			limpar();
        		initTable();
    		}
    	}else {
    		setEvent();
    		evento.set("id", txtid.getText());
    		if(Main.ConfirmationDialog("Atualizando um Evento!", "Você tem certeza disso?").get() == ButtonType.OK) {
    			dao.CED(2, evento);
    			limpar();
        		initTable();
    		}
    	}
    }
    
    private void initTable() {
    	cid.setCellValueFactory(new PropertyValueFactory<>("id"));
    	cvrunit.setCellValueFactory(new PropertyValueFactory<>("vrunit"));
    	chrini.setCellValueFactory(new PropertyValueFactory<>("hrini"));
    	chrfim.setCellValueFactory(new PropertyValueFactory<>("hrfim"));
    	cevento.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	
    	table.setItems(FXCollections.observableArrayList(dao.listEvents()));
    }
    
    private @FXML void Deletar(ActionEvent event) {
    	if(txtid.getText() == null || txtid.getText().isEmpty()) {
    		Main.dialogBox("Preencha todos os campos", 1);
    		return;
    	}
    	evento.set("id", txtid.getText());
    	if(Main.ConfirmationDialog("Excluindo um Evento!", "Você tem certeza disso?").get() == ButtonType.OK) {
    		dao.CED(3, evento);
    		limpar();
    		initTable();
    	
    		
    	}
    }
    
    private @FXML void tableClick() {
    	int i;
    	i= table.getSelectionModel().getSelectedIndex();
    	if(i>=0) {
    		Evento e = table.getItems().get(i);
    		
    		txtevento.setText(e.get("nome"));
    		txtid.setText(e.get("id"));
    		txtinicio.setText(e.get("horainicial"));
    		txtfim.setText(e.get("horafim"));
    		txtvrunit.setText(e.get("vrunit"));
    	}
    }
    
    private void setEvent() {
    	evento.set("nome", txtevento.getText());
    	evento.set("horainicial", txtinicio.getText());
    	evento.set("horafim", txtfim.getText());
    	evento.set("vrunit", txtvrunit.getText());
    	evento.set("idusuario", Main.user.getMatricula());
    }
    
    private void limpar() {
    	txtid.setText(null);
    	txtinicio.setText(null);
    	txtfim.setText(null);
    	txtvrunit.setText(null);
    	txtevento.setText(null);
    }
}
