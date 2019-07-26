
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.*;

public class PickerRunner {
    static Semaphore sem = new Semaphore(7);
    static String pass;
    public static void main(String args[]) throws Exception
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Whats your MySQL Root Password?: ");
        pass = scan.next();
        scan.close();
        setupDB();
    }
    public static void setupDB()throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root", pass);
        con.createStatement().executeUpdate("create schema IF NOT EXISTS movie_matcher");
        Thread thread = new Thread(new Runnable() {
            long start = 0;
            long finish = 0;
            public void run() {
                try {
                    start = System.currentTimeMillis();
                    System.out.println(sem.availablePermits());
                    sem.acquire(7);
                    populateAll();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Finished in "+(double)(finish-start)/1000+" seconds");
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                sem.acquire(14);
                addFKConstraints();
            } catch (Exception e) {
                System.out.println(e);
            }

        });
        thread.start();
        thread2.start();
    }
    public static Connection setConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", pass);
        return con;
    }
    public static void populateAll(){
            System.out.println("Starting connection...");
            Thread thread = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    System.out.println("Populating Movie table...");
                    start = System.currentTimeMillis();
                    populateMovies();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating Movie table done in " + (finish - start) / 1000.00 + " seconds.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());
            });
            thread.start();


            Thread thread2 = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    System.out.println("Populating Movie Director table.");
                    start = System.currentTimeMillis();
                    populateMovieDirectors();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating Movie Director table done in " + ((finish - start) / 1000.00) + " seconds.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());

            });
            thread2.start();

            Thread thread3 = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    System.out.println("Populating Tags Director table.");
                    start = System.currentTimeMillis();
                    populateTags();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating Tags table done in " + ((finish - start) / 1000.00) + " seconds.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());
            });
            thread3.start();

            Thread thread4 = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    start = System.currentTimeMillis();
                    System.out.println("Populating Movie Tags table.");
                    populateMovieTags();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating Movie Tags table done in " + ((finish - start) / 1000.00) + " seconds.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());
            });
            thread4.start();

            Thread thread5 = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    start = System.currentTimeMillis();
                    System.out.println("Populating Movie Genres table.");
                    populateMovieGenres();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating Movie Genres table done in " + ((finish - start) / 1000.00) + " seconds.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());
            });
            thread5.start();

            Thread thread6 = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    start = System.currentTimeMillis();
                    System.out.println("Populating Movie Actors table.");
                    populateMovieActors();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating Movie Actors table done in " + ((finish - start) / 1000.00)/60 + " minutes.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());
            });
            thread6.start();

            Thread thread7 = new Thread(() -> {
                long start = 0;
                long finish = 0;
                try {
                    start = System.currentTimeMillis();
                    System.out.println("Populating User Ratings table.");
                    populateUserRatings();
                } catch (Exception e) {
                    System.out.println(e);
                }
                finish = System.currentTimeMillis();
                System.out.println("Populating User Ratings table done in " + ((finish - start) / 1000.00)/60 + " minutes.");
                sem.release(2);
                System.out.println("Current Permit Amount: "+sem.availablePermits());
                System.out.println("Closing connection.");
            });
            thread7.start();

    }
    public static void addFKConstraints() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
            Connection con = setConnection();
            Statement makeTable = con.createStatement();
            ArrayList<String> sqlAlters = new ArrayList<String>();
            sqlAlters.add("ALTER TABLE MOVIE_USER_RATINGS ADD FOREIGN KEY(movieId) IF NOT EXISTS references Movie(id)");
            sqlAlters.add("ALTER TABLE MOVIE_ACTOR ADD FOREIGN KEY(movieId) IF NOT EXISTS references Movie(id)");
            sqlAlters.add("ALTER TABLE MOVIE_GENRE ADD FOREIGN KEY(movieId) IF NOT EXISTS references Movie(id)");
            sqlAlters.add("ALTER TABLE MOVIE_TAG ADD FOREIGN KEY(movieId) IF NOT EXISTS references Movie(id)");
            sqlAlters.add("ALTER TABLE MOVIE_TAG ADD FOREIGN KEY(tagId) IF NOT EXISTS references MOVIE_TAG_LIST(id)");
            sqlAlters.add("ALTER TABLE MOVIE_DIRECTOR ADD FOREIGN KEY(movieId) IF NOT EXISTS references Movie(id)");
            for(String addFKs : sqlAlters) {
                makeTable.executeUpdate(addFKs);
            }
            makeTable.close();
            con.close();
            
    }
    public static void populateUserRatings() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/user_ratedmovies.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieActorTable = "CREATE TABLE IF NOT EXISTS MOVIE_USER_RATINGS" +
                "(userId int not null,"+
                "movieId int  not null,"+
                "rating double,"+
                "dateDay int,"+
                "dateMonth int,"+
                "dateYear int,"+
                "dateHour int,"+
                "dateMinute int,"+
                "dateSecond int,"+
                "primary key (userId, movieId))";
        makeTable.executeUpdate(createMovieActorTable);
        makeTable.close();
        PreparedStatement insertMovieUserRating = con.prepareStatement("INSERT INTO MOVIE_USER_RATINGS (userId,movieId, " +
                "rating, dateDay, dateMonth, dateYear, dateHour, dateMinute, dateSecond)"+
                "VALUES (?, ?, ?, ?, ? ,? ,? ,? ,?)");
        String line;
        br.readLine();
        ArrayList<PreparedStatement> statements = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            insertMovieUserRating.setInt(1, Integer.parseInt(tokens[0]));
            insertMovieUserRating.setInt(2, Integer.parseInt(tokens[1]));
            insertMovieUserRating.setDouble(3, Double.parseDouble(tokens[2]));
            insertMovieUserRating.setInt(4, Integer.parseInt(tokens[3]));
            insertMovieUserRating.setInt(5, Integer.parseInt(tokens[4]));
            insertMovieUserRating.setInt(6, Integer.parseInt(tokens[5]));
            insertMovieUserRating.setInt(7, Integer.parseInt(tokens[6]));
            insertMovieUserRating.setInt(8, Integer.parseInt(tokens[7]));
            insertMovieUserRating.setInt(9, Integer.parseInt(tokens[8]));
            statements.add(insertMovieUserRating);
            insertMovieUserRating.executeUpdate();
        }
        insertMovieUserRating.close();
        con.close();
        br.close();

    }
    public static void populateMovieActors() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/movie_actors.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieActorTable = "CREATE TABLE IF NOT EXISTS MOVIE_ACTOR" +
                "(movieId int not null,"+
                "actorId varchar(90) not null,"+
                "actorName varchar(90),"+
                "ranking int, "+
                "primary key(movieId, actorId))";
        makeTable.executeUpdate(createMovieActorTable);
        makeTable.close();
        PreparedStatement insertMovieActorList = con.prepareStatement("INSERT INTO MOVIE_ACTOR (movieId, actorId, actorName, ranking)"+
                "VALUES (?, ?, ?, ?)");
        br.readLine();
        String line;
        int movieId;
        String actorName;
        String actorId;
        int ranking;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            movieId = Integer.parseInt(tokens[0]);
            actorId = tokens[1];
            actorName = tokens[2];
            ranking = Integer.parseInt(tokens[3]);
            insertMovieActorList.setInt(1,movieId);
            insertMovieActorList.setString(2,actorId);
            insertMovieActorList.setString(3,actorName);
            insertMovieActorList.setInt(4,ranking);
            insertMovieActorList.executeUpdate();
        }
        insertMovieActorList.close();
        con.close();
        br.close();

    }
    public static void populateMovieGenres() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/movie_genres.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieGenreTable = "CREATE TABLE IF NOT EXISTS MOVIE_GENRE" +
                "(movieId int not null,"+
                "genre varchar(50)  not null, " +
                 "primary key (movieId, genre))";
        makeTable.executeUpdate(createMovieGenreTable);
        makeTable.close();
        PreparedStatement insertMovieGenreList = con.prepareStatement("INSERT INTO MOVIE_GENRE (movieId, genre)"+
                "VALUES (?, ?)");
        br.readLine();
        String line;
        int movieId;
        String genre;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            movieId = Integer.parseInt(tokens[0]);
            genre = tokens[1];
            insertMovieGenreList.setInt(1,movieId);
            insertMovieGenreList.setString(2,genre);
            insertMovieGenreList.executeUpdate();
        }
        insertMovieGenreList.close();
        con.close();
        br.close();

    }
    public static void populateTags() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/tags.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieTagsTable = "CREATE TABLE IF NOT EXISTS MOVIE_TAG_LIST" +
                "(id int not null,"+
                "tag varchar(50),"+
                "primary key (id))";
        makeTable.executeUpdate(createMovieTagsTable);
        makeTable.close();
        PreparedStatement insertMovieTagsList = con.prepareStatement("INSERT INTO MOVIE_TAG_LIST (id, tag)"+
                "VALUES (?, ?)");
        br.readLine();
        String line;
        int id;
        String tag;
        while ((line = br.readLine()) != null)
        {
            String[] tokens = line.split("\t");
            id = Integer.parseInt(tokens[0]);
            tag = tokens[1];
            insertMovieTagsList.setInt(1,id);
            insertMovieTagsList.setString(2,tag);
            insertMovieTagsList.executeUpdate();
        }
        insertMovieTagsList.close();
        con.close();
        br.close();

    }

    public static void populateMovieTags() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/movie_tags.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieTagsTable = "CREATE TABLE IF NOT EXISTS MOVIE_TAG" +
                "(movieId int not null,"+
                "tagId int not null,"+
                "tagWeight int, "+
                "primary key(movieId,tagId))";
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
        con.close();
        br.close();

    }
    public static void populateMovieDirectors() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/movie_directors.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieDirectorsTable = "CREATE TABLE IF NOT EXISTS MOVIE_DIRECTOR" +
                "(movieId int not NULL,"+
                "directorId varchar(50)not NULL,"+
                "directorName varchar(50),"+
                "primary key (movieId, directorId))";
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
        con.close();
        br.close();
    }
    public static void populateMovies() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/movies.dat")));
        Connection con = setConnection();
        Statement makeTable = con.createStatement();
        String createMovieTable = "CREATE TABLE IF NOT EXISTS MOVIE"+
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
        con.close();
        br.close();

    }
}