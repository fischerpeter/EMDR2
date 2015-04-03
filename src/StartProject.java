import emdr.EmdrClient;
import emdr.EmdrReader;
import emdr.data.AllowedRegions;
import emdr.database.SqlConnection;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by user on 01.04.2015.
 */
public class StartProject {
    public static void main(String[] args) {
        BlockingQueue emdrQueue = new ArrayBlockingQueue(1024);
        BlockingQueue typeQueue = new ArrayBlockingQueue(1024);

        LinkedList listqueue = new LinkedList();

        AllowedRegions regions = new AllowedRegions();

        //EmdrReader er = new EmdrReader();
        EmdrClient ec = new EmdrClient(emdrQueue, listqueue);
        new Thread(ec).start();

        EmdrReader er = new EmdrReader(emdrQueue, typeQueue, regions.getRegions(), listqueue);
        new Thread(er).start();


            //SqlConnection sqlc = new SqlConnection(typeQueue, regions.getRegions());
            //new Thread(sqlc).start();


    }
}
