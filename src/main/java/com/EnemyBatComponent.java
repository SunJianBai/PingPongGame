package com;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static java.lang.Math.*;

// 由电脑控制的拍子
public class EnemyBatComponent extends Component {
    // 物理组件
    PhysicsComponent physics;

    // 加速度
    double A = 3200;
    // 减速
    double A_ = -5000;
    // 最大速度
    double MaxSpeed = 400;

    // 当前加速度
    double a = 0;

    // AI难度
    int difficulty = FXGL.geti("difficulty");

    // 球
    Entity ball = FXGL.getGameWorld().getEntitiesByType(PongType.ball).get(0);

    @Override
    public void onUpdate(double tpf) {
        // 调用ai 设置a
        switch (difficulty) {
            case 0 -> AI_0();
            case 1 -> AI_1();
            case 2 -> AI_2();
            case 3 -> AI_3();
            case 4 -> AI_4();
        }

        // 按照加速度修改拍子速度,同玩家
        if (a != 0) {
            physics.setVelocityY(min(max(physics.getVelocityY() + a * tpf, -MaxSpeed), MaxSpeed));
        } else {
            double vy = physics.getVelocityY();
            physics.setVelocityY(max(0, abs(vy) + A_ * tpf) * signum(vy));
        }

        // 出界判定
        if (entity.getY() < 0) {
            physics.overwritePosition(new Point2D(entity.getX(), 0));
        }
        if (entity.getBottomY() > FXGL.getAppHeight()) {
            physics.overwritePosition(new Point2D(entity.getX(), FXGL.getAppHeight() - entity.getHeight()));
        }

        // 旋转
        entity.setRotation(toDegrees(atan2(physics.getVelocityY(), -500)));
    }

    // 预测球的落点
    private double predictBallTarget() {
        double bx = ball.getX(), by = ball.getY();
        double vx = ball.getComponent(PhysicsComponent.class).getVelocityX();
        double vy = ball.getComponent(PhysicsComponent.class).getVelocityY();

        double disY = by + (entity.getX() - bx) / vx * vy;

        if (disY < 0) disY = 1200 - disY;

        double target = disY % 600;
        if (((int) disY / 600) % 2 != 0) {
            target = 600 - target;
        }
        return target;
    }

    void AI_0() {
        // 判断球是否在左边
        boolean isBallLeft = ball.getX() < entity.getRightX();

        // 随机性因子，随机生成一个 -1, 0, 1 的值
        int randomFactor = (int) (Math.random() * 3) - 1; // -1:向下偏移, 0:保持原方向, 1:向上偏移

        if (entity.getY() > ball.getBottomY()) { // 如果球在上方
            // 向上移动，并加入随机性
            a = isBallLeft ? -A : A;
            a += randomFactor * (A / 3); // 随机偏移，但不会过大，A/3 控制偏移量
        } else if (entity.getBottomY() < ball.getY()) { // 如果球在下方
            // 向下移动，并加入随机性
            a = isBallLeft ? A : -A;
            a += randomFactor * (A / 3); // 随机偏移
        } else { // 如果球在中间
            // 偶尔停顿一下
            a = randomFactor == 0 ? 0 : (isBallLeft ? -A : A);
        }
    }

    void AI_1() {
        // 跟着球跑
        // 判断球是否在左边
        boolean isBallLeft = ball.getX() < entity.getRightX();

        if (entity.getY() > ball.getBottomY() - 20) {// 如果球在上面
            // 向上移动
            a = isBallLeft ? -A : A;
        } else if (entity.getBottomY() < ball.getY() + 20) {//如果球在下面
            // 向下移动
            a = isBallLeft ? A : -A;
        } else { // 就在中间
            a = 0;
        }
    }

    double target;

    void AI_2() {
        if (ball.getX() > entity.getX()) {// 球在拍子右边
            // 避让
            if (ball.getY() > 300) {
                target = 20;
            } else {
                target = 580;
            }
        } else if (ball.getX() >= 800 - ball.getComponent(PhysicsComponent.class).getVelocityX() * 1.1 && ball.getComponent(PhysicsComponent.class).getVelocityX() > 0) {// 当球正在往这边飞来时
            // 跟随球
            target = ball.getY();
        } else {
            // 回中
            target = FXGL.getAppHeight() / 2d;
        }

        // 向目标移动
        if (entity.getY() + 20 > target) {// 如果球在上面
            // 向上移动
            a = -A;
        } else if (entity.getBottomY() - 20 < target) {//如果球在下面
            // 向下移动
            a = A;
        } else { // 就在中间
            a = 0;
        }
    }

    boolean isTarget = false;

    void AI_3() {// 尝试预测球的轨迹
        target = predictBallTarget();
        // 向目标移动
        if (entity.getY() + 20 > target) {// 如果球在上面
            // 向上移动
            a = -A;
        } else if (entity.getBottomY() - 20 < target) {//如果球在下面
            // 向下移动
            a = A;
        } else { // 就在中间
            a = 0;
        }
    }

    void AI_4() {
        // 比困难更困难（）
        target = predictBallTarget();

        // 向目标移动
        if (entity.getY() + 30 > target) {// 如其在上
            // 向上移动
            a = -A;
        } else if (entity.getBottomY() - 30 < target) {//如其在下
            // 向下移动
            a = A;
        } else { // 就在中间
            a = 0;
        }
    }

}
