package jump;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;

public class InputController {
    private static InputController inputController;

    private final HashSet<KeyCode> currentKeys = new HashSet<>();

    public EventHandler<KeyEvent> onKeyPressed = event -> currentKeys.add(event.getCode());

    public EventHandler<KeyEvent> onKeyReleased = event -> currentKeys.remove(event.getCode());

    private InputController() {
        Main.canvas.setOnKeyPressed(onKeyPressed);
        Main.canvas.setOnKeyReleased(onKeyReleased);
    }

    public static InputController getInstance() {
        if (inputController == null) {
            inputController = new InputController();
        }
        return inputController;
    }

    public HashSet<KeyCode> getCurrentKeys() {
        return currentKeys;
    }
}
