package Particle;

/**
 * Created by Stefan on 21.02.2016.
 */
public class ParticleTexture {
    private int TextureID;
    private int NumberofRows;

    public ParticleTexture(int textureID, int numberofRows) {
        TextureID = textureID;
        NumberofRows = numberofRows;
    }

    public int getTextureID() {
        return TextureID;
    }

    public int getNumberofRows() {
        return NumberofRows;
    }
}
