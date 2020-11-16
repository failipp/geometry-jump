package jump;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jump.game.Game;

public class Main extends Application {

    public static final int CANVAS_WIDTH = 1400;
    public static final int CANVAS_HEIGHT = 1000;

    public static final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var gc = canvas.getGraphicsContext2D();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(30, 28, 20), CornerRadii.EMPTY, Insets.EMPTY)));
        canvas.setFocusTraversable(true);

        primaryStage.setTitle("Geometry Jump");
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();

        new Game(gc).run();
    }
}
