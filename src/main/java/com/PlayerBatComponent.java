package com;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static java.lang.Math.*;

// 由玩家控制的拍子
public class PlayerBatComponent extends Component {
    // 物理组件
    PhysicsComponent physics;

    // 加速度
    double A = 5000;
    // 减速
    double A_ = -5000;
    // 最大速度
    double MaxSpeed = 400;
    // 当前加速度
    double a = 0;
    // 鼠标操控的速度
    double a_m = 0;

    @Override
    public void onAdded() {
        FXGL.getbp("key_w").addListener((observable, oldValue, newValue) -> a -= newValue ? A : -A);
        FXGL.getbp("key_s").addListener((observable, oldValue, newValue) -> a += newValue ? A : -A);
    }

    @Override
    public void onUpdate(double tpf) {
        // 如果是鼠标操控
        if (FXGL.getb("mousePressed")) {
            double mouseY = FXGL.getInput().getMouseYWorld();
            if (entity.getY() > mouseY - 35) {// 如果鼠标在上面
                // 向上移动
                a_m = -A;
            } else if (entity.getBottomY() < mouseY + 35) {//如果鼠标在下面
                // 向下移动
                a_m = A;
            } else { // 就在中间
                a_m = 0;
            }
            // 修改当前速度
            if (a_m != 0) {
                physics.setVelocityY(min(max(physics.getVelocityY() + a_m * tpf, -MaxSpeed), MaxSpeed));
            } else {
                double vy = physics.getVelocityY();
                physics.setVelocityY(max(0, abs(vy) + A_ * tpf) * signum(vy));
            }
        } else {
            // 修改当前速度
            if (a != 0) {
                physics.setVelocityY(min(max(physics.getVelocityY() + a * tpf, -MaxSpeed), MaxSpeed));
            } else {
                double vy = physics.getVelocityY();
                physics.setVelocityY(max(0, abs(vy) + A_ * tpf) * signum(vy));
            }
        }

        // 出界判定
        if (entity.getY() < 0) {
            physics.overwritePosition(new Point2D(entity.getX(), 0));
        }
        if (entity.getBottomY() > FXGL.getAppHeight()) {
            physics.overwritePosition(new Point2D(entity.getX(), FXGL.getAppHeight() - entity.getHeight()));
        }

        // 旋转
        entity.setRotation(toDegrees(atan2(physics.getVelocityY(), 1000)));
    }
}
