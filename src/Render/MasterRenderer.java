package Render;

import Models.TexuredModel;
import Skybox.SkyboxRenderer;
import Terrain.Terrrain;
import entities.Camera;
import entities.Entity;
import entities.Player;
import entities.light;
import normalMappingRenderer.NormalMappingRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import shaders.EntityShader;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stefan on 13.01.2016.
 */
public class MasterRenderer
{
    private Map<TexuredModel,List<Entity>> entities = new HashMap<TexuredModel, List<Entity>>();
    private Map<TexuredModel,List<Entity>> entitiesNormalMapping = new HashMap<TexuredModel, List<Entity>>();
    private EntityShader entityShader = new EntityShader();
    private TerrainShader terrainShader = new TerrainShader();
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private NormalMappingRenderer normalMappingRenderer;
    private Matrix4f projectionMatrix;
    public static final float FOV=70f;
    public static final float NEAR_PLANE=0.1f;
    private static final float FAR_PLANE=1000f;
    private SkyboxRenderer SkyboxRenderer;

    public static final float R = 0.5444f;
    public static final float G = 0.62f;
    public static final float B = 0.69f;

    private ShadowMapMasterRenderer shadowMapRenderer;

    public MasterRenderer(Loader loader,Camera camera)
    {
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(projectionMatrix, entityShader);
        terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
        SkyboxRenderer = new SkyboxRenderer(loader,projectionMatrix);
        normalMappingRenderer = new NormalMappingRenderer(projectionMatrix);
        this.shadowMapRenderer = new ShadowMapMasterRenderer(camera);
    }
    public void render(ArrayList<light> sun, Camera camera, Terrrain terrain,Vector4f plane)
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(R,G,B,1);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,shadowMapRenderer.getShadowMap());

        entityRenderer.prepare(sun,camera,R,G,B,plane);
        entityRenderer.render(entities);
        terrainRenderer.prepare(camera,sun,R,G,B,plane);
        terrainRenderer.render(terrain,shadowMapRenderer.getToShadowMapSpaceMatrix());
        normalMappingRenderer.render(entitiesNormalMapping,new Vector4f(0,0,0,0),sun,camera);
        this.SkyboxRenderer.render(camera,R,G,B);
        entities.clear();
        entitiesNormalMapping.clear();

    }
    private void createProjectionMatrix()
    {
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
    public void cleanUp()
    {
        entityShader.cleanUp();
        terrainShader.cleanUp();
        normalMappingRenderer.cleanUp();
        shadowMapRenderer.cleanUp();
    }
    public void loadEntity(Entity entity)
    {
        TexuredModel model = entity.getModel();
        if (entities.get(model)!=null)
        {
            List<Entity> batch = entities.get(model);
            batch.add(entity);
        }else
        {
            List<Entity> batch = new ArrayList<Entity>();
            batch.add(entity);
            entities.put(model,batch);
        }
    }

    public void loadNormalEntity(Entity entity)
    {
        TexuredModel model = entity.getModel();
        if (entitiesNormalMapping.get(model)!=null)
        {
            List<Entity> batch = entitiesNormalMapping.get(model);
            batch.add(entity);
        }else
        {
            List<Entity> batch = new ArrayList<Entity>();
            batch.add(entity);
            entitiesNormalMapping.put(model,batch);
        }
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static void enableCulling()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }
    public static void diableCulling()
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void PrepareForRendering(List<Entity> texturedTreeList, List<Entity> texturedFernList, List<Entity> texturedGrassList, Entity LampEntity, Entity normalBarrel, ArrayList<light> lights, Camera camera, Terrrain terrain, Player player,Vector4f plane)
    {
        for (Entity texturedTree:texturedTreeList)
        {
            loadEntity(texturedTree);
        }
        for (Entity texturedFern1:texturedFernList)
        {
            loadEntity(texturedFern1);
        }
        for (Entity texturedGrass1:texturedGrassList)
        {
            loadEntity(texturedGrass1);
        }
        loadEntity(LampEntity);
        loadEntity(player);

        loadNormalEntity(normalBarrel);
        render(lights,camera,terrain,plane);
    }
    public int getShadowMap()
    {
        return shadowMapRenderer.getShadowMap();
    }
    public void rendershadowMap(List<Entity> texturedTreeList, List<Entity> texturedFernList, List<Entity> texturedGrassList, Entity LampEntity, Entity normalBarrel,Player player,light sun)
    {
        for (Entity texturedTree:texturedTreeList)
        {
            loadEntity(texturedTree);
        }
        for (Entity texturedFern1:texturedFernList)
        {
            loadEntity(texturedFern1);
        }
        for (Entity texturedGrass1:texturedGrassList)
        {
            loadEntity(texturedGrass1);
        }
        loadEntity(LampEntity);
        loadEntity(player);

        loadNormalEntity(normalBarrel);

        shadowMapRenderer.render(entities,sun);
        entities.clear();
    }
}
