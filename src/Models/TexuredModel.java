package Models;

import textures.ModelTexture;

/**
 * Created by Stefan on 15.11.2015.
 */
public class TexuredModel
{
    private RawModel rawModel;
    private ModelTexture texture;
    private float Reflectivity = 1f;
    private float ShineDamper = 1f;
    private boolean faceCulling = false;
    private boolean fakeLighting = false;
    private int numberOfRows = 1;



    public TexuredModel(RawModel model, ModelTexture texture)
    {
        this.rawModel=model;
        this.texture=texture;
    }

    public RawModel getRawModel()
    {
        return rawModel;
    }

    public ModelTexture getTexture()
    {
        return texture;
    }


    public boolean isFakeLighting()
    {
        return fakeLighting;
    }

    public void setFakeLighting(boolean fakeLighting)
    {
        this.fakeLighting = fakeLighting;
    }

    public void setFaceCulling(boolean faceCulling)
    {
        this.faceCulling = faceCulling;
    }

    public boolean isFaceCulling()
    {
        return faceCulling;
    }

    public float getReflectivity()
    {
        return Reflectivity;
    }

    public float getShineDamper()
    {
        return ShineDamper;
    }

    public int getNumberOfRows()
    {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows)
    {
        this.numberOfRows = numberOfRows;
    }

    public void setReflectivity(float reflectivity) {
        Reflectivity = reflectivity;
    }

    public void setShineDamper(float shineDamper) {
        ShineDamper = shineDamper;
    }
}
