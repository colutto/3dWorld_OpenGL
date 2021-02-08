package Particle;


import Render.DisplayManager;
import entities.Camera;
import entities.Player;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private float lifeLength;
    private float gravity;
    private float scale;
    private float rotation;
    private ParticleTexture texture;
    private float blendFactor;
    private Vector2f Offset1 = new Vector2f();
    private Vector2f Offset2 = new Vector2f();
    private float distance;

    private float elapsedTime = 0;

    public Particle(ParticleTexture texture,Vector3f position,Vector3f velocity,float gravity,float lifeLength,float rotation,float scale)
    {
        this.texture = texture;
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.position = position;
        this.rotation = rotation;
        this.velocity = velocity;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public Vector3f getPosition() {
        return position;
    }

    public ParticleTexture getTexture() {
        return texture;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public float getLifeLength() {
        return lifeLength;
    }

    public float getGravity() {
        return gravity;
    }

    public float getScale() {
        return scale;
    }

    public float getDistance() {
        return distance;
    }

    public float getBlendFactor() {
        return blendFactor;
    }

    public Vector2f getOffset1() {
        return Offset1;
    }

    public Vector2f getOffset2() {
        return Offset2;
    }

    public float getRotation() {
        return rotation;
    }

    protected boolean update(Camera camera)
    {
        velocity.y += Player.GRAVITY * gravity * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(change,position,position);
        distance = Vector3f.sub(camera.getPosotion(),position,null).lengthSquared();
        updateTextureCoordInfo();
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
    }

    private void updateTextureCoordInfo()
    {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumberofRows() * texture.getNumberofRows();
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int)Math.floor(atlasProgression);
        int index2 = index1<stageCount-1 ? index1+1 : index1;
        this.blendFactor = atlasProgression % 1;
        setTextureOffset(Offset1,index1);
        setTextureOffset(Offset2,index2);
    }
    private void setTextureOffset(Vector2f offset, int index)
    {
        int column = index % texture.getNumberofRows();
        int row = index / texture.getNumberofRows();
        offset.x = (float)column / texture.getNumberofRows();
        offset.y = (float)row / texture.getNumberofRows();
    }
}
