package application;

import com.sun.javafx.geom.Vec2d;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private Canvas canvas = new Canvas(600, 400);

	private double frameRate = 1.0 / 40.0; // Seconds
	private double frameDelay = frameRate * 1000; // ms

	private int radius = 15;
	private double cd = 0.47;
	private double rho = 1.22;
	private double A = Math.PI * (radius * radius) / (10000);
	private double gravity = 9.81;

	// temp
	private double angle = 0.0;
	private double speed = 0.0;

	// kosz
	private Rectangle[] rect = { new Rectangle(550, 200, 5, 25), new Rectangle(500, 200, 5, 25) };

	@Override
	public void start(Stage primaryStage) {
		try {

			// buttons
			HBox buttons = new HBox();
			buttons.setPadding(new Insets(0, 50, 10, 10));
			buttons.setSpacing(10);
			Button startBtn = new Button("Start");
			Button resetBtn = new Button("Reset");
			buttons.getChildren().addAll(startBtn, resetBtn);

			// slider
			Label speedText = new Label("Speed");
			Label angleText = new Label("Angle");
			final Slider speedSlider = new Slider();
			final Slider angleSlider = new Slider(0, 90, 1);
			angleSlider.setShowTickMarks(true);
			angleSlider.setShowTickLabels(true);
			angleSlider.setMajorTickUnit(1f);
			angleSlider.setBlockIncrement(1f);
			angleSlider.setMaxWidth(Double.MAX_VALUE);

			// Listener
			speedSlider.valueProperty().addListener(new ChangeListener() {
				@Override
				public void changed(ObservableValue observable, Object oldValue, Object newValue) {
					speedText.textProperty().setValue(String.valueOf((int) speedSlider.getValue()));
					speed = speedSlider.getValue();
				}
			});

			angleSlider.valueProperty().addListener(new ChangeListener() {
				@Override
				public void changed(ObservableValue observable, Object oldValue, Object newValue) {
					angleText.textProperty().setValue(String.valueOf((double) angleSlider.getValue()));
					angle = angleSlider.getValue();
				}
			});

			GraphicsContext gc = canvas.getGraphicsContext2D();
			Sprite sp = new Sprite(new Vec2d(radius / 2, canvas.getHeight() - 20), 0.1, radius);

			System.out.println(frameRate);
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(frameDelay), ae -> {

				// Clear the canvas
				gc.setFill(new Color(0.85, 0.85, 1.0, 1.0));
				gc.fillRect(0, 0, 600, 400);

				for (Rectangle c : rect) {
					Bounce b = Collision.bounce(sp, c);
					if (b.bounce) {
						System.out.println("colision" + b.x);

						double normal_len = b.x * sp.getVelocity().x + b.y * sp.getVelocity().y;
						Vec2d normal = new Vec2d(b.x * normal_len, b.y * normal_len);
						sp.getVelocity().x = sp.getVelocity().x - 2 * normal.x;
						sp.getVelocity().y = sp.getVelocity().y - 2 * normal.y;
					}
				}
				/* Fizyka */
				double Fx = -0.5 * cd * A * rho * sp.getVelocity().x * sp.getVelocity().x * sp.getVelocity().x
						/ Math.abs(sp.getVelocity().x);
				double Fy = -0.5 * cd * A * rho * sp.getVelocity().y * sp.getVelocity().y * sp.getVelocity().y
						/ Math.abs(sp.getVelocity().y);

				Fx = (Double.isNaN(Fx) ? 0 : Fx);
				Fy = (Double.isNaN(Fy) ? 0 : Fy);

				/* przyspieszenie */

				Double ax = Fx / sp.getMass();
				Double ay = gravity + (Fy / sp.getMass());

				sp.getVelocity().x += ax * frameRate;
				sp.getVelocity().y += ay * frameRate;

				sp.update(frameRate);

				// Handle collisions
				if (sp.getPosition().y > canvas.getHeight() - radius) {
					sp.getVelocity().y *= sp.getRestitution();
					sp.getPosition().y = canvas.getHeight() - radius;
				}
				if (sp.getPosition().x > canvas.getWidth() - radius) {
					sp.getVelocity().x *= sp.getRestitution();
					sp.getPosition().x = canvas.getWidth() - radius;
				}
				if (sp.getPosition().x < radius) {
					sp.getVelocity().x *= sp.getRestitution();
					sp.getPosition().x = radius;
				}

				/*
				 * if (Collision.collisionCirlceRect(sp, rect[0])) {
				 * System.out.println("colision"); sp.setColor(Color.AQUA); }
				 * else {sp.setColor(Color.BEIGE);}
				 */

				sp.render(gc);

				for (Rectangle r : rect) {
					gc.setFill(Color.BLACK);
					gc.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				}

				gc.save();
				gc.restore();
			}));
			timeline.setAutoReverse(true);
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();

			startBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					System.out.println(speed);
					System.out.println(angle);
					sp.getVelocity().x = Math.cos(angle * Math.PI / 180.0) * speed;
					sp.getVelocity().y = Math.sin(angle * Math.PI / 180.0) * speed;

				}
			});

			resetBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					Vec2d p = new Vec2d(radius / 2, canvas.getHeight());
					sp.setPosition(p);
					sp.getVelocity().x = 0;
					sp.getVelocity().y = 0;

				}
			});

			VBox v = new VBox();
			v.setPadding(new Insets(0, 100, 0, 10));
			v.setSpacing(0);
			v.getChildren().addAll(speedText, speedSlider, angleText, angleSlider);

			HBox h = new HBox();
			h.getChildren().addAll(buttons, v);
			h.setPadding(new Insets(5, 12, 10, 52));
			h.setSpacing(5);
			h.setStyle("-fx-background-color: #336699;");
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 600, 480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			root.setTop(canvas);
			root.setBottom(h);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
