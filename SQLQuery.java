package System;
import java.sql.*;

public class SQLQuery{
	

	//Query 1
	public static void btnTopMovies(int k){
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
			Statement selectMov = conn.createStatement();
			String selectMovies = 
			"SELECT title, year, rtAudienceScore, rtPictureURL, IMDbPictureURL" + 
			"FROM MOVIE m" + 
			"INNER JOIN (SELECT DISTINCT TOP " + k.toString() + " rtAudienceScore FROM MOVIE ORDER BY rtAudienceScore DESC)" +
			"AS s ON s.rtAudienceScore = m.rtAudienceScore";
			selectMov.executeUpdate(selectMovies);
			selectMov.close();
			
			//txtBoxOutput.set;
		}catch(Exception e) {
			System.out.println(e.getMessage);
		}
	}

	//Query 2
	public static void btnMovieSearch(String movieName, int amount){
				try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
			Statement searchMov = conn.createStatement();
			String movieSearch =
			"SELECT m.title, m.year, m.rtAudienceScore, m.rtPictureURL, m.IMDbPictureURL, mTagL.tag" + 
			"FROM MOVIE m, MOVIE_TAG_LIST mTagL, MOVIE_TAG mTag" +
			"WHERE m.id = mTag.movieId && mTag.movieID = mTagL.id";
	}

	//Query 7
	public static void btnTopDirectors(int k){
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
			Statement selectDir = conn.createStatement();
			String selectDirectors = 
			"SELECT title, year, rtAudienceScore, rtPictureURL, IMDbPictureURL" + 
			"FROM MOVIE m" + 
			"INNER JOIN (SELECT DISTINCT TOP 10 rtAudienceScore FROM MOVIE ORDER BY rtAudienceScore DESC)" +
			"AS s ON s.rtAudienceScore = m.rtAudienceScore";
			selectDir.executeUpdate(selectDirectors);
			selectDir.close();
			
			//txtBoxOutput.set;
		}catch(Exception e) {
			System.out.println(e.getMessage);
		}
	}

	//Query 8
	public static void btnTopActors(){
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Movie_Matcher?serverTimezone="
                + TimeZone.getDefault().getID(),"root", "assassass1");
			Statement selectAct = conn.createStatement();
			String selectActor = "SELECT Actor FROM MOVIE_ACTOR WHERE ranking <= 10";
			selectAct.executeUpdate(selectActor);
			selectAct.close();
			
			//txtBoxOutput.set;
		}catch(Exception e) {
			System.out.println(e.getMessage);
		}
	}

}