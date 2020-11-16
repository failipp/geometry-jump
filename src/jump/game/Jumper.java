package jump.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import jump.InputController;

public class Jumper {
    private static final double DOWN_ACCEL = 2000;
    private final InputController inputController = InputController.getInstance();
    private final double horVelocity = 600;
    public double x, y;
    public double size = 20;
    public boolean rotating;
    public boolean isFalling = false;
    public double rotation;
    public boolean stop;
    private double vertVelocity = 0;

    public void place(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void jump() {
        vertVelocity = 1200;
        isFalling = true;
        rotating = false;
        rotation = 0;
    }

    public void boost() {
        vertVelocity = 3000;
        rotating = true;
    }

    public void superBoost() {
        vertVelocity = 6000;
        rotating = true;
    }

    public void stop() {
        stop = true;
    }

    public void update(double timeDelta) {
        if (isFalling) {
            vertVelocity -= timeDelta * DOWN_ACCEL;

            if (inputController.getCurrentKeys().contains(KeyCode.D)) {
                this.x += horVelocity * timeDelta;
            }

            if (inputController.getCurrentKeys().contains(KeyCode.A)) {
                this.x -= horVelocity * timeDelta;
            }
        } else {
            vertVelocity = 0;
            if (inputController.getCurrentKeys().contains(KeyCode.SPACE) && !stop) {
                jump();
            }
        }

        if (x < 0) {
            x += World.WIDTH;
        } else if (x > World.WIDTH) {
            x -= World.WIDTH;
        }

        if (rotating) {
            rotation += 310 * timeDelta;
            if (rotation >= 360) {
                rotating = false;
                rotation = 0;
            }
        }

        this.y += vertVelocity * timeDelta;

        if (!stop) {
            collide(timeDelta);
        } else {
            if (y <= 0) {
                isFalling = false;
                rotating = false;
                y = 0;
            }
        }

    }

    public void draw(GraphicsContext gc) {

        gc.save();

        Affine t = new Affine();
        if (rotating) {
            t.appendRotation(rotation, x, y);

        }

//        t.appendRotation(-World.getInstance().rotation, x, y);

        gc.transform(t);

        gc.setFill(Color.TOMATO);
        gc.fillPolygon(
                new double[]{x - size / 3., x + size / 3., x + size / 2., x - size / 2.},
                new double[]{y + size / 2., y + size / 2., y - size / 2., y - size / 2.},
                4
        );

        gc.restore();
    }

    private void collide(double timeDelta) {
        for (Platform p : World.platforms) {
            if (doesCollideWith(p)) {
                var pre_y = this.y - vertVelocity * timeDelta;
                if (pre_y - size / 2. < p.y + p.height / 2.)
                    return;
                if (vertVelocity <= 0) {
                    if (p.superBoost) {
                        superBoost();
                    } else if (p.boost) {
                        boost();
                    } else {
                        jump();
                    }
                }
            }
        }
    }

    private boolean doesCollideWith(Platform platform) {
        return Math.abs(y - platform.y) < platform.height / 2. + size / 2.
                && Math.abs(x - platform.x) < platform.width / 2. + size / 2.;
    }
}
