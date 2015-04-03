package emdr.database;


import emdr.emdrentries.TypeHistory;
import emdr.emdrentries.TypePrice;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

/**
 * Created by user on 02.04.2015.
 */
public class SqlConnection_NoThread {

    String server = "jdbc:mysql://192.168.178.23:3306/";
    //String url = "jdbc:mysql://localhost:3306/";
    String owndb = "owndb";
    String tableprefix = "r";
    String evedb = "evedb";

    String eveNameIdTable = "invTypes";

    //String dbName = "eve_dump";

    String userName = "update_db";
    String password = "update_db";

    String driver = "com.mysql.jdbc.Driver";

    private Connection c;
    private Statement st;
    private PreparedStatement pst;
    private static ResultSet resultSet = null;

    private HashMap<String, String> regionnames = new HashMap<String, String>();


    private static final String dbClassName = "com.mysql.jdbc.Driver";

    BlockingQueue queue;
    List<String> regions;


    public SqlConnection_NoThread(List<String> regions) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        this.queue = queue;
        this.regions = regions;


        //Class.forName(driver).newInstance(); //get driver
        //conn = DriverManager.getConnection(url + dbName, userName, password); // connect to DB
        //st=conn.createStatement();
        Class.forName(dbClassName);
        c = DriverManager.getConnection(server, userName, password);
        st = c.createStatement();

        //getRegionames();
        checkTables();
        //c.close();


    }



    public void go(Object item) {

            //Object item = queue.take();
            if (item.getClass() == TypePrice.class) {
                TypePrice typePrice = (TypePrice)item;
                try {
                    st.executeUpdate(
                            "UPDATE "+tablename(typePrice.regionID)+
                                    " SET " +
                                    " buyprice = " + typePrice.buyprice +
                                    ", sellprice = " + typePrice.sellprice +
                                    ", generatedAt = '" + getTimeFromString(typePrice.generatedAt) +"'"+
                                    " WHERE " +
                                    " typeID = " +typePrice.typeID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("update price ID: "+typePrice.typeID+ "\t\tRegion: "+typePrice.regionID);
            }
            if (item.getClass() == TypeHistory.class) {
            //if (false) {
                TypeHistory typeHistory = (TypeHistory) item;
                try {
                    /*
                    String query = "INSERT IGNORE INTO "+tablename(typeHistory.regionID)+"_h VALUES (" +
                            " typeId =" + typeHistory.typeID +
                            ",generatedAt = '" + getTimeFromString(typeHistory.generatedAt) + "'" +
                            ",orders =" + typeHistory.orders +
                            ",quantity =" + typeHistory.quantity +
                            ",low =" + typeHistory.low +
                            ",high =" + typeHistory.high +
                            ",average =" + typeHistory.average +
                            ")";
                    //System.out.println(query);
                    st.executeUpdate(query);
                    */
                    String query = "INSERT IGNORE INTO "+tablename(typeHistory.regionID)+"_h (" +
                            " typeId " +
                            ",generatedAt " +
                            ",orders " +
                            ",quantity " +
                            ",low " +
                            ",high " +
                            ",average " +
                            ") VALUES (?,?, ?, ?, ?, ?, ?)";
                    pst = c.prepareStatement(query);
                    for (String[] row : typeHistory.rows) {
                        for (int i = 0; i < row.length; i++) {
                            pst.setInt(1, Integer.parseInt(typeHistory.typeID));
                            pst.setTimestamp(2, getTimeFromString(row[0]));
                            pst.setInt(3, Integer.parseInt(row[1]));
                            pst.setInt(4, Integer.parseInt(row[2]));
                            pst.setFloat(5, Float.parseFloat(row[3]));
                            pst.setFloat(6, Float.parseFloat(row[4]));
                            pst.setFloat(7, Float.parseFloat(row[5]));
                        }
                        pst.addBatch();
                    }

                    int[] added =pst.executeBatch();
                    pst.close();
                    System.out.println("History/typeId="+typeHistory.typeID+ "    region="+typeHistory.regionID);


                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                System.out.println("update history");
            }
            //System.out.println(queue.size());

    }


    private void writePrice(TypePrice price) {
        //resultSet.
    }


    private void checkTables() {
        try {

            for (int i = 0; i < regions.size(); i++) {
                use(owndb);
                ResultSet resultSet = st.executeQuery("SHOW TABLES LIKE '"+tablename(regions.get(i)) + "'");
                resultSet.last();
                String query;

                if (resultSet.getRow() > 0) {
                } else {
                    use(owndb);
                    query = "CREATE TABLE " + tablename(regions.get(i)) + " (" +
                            " typeID int, " +
                            " typeName TEXT NOT NULL, " +
                            " marketGroupID int, " +
                            "buyprice double NOT NULL DEFAULT '0', " +
                            "sellprice double NOT NULL DEFAULT '0', " +

                            //"marketGroupName TEXT, " +
                            "generatedAt TIMESTAMP ," +
                            "primary key (typeID))";
                    System.out.println(query);
                    st.executeUpdate(query);

                    query= "INSERT INTO "+tablename(regions.get(i))+" (typeID, typeName, marketGroupID) " +
                            "SELECT typeID, typeName, marketGroupID " +
                            "FROM "+evedb+"."+eveNameIdTable+" " +
                            "WHERE published = 1 AND marketGroupID IS NOT NULL ";
                    System.out.println(query);
                    st.executeUpdate(query);
                }
                ResultSet resultSet_h = st.executeQuery("SHOW TABLES LIKE '" + tablename(regions.get(i)) + "_h'");
                resultSet_h.last();

                if (!(resultSet_h.getRow() > 0)) {

                    // create history table
                    query = "CREATE TABLE "+tablename(regions.get(i))+"_h (" +
                            " typeID int, " +
                            "generatedAt TIMESTAMP, " +
                            "orders int NOT NULL DEFAULT '0', " +
                            "quantity int NOT NULL DEFAULT '0', " +
                            "low float NOT NULL DEFAULT '0', " +
                            "high float NOT NULL DEFAULT '0', " +
                            "average float NOT NULL DEFAULT '0', " +

                            "primary key (typeID, generatedAt(10)))";
                    System.out.println(query);
                    st.executeUpdate(query);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void use(String database) {
        String query = "USE "+database;
        try {
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getRegionames() {
        use(evedb);
        for (String region : regions) {
            String query = "";
            try {
                st.executeQuery(query);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private Timestamp getTimeFromString(Object time) {
        DateTime jodatime = new DateTime(time);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(dtf.print(jodatime));
    }

    private String tablename (String region) {
        return tableprefix+region;
    }

}




