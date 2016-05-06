
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nsankhe
 */
public class populate {

    private Hashtable<String, Integer> ht = null;

    public populate() {
        ht = new Hashtable<String, Integer>();
        ht.put("Active Life", 1);
        ht.put("Restaurants", 1);
        ht.put("Arts & Entertainment", 1);
        ht.put("Automotive", 1);
        ht.put("Car Rental", 1);
        ht.put("Cafes", 1);
        ht.put("Beauty & Spas", 1);
        ht.put("Convenience Stores", 1);
        ht.put("Dentists", 1);
        ht.put("Doctors", 1);
        ht.put("Drugstores", 1);
        ht.put("Department Stores", 1);
        ht.put("Education", 1);
        ht.put("Event Planning & Services", 1);
        ht.put("Flowers & Gifts", 1);
        ht.put("Food", 1);
        ht.put("Health & Medical", 1);
        ht.put("Home Services", 1);
        ht.put("Home & Garden", 1);
        ht.put("Hospitals", 1);
        ht.put("Hotels & Travel", 1);
        ht.put("Hardware Stores", 1);
        ht.put("Grocery", 1);
        ht.put("Medical Centers", 1);
        ht.put("Nurseries & Gardening", 1);
        ht.put("Nightlife", 1);
        ht.put("Restaurants", 1);
        ht.put("Shopping", 1);
        ht.put("Transportation", 1);
    }

