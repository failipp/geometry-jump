package jump.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import jump.InputController;
import jump.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 1000;
    private static World instance;

    public final Jumper jumper;

    public double rotation;

    private double bottom = 0;

    private double prev_y = 0;

    public static final List<Platform> platforms = new ArrayList<>();

    Random rand = new Random();
    public boolean end;
    private boolean displayScore = true;

    private World() {
        jumper = new Jumper();
        jumper.place(WIDTH / 2., 100);

        platforms.add(new Platform(WIDTH / 2., 20, WIDTH));
//        platforms.add(new Platform(WIDTH / 2., 300, 70));
//        platforms.add(new Platform(WIDTH / 2. - 100, 400, 70));
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    public void update(double deltaTime) {
        if (end && !jumper.isFalling && InputController.getInstance().getCurrentKeys().contains(KeyCode.SPACE)) {
            var bottomPlatform = platforms.get(0);
            platforms.clear();
            platforms.add(bottomPlatform);
            bottom = 0;
            jumper.stop = false;
            end = false;
        }

        generatePlatforms();

        jumper.update(deltaTime);

        if (InputController.getInstance().getCurrentKeys().contains(KeyCode.D)) {
            if (rotation > -5)
                rotation -= 0.04;
        } else if (InputController.getInstance().getCurrentKeys().contains(KeyCode.A)) {
            if (rotation < 5)
                rotation += 0.04;
        } else {
            if (rotation < 0)
                rotation += 0.04;
            else if (rotation > 0)
                rotation -= 0.04;
            if (rotation < 0.04 && rotation > -0.04)
                rotation = 0;
        }

        updateWorldView();

        if (jumper.y < bottom) {
            end = true;
            jumper.stop();
        }
    }

    public void draw(GraphicsContext gc) {

        gc.save();

        Affine t = new Affine();
        t.appendTranslation((Main.CANVAS_WIDTH - WIDTH) / 2., Main.CANVAS_HEIGHT);
        t.appendScale(1, -1);
        t.appendRotation(rotation, WIDTH / 2., HEIGHT / 4.);
//        t.appendShear(-rotation * 0.03, 0,WIDTH / 2., HEIGHT / 4.);
        t.appendTranslation(0, -bottom);

        if (end) {
            double scale = HEIGHT / ((bottom + HEIGHT) - jumper.y);
            t.appendScale(scale, scale, WIDTH / 2., bottom + HEIGHT);
        }

        gc.transform(t);

        // draw world background
        gc.setFill(Color.ANTIQUEWHITE);
        gc.fillRect(0, -400, WIDTH, bottom + HEIGHT + 800);

        jumper.draw(gc);

        for (Platform p : platforms) {
            p.draw(gc);
        }

        gc.restore();

        if (displayScore) {
            gc.setFill(Color.ANTIQUEWHITE);
            gc.setFont(new Font("sans", 30));
            gc.fillText("Score: " + Math.round(bottom), 50, 75);
        }

    }

    private void updateWorldView() {
        if (jumper.y > bottom + HEIGHT / 2.) {
            bottom = jumper.y - HEIGHT / 2.;
        }
    }

    private void generatePlatforms() {
        double topy;

        while ((topy = platforms.get(platforms.size() - 1).y) < bottom + HEIGHT) {
            int width = rand.nextInt(40) + 60;

            double newx = rand.nextInt(WIDTH - width) + width / 2.;

            double miny = topy + 20;
            double newy = rand.nextInt(330) + miny;

            boolean boost = false;
            var superBoost = false;
            if (rand.nextDouble() < 0.1) {
                boost = true;
                if (rand.nextDouble() < 0.06) {
                    superBoost = true;
                }
            }


            platforms.add(new Platform(newx, newy, width, boost, superBoost));
        }
    }

    public void displayScore() {
        displayScore = true;
    }
}
