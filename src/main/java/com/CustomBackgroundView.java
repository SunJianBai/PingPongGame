package com;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

public class CustomBackgroundView extends Pane {

    private final Color leftColor;
    private final Color rightColor;

    public CustomBackgroundView(Color leftColor, Color rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;

        // 创建与窗口相同大小的 Canvas
        Canvas canvas = new Canvas(getAppWidth(), getAppHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 左半边动态颜色
        gc.setFill(leftColor);
        gc.fillRect(0, 0, getAppWidth() / 2.0, getAppHeight());
        // 右半边动态颜色
        gc.setFill(rightColor);
        gc.fillRect(getAppWidth() / 2.0, 0, getAppWidth() / 2.0, getAppHeight());
        // 添加到 Pane 中
        getChildren().add(canvas);
    }
}