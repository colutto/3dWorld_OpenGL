package entities;

import Models.RawModel;
import Models.TexuredModel;
import Render.DisplayManager;
import Render.Loader;
import Terrain.Terrrain;
import objConverter.ModelData;
import objConverter.OBJloader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import textures.ModelTexture;

/**
 * Created by Stefan on 18.01.2016.
 */
public class Player extends Entity
{
    private final float MOVESPEED = 20;
    private final float TURNSPEED = 160;
    public static final float GRAVITY = -50;
    private final float JUMPPOWER = 25;

    private boolean Jumping = false;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;


    public Player(TexuredModel model, Vector3f position,float rotx,float roty,float rotz,float scale)
    {
        super(model,position,rotx,roty,rotz,scale);

    }
    public void Moving(Terrrain terrain)
    {
        checkInputs();
        super.increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSeconds(),0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(super.getRoty()) + 80));
        float dz = (float)(distance * Math.cos(Math.toRadians(super.getRoty()) + 80));
        super.increasePosition(dx,0,dz);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0,upwardsSpeed * DisplayManager.getFrameTimeSeconds(),0);
        if (super.getPosition().y<terrain.getHeightofTerrain(getPosition().getX(),getPosition().getZ()))
        {
            upwardsSpeed = 0;
            super.getPosition().y = terrain.getHeightofTerrain(getPosition().getX(),getPosition().getZ());
            Jumping = false;
        }
    }
    private void checkInputs()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            this.currentSpeed = MOVESPEED;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            this.currentSpeed = -MOVESPEED;
        }else this.currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            this.currentTurnSpeed = TURNSPEED;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            this.currentTurnSpeed = -TURNSPEED;
        }else this.currentTurnSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)&&!Jumping)
        {
            Jumping = true;
            Jump();
        }
    }
    private void Jump()
    {
        upwardsSpeed = JUMPPOWER;
    }


}
