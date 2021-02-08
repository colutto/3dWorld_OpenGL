package Terrain;
import Models.RawModel;
import Models.TexuredModel;
import Render.Loader;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import textures.ModelTexture;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Stefan on 13.01.2016.
 */
public class Terrrain
{
    private final float MAX_HEIGHT = 80;
    private final float MAX_PIXEL_COLOUR = 255 * 255 * 255;

    private static final int SIZE = 800;

    private float x;
    private float z;
    private RawModel model;
    private float [][] heights;

    private TexturePack texturePack;


    public float getX()
    {
        return x;
    }

    public float getZ()
    {
        return z;
    }


    public Terrrain(int Gridx, int Gridy, Loader loader,TexturePack texturePack)
    {
        this.texturePack = texturePack;
        this.x = Gridx * SIZE;
        this.z = Gridy * SIZE;
        this.model = generateTerrain(loader,"heightmap");
    }
    private RawModel generateTerrain(Loader loader,String heigtmap){

        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File("res/"+heigtmap+".png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        int VERTEX_COUNT = image.getHeight();

        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j,i,image);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j,i,image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    public TexturePack getTexturePack()
    {
        return texturePack;
    }

    public RawModel getModel()
    {
        return model;
    }

    private float getHeight(int x,int y,BufferedImage image)
    {
        if (x<0 || x>=image.getHeight() || y<0 || y>=image.getHeight())
        {
            return 0;
        }
        float height = image.getRGB(x,y);
        height += MAX_PIXEL_COLOUR/2f;
        height /= MAX_PIXEL_COLOUR/2f;
        height *= MAX_HEIGHT;
        return height;
    }
    private Vector3f calculateNormal(int x,int z,BufferedImage image)
    {
        float heightL = getHeight(x - 1,z,image);
        float heightR = getHeight(x + 1,z,image);
        float heightD = getHeight(x,z - 1,image);
        float heightU = getHeight(x,z + 1,image);
        Vector3f normal = new Vector3f(heightL - heightR,2f,heightD - heightU);
        normal.normalise();
        return normal;
    }
    public float getHeightofTerrain(float worldx,float worldz)
    {
        float terrainX = worldx - this.x;
        float terrainZ = worldz - this.z;
        float gridSquareSize = SIZE / ((float)heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length || gridZ >= heights.length || gridX < 0 || gridZ < 0)
        {
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;
        if (xCoord<=1 - zCoord)
        {
            answer = Maths
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }else
        {
            answer = Maths
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return  answer;
    }
}
