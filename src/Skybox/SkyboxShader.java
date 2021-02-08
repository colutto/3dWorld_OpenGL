package Skybox;

import Render.DisplayManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;

import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_FogColor;
	private int location_BlendFactor;
	private int location_cubeMap1;
	private int location_cubeMap2;

	private final float ROTATION = 1f;
	private float rotation;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;

		rotation += ROTATION * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float)Math.toRadians(rotation),new Vector3f(0,1,0),matrix,matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	public void loadFogColor(float r,float g,float b)
	{
		super.loadVector(location_FogColor,new Vector3f(r,g,b));
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_FogColor = super.getUniformLocation("FogColor");
		location_BlendFactor = super.getUniformLocation("blendFactor");
		location_cubeMap1 = super.getUniformLocation("cubeMap1");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadcubeMap()
	{
		super.loadInt(location_cubeMap1,0);
		super.loadInt(location_cubeMap2,1);
	}
	public void loadblendFactor(float blendFactor)
	{
		super.loadFloat(location_BlendFactor,blendFactor);
	}
}
