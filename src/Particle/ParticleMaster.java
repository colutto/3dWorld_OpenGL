package Particle;


import Render.Loader;
import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.InsertionSort;

import java.util.*;

/**
 * Created by Stefan on 15.02.2016.
 */
public class ParticleMaster
{
    private static Map<ParticleTexture,List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
    private static ParticleRenderer renderer;

    public static void intit(Loader loader, Matrix4f projectionMatrix)
    {
        renderer = new ParticleRenderer(loader,projectionMatrix);
    }

    public static void update(Camera camera)
    {
        Iterator<Map.Entry<ParticleTexture,List<Particle>>> MapIterator = particles.entrySet().iterator();
        while (MapIterator.hasNext())
        {
            List<Particle> list = MapIterator.next().getValue();
            Iterator<Particle> iterator = list.iterator();
            while (iterator.hasNext())
            {
                Particle p = iterator.next();
                boolean stillALive = p.update(camera);
                if (!stillALive)
                {
                    iterator.remove();
                    if (list.isEmpty())
                    {
                        MapIterator.remove();
                    }
                }
            }
            InsertionSort.sortHighToLow(list);
        }
    }

    public static void renderParticles(Camera camera)
    {
        renderer.render(particles,camera);
    }
    public static void cleanUp()
    {
        renderer.cleanUp();
    }
    public static void addParticle(Particle particle)
    {
       List<Particle> list = particles.get(particle.getTexture());
        if (list == null)
        {
            list = new ArrayList<Particle>();
            particles.put(particle.getTexture(),list);
        }
        list.add(particle);
    }
}
