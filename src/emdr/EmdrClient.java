package emdr;

/**
 * Created by user on 01.04.2015.
 */


import com.google.gson.Gson;
import emdr.message.EmdrMessage;
import jdk.nashorn.internal.parser.JSONParser;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


public class EmdrClient implements Runnable {

    BlockingQueue queue;
    LinkedList listqueue;

    ZMQ.Context context = null;
    ZMQ.Socket subscriber = null;

    Gson gson;

    public EmdrClient(BlockingQueue queue, LinkedList listqueue) {
        this.listqueue = listqueue;
        this.queue = queue;

        context = ZMQ.context(1);
        subscriber = context.socket(ZMQ.SUB);

        subscriber.connect("tcp://relay-us-central-1.eve-emdr.com:8050");
        subscriber.connect("tcp://relay-eu-germany-1.eve-emdr.com:8050");

        subscriber.subscribe(new byte[0]);


        gson = new Gson();
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] receivedData = subscriber.recv(0);

                // We build a large enough buffer to contain the decompressed data.
                byte[] decompressed = new byte[receivedData.length * 16];

                // Decompress the raw market data.
                Inflater inflater = new Inflater();
                inflater.setInput(receivedData);
                int decompressedLength = 0;
                decompressedLength = inflater.inflate(decompressed);
                inflater.end();

                byte[] output = new byte[decompressedLength];
                System.arraycopy(decompressed, 0, output, 0, decompressedLength);

                EmdrMessage message = gson.fromJson(new String(output, "UTF-8"), EmdrMessage.class);



                queue.put(message);
                System.out.println(queue.size());



                //listqueue.add(message);
                //System.out.println("added to list");
                //System.out.println(gson.toJson(message));

            } catch (DataFormatException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
