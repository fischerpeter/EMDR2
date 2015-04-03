package emdr.message;

/**
 * Created by user on 01.04.2015.
 */
public class Generator
{
    private String name;

    private String version;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", version = "+version+"]";
    }
}

