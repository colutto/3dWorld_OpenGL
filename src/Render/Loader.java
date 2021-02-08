package Render;
import Models.RawModel;
import Skybox.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 14.11.2015.
 */
public class Loader
{
    private List<Integer> vaos=new ArrayList<Integer>();
    private List<Integer> vbos=new ArrayList<Integer>();
    private List<Integer> textures=new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals, int[] indices)
    {
        int vaoID=createVAO();
        bindIndicesBuffer(indices);
        storeDatatInAttributeList(0,positions,3);
        storeDatatInAttributeList(1,textureCoords,2);
        storeDatatInAttributeList(2,normals,3);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals, int[] indices, float[] tangents)
    {
        int vaoID=createVAO();
        bindIndicesBuffer(indices);
        storeDatatInAttributeList(0,positions,3);
        storeDatatInAttributeList(1,textureCoords,2);
        storeDatatInAttributeList(2,normals,3);
        storeDatatInAttributeList(3,tangents,3);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public RawModel loadtoVAO(float[] positions,int dimension)
    {
        int vao_id = createVAO();
        this.storeDatatInAttributeList(0,positions,dimension);
        unbindVAO();
        return new RawModel(vao_id,positions.length/dimension);
    }

    public int loadTexture(String fileName)
    {
        Texture texture=null;
        try
        {
            texture=TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS,0);
            if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic)
            {
                float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,amount);
            }else
            {
                System.out.println("Anisotropic don´t work");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        int textureID=texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }
    public void cleanUp()
    {
        for (int vao:vaos)
        {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo:vbos)
        {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture:textures)
        {
            GL11.glDeleteTextures(texture);
        }
    }
    private int createVAO()
    {
        int vaoID= GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    private void storeDatatInAttributeList(int attributeNumber,float[] data,int coordinateSize)
    {
        int vboID= GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer=storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }
    private void unbindVAO()
    {
        GL30.glBindVertexArray(0);
    }
    private void bindIndicesBuffer(int[] indices)
    {
        int vboID=GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);
        IntBuffer buffer=storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }
    private IntBuffer storeDataInIntBuffer(int[] data)
    {
        IntBuffer buffer=BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    private FloatBuffer storeDataInFloatBuffer(float[] data)
    {
        FloatBuffer buffer= BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public int loadCubeMap(String[] Texture_Files)
    {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,texID);

        for (int i = 0; i < Texture_Files.length; i++)
        {
            TextureData data = decodeTextureFile("res/"+Texture_Files[i]+".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i,0,GL11.GL_RGBA,data.getWidth(),data.getHeight(),0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR);
        textures.add(texID);
        return texID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }
}
