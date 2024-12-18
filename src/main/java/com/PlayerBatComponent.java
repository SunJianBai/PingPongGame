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

    // 玩家 ID
    int playerID;

    // 加速度
    double A = 5000;
    // 减速
    double A_ = -5000;
    // 最大速度
    double MaxSpeed = 400;
    // 当前加速度
    double a = 0;


    public PlayerBatComponent(int i) {
        // 玩家 ID
        playerID = i;
    }


    @Override
    public void onAdded() {
        int playerID;
        if (this.playerID == 1) {
            // 玩家 1 控制按键
            FXGL.getbp("key_w").addListener((observable, oldValue, newValue) -> a -= newValue ? A : -A);
            FXGL.getbp("key_s").addListener((observable, oldValue, newValue) -> a += newValue ? A : -A);
        } else if (this.playerID == 2) {
            // 玩家 2 控制按键（双人模式）
            FXGL.getbp("key_up").addListener((observable, oldValue, newValue) -> a -= newValue ? A : -A);
            FXGL.getbp("key_down").addListener((observable, oldValue, newValue) -> a += newValue ? A : -A);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        handleKeyboardControl(tpf); // 处理键盘控制
        constrainToBounds(); // 限制在屏幕内
        updateRotation(); // 更新旋转
    }
    // 处理键盘控制
    private void handleKeyboardControl(double tpf) {
        if (a != 0) {
            physics.setVelocityY(min(max(physics.getVelocityY() + a * tpf, -MaxSpeed), MaxSpeed));
        } else {
            double vy = physics.getVelocityY();
            physics.setVelocityY(max(0, abs(vy) + A_ * tpf) * signum(vy));
        }
    }

    // 限制在屏幕内
    private void constrainToBounds() {
        if (entity.getY() < 0) {
            physics.overwritePosition(new Point2D(entity.getX(), 0));
        }
        if (entity.getBottomY() > FXGL.getAppHeight()) {
            physics.overwritePosition(new Point2D(entity.getX(), FXGL.getAppHeight() - entity.getHeight()));
        }
    }

    // 更新旋转
    private void updateRotation() {
        entity.setRotation(toDegrees(atan2(physics.getVelocityY(), 1000)));
    }
}
