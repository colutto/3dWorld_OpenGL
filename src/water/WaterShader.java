package water;

import entities.light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/water/waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_RefrectionTexture;
	private int location_RefractionTexture;
	private int location_dudvMap;
	private int location_waterSpeed;
	private int location_CameraPos;
	private int location_texturenormalMap;
	private int location_lightColour;
	private int location_lightPosition;
	private int location_depthMap;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_RefrectionTexture = getUniformLocation("refrectionTexture");
		location_RefractionTexture = getUniformLocation("refractionTexture");
		location_dudvMap = getUniformLocation("dudvMap");
		location_waterSpeed = getUniformLocation("waterSpeed");
		location_CameraPos = getUniformLocation("cameraPosition");
		location_lightColour = getUniformLocation("lightColour");
		location_lightPosition = getUniformLocation("lightPosition");
		location_texturenormalMap = getUniformLocation("normalMap");
		location_depthMap = getUniformLocation("depthMap");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_CameraPos,camera.getPosotion());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

	public void loadTextures()
	{
		super.loadInt(location_RefrectionTexture,0);
		super.loadInt(location_RefractionTexture,1);
		super.loadInt(location_dudvMap,2);
		super.loadInt(location_texturenormalMap,3);
		super.loadInt(location_depthMap,4);
	}
	public void loadWaterSpeed(float waterSpeed)
	{
		super.loadFloat(location_waterSpeed,waterSpeed);
	}

	public void loadLight(light sun)
	{
		super.loadVector(location_lightColour,sun.getColour());
		super.loadVector(location_lightPosition,sun.getPosition());
	}
}
