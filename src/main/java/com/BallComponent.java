package com;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class BallComponent extends Component {

    private PhysicsComponent physics;

    @Override
    public void onUpdate(double tpf) {
        limitVelocity();
        checkOffscreen();
    }

    private void limitVelocity() {
        //这是限制速度的函数，限制速度的原因是为了防止球速过快导致球体无法移动
        if (abs(physics.getVelocityX()) < 5 * 60) {
            physics.setVelocityX(signum(physics.getVelocityX()) * 5 * 60);
        }

        // 限制速度的原因是为了防止球速过快导致球体无法移动
        if (abs(physics.getVelocityY()) > 5 * 60 * 2) {
            physics.setVelocityY(signum(physics.getVelocityY()) * 5 * 60);
        }
    }

    // 检查球是否越界，如果越界，则将球回到屏幕中心
    private void checkOffscreen() {
        if (getEntity().getBoundingBoxComponent().isOutside(getGameScene().getViewport().getVisibleArea())) {
            physics.overwritePosition(new Point2D(
                    getAppWidth() / 2d,
                    getAppHeight() / 2d
            ));
        }
    }
}
