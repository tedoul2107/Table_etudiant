/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * FXML Controller class
 *
 * @author ULRICH TEDONGMO
 */
public class FXMLMainController implements Initializable {

    @FXML
    private ListView<String> listForm;
    private ObservableList<String> subListForm;
    @FXML
    private TabPane main_tab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        generateSubList();
        selectedListForm();
    }   
    
    private void generateSubList(){
        subListForm = FXCollections.observableArrayList();
        subListForm.add("Student");
        subListForm.add("Add Student");
        listForm.setItems(subListForm);
    }
    
    private void selectedListForm(){
        listForm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            
                int i = listForm.getSelectionModel().getSelectedIndex();
                
                if(i==0){
                    try {
                        Node studentForm = FXMLLoader.load(getClass().getResource("tableView.fxml"));
                        
                        Tab tab = new Tab("Student", studentForm);
                        main_tab.getTabs().add(tab);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if(i==1){
                    try {
                        Node addForm = FXMLLoader.load(getClass().getResource("addStudent.fxml"));
                        
                        Tab tab = new Tab("Add Student", addForm);
                        main_tab.getTabs().add(tab);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
    }
    
}
