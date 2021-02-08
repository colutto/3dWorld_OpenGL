package engine_Test;
import Gui.Logo;
import Models.TexuredModel;
import Particle.ParticleMaster;
import Particle.ParticleSystem;
import Particle.ParticleTexture;
import Render.*;
import Models.RawModel;
import Terrain.Terrrain;
import Terrain.TexturePack;
import entities.Camera;
import entities.Entity;
import entities.Player;
import entities.light;
import guiRenderer.GuiRenderer;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.ModelData;
import objConverter.OBJloader;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.util.Log;
import textures.ModelTexture;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Stefan on 14.11.2015.
 */
public class gameLoop
{
    public static void main(String[]args)
    {
        DisplayManager.DisplayOpen();
        Loader loader=new Loader();
        WaterFrameBuffers fbos = new WaterFrameBuffers();

        ModelData bunny = OBJloader.loadOBJ("bunny");
        RawModel modelBunny = loader.loadToVAO(bunny.getVertices(),bunny.getTextureCoords(),bunny.getNormals(),bunny.getIndices());
        TexuredModel texturedBunny = new TexuredModel(modelBunny, new ModelTexture(loader.loadTexture("white")));
        Player player = new Player(texturedBunny,new Vector3f(400,0,20),0,90,0,0.3f);

        Camera camera=new Camera(player);

        MasterRenderer renderer = new MasterRenderer(loader,camera);
        ParticleMaster.intit(loader,renderer.getProjectionMatrix());

        Random random = new Random();

        ModelData fern = OBJloader.loadOBJ("fern");
        RawModel modelFern = loader.loadToVAO(fern.getVertices(),fern.getTextureCoords(),fern.getNormals(),fern.getIndices());
        TexuredModel texturedFern = new TexuredModel(modelFern,new ModelTexture(loader.loadTexture("fern")));
        texturedFern.setFaceCulling(true);
        texturedFern.setNumberOfRows(4);

        ModelData grass = OBJloader.loadOBJ("grassModel");
        RawModel modelGrass = loader.loadToVAO(grass.getVertices(),grass.getTextureCoords(),grass.getNormals(),grass.getIndices());
        TexuredModel texturedGrass = new TexuredModel(modelGrass,new ModelTexture(loader.loadTexture("grassTexture")));
        texturedGrass.setFaceCulling(true);
        texturedGrass.setFakeLighting(true);


        ModelData tree = OBJloader.loadOBJ("tree");
        RawModel modelTree = loader.loadToVAO(tree.getVertices(),tree.getTextureCoords(),tree.getNormals(),tree.getIndices());
        TexuredModel texuredTree = new TexuredModel(modelTree,new ModelTexture(loader.loadTexture("tree")));

        ArrayList<light> lights = new ArrayList<light>();
        lights.add(new light(new Vector3f(4000000,2000000,2000000),new Vector3f(1f,1f,1f),new Vector3f(1,0,0)));
        lights.add(new light(new Vector3f(400,10,10),new Vector3f(0,0,0),new Vector3f(1,0.01f,0.002f)));
        lights.add(new light(new Vector3f(400,10,50),new Vector3f(0,0,0),new Vector3f(1,0.01f,0.002f)));
        lights.add(new light(new Vector3f(400,10,100),new Vector3f(10,4,8),new Vector3f(1,0.01f,0.002f)));


        Terrrain terrain = new Terrrain(0,0,loader,new TexturePack(new ModelTexture(loader.loadTexture("blendMap")),new ModelTexture(loader.loadTexture("mud")),new ModelTexture(loader.loadTexture("grassFlowers")),new ModelTexture(loader.loadTexture("path")),new ModelTexture(loader.loadTexture("grass"))));

        ModelData lamp = OBJloader.loadOBJ("lamp");
        RawModel modelLamp = loader.loadToVAO(lamp.getVertices(),lamp.getTextureCoords(),lamp.getNormals(),lamp.getIndices());
        TexuredModel texturedLamp = new TexuredModel(modelLamp,new ModelTexture(loader.loadTexture("lamp")));
        Entity LampEntitiy = new Entity(texturedLamp,new Vector3f(400,terrain.getHeightofTerrain(400,100),100),0,0,0,1);


        List<Entity> texuredTreeList = new ArrayList<Entity>();
        List<Entity> texuredFernList = new ArrayList<Entity>();
        List<Entity> texturedGrassList = new ArrayList<Entity>();

        MousePicker mouse = new MousePicker(camera,renderer.getProjectionMatrix(),terrain);

        for (int i = 0; i <1000 ; i++)
        {
            float x = random.nextFloat()*800;
            float z = random.nextFloat()*800;
            float y = terrain.getHeightofTerrain(x,z);

            float x2 = random.nextFloat()*800;
            float z2 = random.nextFloat()*800;
            float y2 = terrain.getHeightofTerrain(x2,z2);
            texuredTreeList.add(new Entity(texuredTree,new Vector3f(x,y,z),0,0,0,2));
            texuredFernList.add(new Entity(texturedFern,new Vector3f(x2,y2,z2),0,0,0,0.5f,random.nextInt(4)));
        }

        for (int i = 0; i <10000 ; i++)
        {
            float x1 = random.nextFloat() * 800;
            float z1 = random.nextFloat() * 800;
            float y1 = terrain.getHeightofTerrain(x1,z1);
            texturedGrassList.add(new Entity(texturedGrass, new Vector3f(x1, y1, z1), 0, 0, 0, 0.2f));
        }
        GuiRenderer gui_renderer = new GuiRenderer(loader);

        Logo logo = new Logo(new Vector2f(0.5f,0.5f),new Vector2f(0.25f,0.25f),loader.loadTexture("starbucks"));
        Logo logo2 = new Logo(new Vector2f(0,0.5f),new Vector2f(0.5f,0.5f),renderer.getShadowMap());
        List<Logo> listLogos = new ArrayList<Logo>();

        TexuredModel texturedBarrel = new TexuredModel(NormalMappedObjLoader.loadOBJ("barrel",loader),new ModelTexture(loader.loadTexture("barrel")));
        Entity normalMapEntity = new Entity(texturedBarrel,new Vector3f(400,10,40),0,0,0,1);
        texturedBarrel.getTexture().setNormalMapID(loader.loadTexture("barrelNormal"));

        ParticleSystem system = new ParticleSystem(new ParticleTexture(loader.loadTexture("fire"),8),500,20,0.2f,4,2);
        system.randomizeRotation();
        system.setDirection(new Vector3f(0,1,0),0.1f);
        system.setLifeError(0.1f);
        system.setSpeedError(0.4f);
        system.setScaleError(0.8f);

        ArrayList<WaterTile> waterTileList = new ArrayList<WaterTile>();
        WaterTile water = new WaterTile(400,60,2);
        waterTileList.add(water);

        WaterRenderer RenderWater = new WaterRenderer(loader,renderer.getProjectionMatrix(),fbos);

        while (!Display.isCloseRequested())
        {
            player.Moving(terrain);
            camera.move();

            system.generateParticles(player.getPosition());

            ParticleMaster.update(camera);

            renderer.rendershadowMap(texuredTreeList,texuredFernList,texturedGrassList,LampEntitiy,normalMapEntity,player,lights.get(0));

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosotion().y-water.getHeight());
            camera.getPosotion().y -= distance;
            camera.invertPitch();
            renderer.PrepareForRendering(texuredTreeList,texuredFernList,texturedGrassList,LampEntitiy,normalMapEntity,lights,camera,terrain,player,new Vector4f(0,1,0,-water.getHeight()+1f));
            camera.getPosotion().y += distance;
            camera.invertPitch();

            fbos.bindRefractionFrameBuffer();
            renderer.PrepareForRendering(texuredTreeList,texuredFernList,texturedGrassList,LampEntitiy,normalMapEntity,lights,camera,terrain,player,new Vector4f(0,-1,0,water.getHeight()+1f));
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();


            renderer.PrepareForRendering(texuredTreeList,texuredFernList,texturedGrassList,LampEntitiy,normalMapEntity,lights,camera,terrain,player,new Vector4f(0,-1,0,10000));

            RenderWater.render(waterTileList,camera,lights.get(0));

            gui_renderer.render(listLogos);

            DisplayManager.DisplayUpdate();

        }
        fbos.cleanUp();
        ParticleMaster.cleanUp();
        gui_renderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.DisplayClose();
    }
}
