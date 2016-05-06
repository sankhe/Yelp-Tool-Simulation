Name: NAJUKA SANKHE 

SCU-ID: W1076713



Files Submitted:
1. createdb.sql
2. dropdb.sql
3. populate.java 
4. hw3.java
5. Sentimental Analysis folder with: i) Sentimental Analysis of Yelp Reviews (pdf file)      
				      ii) ReviewsSeqFile.java (json to sequence file conversion)'

6. jar files folder with : i) ojdbc6.jar 	   
			   ii) json-simple-1.1.jar     
			   iii) jfreechart-1.0.19.jar    
			   iv) jcommon-1.0.23.jar


createdb.sql : 
	contains create statements and insert statements for the following tables:

		1. yelp_user
		2. yelp_business
		3. category
		4. sub_category
		5. hours
		6. attributes
		7. yelp reviews



Compilation and execution steps :

1) Run the "createdb.sql" file to create tables in the database.

2) Need to change the openConnection() parameters to establish the jdbc connection to your machine. 
   Changes are in both populate.java and hw.java

3) To populate the database:
	compile : javac -classpath :ojdbc6.jar json-simple-1.1.jar populate.java
	execute : java -classpath :ojdbc6.jar json-simple-1.1.jar populate yelp_business.json yelp_user.json yelp_review.json  

4) To run gui code:
	compile : javac -classpath :ojdbc6.jar jfreechart-1.0.19.jar jcommon-1.0.23.jar HW3.java
	
	execute : java -classpath :ojdbc6.jar jfreechart-1.0.19.jar jcommon-1.0.23.jar HW3

5) Drop tables by running "dropdb.sql".



Assumptions:


1) By default, "Any attributes" option from "Search For" combobox is selected.

2) City, Zip, State values displayed on the gui are fetched from the database.

3) "Clear Combo_boxes"" button clears all the comboboxes.

4) Whenever, any changes are made into main_category, sub_catgory or attributes checkbox selection, business_table will get refreshed. 
   i.e. all the data from the table will get deleted. So, need to click search button again to get the result after making changes to selections of checkboxes.
5) 24 hours clock format is used.
6) If query has "no rows selected" result, GUI will show message that "no data to display".
7) I have added Combobox "Open or Close time" with two options "open" and "close". By default, "open" option is selected.
   This is to check if we are searching for businesses which are open or close on a particular day. 
   When "close" option is selected --> please do not select From and To time.
8) From and To time is only for query searching for business "open" option.

NOTE : When a business (row) is selected from the business_list, it takes 1-2 seconds to load and display the reviews for that particular business.

Extra Features:

--> Piechart for particular business will be displayed according to review_stars.

--> Bar-graph displays the top 5 useful review_count.
    This can be used how useful and informative listed yelp reviews are. 
    For example, if top useful review counts are 7, 5, 4, 3, 3. Then we can say it is useful and 
    if top useful review counts are 1,1,1,1,1 then we can say reviews listed are not much useful.

--> Sentimental Analysis of yelp_reviews : please find "Sentimental Analysis of Yelp Reviews.pdf" file for details.
					   ReviewsSeqFile.java : converts the json file to sequence file. 
					   Mahout machine learning library requires the input in sequence file format.
