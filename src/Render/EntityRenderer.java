package Render;

import Models.RawModel;
import Models.TexuredModel;
import entities.Camera;
import entities.Entity;
import entities.light;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import shaders.EntityShader;
import toolbox.Maths;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Stefan on 14.11.2015.
 */
public class EntityRenderer
{
    private EntityShader shader;
    public EntityRenderer(Matrix4f projectionMatrix, EntityShader shader)
    {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    public void prepare(ArrayList<light> sun, Camera camera, float r, float g, float b,Vector4f plane)
    {
        shader.start();
        shader.loadlightPosition(sun);
        shader.loadViewMatrix(camera);
        shader.loadskyColour(r,g,b);
        shader.loadPlane(plane);

    }
    public void render(Map<TexuredModel,List<entities.Entity>> entities)
    {
        for (TexuredModel model:entities.keySet())
        {
            prepareTexturedModel(model);
            List<entities.Entity> batch = entities.get(model);
            for (entities.Entity entity:batch)
            {
                entity.increaseRotation(0,0,0);
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES,model.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
            }
        }
        unbindTexturedModel();
    }
    private void prepareTexturedModel(TexuredModel model)
    {
        RawModel rawModel=model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        shader.loadShine(model.getShineDamper(),model.getReflectivity());
        shader.loadfakeLighting(model.isFakeLighting());
        shader.loadNumberofRows(model.getNumberOfRows());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,model.getTexture().getID());
        if (model.isFaceCulling())
        {
            disableFaceCulling();
        }
    }
    private void unbindTexturedModel()
    {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        enableFaceCulling();
        shader.stop();
    }
    private void prepareInstance(entities.Entity entity)
    {
        Matrix4f transformationMatrix= Maths.createTranformationMatrix(entity.getPosition(),entity.getRotx(),entity.getRoty(),entity.getRotz(),entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadTextureOffset(entity.getTextureXOffset(),entity.getTextureYOffset());
    }
    private void enableFaceCulling()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }
    private void disableFaceCulling()
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }
}
