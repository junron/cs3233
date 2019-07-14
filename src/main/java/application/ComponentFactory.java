package application;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class ComponentFactory implements EntityFactory {

  @Spawns("Bullet")
  public Entity newBullet(SpawnData data) {
    Point2D vector = (Point2D) data.getData().get("angle");
    PhysicsComponent pc = new PhysicsComponent();
    FixtureDef fd = new FixtureDef();
    fd.setDensity(0.2f);
    fd.setFriction(0f);
//    Bounciness of bullet
    fd.setRestitution(0.9f);
    pc.setFixtureDef(fd);
    pc.setOnPhysicsInitialized(() -> pc.setLinearVelocity(vector.getX() * 500, vector.getY() * 500));
    pc.setBodyType(BodyType.DYNAMIC);
    return entityBuilder()
            .from(data)
            .type(Types.BULLET)
            .with(new CollidableComponent(true))
            .with(pc)
            .with(new ExpireCleanComponent(Duration.seconds(10)))
            .viewWithBBox(new Circle(3, Color.BLACK))
            .build();
  }


}
