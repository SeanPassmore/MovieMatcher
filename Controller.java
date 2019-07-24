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
     @FXML private ImageView rtImage, imdbImage;
     @FXML private TextField tagSearch, genreSearch, actorSearch, dirSearch, movieSearch, userSearch;
     @FXML private TableView<ObservableList<String>> table;
     @FXML private TabPane tabPane;
     @FXML private Slider slider;
     @FXML private Label sliderLabel, RTimgLabel, IMDBimgLabel, movieTitle, rtScore, imdbScore, yearLabel;
     @FXML private TableColumn<ObservableList<String>, String> column;

    private static Connection setConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", PickerRunner.pass);
    }
    @FXML
    private void updateTable(ResultSet rs)throws SQLException {
        table.getColumns().clear();
        table.getItems().clear();
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            column = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
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
     protected void test(MouseEvent event) throws SQLException, ClassNotFoundException {
         if(event.getClickCount() > 1 && !(table.getColumns().get(0).getText().equals("Num_Movies"))) {
             ObservableList<String> row = table.getSelectionModel().getSelectedItem();
             movieTab(row.get(0).toString());
             tabPane.getSelectionModel().select(1);
        }
     }
     protected void movieTab(String id)throws SQLException, ClassNotFoundException{
        IMDBimgLabel.setVisible(false);
        RTimgLabel.setVisible(false);

        Connection con = setConnection();
        String sql = "SELECT title, year, rtAudienceScore, rtAllCriticsScore, GROUP_CONCAT(movie_tag_list.tag), rtPictureURL, imdbPictureURL " +
                "FROM movie_matcher.movie " +
                "INNER JOIN movie_tag " +
                "ON movie.id = movie_tag.movieId " +
                "INNER JOIN movie_tag_list " +
                "ON movie_tag.tagId = movie_tag_list.id " +
                "WHERE movie.id LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        movieTitle.setText(rs.getString(1));
        yearLabel.setText(rs.getString(2));
        imdbScore.setText(rs.getString(3));
        rtScore.setText(rs.getString(4));
        tags.setText(rs.getString(5));
        try{
            String image = rs.getString(7);
            image = image.substring(0, 4) + "s" + image.substring(4);
            rtImage.setImage(new Image(rs.getString(6)));
            imdbImage.setImage(new Image(image));
        }catch(NullPointerException npe){
            IMDBimgLabel.setVisible(true);
        }
        try{
            String image = rs.getString(6);
            image = image.substring(0, 4) + "s" + image.substring(4);
            rtImage.setImage(new Image(image));
        }catch(NullPointerException npe){

            RTimgLabel.setVisible(true);
        }
        

     }

    @FXML protected void topActors(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT count(movieId)as Num_Movies, actorName as Name, rtAudienceScore " +
        "from movie_matcher.movie_actor " + 
        "INNER JOIN movie " +
        "ON movie.id = movie_actor.movieID " +
        "group by actorID " +
        "having count(movie_actor.movieId) >= ? " + 
        "ORDER BY rtAudienceScore DESC, rtAudienceNumRatings DESC, count(movieID) DESC " +
        "LIMIT 10;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, String.valueOf(slider.getValue()));
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();

    }
    @FXML protected void topDir(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT count(movieId)as Num_Movies, directorName as Name, rtAudienceScore " +
        "from movie_matcher.movie_director " + 
        "INNER JOIN movie " +
        "ON movie.id = movie_director.movieID " +
        "group by directorID " +
        "having count(movie_director.movieId) >= ? " + 
        "ORDER BY rtAudienceScore DESC, rtAudienceNumRatings DESC, count(movieID) DESC " +
        "LIMIT 10;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, String.valueOf(slider.getValue()));
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();

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
    @FXML protected void worstActors(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT count(movieId)as Num_Movies, actorName as Name, rtAudienceScore " +
        "from movie_matcher.movie_actor " + 
        "INNER JOIN movie " +
        "ON movie.id = movie_actor.movieID " +
        "group by actorID " +
        "having count(movie_actor.movieId) >= ? " + 
        "ORDER BY rtAudienceScore ASC, rtAudienceNumRatings ASC, count(movieID) DESC " +
        "LIMIT 10;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, String.valueOf(slider.getValue()));
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();

    }
    @FXML protected void worstDir(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT count(movieId)as Num_Movies, directorName as Name, rtAudienceScore " +
        "from movie_matcher.movie_director " + 
        "INNER JOIN movie " +
        "ON movie.id = movie_director.movieID " +
        "group by directorID " +
        "having count(movie_director.movieId) >= ? " + 
        "ORDER BY rtAudienceScore ASC, rtAudienceNumRatings ASC, count(movieID) DESC " +
        "LIMIT 10;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, String.valueOf(slider.getValue()));
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();

    }
    @FXML protected void  worstMovies(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        Statement selectAct = con.createStatement();
        String sql = "SELECT id, title, year, rtAllCriticsNumReviews, rtAllCriticsScore FROM movie_matcher.movie" +
                " group by title" +
                " order by rtAllCriticsScore ASC, rtAllCriticsNumReviews ASC" +
                " LIMIT " + parseTop((int) slider.getValue());
        ResultSet rs = selectAct.executeQuery(sql);
        updateTable(rs);
        selectAct.close();
    }
    @FXML protected void  genreSearchQuery(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT id, title,year, rtAllCriticsScore,rtAudienceScore " +
        "FROM movie_matcher.movie " +
        "INNER JOIN movie_matcher.movie_genre " +
        "ON movie.id = movie_genre.movieID " +
        "WHERE movie_genre.genre = ?"+
        " GROUP BY movie.title " +
        "ORDER BY rtAllCriticsScore DESC, rtAllCriticsNumReviews DESC " +
        "LIMIT ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, genreSearch.getText());
        ps.setInt(2, parseTop((int) slider.getValue()));
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();
    }
    @FXML protected void  actorQuery(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT actorName, movie.title,year,rtAudienceScore " +
        "FROM movie_actor " +
        "INNER JOIN movie " +
        "ON movie.id = movie_actor.movieId "+
        "WHERE actorName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +actorSearch.getText()+ "%");
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();
    }
    @FXML protected void  dirQuery(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT directorName, movie.title,year,rtAudienceScore " +
                "FROM movie_director " +
                "INNER JOIN movie " +
                "ON movie.id = movie_director.movieId " +
                "WHERE directorName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +dirSearch.getText() +"%");
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();
    }
    @FXML protected void  movieQuery(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT movie.title,year,rtAudienceScore, GROUP_CONCAT(movie_tag_list.tag) AS Tags " +
                "FROM movie " +
                "INNER JOIN movie_tag " +
                "ON movie.id = movie_tag.movieId " +
                "INNER JOIN movie_tag_list " +
                "ON movie_tag.tagId = movie_tag_list.id " +
                "WHERE movie.title LIKE ? " +
                "GROUP BY movie.title " +
                "order by rtAllCriticsScore";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +movieSearch.getText()+"%");
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();
    }
    @FXML protected void  movieSimQuery(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT movieId,title, GROUP_CONCAT(movie_tag_list.tag ORDER BY tag ASC separator ', ') as Tags " +
       "FROM MOVIE_TAG " +
       "INNER JOIN Movie " +
       "ON movie.id = movie_tag.movieId " +
       "INNER JOIN movie_tag_list " +
       "ON movie_tag_list.id = tagId " +
       "WHERE movie_tag.tagId in ( SELECT tagId from MOVIE_TAG " +
       "INNER JOIN Movie " +
       "ON movie.id = movie_tag.movieId " +
       "WHERE movie.title = ?) " +
       "GROUP BY movieId " +
       "having count(distinct MOVIE_TAG.tagId) >= (SELECT COUNT(tagId) from MOVIE_TAG " +
       "       INNER JOIN Movie " +
       "ON movie.id = movie_tag.movieId  " +
        "WHERE movie.title = ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, movieSearch.getText());
        ps.setString(2, movieSearch.getText());
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();
    }

    @FXML protected void  tagQuery(ActionEvent event) throws SQLException, ClassNotFoundException{
        Connection con = setConnection();
        String sql = "SELECT movie_tag_list.tag, movie.title,year,rtAudienceScore " +
                "FROM movie " +
                "INNER JOIN movie_tag " +
                "ON movie.id = movie_tag.movieId " +
                "INNER JOIN movie_tag_list " +
                "ON movie_tag.tagId = movie_tag_list.id " +
                "WHERE tag LIKE ? " +
                "order by rtAllCriticsScore";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,"%" + tagSearch.getText() +"%");
        ResultSet rs = ps.executeQuery();
        updateTable(rs);
        ps.close();
    }
    @FXML protected void sliderNum(MouseEvent event){
        sliderLabel.setText(String.valueOf((int)slider.getValue()));
    }

    private int parseTop(int value) {
        return value;
    }
}
