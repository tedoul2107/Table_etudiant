/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableView;

import helpers.DbConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Student;

/**
 * FXML Controller class
 *
 * @author ULRICH TEDONGMO
 */
public class AddStudentController implements Initializable {

    @FXML
    private TextField nameFid;
    @FXML
    private DatePicker birthFid;
    @FXML
    private TextField addressFid;
    @FXML
    private TextField emailFid;

    String query = null;
    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet resultSet = null;
    Student student = null;

    private boolean update = false;
    int studentID;

    TableViewController tableviewcontroller;

    public void setTableviewcontroller(TableViewController tableviewcontroller) {
        this.tableviewcontroller = tableviewcontroller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void save(ActionEvent event) {

        connection = DbConnect.getConnect();
        String name = nameFid.getText();
        String birth = String.valueOf(birthFid.getValue());
        String address = addressFid.getText();
        String email = emailFid.getText();

        if (name.isEmpty() || birth.isEmpty() || address.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data");
            alert.showAndWait();
        } else {
            getQuery();
            insert();
            tableviewcontroller.refreshTable();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }

    }

    @FXML
    private void clean(ActionEvent event) {
        nameFid.setText(null);
        birthFid.setValue(null);
        addressFid.setText(null);
        emailFid.setText(null);
    }

    private void getQuery() {

        if (update == false) {
            query = "insert into student (name, birth, address, email) values (?,?,?,?)";
        } else {
            query = "UPDATE student SET "
                    + "name=?,"
                    + "birth=?,"
                    + "address=?,"
                    + "email=? WHERE id ='" + studentID + "'";
        }
    }

    private void insert() {
        try {

            pst = connection.prepareStatement(query);
            pst.setString(1, nameFid.getText());
            pst.setString(2, String.valueOf(birthFid.getValue()));
            pst.setString(3, addressFid.getText());
            pst.setString(4, emailFid.getText());
            pst.execute();

        } catch (Exception e) {
        }
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

    void setTextField(int id, String name, LocalDate toLocalDate, String address, String email) {
        studentID = id;
        nameFid.setText(name);
        birthFid.setValue(toLocalDate);
        addressFid.setText(address);
        emailFid.setText(email);
    }

}
