package Movie_Matcher;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.*;
import java.util.*;


public class Controller {
     @FXML private TextArea tags;
     @FXML private ImageView rtImage;
     @FXML private TextField tagSearch, topNumber;
     @FXML private TableView table;
     @FXML private TableRow tableRow;
     @FXML private TabPane tabPane;
     @FXML private Slider slider;
     @FXML private Label sliderLabel, RTimgLabel, IMDBimgLabel;
     @FXML private TableColumn column;

    public static Connection setConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", Main.pass);
        return con;
    }
    @FXML
    private void updateTable(ResultSet rs)throws SQLException {
        table.getColumns().clear();
        table.getItems().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            column = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            column.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                    new SimpleStringProperty(param.getValue().get(j).toString()));
            table.getColumns().add(column);
        }

        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                row.add(rs.getString(i));
            }
            data.add(row);

        }
        table.setItems(data);
    }
     @FXML
     protected void test(MouseEvent event){
         if(event.getClickCount() > 1) {
             tabPane.getSelectionModel().select(1);
             rtImage.setImage(new Image("https://ia.media-imdb.com/images/M/MV5BMTY5NTU3NDgxNV5BMl5BanBnXkFtZTcwMTI4NzEzMQ@@._V1._SY314_CR2,0,214,314_.jpg"));
             IMDBimgLabel.setVisible(false);
         }
         tags.setText("hey fag");
         tagSearch.setText("oi");
     }

    @FXML protected void topActors(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        Statement selectAct = con.createStatement();
        String sql = "SELECT * FROM movie_matcher.movie_actor WHERE ranking < 10 group by movieId order by ranking ASC" +
                " LIMIT " + parseTop((int) slider.getValue());
        ResultSet rs = selectAct.executeQuery(sql);
        updateTable(rs);
        selectAct.close();

    }
    @FXML protected void  topMovies(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        Statement selectAct = con.createStatement();
        String sql = "SELECT id, title, year, rtAllCriticsNumReviews, rtAllCriticsScore FROM movie_matcher.movie" +
                " group by title" +
                " order by rtAllCriticsScore DESC, rtAllCriticsNumReviews DESC" +
                " LIMIT " + parseTop((int) slider.getValue());
        ResultSet rs = selectAct.executeQuery(sql);
        updateTable(rs);
        selectAct.close();
    }

    @FXML protected void sliderNum(MouseEvent event){
        sliderLabel.setText(String.valueOf((int)slider.getValue()));
        parseTop((int)slider.getValue());
    }

    private int parseTop(int value) {
        return value;
    }
}
