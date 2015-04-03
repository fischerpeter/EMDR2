package emdr;

import com.google.gson.Gson;
import emdr.database.SqlConnection_NoThread;
import emdr.emdrentries.TypeHistory;
import emdr.emdrentries.TypePrice;
import emdr.message.EmdrMessage;

import java.net.MalformedURLException;
import java.security.Timestamp;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by user on 01.04.2015.
 */
public class EmdrReader implements Runnable {

    BlockingQueue inputqueue = null;
    BlockingQueue outputqueue = null;

    EmdrMessage emdrMessage = null;

    List<String> allowedRegions;

    LinkedList listqueue;

    //test für non-thread sql
    SqlConnection_NoThread sqlConnection_noThread;

    public EmdrReader(BlockingQueue input, BlockingQueue output, List<String> regions, LinkedList listqueue) {
        this.listqueue = listqueue;
        inputqueue = input;
        outputqueue = output;
        allowedRegions = regions;
        try {
            sqlConnection_noThread = new SqlConnection_NoThread(regions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {

            try {
                emdrMessage = (EmdrMessage) inputqueue.take();
                //System.out.println(inputqueue.size());



            //if (listqueue.size() > 0) {
                //System.out.println(listqueue.size());
                //emdrMessage = (EmdrMessage) listqueue.removeFirst();


                //emdrMessage = ec.getMessage();


                if (allowedRegions.contains(emdrMessage.getRowsets()[0].getRegionID())) {
                    if (emdrMessage.getResultType().equals("orders")) {
                        TypePrice typePrice = new TypePrice();
                        //System.out.println("--------------------");
                        //System.out.println("TypeID: "+emdrMessage.getRowsets()[0].getTypeID());

                        typePrice.typeID = emdrMessage.getRowsets()[0].getTypeID();
                        float[] prices = getPrices();
                        typePrice.buyprice = prices[0];
                        typePrice.sellprice = prices[0];
                        typePrice.generatedAt = emdrMessage.getRowsets()[0].getGeneratedAt();
                        typePrice.regionID = emdrMessage.getRowsets()[0].getRegionID();

                        //### TODO: name, time of generation, marketGroupID
                        //Date javaUtilDate= new Date();

                        //Gson gson = new Gson();
                        //System.out.println(gson.toJson(typePrice));
                        sqlConnection_noThread.go(typePrice);
                        /*
                        try {
                            outputqueue.put(typePrice);
                        } catch (InterruptedException e) {
                            System.out.println("EmdrReader: OutputQueue overrun");
                            e.printStackTrace();
                        }
                        */

                    } else if (emdrMessage.getResultType().equals("history")) {
                        for (String[] row : emdrMessage.getRowsets()[0].getRows()) {
                            TypeHistory typeHistory = new TypeHistory();
                            typeHistory.typeID = emdrMessage.getRowsets()[0].getTypeID();
                            /*
                            typeHistory.orders = row[indexOf("orders")];
                            typeHistory.quantity = row[indexOf("quantity")];
                            typeHistory.low = row[indexOf("low")];
                            typeHistory.high = row[indexOf("high")];
                            typeHistory.average = row[indexOf("average")];
                            typeHistory.generatedAt = emdrMessage.getRowsets()[0].getGeneratedAt();
                            */
                            typeHistory.regionID = emdrMessage.getRowsets()[0].getRegionID();
                            typeHistory.rows = emdrMessage.getRowsets()[0].getRows();


                            //test
                            sqlConnection_noThread.go(typeHistory);


                            /*
                            try {
                                outputqueue.put(typeHistory);
                            } catch (InterruptedException e) {
                                System.out.println("EmdrReader: OutputQueue overrun");
                                e.printStackTrace();
                            }
                            */
                        }


                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            //}
        }
    }
    public int indexOf(String key) {
       for (int i = 0; i<emdrMessage.getColumns().length; i++) {
           if (emdrMessage.getColumns()[i].toLowerCase().equals(key.toLowerCase())) {
               return i;
           }
       }
       return 99;
    }

    private float[] getPrices () {
        float  maxbuy = 0;//Double.parseDouble(emdrMessage.getRowsets()[0].getRows()[0][indexOf("price")]);
        float minsell = Float.MAX_VALUE;  // einfach beim ersten anfangen , und '=' um das parsing nicht nochmal machen zu müssen
        for (String[] row : emdrMessage.getRowsets()[0].getRows()) {
            float price = Float.parseFloat(row[indexOf("price")]);
            if (Boolean.parseBoolean(row[indexOf("bid")])) {
                if (maxbuy < price) maxbuy = price;
            } else {
                if (minsell > price) minsell = price;
            }

        }
        return new float[]{maxbuy, minsell};
    }

}
