package Render;

import Models.RawModel;
import Models.TexuredModel;
import Terrain.Terrrain;
import Terrain.TexturePack;
import entities.Camera;
import entities.light;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.TerrainShader;
import toolbox.Maths;

import java.util.ArrayList;

/**
 * Created by Stefan on 13.01.2016.
 */
public class TerrainRenderer
{
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
    {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadTexturesUnits();
        shader.stop();
    }

    public void render( Terrrain terrain,Matrix4f toShadowSpace)
    {
        shader.loadtoShadowMapSpace(toShadowSpace);
        prepareTexturedModel(terrain);
        prepareInstance(terrain);
        GL11.glDrawElements(GL11.GL_TRIANGLES,terrain.getModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
        unbindTexturedModel();
    }
    private void prepareTexturedModel(Terrrain terrain)
    {
        RawModel model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
    }
    private void unbindTexturedModel()
    {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
    public void prepare(Camera camera, ArrayList<light> sun, float r, float g, float b,Vector4f plane)
    {
        shader.start();
        shader.loadlightPosition(sun);
        shader.loadViewMatrix(camera);
        shader.loadShine(0,1);
        shader.loadskyColour(r,g,b);
        shader.loadPlane(plane);
    }

    private void prepareInstance(Terrrain terrain)
    {
        Matrix4f transformationMatrix= Maths.createTranformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()),0,0,0,1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
    private void bindTextures(Terrrain terrain)
    {
        TexturePack texturePack = terrain.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texturePack.getBackgroundTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texturePack.getrTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texturePack.getgTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texturePack.getbTexture().getID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texturePack.getBlendMap().getID());
    }
}
