package application.controller;

import application.ArquivoTxt;
import application.Main;
import application.dao.RelatorioDao;
import application.model.Combobox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CAF_relatorio_Controller {

    
    private @FXML DatePicker datafim;

    
    private @FXML ComboBox<Combobox> cbrelatorio;

    
    private @FXML Label labelfil;

    
    private @FXML DatePicker dataini;

    
    private RelatorioDao dao = new RelatorioDao();
    private @FXML void initialize() {
    	carregarItems();
    }
    
    @FXML void imprimir(ActionEvent event) {
    	if(cbrelatorio !=null) {
    		if(dataini.getValue() == null || datafim.getValue() == null) {
    			Main.dialogBox("As datas não podem ser nulas!", 1);
    			return;
    		}
	    	switch(cbrelatorio.getValue().getId()) {
	    	case 5:
	    		ArquivoTxt.writeRelNutrinor("dia", dataini.getValue().toString(), datafim.getValue().toString());
	    		break;
	    	case 4:
	    		ArquivoTxt.writeRelNutrinor("periodo", dataini.getValue().toString(), datafim.getValue().toString());
	    	}
    	}
    }
    private @FXML void change() {
    	if(cbrelatorio.getValue().getId() == 1) {
    		
    	} else if(cbrelatorio.getValue().getId() == 2) {
    		
    	} else if(cbrelatorio.getValue().getId() == 3) {
    		
    	} else if(cbrelatorio.getValue().getId() == 4) {
    		
    	} else if(cbrelatorio.getValue().getId() == 5) {
    		
    	}
    	
    }
    
    private void carregarItems() {
    	cbrelatorio.setItems(FXCollections.observableArrayList(dao.listItems()));
    }

}
