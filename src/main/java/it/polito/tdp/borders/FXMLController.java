
package it.polito.tdp.borders;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import it.polito.tdp.borders.model.StatoGrado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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
    private ComboBox<Country> cmbStati;

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


    @FXML
    void handleStatiRaggiungibili(ActionEvent event) 
    {
    	Country partenza = this.cmbStati.getValue();
    	
    	if(partenza == null)
    	{
    		this.txtResult.setText("Seleziona uno stato dal menù a tendina!!!");
    		return;
    	}
    	
    	this.model.creaGrafo(2006);
    	
    	List<Country> albero = this.model.getRaggiungibiliIterativo(partenza);
    	albero.remove(partenza);
    	
    	this.txtResult.setText("Sono raggiungibili " + albero.size() + " da " + partenza.toString() + ":\n");;
    	for(Country c: albero)
    	{
    		this.txtResult.appendText(c.toString() + "\n");
    	}
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbStati != null : "fx:id=\"cmbStati\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    	
    	List<Country> stati = this.model.getAllCountries();
    	
    	this.cmbStati.getItems().setAll(stati);
    }
}
