package sample.world;

import java.util.Random;

public class Generator {

    private Configuration configuration;

    public Generator(Configuration configuration) {
        this.configuration = configuration;
    }

    public World getRandomlyPopulatedWorld() {
        World world = new World(configuration.getWidth(), configuration.getHeight());
        double aliveCells = world.getHeight() * world.getWidth() * (configuration.getStartingAliveCells() / 100.0);
        populateWorldRandomly(world, ((int) aliveCells));
        return world;
    }

    private void populateWorldRandomly(World world, int aliveCells) {
        Random rng = new Random();
        for (int i = 0; i < aliveCells; i++) {
            int x = rng.nextInt(world.getWidth());
            int y = rng.nextInt(world.getHeight());
            world.getCellAt(x, y).setAlive(true);
        }
    }
}
