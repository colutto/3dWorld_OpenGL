package Gui;

import Models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import textures.ModelTexture;

/**
 * Created by Stefan on 04.02.2016.
 */
public class Logo
{
    private Vector2f pos;
    private Vector2f scale;
    private int texture;


    public Logo(Vector2f pos, Vector2f scale, int texture)
    {
        this.pos = pos;
        this.scale = scale;
        this.texture = texture;

    }

    public Vector2f getPos() {
        return pos;
    }

    public Vector2f getScale() {
        return scale;
    }

    public int getTexture() {
        return texture;
    }

}
