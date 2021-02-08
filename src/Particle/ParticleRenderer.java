package Particle;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import Models.RawModel;
import Render.Loader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import toolbox.Maths;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};


	private RawModel quad;
	private ParticleShader shader;
	
	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix)
	{
		quad = loader.loadtoVAO(VERTICES,2);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	protected void render(Map<ParticleTexture,List<Particle>> MapParticles, Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		for (ParticleTexture texture:MapParticles.keySet())
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture.getTextureID());
			List<Particle> particleList = MapParticles.get(texture);
			for (Particle particle : particleList) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
				shader.loadTexOffset(particle.getOffset1(),particle.getOffset2());
				shader.loadTexturedInfo(particle.getBlendFactor(),particle.getTexture().getNumberofRows());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,0,quad.getVertexCount());
			}
		}
		finishRendering();
	}

	protected void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare()
	{
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering()
	{
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();

	}
	private void updateModelViewMatrix(Vector3f position,float rotation,float scale,Matrix4f viewMatrix)
	{
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(position,modelMatrix,modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix,modelMatrix,null);
		Matrix4f.rotate((float)Math.toRadians(rotation),new Vector3f(0,0,1),modelViewMatrix,modelViewMatrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale),modelViewMatrix,modelViewMatrix);
		shader.loadViewMatrix(modelViewMatrix);
	}

}
