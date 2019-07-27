package optics.objects;

import javafx.Draggable;
import javafx.Rotatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import math.IntersectionSideData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Function;

public class Wall extends OpticalRectangle{
  private ArrayList<EventHandler<Event>> onStateChange = new ArrayList<>();
  private Function<Event, Void> onDestroy;
  public Wall(double x, double y, double width, double height, Pane parent,double rotation) {
    super(x, y, width, height);
    this.setRotate(rotation);
    this.setFill(Color.rgb(180,179,176));
    this.setStroke(Color.BLACK);
    new Draggable(this, this::triggerStateChange, this::triggerDestroy, parent);
    new Rotatable(this, this::triggerStateChange);
  }

  private void triggerStateChange(Event e) {
    for (EventHandler<Event> handler : this.onStateChange) {
      handler.handle(e);
    }
  }

  private void triggerDestroy(Event e) {
    this.onDestroy.apply(e);
  }

  @Override
  public Line transform(Line l, Point2D iPoint) {
    l.setEndX(iPoint.getX());
    l.setEndY(iPoint.getY());
    return null;
  }

  @Override
  public IntersectionSideData getIntersectionSideData(Point2D iPoint) {
    return null;
  }

  @Override
  public Line drawNormal(IntersectionSideData iData, Point2D iPoint) {
    return null;
  }

  @Override
  public void addOnStateChange(EventHandler<Event> handler) {
    this.onStateChange.add(handler);
  }

  @Override
  public void setOnDestroy(Function<Event, Void> onDestroy) {
    this.onDestroy = onDestroy;
  }

  @Override
  public byte[] serialize() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(Character.BYTES + Double.BYTES * 4 + Integer.BYTES);
    byteBuffer.putChar('w');
    byteBuffer.putDouble(this.getX());
    byteBuffer.putDouble(this.getY());
    byteBuffer.putDouble(this.getWidth());
    byteBuffer.putDouble(this.getHeight());
    byteBuffer.putInt((int) this.getRotate());
    return byteBuffer.array();
  }

  @Override
  public void deserialize(byte[] serialized) {
    ByteBuffer buffer = ByteBuffer.wrap(serialized);
    double x = buffer.getDouble(Character.BYTES);
    double y = buffer.getDouble(Character.BYTES + Double.BYTES);
    double width = buffer.getDouble(Character.BYTES + Double.BYTES * 2);
    double height = buffer.getDouble(Character.BYTES + Double.BYTES * 3);
    double angle = buffer.getInt(Character.BYTES + Double.BYTES * 4);
    this.setX(x);
    this.setY(y);
    this.setWidth(width);
    this.setHeight(height);
    this.setRotate(angle);
  }
}
