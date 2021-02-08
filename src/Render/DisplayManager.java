package Render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

/**
 * Created by Stefan on 14.11.2015.
 */
public class DisplayManager
{
    private final static int height=720;
    private final static int width=1250;
    private static float lastFrameTime;
    private static float delta;

    public static void DisplayOpen()
    {
        ContextAttribs attribs=new ContextAttribs(3,3)
        .withForwardCompatible(true)
        .withProfileCore(true);

        try
        {
            Display.setDisplayMode(new DisplayMode(width,height));
            Display.create(new PixelFormat().withSamples(8),attribs);
        } catch (LWJGLException e)
        {
            e.printStackTrace();
        }
        GL11.glViewport(0,0,width,height);
        lastFrameTime = getCurrentTime();
    }
    public static void DisplayUpdate()
    {
        Display.sync(60);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }
    public static float getFrameTimeSeconds()
    {
        return delta;
    }
    public static void DisplayClose()
    {
        Display.destroy();
    }

    public static long getCurrentTime()
    {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
