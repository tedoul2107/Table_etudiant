/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Student;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author ULRICH TEDONGMO
 */
public class TableViewController implements Initializable {

    @FXML
    private TableView<Student> StudentTable;

    @FXML
    private TableColumn<Student, String> idCol;

    @FXML
    private TableColumn<Student, String> nameCol;

    @FXML
    private TableColumn<Student, String> birthCol;

    @FXML
    private TableColumn<Student, String> addressCol;

    @FXML
    private TableColumn<Student, String> emailCol;

    @FXML
    private TableColumn<Student, String> editCol;

    String query = null;
    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet resultSet = null;
    Student student = null;

    ObservableList<Student> StudentList = FXCollections.observableArrayList();
    @FXML
    private TextField txt_search;

    @FXML
    void Close(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    void Print(ActionEvent event) {
        connection = DbConnect.getConnect();
        try {
            JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\ULRICH TEDONGMO\\Documents\\NetBeansProjects\\operationTable\\src\\print\\report.jrxml");
            query = "select * from student order by name asc";
       
            JRDesignQuery updateQuery = new JRDesignQuery();
            updateQuery.setText(query);
        
            jdesign.setQuery(updateQuery);
            
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, null , connection);
            
            JasperViewer.viewReport(jprint);
            
                    
        } catch (JRException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void getAddView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tableView/addStudent.fxml"));
            Parent parent = loader.load();
            AddStudentController AddStudentController = loader.getController();
            AddStudentController.setTableviewcontroller(this);

            // Parent parent = FXMLLoader.load(getClass().getResource("/tableView/addStudent.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            //stage.initStyle(StageStyle.UTILITY);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void refreshTable() {

        try {
            StudentList.clear();

            query = "select * from student";
            pst = connection.prepareStatement(query);
            resultSet = pst.executeQuery();

            while (resultSet.next()) {
                StudentList.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDate("birth"),
                        resultSet.getString("address"),
                        resultSet.getString("email")));

                StudentTable.setItems(StudentList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
        SearchName();
    }

    private void loadDate() {

        connection = DbConnect.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableViewController tableviewcontroller = this;

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFoctory = (TableColumn<Student, String> param) -> {
            // make cell containing buttons
            final TableCell<Student, String> cell = new TableCell<Student, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                "  -fx-cursor : hand ; "
                                + " -glyph-size :28px ; "
                                + " -fx-fill :#ff1744 ; "
                        );
                        editIcon.setStyle(
                                "  -fx-cursor : hand ; "
                                + " -glyph-size :28px ; "
                                + " -fx-fill :#00E676 ; "
                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            try {
                                student = StudentTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM student WHERE id  =" + student.getId();
                                connection = DbConnect.getConnect();
                                pst = connection.prepareStatement(query);
                                pst.execute();
                                refreshTable();

                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            student = StudentTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/tableView/addStudent.fxml"));

                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            AddStudentController addStudentController = loader.getController();
                            addStudentController.setTableviewcontroller(tableviewcontroller);

                            addStudentController.setUpdate(true);
                            addStudentController.setTextField(student.getId(), student.getName(),
                                    student.getBirth().toLocalDate(), student.getAddress(), student.getEmail());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            //stage.initStyle(StageStyle.UTILITY);

                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle(" -fx-alignment :center ");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        editCol.setCellFactory(cellFoctory);
        StudentTable.setItems(StudentList);

    }

    private void SearchName() {

        txt_search.setOnKeyReleased(e -> {

            if (txt_search.getText().isEmpty()) {

                loadDate();

            } else {
                try {
                    StudentList.clear();

                    query = "Select * from student where name LIKE '%" + txt_search.getText() + "%'";
                    pst = connection.prepareStatement(query);
                    resultSet = pst.executeQuery();

                    while (resultSet.next()) {
                        StudentList.add(new Student(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getDate("birth"),
                                resultSet.getString("address"),
                                resultSet.getString("email")));

                        StudentTable.setItems(StudentList);

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
