package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import math.Vectors;
import optics.light.Ray;
import optics.objects.Mirror;
import optics.objects.OpticalRectangle;
import serialize.FileOps;
import utils.FxDebug;
import utils.Geometry;
import utils.OpticsList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

  private ArrayList<Ray> rays = new ArrayList<>();
  private OpticsList<OpticalRectangle> mirrors = new OpticsList<>();

  @FXML
  private AnchorPane parent;

  @FXML
  private Button newMirror;

  @FXML
  private Button saveBtn;
  @FXML
  private Button load;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    double angle = Math.toRadians(30);
    Point2D origin = new Point2D(200, 100);
    FxDebug.indicatePoint(origin, Color.GREEN, parent);
    Line l = Geometry.createLineFromPoints(origin, Vectors.constructWithMagnitude(angle, 1000));
    Ray r1 = new Ray(l, parent);
    rays.add(r1);
    newMirror.setOnMouseClicked(event -> {
      Mirror m = new Mirror(300, 100, 14, 200, parent, 0);
      addObject(m,r1);
    });
    saveBtn.setOnMouseClicked(event -> {
      try {
        FileOps.save(mirrors,(Stage)parent.getScene().getWindow());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    load.setOnMouseClicked(e->{
      ArrayList<byte[]> data;
      try {
        data = FileOps.load((Stage)parent.getScene().getWindow());
      } catch (IOException ex) {
        ex.printStackTrace();
        return;
      }
      if(data==null) return;
      for(byte[] object:data){
        Mirror m = new Mirror(0,0,0,0,parent,0);
        m.deserialize(object);
        addObject(m,r1);
      }
    });
    r1.renderRays(mirrors);
    parent.getChildren().addAll(mirrors);
  }

  private void addObject(Mirror object,Ray r){
    this.mirrors.add(object);
    object.addOnStateChange(event1 -> r.renderRays(mirrors));
    object.setOnDestroy(e -> {
      mirrors.remove(object);
      r.renderRays(mirrors);
      return null;
    });
    r.renderRays(mirrors);
    parent.getChildren().add(object);
  }
}

