package edu.cnm.deepdive.rps.controller;

import edu.cnm.deepdive.rps.model.Terrain;
import edu.cnm.deepdive.rps.view.TerrainView;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class Controller {

  private static final int STEPS_PER_ITERATION = 6667;
  private static final int MAX_SLEEP_PER_ITERATION = 10;
  private static final int MIX_THRESHOLD = 10;
  private static final int PAIRS_TO_MIX = 5;

  @FXML
  private ResourceBundle resources;
  @FXML
  private CheckBox fitCheckbox;
  @FXML
  private Text iterationsLabel;
  @FXML
  private ScrollPane viewScroller;
  @FXML
  private TerrainView terrainView;
  @FXML
  private Slider speedSlider;
  @FXML
  private Slider mixingSlider;
  @FXML
  private Button reset;
  @FXML
  private Button stop;
  @FXML
  private Button start;

  private double defaultViewHeight;
  private double defaultViewWidth;
  private String iterationFormat;
  private Terrain terrain;
  private boolean running = false;
  private Runner runner = null;
  private final Object lock = new Object();
  private Timer timer;

  @FXML
  private void initialize() {
    terrain = new Terrain(100, new Random());
    terrainView.setSource(terrain.getCells());
    defaultViewWidth = terrainView.getWidth();
    defaultViewHeight = terrainView.getHeight();
    iterationFormat = iterationsLabel.getText();
    speedSlider.setMax(MAX_SLEEP_PER_ITERATION);
    draw();
    timer = new Timer();
  }

  @FXML
  private void fitView(ActionEvent actionEvent) {
    if (fitCheckbox.isSelected()) {
      terrainView.setWidth(viewScroller.getWidth() - 2);
      terrainView.setHeight(viewScroller.getHeight() - 2);
    } else {
      terrainView.setWidth(defaultViewWidth);
      terrainView.setHeight(defaultViewHeight);
    }
    if (!running) {
      draw();
    }
  }

  @FXML
  private void start(ActionEvent actionEvent) {
    running = true;
    start.setDisable(true);
    stop.setDisable(false);
    reset.setDisable(true);
    timer.start();
    runner = new Runner();
    runner.start();
  }

  @FXML
  public void stop(ActionEvent actionEvent) {
    running = false;
    runner = null;
    start.setDisable(false);
    stop.setDisable(true);
    reset.setDisable(false);
    timer.stop();
  }

  @FXML
  private void reset(ActionEvent actionEvent) {
    terrain.reset();
    draw();
  }

  private void draw() {
    synchronized (lock) {
      terrainView.draw();
      iterationsLabel.setText(String.format(iterationFormat, terrain.getIterations()));
    }
  }

  private class Timer extends AnimationTimer {

    @Override
    public void handle(long now) {
      draw();
    }

  }

  private class Runner extends Thread {

    @Override
    public void run() {
      int mixAccumulator = 0;
      while (running) {
        int mixLevel = (int) mixingSlider.getValue();
        int sleep = (int) (1 + MAX_SLEEP_PER_ITERATION - speedSlider.getValue());
        synchronized (lock) {
          terrain.iterate(STEPS_PER_ITERATION);
          mixAccumulator += mixLevel;
          if (mixAccumulator >= MIX_THRESHOLD) {
            terrain.mix(PAIRS_TO_MIX);
            mixAccumulator = 0;
          }
        }
        try {
          Thread.sleep(sleep);
        } catch (InterruptedException e) {
          // DO NOTHING.
        }
      }
    }

  }

}
