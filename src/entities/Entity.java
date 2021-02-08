package entities;

import Models.TexuredModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Stefan on 16.11.2015.
 */
public class Entity
{
    private TexuredModel model;
    private Vector3f position;
    private float rotx,roty,rotz;
    private float scale;
    private int textureIndex;

    public Entity(TexuredModel model, Vector3f position, float rotx, float roty, float rotz, float scale, int textureindex)
    {
        this.model = model;
        this.position = position;
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
        this.scale = scale;
        this.textureIndex = textureindex;
    }

    public Entity(TexuredModel model, Vector3f position, float rotx, float roty, float rotz, float scale)
    {
        this.model = model;
        this.position = position;
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
        this.scale = scale;
    }

    public void increasePosition(float dx,float dy,float dz)
    {
        this.position.x+=dx;
        this.position.y+=dy;
        this.position.z+=dz;
    }

    public void increaseRotation(float dx, float dy, float dz)
    {
        this.rotx+=dx;
        this.roty+=dy;
        this.rotz+=dz;
    }

    public TexuredModel getModel()
    {
        return model;
    }

    public void setModel(TexuredModel model)
    {
        this.model = model;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public float getRotx()
    {
        return rotx;
    }

    public void setRotx(float rotx)
    {
        this.rotx = rotx;
    }

    public float getRoty()
    {
        return roty;
    }

    public void setRoty(float roty)
    {
        this.roty = roty;
    }

    public float getRotz()
    {
        return rotz;
    }

    public void setRotz(float rotz)
    {
        this.rotz = rotz;
    }

    public float getScale()
    {
        return scale;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public float getTextureXOffset()
    {
        int colum = textureIndex%model.getNumberOfRows();
        return (float)colum / (float)model.getNumberOfRows();
    }
    public float getTextureYOffset()
    {
        int row = textureIndex / model.getNumberOfRows();
        return (float)row / (float)model.getNumberOfRows();
    }


}
