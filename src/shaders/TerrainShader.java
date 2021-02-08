package shaders;

import entities.Camera;
import entities.light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

import java.util.ArrayList;

/**
 * Created by Stefan on 13.01.2016.
 */
public class TerrainShader extends ShaderProgram
{
    private static final String VERTEX_FILE="src/shaders/vertexShaderTerrain.txt";
    private static final String FRAGMENT_FILE="src/shaders/fragmentShaderTerrain.txt";

    private int location_projectionMatrix;
    private int locaation_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_transformationMatrix;
    private int location_ShineDamper;
    private int location_Reflectivity;
    private int location_SkyColour;
    private int location_backgroundColour;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMap;
    private final int MAXLIGHTS = 4;
    private int location_attenuation[];
    private int location_Plane;
    private int location_ShadowMapSpace;
    private int location_ShadowMap;

    public TerrainShader()
    {
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_projectionMatrix=super.getUniformLocation("projectionMatrix");
        locaation_viewMatrix=super.getUniformLocation("viewMatrix");
        location_transformationMatrix=super.getUniformLocation("transformationMatrix");
        location_ShineDamper=super.getUniformLocation("shineDamper");
        location_Reflectivity=super.getUniformLocation("reflectivity");
        location_SkyColour = super.getUniformLocation("skyColour");
        location_backgroundColour = super.getUniformLocation("backgroundTexture");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");
        location_Plane = super.getUniformLocation("plane");
        location_ShadowMapSpace = super.getUniformLocation("toShaowMapSpace");
        location_ShadowMap = super.getUniformLocation("shadowMap");

        location_lightPosition = new int[MAXLIGHTS];
        location_lightColour = new int[MAXLIGHTS];
        location_attenuation = new int[MAXLIGHTS];
        for (int i = 0; i < MAXLIGHTS; i++)
        {
            location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
            location_lightColour[i] = super.getUniformLocation("lightColour["+i+"]");
            location_attenuation[i] = super.getUniformLocation("attenuation["+i+"]");
        }
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2,"normal");
    }

    public void loadProjectionMatrix(Matrix4f projection)
    {
        super.loadMatrix(location_projectionMatrix,projection);
    }
    public void loadViewMatrix(Camera camera)
    {
        Matrix4f viewMatrix= Maths.createViewMatrix(camera);
        super.loadMatrix(locaation_viewMatrix,viewMatrix);
    }
    public void loadPlane(Vector4f plane)
    {
        super.loadVector4f(location_Plane,plane);
    }

    public void loadlightPosition(ArrayList<light> lightPosition)
    {
        for (int i = 0; i < MAXLIGHTS; i++)
        {
            if (i<lightPosition.size())
            {
                super.loadVector(location_lightPosition[i], lightPosition.get(i).getPosition());
                super.loadVector(location_lightColour[i], lightPosition.get(i).getColour());
                super.loadVector(location_attenuation[i],lightPosition.get(i).getAttenuation());
            }else
            {
                super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
                super.loadVector(location_lightColour[i], new Vector3f(0,0,0));
                super.loadVector(location_attenuation[i],new Vector3f(1,0,0));
            }
        }
    }
    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix,matrix);
    }

    public void loadShine(float ShineDamper,float Reflectivity)
    {
        super.loadFloat(location_ShineDamper,ShineDamper);
        super.loadFloat(location_Reflectivity,Reflectivity);
    }
    public void loadskyColour(float r,float g,float b)
    {
        super.loadVector(location_SkyColour,new Vector3f(r,g,b));
    }
    public void loadTexturesUnits()
    {
        super.loadInt(location_backgroundColour,0);
        super.loadInt(location_rTexture,1);
        super.loadInt(location_gTexture,2);
        super.loadInt(location_bTexture,3);
        super.loadInt(location_blendMap,4);
        super.loadInt(location_ShadowMap,5);
    }

    public void loadtoShadowMapSpace(Matrix4f matrix)
    {
        super.loadMatrix(location_ShadowMapSpace,matrix);
    }
}