    public static void main(String args[]) throws IOException {
        populate p = new populate();
        if (args.length > 0) {
            p.run(args[0], args[1], args[2]);

        } else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(1);
        }
    }

    private void run(String file1, String file2, String file3) throws IOException {
        Connection con = null;
        ResultSet result = null;
        try {
            con = openConnection();
            //parseDataUser(file2, con);
            parseDataBusiness(file1, con);
            //parseDataReview(file3, con);
            System.out.println("Done !!");
        } catch (SQLException e) {
            System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the database driver");
        } finally {
            // Never forget to close database connection 
            closeConnection(con);
        }
    }

    private Connection openConnection() throws SQLException, ClassNotFoundException {
        // Load the Oracle database driver 
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

        /* 
         Here is the information needed when connecting to a database 
         server. These values are now hard-coded in the program. In 
         general, they should be stored in some configuration file and 
         read at run time. 
         */
        //String host = "localhost";
        //String port = "1527";
        //String dbName = "orcl";
        String userName = "Scott";
        //String password = "Najuka123";

        // Construct the JDBC URL 
        //String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        String dbURL = "jdbc:oracle:thin:@localhost:1521:orcl";
        String pass = "Najuka123";
        return DriverManager.getConnection(dbURL, userName, pass);
    }

    /**
     * Close the database connection
     *
     * @param con
     */
    private void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }

    private void parseHours(JSONObject jsonObject, PreparedStatement stmt) throws SQLException {
        JSONObject jsonObj = (JSONObject) jsonObject.get("hours");
        Set keys = jsonObj.keySet();
        Iterator<String> iter = keys.iterator();
        //System.out.println(jsonObj);
        while (iter.hasNext()) {
            String key = iter.next();
            stmt.setString(2, key);
            JSONObject value = (JSONObject) jsonObj.get(key);
            String str1 = (String) value.get("open");
            float num1 = 0;
            float num2 = 0;
            if (str1.contains(":")) {
                str1 = str1.replace(":", ".");
                num1 = Float.parseFloat(str1);
            }
            String str2 = (String) value.get("close");
            if (str2.contains(":")) {
                str2 = str2.replace(":", ".");
                num2 = Float.parseFloat(str2);
            }
            stmt.setFloat(3, num1);
            stmt.setFloat(4, num2);
            stmt.executeUpdate();
            //System.out.println(key + " " + value.get("open") + " " + value.get("close"));
        }
    }

    private void parseCategory(JSONObject jsonObject, PreparedStatement stmt1, PreparedStatement stmt2) throws SQLException {
        JSONArray catArray = (JSONArray) jsonObject.get("categories");
        //System.out.println("*****\n" + catArray);
        Iterator<String> iterator = catArray.iterator();
        int flag = -1;
        ArrayList<String> mainCat = new ArrayList<String>();
        ArrayList<String> subCat = new ArrayList<String>();
        while (iterator.hasNext()) {
            String cat_name = iterator.next();
            flag = seperateCategory(cat_name);
            if (flag == 1) {
                mainCat.add(cat_name);
            } else if (flag == 0) {
                subCat.add(cat_name);
            }
        }
        for (String item : mainCat) {
            stmt2.setString(2, item);
            stmt2.executeUpdate();
        }
        for (String itm : mainCat) {
            stmt1.setString(2, itm);
            for (String it : subCat) {
                stmt1.setString(3, it);
                stmt1.executeUpdate();
            }
        }
    }

    private int seperateCategory(String cat_name) {
        if (getHt().containsKey(cat_name)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the ht
     */
    public Hashtable<String, Integer> getHt() {
        return ht;
    }

    private void parseBusiness(JSONObject jsonObject, PreparedStatement stmt) throws SQLException {
        boolean open = (boolean) jsonObject.get("open");
        if (open == true) {
            stmt.setInt(2, 1);
        } else {
            stmt.setInt(2, 0);
        }
        String city = (String) jsonObject.get("city");
        stmt.setString(3, city);
        String state = (String) jsonObject.get("state");
        stmt.setString(4, state);
        long review_count = (long) jsonObject.get("review_count");
        stmt.setLong(5, review_count);
        String name = (String) jsonObject.get("name");
        stmt.setString(6, name);
        stmt.setFloat(7, (float) ((double) jsonObject.get("stars")));
        String addr = (String) jsonObject.get("full_address");
        Pattern pattern = Pattern.compile("\\d{5}?");
        Matcher matcher = pattern.matcher(addr);
        if (matcher.find()) {
            stmt.setString(8, matcher.group(0));
        }
        stmt.executeUpdate();
    }

    private void parseAttribute(JSONObject jsonObject, PreparedStatement stmt) throws SQLException {
        JSONObject jsonObj1 = (JSONObject) jsonObject.get("attributes");
        if (!jsonObj1.isEmpty()) {
            //System.out.println("\n\n\n" + jsonObj1);
            String insertKey = null;
            Object insertValue = null;
            Set keyset = jsonObj1.keySet();
            Iterator iter1 = keyset.iterator();

            if (keyset.size() == 1) {
                String k = (String) iter1.next();
                Object value = jsonObj1.get(k);
                /*if ("Good for Kids".equals(k)) {
                    k = "Good For Kids";
                }*/
                if (value instanceof JSONObject) {
                    JSONObject valObj1 = (JSONObject) value;
                    Set subkeys1 = valObj1.keySet();

                    Iterator iter2 = subkeys1.iterator();
                    while (iter2.hasNext()) {
                        String subkey1 = (String) iter2.next();
                        Object subkeyValue1 = valObj1.get(subkey1);
                        if ((boolean) subkeyValue1 == true) {
                            subkeyValue1 = "T";
                        } else {
                            subkeyValue1 = "F";
                        }
                        //System.out.println(k + "_" + subkey1 + " : " + subkeyValue1);
                        stmt.setString(2, k + "_" + subkey1);
                        stmt.setObject(3, subkeyValue1);
                        stmt.setObject(4, k + "_" + subkey1 + "_" + subkeyValue1);
                        stmt.executeUpdate();
                    }
                } else if (value instanceof Boolean) {
                    if ((boolean) value == true) {
                        value = "T";
                    } else {
                        value = "F";
                    }
                    //System.out.println(k + " : " + value);
                    stmt.setString(2, k);
                    stmt.setString(3, (String) value);
                    stmt.setString(4, k+"_"+value);
                    stmt.executeUpdate();
                } else {
                    if (value != null) {
                        //System.out.println(k + " : " + value);
                        stmt.setString(2, k);
                        stmt.setObject(3, (Object) value);
                        stmt.setObject(4, k + "_"+value);
                        stmt.executeUpdate();
                    }
                }
            }

            if (keyset.size() > 1) {
                //System.out.println(keyset);
                while (iter1.hasNext()) {
                    String key = (String) iter1.next();
                    Object value = jsonObj1.get(key);
                    /*if ("Good for Kids".equals(key)) {
                        key = "Good For Kids";
                    }*/
                    if (value instanceof Boolean) {
                        if ((boolean) value == true) {
                            value = "T";
                        } else {
                            value = "F";
                        }
                        insertKey = key;
                        insertValue = value;
                        //System.out.println("1 : " + insertKey + " : " + insertValue);
                        stmt.setString(2, insertKey);
                        stmt.setObject(3, (Object) insertValue);
                        stmt.setObject(4, insertKey+"_"+insertValue);
                        stmt.executeUpdate();
                        //System.out.println("%%%%+ key + " : " + value);
                    } else if (value instanceof JSONObject) {
                        //System.out.println("**** " + key + " : " + value);
                        JSONObject valObj = (JSONObject) value;
                        Set subkeys = valObj.keySet();

                        Iterator iter = subkeys.iterator();
                        while (iter.hasNext()) {
                            String subkey = (String) iter.next();
                            Object subkeyValue = valObj.get(subkey);
                            if ((boolean) subkeyValue == true) {
                                subkeyValue = "T";
                            } else {
                                subkeyValue = "F";
                            }
                            insertKey = key + "_" + subkey;
                            insertValue = subkeyValue;
                            //System.out.println("####### " + subkey + " : " + subkeyValue);
                            //System.out.println("2 : " + insertKey + " : " + insertValue);
                            stmt.setString(2, insertKey);
                            stmt.setObject(3, (Object) insertValue);
                            stmt.setObject(4, insertKey + "_" + insertValue);
                            stmt.executeUpdate();
                        }
                    } else {
                        insertKey = key;
                        insertValue = value;
                        //System.out.println(key + " : " + value);
                        //System.out.println("3 : " + insertKey + " : " + insertValue);
                        stmt.setString(2, insertKey);
                        stmt.setObject(3, (Object) insertValue);
                        stmt.setObject(4, insertKey + "_" + insertValue);
                        stmt.executeUpdate();
                    }
                }
                // System.out.println("********");
            }
        }
    }

    private void deleteTableData(Connection con) throws SQLException {
        Statement del_stmt = con.createStatement();
        System.out.println("Deleting previous tuples from table yelp_business...");
        del_stmt.addBatch("DELETE FROM yelp_business");

        System.out.println("Deleting previous tuples from table category...");
        del_stmt.addBatch("DELETE FROM category");

        System.out.println("Deleting previous tuples from table sub_category...");
        del_stmt.addBatch("DELETE FROM sub_category");

        System.out.println("Deleting previous tuples from table hours...");
        del_stmt.addBatch("DELETE FROM hours");

        System.out.println("Deleting previous tuples from table attributes...");
        del_stmt.addBatch("DELETE FROM attributes");

        int[] results = del_stmt.executeBatch();
        del_stmt.close();
        System.out.println("\n");
    }

    private void parseDataBusiness(String file, Connection con) throws IOException, SQLException {

        deleteTableData(con);

        PreparedStatement stmt = con.prepareStatement("INSERT INTO yelp_business values (?,?,?,?,?,?,?,?)");
        System.out.println("Inserting Data into table yelp_business...");

        PreparedStatement stmt2 = con.prepareStatement("INSERT INTO category values (?,?)");
        System.out.println("Inserting Data into table category...");

        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO sub_category values(?,?,?)");
        System.out.println("Inserting Data into table sub_category...");

        PreparedStatement stmt3 = con.prepareStatement("INSERT INTO hours values(?,?,?,?)");
        System.out.println("Inserting Data into table hours...");

        PreparedStatement stmt4 = con.prepareStatement("INSERT INTO attributes values(?,?,?,?)");
        System.out.println("Inserting Data into table attributes...");

        System.out.println("\n");
        BufferedReader br = null;
        JSONParser parser = new JSONParser();

        String sCurrentLine;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());;
        }

        while ((sCurrentLine = br.readLine()) != null) {
            //System.out.println(sCurrentLine);
            Object obj;
            try {
                obj = parser.parse(sCurrentLine);
                JSONObject jsonObject = (JSONObject) obj;

                /* Business Table parsing */
                String business_id = (String) jsonObject.get("business_id");
                stmt.setString(1, business_id);
                parseBusiness(jsonObject, stmt);

                /* hours table parsing */
                stmt3.setString(1, business_id);
                parseHours(jsonObject, stmt3);

                /* attribute table parsing */
                stmt4.setString(1, business_id);
                //System.out.println(business_id);
                parseAttribute(jsonObject, stmt4);

                /* catgory and sub_category table parsing */
                stmt2.setString(1, business_id);
                stmt1.setString(1, business_id);
                parseCategory(jsonObject, stmt1, stmt2);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                System.err.println("Error in Parsing:" + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Error with database quering:" + e.getMessage());
            }
        }
        try {
            stmt4.close();
            stmt3.close();
            stmt2.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("Error while closing prepared statements: " + ex.getMessage());
        }
    }

    private void parseDataUser(String file, Connection con) throws IOException, SQLException {
        Statement del_stmt = con.createStatement();
        System.out.println("Deleting previous tuples from table yelp_user...");
        del_stmt.executeUpdate("DELETE FROM yelp_user");
        del_stmt.close();

        PreparedStatement stmt = con.prepareStatement("INSERT INTO yelp_user values (?,?,?,?,?,?,?,?,?)");
        System.out.println("Inserting Data into table yelp_user...");
        BufferedReader br = null;
        JSONParser parser = new JSONParser();

        String sCurrentLine;

        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());;
        }

        while ((sCurrentLine = br.readLine()) != null) {
            //System.out.println(sCurrentLine);
            Object obj;
            try {
                obj = parser.parse(sCurrentLine);
                JSONObject jsonObject = (JSONObject) obj;
                stmt.setString(1, (String) jsonObject.get("yelping_since"));

                // get an array from the JSON object
                JSONObject objVote = (JSONObject) jsonObject.get("votes");
                stmt.setLong(2, (long) objVote.get("funny"));
                stmt.setLong(3, (long) objVote.get("useful"));
                stmt.setLong(4, (long) objVote.get("cool"));

                stmt.setLong(5, (long) jsonObject.get("review_count"));
                stmt.setString(6, (String) jsonObject.get("name"));
                stmt.setString(7, (String) jsonObject.get("user_id"));
                stmt.setLong(8, (long) jsonObject.get("fans"));
                stmt.setFloat(9, (float) ((double) jsonObject.get("average_stars")));

                stmt.executeUpdate();

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        stmt.close();
    }

    private void parseDataReview(String file, Connection con) throws IOException, SQLException {
        Statement del_stmt = con.createStatement();
        System.out.println("Deleting previous tuples from table yelp_review...");
        del_stmt.executeUpdate("DELETE FROM yelp_review");
        del_stmt.close();

        PreparedStatement stmt = con.prepareStatement("INSERT INTO yelp_review values (?,?,?,?,?,?,?,?,?)");
        System.out.println("Inserting Data into table yelp_review...");
        BufferedReader br = null;
        JSONParser parser = new JSONParser();

        String sCurrentLine;

        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());;
        }
        while ((sCurrentLine = br.readLine()) != null) {
            //System.out.println(sCurrentLine);
            Object obj;
            try {
                obj = parser.parse(sCurrentLine);
                JSONObject jsonObject = (JSONObject) obj;

                // get an array from the JSON object
                JSONObject objVote = (JSONObject) jsonObject.get("votes");
                stmt.setLong(1, (long) objVote.get("funny"));
                stmt.setLong(2, (long) objVote.get("useful"));
                stmt.setLong(3, (long) objVote.get("cool"));

                stmt.setString(4, (String) jsonObject.get("review_id"));
                stmt.setString(5, (String) jsonObject.get("user_id"));
                stmt.setLong(6, (long) jsonObject.get("stars"));

                String date_string = (String) jsonObject.get("date");
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = null;
                try {
                    date = fmt.parse(date_string);
                } catch (java.text.ParseException ex) {
                    System.err.println(ex.getMessage());
                }
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                //System.out.println(sqlDate);
                stmt.setDate(7, sqlDate);

                stmt.setString(8, (String) jsonObject.get("text"));
                stmt.setString(9, (String) jsonObject.get("business_id"));
                stmt.executeUpdate();

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        stmt.close();
    }
}
