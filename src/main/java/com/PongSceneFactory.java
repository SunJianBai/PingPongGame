package com;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import static javafx.scene.layout.Background.*;

public class PongSceneFactory extends SceneFactory {

    @Override
    public FXGLMenu newMainMenu() {
        return new FXGLMenu(MenuType.MAIN_MENU) {

            // 难度设置
            final String[] difficultyTooltip = new String[]{
                    "入机模式就是入机模式嘛",
                    "只会追着球傻跑的AI,\n但是并不简单",
                    "更加沉稳的AI",
                    "会尝试预测球的轨迹的AI",
                    "会更加准确的预测轨迹\n会尝试使球转向"
            };
            final String[] difficultyName = new String[]{"入机", "经典", "普通", "困难", "困难+"};

            int difficulty = 1; // 默认难度
            final int num_dif = 5; // 难度种类数

            // 一级菜单
            private void showMainMenu() {
                getContentRoot().getChildren().clear(); // 清空之前的内容

                // 设置背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
                getContentRoot().setPrefWidth(800);
                getContentRoot().setPrefHeight(700);

                // 标题
                Label titleLabelLabel = new Label("Ping - Pong");
                titleLabelLabel.setFont(Font.font(80));
                titleLabelLabel.setTextFill(Color.WHITE);
                titleLabelLabel.setLayoutX(120);
                titleLabelLabel.setLayoutY(120);

                // 标题动画
                TranslateTransition titleLabelTranslateTransition = new TranslateTransition(Duration.seconds(1), titleLabelLabel);
                titleLabelTranslateTransition.setFromX(-200);
                titleLabelTranslateTransition.setToX(0);
                titleLabelTranslateTransition.play();
                FadeTransition titleLabelFadeTransition = new FadeTransition(Duration.seconds(1), titleLabelLabel);
                titleLabelFadeTransition.setFromValue(0);
                titleLabelFadeTransition.setToValue(1);
                titleLabelFadeTransition.play();

                // 开始游戏按钮
                Button startGameButton = new Button(String.format("开始游戏 (%s)", difficultyName[difficulty]));
                startGameButton.setFont(Font.font(20));
                startGameButton.setTextFill(Color.WHITE);
                startGameButton.setBackground(EMPTY);
                startGameButton.setLayoutX(100);
                startGameButton.setLayoutY(400);
                startGameButton.setOnAction(event -> getController().startNewGame());
                startGameButton.setOnMouseEntered(event -> startGameButton.setTextFill(Color.GOLDENROD));
                startGameButton.setOnMouseExited(event -> startGameButton.setTextFill(Color.WHITE));

                // 人机模式按钮
                Button aiModeButton = new Button("人机模式");
                aiModeButton.setFont(Font.font(20));
                aiModeButton.setTextFill(Color.WHITE);
                aiModeButton.setBackground(EMPTY);
                aiModeButton.setLayoutX(100);
                aiModeButton.setLayoutY(450);
                aiModeButton.setOnAction(event -> showDifficultyMenu()); // 切换到二级菜单
                aiModeButton.setOnMouseEntered(event -> aiModeButton.setTextFill(Color.GOLDENROD));
                aiModeButton.setOnMouseExited(event -> aiModeButton.setTextFill(Color.WHITE));

                getContentRoot().getChildren().addAll(titleLabelLabel, startGameButton, aiModeButton);
            }

            // 二级菜单：难度选择
            private void showDifficultyMenu() {
                getContentRoot().getChildren().clear(); // 清空之前的内容

                // 设置背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

                // 难度标题
                Label difficultyLabel = new Label("选择人机难度");
                difficultyLabel.setFont(Font.font(40));
                difficultyLabel.setTextFill(Color.WHITE);
                difficultyLabel.setLayoutX(100);
                difficultyLabel.setLayoutY(100);

                // 难度按钮
                for (int i = 0; i < num_dif; i++) {
                    final int diff = i; // 匿名类中使用必须是 final
                    Button difficultyButton = new Button(difficultyName[i]);
                    difficultyButton.setFont(Font.font(20));
                    difficultyButton.setTextFill(Color.WHITE);
                    difficultyButton.setBackground(EMPTY);
                    difficultyButton.setLayoutX(100);
                    difficultyButton.setLayoutY(200 + i * 50);
                    difficultyButton.setOnAction(event -> {
                        difficulty = diff; // 设置难度
                        PongApp.dif = difficulty; // 全局变量
                        getController().startNewGame(); // 直接开始游戏
                    });
                    difficultyButton.setOnMouseEntered(event -> difficultyButton.setTextFill(Color.GOLDENROD));
                    difficultyButton.setOnMouseExited(event -> difficultyButton.setTextFill(Color.WHITE));
                    getContentRoot().getChildren().add(difficultyButton);
                }

                // 返回按钮
                Button backButton = new Button("返回");
                backButton.setFont(Font.font(20));
                backButton.setTextFill(Color.WHITE);
                backButton.setBackground(EMPTY);
                backButton.setLayoutX(100);
                backButton.setLayoutY(500);
                backButton.setOnAction(event -> showMainMenu()); // 返回一级菜单
                backButton.setOnMouseEntered(event -> backButton.setTextFill(Color.GOLDENROD));
                backButton.setOnMouseExited(event -> backButton.setTextFill(Color.WHITE));

                getContentRoot().getChildren().addAll(difficultyLabel, backButton);
            }

            @Override
            public void onCreate() {
                showMainMenu(); // 启动时显示一级菜单
            }
        };
    }
}
