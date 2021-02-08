package shaders;

import entities.Camera;
import entities.light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

import java.util.ArrayList;

/**
 * Created by Stefan on 15.11.2015.
 */
public class EntityShader extends ShaderProgram
{
    private static final String VERTEX_FILE="src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE="src/shaders/fragmentShader.txt";
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int locaation_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_ShineDamper;
    private int location_Reflectivity;
    private int location_SkyColour;
    private int location_fakeLighting;
    private int location_Offset;
    private int location_NumberofRows;
    private final int MAXLIGHTS = 4;
    private int location_attenuation[];
    private int location_Plane;

    public EntityShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix=super.getUniformLocation("transformationMatrix");
        location_projectionMatrix=super.getUniformLocation("projectionMatrix");
        locaation_viewMatrix=super.getUniformLocation("viewMatrix");
        location_ShineDamper=super.getUniformLocation("shineDamper");
        location_Reflectivity=super.getUniformLocation("reflectivity");
        location_SkyColour = super.getUniformLocation("skyColour");
        location_fakeLighting = super.getUniformLocation("fakeLighting");
        location_Offset = super.getUniformLocation("Offset");
        location_NumberofRows = super.getUniformLocation("numberofRows");
        location_Plane = super.getUniformLocation("plane");

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
    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix,matrix);
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
    public void loadShine(float ShineDamper,float Reflectivity)
    {
        super.loadFloat(location_ShineDamper,ShineDamper);
        super.loadFloat(location_Reflectivity,Reflectivity);
    }
    public void loadskyColour(float r,float g,float b)
    {
        super.loadVector(location_SkyColour,new Vector3f(r,g,b));
    }
    public void loadfakeLighting(boolean fakeLighting)
    {
        super.loadBoolean(location_fakeLighting,fakeLighting);
    }
    public void loadTextureOffset(float offsetX,float offsetY)
    {

        super.loadVector2f(location_Offset,new Vector2f(offsetX,offsetY));
    }
    public void loadNumberofRows(int rows)
    {
        super.loadFloat(location_NumberofRows,rows);
    }
}
