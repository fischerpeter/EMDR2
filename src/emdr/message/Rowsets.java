package emdr.message;

import java.security.Timestamp;

/**
 * Created by user on 01.04.2015.
 */
public class Rowsets
{
    private String generatedAt;

    private String regionID;

    private String[][] rows;

    private String typeID;

    public String getGeneratedAt ()
    {
        return generatedAt;
    }

    public void setGeneratedAt (String generatedAt)
    {
        this.generatedAt = generatedAt;
    }

    public String getRegionID ()
    {
        return regionID;
    }

    public void setRegionID (String regionID)
    {
        this.regionID = regionID;
    }

    public String[][] getRows ()
    {
        return rows;
    }

    public void setRows (String[][] rows)
    {
        this.rows = rows;
    }

    public String getTypeID ()
    {
        return typeID;
    }

    public void setTypeID (String typeID)
    {
        this.typeID = typeID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [generatedAt = "+generatedAt+", regionID = "+regionID+", rows = "+rows+", typeID = "+typeID+"]";
    }

}

