package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Stefan on 17.11.2015.
 */
public class Camera
{
    private Vector3f position=new Vector3f(0,5,0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;
    private Player player;
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = -90;

    public Camera(Player player)
    {
        this.player = player;
    }

    public void move()
    {
        ScrollPitch();
        calculatePitch();
        float HorizontalDistance = calculateHorizontalDistance();
        float VerticalDistance = calculateVerticalDistance();
        calculateCameraPosition(HorizontalDistance,VerticalDistance);
        this.yaw = 180 - (player.getRoty() + angleAroundPlayer);
    }

    public float getRoll()
    {
        return roll;
    }

    public float getYaw()
    {
        return yaw;
    }

    public float getPitch()
    {
        return pitch;
    }

    public Vector3f getPosotion()
    {
        return position;
    }

    private void ScrollPitch()
    {
        distanceFromPlayer -= Mouse.getDWheel() * 0.1f;
    }
    private void calculatePitch()
    {
        if (Mouse.isButtonDown(1))
        {
            pitch -= Mouse.getDY() * 0.1f;
            angleAroundPlayer -= Mouse.getDX() * 0.1f;
        }
    }
    private float calculateHorizontalDistance()
    {
        return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }
    private float calculateVerticalDistance()
    {
        return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }
    private void calculateCameraPosition(float horizDistance,float verticalDistance)
    {
        float theta = player.getRoty() + angleAroundPlayer;
        float offsetX = (float)(horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }
    public void invertPitch()
    {
        this.pitch = -pitch;
    }
}
