package Skybox;

import Models.RawModel;
import Render.DisplayManager;
import Render.Loader;
import entities.Camera;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Stefan on 08.02.2016.
 */
public class SkyboxRenderer
{
    private RawModel model;
    private int textures;
    private int nightTexture;
    private SkyboxShader shader;
    private static final float SIZE = 500f;
    private float time;
    private float blendFactor = 0.5f;
    private final String[] PICTURES = {"right","left","top","bottom","back","front"};
    private final String[] PICTURES2 = {"nightRight","nightLeft","nightTop","nightBottom","nightBack","nightFront"};
    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix)
    {
        shader = new SkyboxShader();
        model = loader.loadtoVAO(VERTICES,3);
        textures = loader.loadCubeMap(PICTURES);
        nightTexture = loader.loadCubeMap(PICTURES2);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadcubeMap();
        shader.stop();

    }

    public void render(Camera camera,float r,float g,float b)
    {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(r,g,b);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES,0,model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
    private void bindTextures(){
        time += DisplayManager.getFrameTimeSeconds() * 1000;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if(time >= 0 && time < 5000){
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = (time - 0)/(5000 - 0);
        }else if(time >= 5000 && time < 8000){
            texture1 = nightTexture;
            texture2 = textures;
            blendFactor = (time - 5000)/(8000 - 5000);
        }else if(time >= 8000 && time < 21000){
            texture1 = textures;
            texture2 = textures;
            blendFactor = (time - 8000)/(21000 - 8000);
        }else{
            texture1 = textures;
            texture2 = nightTexture;
            blendFactor = (time - 21000)/(24000 - 21000);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadblendFactor(blendFactor);
    }
}
