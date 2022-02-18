package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.ArquivoTxt;
import application.dao.RelatorioDao;
import application.model.Combobox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CAF_relatorio_Controller {
	 
    private @FXML GridPane gridduplo;

    
    private @FXML DatePicker datafim;

    
    private @FXML ComboBox<Combobox> cbrelatorio;

    
    private @FXML Label labelfil;

    
    private @FXML DatePicker dataini;

    
    private RelatorioDao dao = new RelatorioDao();
    private ArquivoTxt txt = new ArquivoTxt();
    private @FXML void initialize() {
    	gridduplo.setVisible(false);
    	carregarItems();
    }
    
    @FXML void imprimir(ActionEvent event) {
    	switch(cbrelatorio.getValue().getId()) {
    	case 5:
    		//txt.writeRelNutrinor("dia", , null);
    	}
    }
    private @FXML void change() {
    	if(cbrelatorio.getValue().getId() == 1) {
    		
    	} else if(cbrelatorio.getValue().getId() == 2) {
    		
    	} else if(cbrelatorio.getValue().getId() == 3) {
    		
    	} else if(cbrelatorio.getValue().getId() == 4) {
    		gridduplo.setVisible(true);
    	} else if(cbrelatorio.getValue().getId() == 5) {
    		
    	}
    	
    }
    
    private void carregarItems() {
    	cbrelatorio.setItems(FXCollections.observableArrayList(dao.listItems()));
    }

}
