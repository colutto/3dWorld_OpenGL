package textures;

/**
 * Created by Stefan on 15.11.2015.
 */
public class ModelTexture
{
    private int textureID;
    private int normalMapID;

    public ModelTexture(int id)
    {
        this.textureID=id;
    }
    public int getID()
    {
        return this.textureID;
    }

    public int getNormalMapID() {
        return normalMapID;
    }

    public void setNormalMapID(int normalMapID) {
        this.normalMapID = normalMapID;
    }
}
