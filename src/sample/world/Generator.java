package sample.world;

import java.util.Random;

public class Generator {

    private Configuration configuration;

    public Generator(Configuration configuration) {
        this.configuration = configuration;
    }


    public World getRandomlyPopulatedWorld() {
        World world = new World(configuration.getWidth(), configuration.getHeight());
        populateWorldRandomly(world, configuration.getStartingAliveCells());
        return world;
    }

    public World getEmptyWorld() {
        return new World(configuration.getWidth(), configuration.getHeight());
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
