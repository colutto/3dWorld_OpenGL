package guiRenderer;

import Gui.Logo;
import Models.RawModel;
import Render.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 04.02.2016.
 */
public class GuiRenderer
{
    private RawModel quad;
    private GuiShader shader;

    public GuiRenderer(Loader loader)
    {
        float[] position = {-1,1,-1,-1,1,1,1,-1};
        quad = loader.loadtoVAO(position,2);
        shader = new GuiShader();
    }
    public void render(List<Logo> guis)
    {
        shader.start();

        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for (Logo gui: guis)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,gui.getTexture());
            shader.loadTransformation(Maths.createTransformationMatrix(gui.getPos(),gui.getScale()));
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,0,quad.getVertexCount());
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
    public  void cleanUp()
    {
        shader.cleanUp();
    }
}
