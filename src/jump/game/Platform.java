package jump.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class Platform {
    public final double x;
    public final double y;

    public final double width;

    public final double height = 10;

    public final boolean boost;

    public final boolean superBoost;

    public Platform(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.boost = false;
        this.superBoost = false;
    }

    public Platform(double x, double y, double width, boolean boost, boolean superBoost) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.boost = boost;
        this.superBoost = superBoost;
    }

    public void update(double deltaTime) {

    }

    public void draw(GraphicsContext gc) {
        gc.save();

        Affine t = new Affine();

        if (World.getInstance().jumper.rotating) {
            t.appendRotation(-World.getInstance().jumper.rotation, x, y);

        }


//        t.appendRotation(-World.getInstance().rotation, x, y);

        gc.transform(t);


        if (superBoost) {
            gc.setFill(Color.CRIMSON);
        } else if (boost) {
            gc.setFill(Color.OLIVE);
        } else {
            gc.setFill(Color.NAVY);
        }
        gc.fillRoundRect(x - width / 2., y + height / 2., width, height, 8, 8);
//        gc.fillPolygon(
//                new double[]{x - width / 2., x + width / 2., x + width / 2., x - width / 2.},
//                new double[]{y + height / 2., y + height / 2., y - height / 2., y - height / 2.},
//                4
//        );

        gc.restore();
    }
}
