package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Stefan on 17.11.2015.
 */
public class light
{
    private Vector3f position;
    private Vector3f colour;
    private Vector3f attenuation;


    public light(Vector3f position, Vector3f colour)
    {
        this.position = position;
        this.colour = colour;
    }
    public light(Vector3f position, Vector3f colour,Vector3f attenuation)
    {
        this.position = position;
        this.colour = colour;
        this.attenuation = attenuation;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getColour()
    {
        return colour;
    }

    public void setColour(Vector3f colour)
    {
        this.colour = colour;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }
}
