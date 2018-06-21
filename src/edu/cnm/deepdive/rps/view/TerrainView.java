package edu.cnm.deepdive.rps.view;

import edu.cnm.deepdive.rps.model.Breed;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainView extends Canvas {

  private static final double MAX_HUE = 360;

  private static final Color[] BREED_COLORS = new Color[Breed.values().length];

  private Breed[][] source;

  static {
    for (int i = 0; i < Breed.values().length; i++) {
      BREED_COLORS[i] = Color.hsb(i * MAX_HUE / BREED_COLORS.length, 1, 0.9);
    }
  }

  public void draw() {
    if (source != null) {
      GraphicsContext context = getGraphicsContext2D();
      double cellSize = Math.min(getHeight() / source.length, getWidth() / source[0].length);
      context.clearRect(0, 0, getWidth(), getHeight());
      for (int i = 0; i < source.length; i++) {
        for (int j = 0; j < source[i].length; j++) {
          context.setFill(BREED_COLORS[source[i][j].ordinal()]);
          context.fillOval(j * cellSize, i * cellSize, cellSize, cellSize);
        }
      }
    }
  }

  public void setSource(Breed[][] source) {
    this.source = source;
  }

}
