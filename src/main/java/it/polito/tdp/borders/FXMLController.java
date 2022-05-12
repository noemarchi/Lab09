
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Model;
import it.polito.tdp.borders.model.StatoGrado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader
    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) 
    {
    	int anno;
    	try
    	{
    		anno = Integer.parseInt(this.txtAnno.getText());
    		
    		if(anno < 1816 || anno > 2006)
    		{
    			this.txtResult.setText("L'anno deve essere compreso tra 1816 e 2006!!!");
        		this.txtAnno.clear();
        		return;
    		}
    	}
    	catch (NumberFormatException e)
    	{
    		this.txtResult.setText("Inserisci l'anno in formato numerico!!!");
    		this.txtAnno.clear();
    		return;
    	}
    	
    	this.model.creaGrafo(anno); 
    	this.txtResult.setText("Grafo creato con " + model.nVertici() + " vertici e " + model.nArchi() + " archi.\n");
    	this.txtResult.appendText("Elenco degli stati e relativo numero di stati confinanti:\n");
    	
    	for(StatoGrado s: model.getStatiGrado())
    	{
    		this.txtResult.appendText(s.toString() + "\n");
    	}
    	this.txtResult.appendText("\nNumero di componenti connesse nel grafo: " + model.getComponentiConnesse());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    }
}
