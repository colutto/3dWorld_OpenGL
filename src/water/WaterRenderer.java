package water;

import java.util.List;

import Models.RawModel;
import Render.DisplayManager;
import Render.Loader;
import entities.light;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import org.newdawn.slick.opengl.Texture;
import toolbox.Maths;
import entities.Camera;

public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader = new WaterShader();
	private WaterFrameBuffers buffer;
	private final String DUDVMAP = "waterDUDV";
	private final String normalMap = "matchingNormalMap";
	private int textureDUDVMap;
	private int textureNormalMap;
	private static final float WAVESPEED = 0.03f;
	private float moveFactor = 0;

	public WaterRenderer(Loader loader, Matrix4f projectionMatrix,WaterFrameBuffers buffer) {
		this.buffer = buffer;
		textureDUDVMap = loader.loadTexture(DUDVMAP);
		textureNormalMap = loader.loadTexture(normalMap);
		shader.start();
		shader.loadTextures();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera,light sun) {
		prepareRender(camera,sun);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTranformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, light sun){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVESPEED * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadWaterSpeed(moveFactor);
		shader.loadLight(sun);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,buffer.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,buffer.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureDUDVMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureNormalMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,buffer.getRefractionDepthTexture());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadtoVAO(vertices,2);
	}

}
