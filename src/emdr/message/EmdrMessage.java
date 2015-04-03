package emdr.message;

/**
 * Created by user on 01.04.2015.
 */
public class EmdrMessage
{
    private String currentTime;

    private Rowsets[] rowsets;

    private UploadKeys[] uploadKeys;

    private String[] columns;

    private String resultType;

    private Generator generator;

    private String version;

    public String getCurrentTime ()
    {
        return currentTime;
    }

    public void setCurrentTime (String currentTime)
    {
        this.currentTime = currentTime;
    }

    public Rowsets[] getRowsets ()
    {
        return rowsets;
    }

    public void setRowsets (Rowsets[] rowsets)
    {
        this.rowsets = rowsets;
    }

    public UploadKeys[] getUploadKeys ()
    {
        return uploadKeys;
    }

    public void setUploadKeys (UploadKeys[] uploadKeys)
    {
        this.uploadKeys = uploadKeys;
    }

    public String[] getColumns ()
    {
        return columns;
    }

    public void setColumns (String[] columns)
    {
        this.columns = columns;
    }

    public String getResultType ()
    {
        return resultType;
    }

    public void setResultType (String resultType)
    {
        this.resultType = resultType;
    }

    public Generator getGenerator ()
    {
        return generator;
    }

    public void setGenerator (Generator generator)
    {
        this.generator = generator;
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
        return "ClassPojo [currentTime = "+currentTime+", rowsets = "+rowsets+", uploadKeys = "+uploadKeys+", columns = "+columns+", resultType = "+resultType+", generator = "+generator+", version = "+version+"]";
    }
}


