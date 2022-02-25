package application.controller;

import application.dao.EventoDao;
import application.dao.MainDao;
import application.model.Combobox;
import application.model.Evento;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CAF_login_refperiodo_Controller {
	
    private @FXML ComboBox<Combobox> cbeventos;

    private @FXML TextField qtdmin;
    private @FXML TextField quentinha; 

    
    private @FXML DatePicker dtini;
    private @FXML DatePicker dtfim;

    
    private @FXML TableView<Evento> table; 
    private @FXML TableColumn<Evento, String> cid;
    private @FXML TableColumn<Evento, String> cdata;
    private @FXML TableColumn<Evento, String> cevento;  
    private @FXML TableColumn<Evento, String> cquen;  
    private @FXML TableColumn<Evento, String> cqtdmin;
    private EventoDao dao = new EventoDao();
    @FXML void initialize() {
    	initTable(null);
    	CarregarItems();
    }
    
    @FXML void pesquisar(ActionEvent event) {
    	if(cbeventos.getValue() == null && dtini.getValue() == null && dtfim.getValue() == null && qtdmin.getText() == null 
    		&& quentinha.getText()==null && qtdmin.getText().isEmpty() && quentinha.getText().isEmpty()) {
    		initTable(null);
    		return;
    	}
    	String where = "";
    	int i = 0;
    	if(cbeventos.getValue() != null) {
    		if(i==0) {
    			where += ". ";
    			i++;
    		}else {
    			where += ", ";
    		}
    		where += " nmtipo='"+cbeventos.getValue().getNome()+"' ";
    	}
		if(dtini.getValue() !=null && dtfim.getValue() !=null) {
			if(i==0) {
    			where += ". ";
    			i++;
    		}else {
    			where += ", ";
    		}
			where += " (dtlnc BETWEEN '"+dtini.getValue().toString()+"' AND '"+dtfim.getValue().toString()+"') ";
			
		}
		if(qtdmin != null && !(qtdmin.getText().isEmpty())) {
			if(i==0) {
    			where += ". ";
    			i++;
    		}else {
    			where += ", ";
    		}
			where += " qtd = '"+qtdmin.getText()+"' ";
		}
		if(quentinha != null && !(quentinha.getText().isEmpty())){
			if(i==0) {
    			where += ". ";
    			i++;
    		}else {
    			where += ", ";
    		}
			where +=" qtdq = '"+quentinha.getText()+"' ";
		}
		initTable(where.replace(".", "WHERE").replaceAll(",", "AND"));	
    }
    
    private void CarregarItems() {
    	cbeventos.setItems(FXCollections.observableArrayList(MainDao.ListItems("idtipo", "nmtipo", "tb_tipos_refeicoes")));
    }
    
    private void initTable(String where) {
    	cid.setCellValueFactory(new PropertyValueFactory<>("id"));
    	cdata.setCellValueFactory(new PropertyValueFactory<>("data"));
    	cqtdmin.setCellValueFactory(new PropertyValueFactory<>("qtdmin"));
    	cquen.setCellValueFactory(new PropertyValueFactory<>("qq"));
    	cevento.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	
    	table.setItems(FXCollections.observableArrayList(dao.listHis(where)));
    }
    
    private @FXML void limpar() {
    	cbeventos.setValue(null);
    	quentinha.setText("");
    	qtdmin.setText("");
    	dtini.setValue(null);
    	dtfim.setValue(null);
    	
    }
    
}
