package com;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;


import static javafx.scene.layout.Background.EMPTY;

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
            final String[] difficultyName = new String[]{"简单", "经典", "普通", "困难", "困难+"};

            int difficulty = 1; // 默认难度
            final int num_dif = 5; // 难度种类数

            private void applyButtonHoverEffect(Button button) {
                button.setOnMouseEntered(event -> {
                    ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), button);
                    st.setToX(1.2); // 放大 1.2 倍
                    st.setToY(1.2);
                    st.play();
                    button.setTextFill(Color.GOLDENROD); // 鼠标悬停时字体颜色变为金色
                });

                button.setOnMouseExited(event -> {
                    ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), button);
                    st.setToX(1.0); // 恢复原大小
                    st.setToY(1.0);
                    st.play();
                    button.setTextFill(Color.WHITE); // 恢复原字体颜色
                });
            }


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

                // 人机模式按钮
                Button aiModeButton = new Button("人机模式");
                aiModeButton.setFont(Font.font(20));
                aiModeButton.setTextFill(Color.WHITE);
                aiModeButton.setBackground(EMPTY);
                aiModeButton.setLayoutX(100);
                aiModeButton.setLayoutY(350);
                aiModeButton.setOnAction(event -> showDifficultyMenu()); // 切换到二级菜单
                applyButtonHoverEffect(aiModeButton);

                // 双人模式按钮
                Button twoPlayerButton = new Button("双人模式");
                twoPlayerButton.setFont(Font.font(20));
                twoPlayerButton.setTextFill(Color.WHITE);
                twoPlayerButton.setBackground(Background.EMPTY);
                twoPlayerButton.setLayoutX(100);
                twoPlayerButton.setLayoutY(400);
                twoPlayerButton.setOnAction(event -> {
                    PongApp.isTwoP = true; // 设置双人模式
                    getController().startNewGame();
                });
                applyButtonHoverEffect(twoPlayerButton);


                // 操作说明按钮
                Button instructionsButton = new Button("操作说明");
                instructionsButton.setFont(Font.font(20));
                instructionsButton.setTextFill(Color.WHITE);
                instructionsButton.setBackground(EMPTY);
                instructionsButton.setLayoutX(100);
                instructionsButton.setLayoutY(450);
                instructionsButton.setOnAction(event -> showInstructions()); // 显示操作说明界面
                applyButtonHoverEffect(instructionsButton);

                // 开发者按钮
                Button developerButton = new Button("开发者信息");
                developerButton.setFont(Font.font(20));
                developerButton.setTextFill(Color.WHITE);
                developerButton.setBackground(EMPTY);
                developerButton.setLayoutX(100);
                developerButton.setLayoutY(500);
                developerButton.setOnAction(event -> showDeveloperInfo()); // 显示开发者信息界面
                applyButtonHoverEffect(developerButton);

                getContentRoot().getChildren().addAll(titleLabelLabel, aiModeButton, twoPlayerButton, instructionsButton, developerButton);
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
                        PongApp.isTwoP = false;
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

            // 操作说明界面
            private void showInstructions() {
                getContentRoot().getChildren().clear();

                // 背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

                // 说明内容
                Label instructionsLabel = new Label("操作说明：\n玩家1：W/S键控制上/下\n玩家2：方向键控制上/下\n目标：将球打到对方区域");
                instructionsLabel.setFont(Font.font(20));
                instructionsLabel.setTextFill(Color.WHITE);
                instructionsLabel.setLayoutX(50);
                instructionsLabel.setLayoutY(100);

                // 返回按钮
                Button backButton = new Button("返回");
                backButton.setFont(Font.font(20));
                backButton.setTextFill(Color.WHITE);
                backButton.setBackground(EMPTY);
                backButton.setLayoutX(100);
                backButton.setLayoutY(500);
                backButton.setOnAction(event -> showMainMenu());
                backButton.setOnMouseEntered(event -> backButton.setTextFill(Color.GOLDENROD));
                backButton.setOnMouseExited(event -> backButton.setTextFill(Color.WHITE));

                getContentRoot().getChildren().addAll(instructionsLabel, backButton);
            }

            // 开发者信息界面
            private void showDeveloperInfo() {
                getContentRoot().getChildren().clear();

                // 背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

                // 开发者信息
                Label developerLabel = new Label("开发者：XXX\n版本：1.0\n制作时间：2024年12月");
                developerLabel.setFont(Font.font(20));
                developerLabel.setTextFill(Color.WHITE);
                developerLabel.setLayoutX(50);
                developerLabel.setLayoutY(100);

                // 返回按钮
                Button backButton = new Button("返回");
                backButton.setFont(Font.font(20));
                backButton.setTextFill(Color.WHITE);
                backButton.setBackground(EMPTY);
                backButton.setLayoutX(100);
                backButton.setLayoutY(500);
                backButton.setOnAction(event -> showMainMenu());
                backButton.setOnMouseEntered(event -> backButton.setTextFill(Color.GOLDENROD));
                backButton.setOnMouseExited(event -> backButton.setTextFill(Color.WHITE));

                getContentRoot().getChildren().addAll(developerLabel, backButton);
            }

            @Override
            public void onCreate() {
                showMainMenu(); // 启动时显示一级菜单
            }
        };
    }
}
