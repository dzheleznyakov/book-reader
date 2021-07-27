package zh.op.bookreader.onboarding;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OnboardingApp extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        Label label = new Label();
        Button button = new Button();

        root.getChildren().addAll(label, button);

        button.setOnMouseClicked(me -> label.setText("Hello World"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
    }
}
