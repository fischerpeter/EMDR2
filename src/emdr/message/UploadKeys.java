package emdr.message;

/**
 * Created by user on 01.04.2015.
 */
public class UploadKeys
{
    private String name;

    private String key;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", key = "+key+"]";
    }
}