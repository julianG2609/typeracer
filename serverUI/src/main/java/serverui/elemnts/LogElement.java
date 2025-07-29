package serverui.elemnts;

import java.util.logging.LogRecord;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

/** The LogElement class represents a log element. */
public class LogElement extends HBox {

  private static String infoSVG =
      "M440-280h80v-240h-80v240Zm40-320q17 0 28.5-11.5T520-640q0-17-11.5-28.5T480-680q-17 0-28.5"
          + " 11.5T440-640q0 17 11.5 28.5T480-600Zm0 520q-83"
          + " 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54"
          + " 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5"
          + " 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0"
          + " 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z";
  private static String warningSVG =
      "m40-120 440-760 440 760H40Zm138-80h604L480-720 178-200Zm302-40q17 0"
          + " 28.5-11.5T520-280q0-17-11.5-28.5T480-320q-17 0-28.5 11.5T440-280q0 17 11.5"
          + " 28.5T480-240Zm-40-120h80v-200h-80v200Zm40-100Z";
  private static String errorSVG =
      "M480-280q17 0 28.5-11.5T520-320q0-17-11.5-28.5T480-360q-17 0-28.5 11.5T440-320q0 17 11.5"
          + " 28.5T480-280Zm-40-160h80v-240h-80v240Zm40 360q-83"
          + " 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54"
          + " 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5"
          + " 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0"
          + " 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z";
  private static String fineSVG =
      "M480-80q-33 0-56.5-23.5T400-160h160q0 33-23.5"
          + " 56.5T480-80ZM320-200v-80h320v80H320Zm10-120q-69-41-109.5-110T180-580q0-125"
          + " 87.5-212.5T480-880q125 0 212.5 87.5T780-580q0 81-40.5"
          + " 150T630-320H330Zm24-80h252q45-32 69.5-79T700-580q0-92-64-156t-156-64q-92 0-156 64t-64"
          + " 156q0 54 24.5 101t69.5 79Zm126 0Z";

  /**
   * Constructs a LogElement.
   *
   * @param record the record to display in the log element
   */
  public LogElement(LogRecord record) {
    this.getStyleClass().add("log-element");

    String color;
    String svg;
    switch (record.getLevel().getName()) {
      case "INFO" -> {
        svg = infoSVG;
        color = "#1b85b8";
      }
      case "SEVERE" -> {
        svg = errorSVG;
        color = "#ae5a41";
      }
      case "FINE", "FINER", "FINEST" -> {
        svg = fineSVG;
        color = "#559e83";
      }
      default -> {
        svg = warningSVG;
        color = "#c3cb71";
      }
    }

    this.setStyle("-fx-background-color: " + color + "20;");

    SVGPath svgPath = new SVGPath();
    svgPath.setContent(svg);

    Region svgRegion = new Region();
    svgRegion.setShape(svgPath);
    svgRegion.minWidthProperty().bind(svgRegion.heightProperty());
    svgRegion.maxWidthProperty().bind(svgRegion.heightProperty());
    svgRegion.setBackground(Background.fill(Paint.valueOf(color)));
    this.getChildren().add(svgRegion);

    Text text = new Text(record.getMessage());
    this.getChildren().add(text);
  }
}
