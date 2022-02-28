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
	    		ArquivoTxt.writeRel("dia", dataini.getValue().toString(), datafim.getValue().toString());
	    		break;
	    	case 4:
	    		ArquivoTxt.writeRel("periodo", dataini.getValue().toString(), datafim.getValue().toString());
	    		break;
	    	case 3:
	    		ArquivoTxt.writeRel("movdiario", dataini.getValue().toString(), datafim.getValue().toString());
	    		break;
	    	case 2:
	    		Main.dialogBox("Sem conexão com fluxos!", 1);
	    		break;
	    	case 1:
	    		ArquivoTxt.writeRel("faturamento mensal", dataini.getValue().toString(), datafim.getValue().toString());
	    		break;
	    	}
	    	
	    		
    	}
    }
    
    private void carregarItems() {
    	cbrelatorio.setItems(FXCollections.observableArrayList(dao.listItems()));
    }

}
