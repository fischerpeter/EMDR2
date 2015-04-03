package emdr.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 02.04.2015.
 */
public class AllowedRegions {
    public List<String> getRegions () {

            List<String> allowedRegions= new ArrayList<String>();

            String Domain = "10000043"; // Amarr
            String TheForge = "10000002"; //Jita
            String BlackRise = "10000069"; //Ichoraya
            String Syndicate = "10000041";  //VSIG
            String Heimatar = "10000030"; //Rens
            String Metropolis = "10000042"; //Hek

            allowedRegions.add(Domain); //Domain (Amarr)
            allowedRegions.add(TheForge); //The Forge (Jita)
            allowedRegions.add(BlackRise);
            allowedRegions.add(Syndicate);
            allowedRegions.add(Metropolis);


            return allowedRegions;

    }
}
