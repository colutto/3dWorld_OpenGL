package Terrain;

import textures.ModelTexture;

/**
 * Created by Stefan on 17.01.2016.
 */
public class TexturePack
{
    private ModelTexture blendMap;
    private ModelTexture rTexture;
    private ModelTexture gTexture;
    private ModelTexture bTexture;
    private ModelTexture backgroundTexture;

    public TexturePack(ModelTexture blendMap, ModelTexture rTexture, ModelTexture gTexture, ModelTexture bTexture, ModelTexture backgroundTexture)
    {
        this.blendMap = blendMap;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
        this.backgroundTexture = backgroundTexture;
    }

    public ModelTexture getBlendMap()
    {
        return blendMap;
    }

    public ModelTexture getrTexture()
    {
        return rTexture;
    }

    public ModelTexture getgTexture()
    {
        return gTexture;
    }

    public ModelTexture getbTexture()
    {
        return bTexture;
    }

    public ModelTexture getBackgroundTexture()
    {
        return backgroundTexture;
    }
}
