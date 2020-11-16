package jump.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import jump.Main;

public class Game {
    private final GraphicsContext gc;

    private final World gameWorld = World.getInstance();

    public Game(GraphicsContext gc) {
        this.gc = gc;

        initWorld();
    }

    public void run() {
        new AnimationTimer() {

            long pastTick = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {
                double timeDelta = (currentNanoTime - pastTick)  / 1000000000.0;

                gc.clearRect(0, 0, Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT);

                gameWorld.update(timeDelta);
                gameWorld.draw(gc);

                pastTick = currentNanoTime;
            }
        }.start();
    }

    private void initWorld() {
    }
}
