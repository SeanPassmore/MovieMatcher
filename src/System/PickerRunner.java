package System;
import java.io.*;
import java.sql.*;
import java.util.TimeZone;

public class PickerRunner {
    public static void main(String args[]) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        populateMovies();
        populateMovieDirectors();
        populateMovieTags();
        populateTags();
    }
    public static void populateMovieTags() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("Rotten Tomatos Dataset/movie_tags.dat"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
        Statement makeTable = con.createStatement();
        String createMovieTagsTable = "CREATE TABLE MOVIE_TAG" +
                "(movieId int,"+
                "tagId int,"+
                "tagWeight int,"+
                "foreign key  (movieId) references Movie(id))";
        makeTable.executeUpdate(createMovieTagsTable);
        makeTable.close();
        PreparedStatement insertMovieTags = con.prepareStatement("INSERT INTO MOVIE_TAG (movieId, tagId, tagWeight)"+
                "VALUES (?, ?, ?)");
        br.readLine();
        String line;
        int tagId;
        int tagWeight;
        int movieId;
        while ((line = br.readLine()) != null)
        {
            String[] tokens = line.split("\t");
            movieId = Integer.parseInt(tokens[0]);
            tagId = Integer.parseInt(tokens[1]);
            tagWeight = Integer.parseInt(tokens[2]);
            insertMovieTags.setInt(1,movieId);
            insertMovieTags.setInt(2,tagId);
            insertMovieTags.setInt(3,tagWeight);
            insertMovieTags.executeUpdate();
        }
        insertMovieTags.close();
    }
    public static void populateMovieDirectors() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("Rotten Tomatos Dataset/movie_directors.dat"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
        Statement makeTable = con.createStatement();
        String createMovieDirectorsTable = "CREATE TABLE MOVIE_DIRECTOR" +
                "(movieId int not NULL,"+
                "directorId varchar(50)not NULL,"+
                "directorName varchar(50),"+
                "primary key (movieId)," +
                "foreign key  (movieId) references Movie(id))";
        makeTable.executeUpdate(createMovieDirectorsTable);
        makeTable.close();
        PreparedStatement insertMovieDirector = con.prepareStatement("INSERT INTO MOVIE_DIRECTOR (movieId, directorId, DirectorName)"+
                "VALUES (?, ?, ?)");
        br.readLine();
        String line;
        String directorName;
        String directorId;
        int movieId;
        while ((line = br.readLine()) != null)
        {
            String[] tokens = line.split("\t");
            movieId = Integer.parseInt(tokens[0]);
            directorId = tokens[1];
            directorName = tokens[2];
            insertMovieDirector.setInt(1,movieId);
            insertMovieDirector.setString(2,directorId);
            insertMovieDirector.setString(3,directorName);
            insertMovieDirector.executeUpdate();
        }
        insertMovieDirector.close();
    }
    public static void populateMovies() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("Rotten Tomatos Dataset/movies.dat"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
        Statement makeTable = con.createStatement();
        String createMovieTable = "CREATE TABLE MOVIE"+
                "(id integer not NULL,"+
                "title varchar(300),"+
                "imdbId integer,"+
                "spanishTitle varchar(300),"+
                "imdbPictureURL varchar(200),"+
                "year integer,"+
                "rtID varchar(300),"+
                "rtAllCriticsRating decimal,"+
                "rtAllCriticsNumReviews integer,"+
                "rtAllCriticsNumFresh integer,"+
                "rtAllCriticsNumRotten integer,"+
                "rtAllCriticsScore decimal,"+
                "rtTopCriticsRating decimal,"+
                "rtTopCriticsNumReviews integer,"+
                "rtTopCriticsNumFresh integer,"+
                "rtTopCriticsNumRotten integer,"+
                "rtTopCriticsScore decimal,"+
                "rtAudienceRating decimal,"+
                "rtAudienceNumRatings integer,"+
                "rtAudienceScore integer,"+
                "rtPictureURL varchar(200),"+
                "primary key(id))";
        makeTable.executeUpdate(createMovieTable);
        makeTable.close();
        PreparedStatement insertMovie = con.prepareStatement("INSERT INTO MOVIE (id,title,imdbId,spanishTitle,imdbPictureURL," +
                "year, rtID, rtAllCriticsRating,rtAllCriticsNumReviews," +
                "	rtAllCriticsNumFresh,	rtAllCriticsNumRotten,	" +
                "rtAllCriticsScore,	rtTopCriticsRating,	rtTopCriticsNumReviews," +
                "	rtTopCriticsNumFresh,	rtTopCriticsNumRotten,	" +
                "rtTopCriticsScore,	rtAudienceRating,	rtAudienceNumRatings," +
                "	rtAudienceScore,	rtPictureURL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        String line;
        String rtPictureURL;
        String rtID;
        double rtAllCriticsRating;
        int rtAllCriticsNumReviews;
        int rtAllCriticsNumFresh;
        int rtAllCriticsNumRotten;
        int rtTopCriticsNumReviews;
        int rtTopCriticsNumFresh;
        int rtTopCriticsNumRotten;
        double rtAllCriticsScore;
        double rtTopCriticsRating;
        double rtTopCriticsScore;
        double rtAudienceRating;
        int rtAudienceNumRatings;
        double rtAudienceScore;
        br.readLine();
        while ((line = br.readLine()) != null)
        {
            String[] tokens = line.split("\t");
            int id = Integer.parseInt(tokens[0]);
            String title = tokens[1];
            int imdbId = Integer.parseInt(tokens[2]);
            String spanishTitle = tokens[3];
            String imdbPictureURL = tokens[4];
            int year = Integer.parseInt(tokens[5]);
            if(!tokens[6].equals("\\N"))
            {
                rtID = tokens[6];
            }
            else{
                rtID = null;
            }
            if(!tokens[7].equals("\\N")) {
                rtAllCriticsRating = Float.parseFloat(tokens[7]);
            }
            else{
                rtAllCriticsRating = 0.0;
            }
            if(!tokens[8].equals("\\N")) {
                rtAllCriticsNumReviews = Integer.parseInt(tokens[8]);
            }
            else{
                rtAllCriticsNumReviews = 0;
            }
            if(!tokens[9].equals("\\N"))
            {
                rtAllCriticsNumFresh = Integer.parseInt(tokens[9]);
            }
            else{
                rtAllCriticsNumFresh = 0;
            }
            if(!tokens[10].equals("\\N")) {
                rtAllCriticsNumRotten = Integer.parseInt(tokens[10]);
            }
            else{
                rtAllCriticsNumRotten = 0;
            }
            if(!tokens[11].equals("\\N")) {
                rtAllCriticsScore = Float.parseFloat(tokens[11]);
            }
            else{
                rtAllCriticsScore = 0.0;
            }
            if(!tokens[12].equals("\\N")) {
                rtTopCriticsRating = Float.parseFloat(tokens[12]);
            }
            else{
                rtTopCriticsRating = 0.0;
            }
            if(!tokens[13].equals("\\N")) {
                rtTopCriticsNumReviews = Integer.parseInt(tokens[13]);
            }
            else{
                rtTopCriticsNumReviews = 0;
            }
            if(!tokens[14].equals("\\N")) {
                rtTopCriticsNumFresh = Integer.parseInt(tokens[14]);
            }
            else{
                rtTopCriticsNumFresh = 0;
            }
            if(!tokens[15].equals("\\N")) {
                rtTopCriticsNumRotten = Integer.parseInt(tokens[15]);
            }
            else{
                rtTopCriticsNumRotten = 0;
            }
            if(!tokens[16].equals("\\N")) {
                rtTopCriticsScore = Float.parseFloat(tokens[16]);
            }
            else{
                rtTopCriticsScore = 0.0;
            }
            if(!tokens[17].equals("\\N")) {
                rtAudienceRating = Float.parseFloat(tokens[17]);
            }
            else{
                rtAudienceRating = 0.0;
            }
            if(!tokens[18].equals("\\N")) {
                rtAudienceNumRatings = Integer.parseInt(tokens[18]);
            }
            else{
                rtAudienceNumRatings = 0;
            }
            if(!tokens[19].equals("\\N")) {
                rtAudienceScore = Integer.parseInt(tokens[19]);
            }
            else{
                rtAudienceScore = 0;
            }
            if(!tokens[20].equals("\\N")) {
                rtPictureURL = tokens[20];
            }
            else{
                rtPictureURL = null;
            }
            insertMovie.setInt(1, id);
            insertMovie.setString(2, title);
            insertMovie.setInt(3, imdbId);
            insertMovie.setString(4, spanishTitle);
            insertMovie.setString(5, imdbPictureURL);
            insertMovie.setInt(6, year);
            insertMovie.setString(7, rtID);
            insertMovie.setDouble(8, rtAllCriticsRating);
            insertMovie.setInt(9, rtAllCriticsNumReviews);
            insertMovie.setInt(10, rtAllCriticsNumFresh);
            insertMovie.setInt(11, rtAllCriticsNumRotten);
            insertMovie.setDouble(12, rtAllCriticsScore);
            insertMovie.setDouble(13, rtTopCriticsRating);
            insertMovie.setInt(14, rtTopCriticsNumReviews);
            insertMovie.setInt(15, rtTopCriticsNumFresh);
            insertMovie.setInt(16, rtTopCriticsNumRotten);
            insertMovie.setDouble(17, rtTopCriticsScore);
            insertMovie.setDouble(18, rtAudienceRating);
            insertMovie.setInt(19, rtAudienceNumRatings);
            insertMovie.setDouble(20, rtAudienceScore);
            insertMovie.setString(21, rtPictureURL);
            insertMovie.executeUpdate();




        }
    }
}