package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.ArquivoTxt;
import application.Main;
import application.model.Combobox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CAF_refeitorio_relatorio_Controller {
	  
    private @FXML Label lbfiltro;
    
    private @FXML ComboBox<Combobox> cbrelatorio;

    
    private @FXML DatePicker dpfim;

    
    private @FXML DatePicker dpini;

    //private ArquivoExcel excel = new ArquivoExcel();
    
    
    @FXML void initialize() {
    	CarregarItems();
    }
    
    void CarregarItems() {
    	List<Combobox> l = new ArrayList<>();
    	ObservableList<Combobox> obs;
    	Combobox c = new Combobox(1, "Extrato Refeições Período");
    	l.add(c);
    	c = new Combobox(2, "Extrato Refeições Dia");
    	l.add(c);
    	obs = FXCollections.observableArrayList(l);
    	cbrelatorio.setItems(obs);
    }
    
    @FXML void imprimir(ActionEvent event) {
    	if(cbrelatorio.getValue().getId() == 1) {
    		if(dpini.getValue() == null || dpfim.getValue() == null) {
    			Main.dialogBox("As datas não podem ser vazias", 1);
    			return;
    		}
    		ArquivoTxt.writeRel("periodo", dpini.getValue().toString(), dpfim.getValue().toString());
    	}
    	else if(cbrelatorio.getValue().getId() == 2) {
    		if(dpfim.getValue() == null) {
    			dpfim.setValue(dpini.getValue());
    		}
    		if(dpini.getValue() == null) {
    			Main.dialogBox("A data não pode ser vazia!", 1);
    			return;
    		}
    		ArquivoTxt.writeRel("dia", dpini.getValue().toString(), dpfim.getValue().toString());
    		//excel.CriareImprimirRelatorioNutrionor("Relat�rio do Dia "+dateuni.getValue(), null, null, null, null);
    	}
    }
}
