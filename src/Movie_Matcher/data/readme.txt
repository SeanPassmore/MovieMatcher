
=======================
Rotten Tomatoes Dataset
=======================

-------
Version
-------

Version 1.0 (May 2011)

-----------
Description
-----------
    
    It contains the data crawled from 
    Internet Movie Database (IMDb) and Rotten Tomatoes movie review systems.
    http://www.imdb.com 
    http://www.rottentomatoes.com 


---------------
Data statistics
---------------

    2113 users
   10197 movies
   
      20 movie genres
   20809 movie genre assignments
         avg. 2.040 genres per movie

    4060 directors
   95321 actors
         avg. 22.778 actors per movie
      72 countries

   10197 country assignments
         avg. 1.000 countries per movie
   47899 location assignments
         avg. 5.350 locations per movie

   13222 tags
   47957 tag assignments (tas), i.e. tuples [user, tag, movie]
         avg. 22.696 tas per user
         avg. 8.117 tas per movie

  855598 ratings
         avg. 404.921 ratings per user
         avg. 84.637 ratings per movie

-----
Files
-----

   * movies.dat
   
   	This file contains information about the movies of the database, including: 
   	
   
           - Titles in Spanish
           - IMDb movie ids
           - IMDb picture URLs
           - Rotten Tomatoes movie ids
           - Rotten Tomatoes picture URLs
           - Rotten Tomatoes (all/top) critics' ratings, avg. scores, numbers of 
             reviews/fresh_scores/rotten_scores
           - Rotten Tomatoes audience' avg. ratings, number of ratings, avg. scores
   
   * movie_genres.dat
   
        This file contains the genres of the movies.
   
   * movie_directors.dat
   
   	This file contains the directors of the movies.
   
   * movie_actors.dat
   
   	This file contains the main actores and actresses of the movies.
   	
   	A ranking is given to the actors of each movie according to the order in which 
   	they appear on the movie IMDb cast web page.
   
   * movie_countries.dat
   
        This file contains the countries of origin of the movies.
   
   * movie_locations.dat
   
        This file contains filming locations ot the movies.
   
   * tags.dat
   
   	This file contains the set of tags available in the dataset.
   
   * user_taggedmovies.dat - user_taggedmovies-timestamps.dat
   
        These files contain the tag assignments of the movies provided by each particular user.
        
        They also contain the timestamps when the tag assignments were done.
   
   * movie_tags.dat
   
        This file contains the tags assigned to the movies, and the number of times 
        the tags were assigned to each movie.
   
   * user_ratedmovies.dat - user_ratedmovies-timestamps.dat
   
        These files contain the ratings of the movies provided by each particular user.
        
        They also contain the timestamps when the ratings were provided.

-----------
Data format
-----------

   The data is formatted one entry per line as follows (tab separated, "\t"):

   * movies.dat
   
        id \t title \t imdbID \t spanishTitle \t imdbPictureURL \t year \t rtID \t rtAllCriticsRating \t rtAllCriticsNumReviews \t rtAllCriticsNumFresh \t rtAllCriticsNumRotten \t rtAllCriticsScore \t rtTopCriticsRating \t rtTopCriticsNumReviews \t rtTopCriticsNumFresh \t rtTopCriticsNumRotten \t rtTopCriticsScore \t rtAudienceRating \t rtAudienceNumRatings \t rtAudienceScore \t rtPictureURL

        Example:
        1	Toy story	0114709	Toy story (juguetes)	http://ia.media-imdb.com/images/M/MV5BMTMwNDU0NTY2Nl5BMl5BanBnXkFtZTcwOTUxOTM5Mw@@._V1._SX214_CR0,0,214,314_.jpg	1995	toy_story	9	73	73	0	100	8.5	17	17	0	100	3.7	102338	81	http://content7.flixster.com/movie/10/93/63/10936393_det.jpg

   * movie_genres.dat
   
        movieID	\t genre

        Example:
        1	Adventure

   * movie_directors.dat

        movieID \t directorID \t directorName

        Example:
        1	john_lasseter	John Lasseter
   
   * movie_actors.dat

        movieID \t actorID \t actorName \t ranking

        Example:
        1	annie_potts	Annie Potts	10
   
   * movie_countries.dat

        movieID \t country

        Example:
        1	USA

   * movie_locations.dat

        movieID \t location1 \t location2 \t location3 \t location4

        Example:
        2	Canada	British Columbia	Vancouver
   
   * tags.dat

        id \t value

        Example:
        1	earth

   * movie_tags.dat

        movieID \t tagID \t tagWeight

        Example:
        1	13	3
   
   * user_taggedmovies-timestamps.dat

        userID \t movieID  \t tagID  \t timestamp

        Example:
        75	353	5290	1162160415000
        
   * user_taggedmovies.dat

        userID \t movieID \t tagID \t date_day \t date_month \t date_year \t date_hour \t date_minute \t date_second

        Example:
        75	353	5290	29	10	2006	23	20	15
   
   * user_ratedmovies-timestamps.dat

        userID \t movieID \t rating \t timestamp

        Example:
        75	3	1	1162160236000

   * user_ratedmovies.dat

        userID \t movieID \t rating \t date_day \t date_month \t date_year \t date_hour \t date_minute \t date_second

        Example:
        75	3	1	29	10	2006	23	17	16

