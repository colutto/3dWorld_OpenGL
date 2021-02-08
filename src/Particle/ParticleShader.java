package Particle;

import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/Particle/ParticleVShader.txt";
	private static final String FRAGMENT_FILE = "src/Particle/particleFShader.txt";

	private int location_projectionMatrix;
	private int location_modelviewMatrix;
	private int location_texturedInfo;
	private int location_texOffset;



	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_modelviewMatrix = super.getUniformLocation("modelViewMatrix");
		location_texOffset = super.getUniformLocation("texOffset");
		location_texturedInfo = super.getUniformLocation("texturesInfo");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}


	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

	protected void loadViewMatrix(Matrix4f viewMatrix)
	{
		super.loadMatrix(location_modelviewMatrix,viewMatrix);
	}
	protected void loadTexturedInfo(float blend,float NumberofRows)
	{
		super.loadVector2f(location_texturedInfo,new Vector2f(NumberofRows,blend));
	}
	protected void loadTexOffset(Vector2f offset1,Vector2f offset2)
	{
		super.loadVector4f(location_texOffset,new Vector4f(offset1.x,offset1.y,offset2.x,offset2.y));
	}


}
