package com.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import com.almasb.fxgl.physics.PhysicsComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class CustomBackgroundView extends Pane {
    PhysicsComponent physics; // 用于碰撞检测

    public CustomBackgroundView(Color leftColor, Color rightColor) {

        // 创建与窗口相同大小的 Canvas
        Canvas canvas = new Canvas(getAppWidth(), getAppHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 左半边动态颜色
        gc.setFill(leftColor);
        gc.fillRect(0, 0, getAppWidth() / 2.0, getAppHeight());

        // 右半边动态颜色
        gc.setFill(rightColor);
        gc.fillRect(getAppWidth() / 2.0, 0, getAppWidth() / 2.0, getAppHeight());

        // 绘制上下边界中间的三角形
        drawTriangle(gc);

        // 添加到 Pane 中
        getChildren().add(canvas);

    }

    private void drawTriangle(GraphicsContext gc) {
        double width = getAppWidth();
        double height = getAppHeight();

        // 设置三角形的颜色为白色
        gc.setFill(Color.WHITE);

        // 上边界位置的三角形
        double x = width / 50;

//        // 绘制等腰三角形
//        gc.beginPath(); // 开始绘制路径
//        gc.moveTo(width / 2 - x, 0); // 顶点 1
//        gc.lineTo(width / 2, x); // 顶点 2
//        gc.lineTo(width / 2 + x, 0); // 顶点 3
//        gc.closePath();
//        gc.fill();
//        // 下边界位置的三角形
//        gc.beginPath(); // 开始绘制路径
//        gc.moveTo(width / 2 - x, height); // 顶点 1
//        gc.lineTo(width / 2, height - x); // 顶点 2
//        gc.lineTo(width / 2 + x, height); // 顶点 3
//        gc.closePath();
//        gc.fill();
    }

}