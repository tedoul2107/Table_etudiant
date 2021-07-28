/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operationtable;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ULRICH TEDONGMO
 */
public class OperationTable extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{
            Parent parent = FXMLLoader.load(getClass().getResource("/tableView/FXMLMain.fxml"));
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            //primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.show();
        }catch(Exception ex){
            Logger.getLogger(OperationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
