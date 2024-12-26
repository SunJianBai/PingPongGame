package com.ui;

import com.PongApp;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.net.GameServer;
import com.net.LanScanner;
import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.play;
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

            // 加载音效
            private final String hoverSoundPath = "hover_sound.wav";  // 鼠标悬停音效路径
            private final String clickSoundPath = "click_sound.wav";  // 点击音效路径

            // 播放音效方法
            private void playSound(String soundPath) {
                play(soundPath);
            }

            // 按钮效果
            private void applyButtonHoverEffect(Button button) {
                button.setOnMouseEntered(event -> {
                    // 鼠标悬停时播放音效
                    playSound(hoverSoundPath);

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
                aiModeButton.setOnAction(event -> {
                    showDifficultyMenu();
                    playSound(clickSoundPath);
                }); // 切换到二级菜单
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
                    playSound(clickSoundPath);
                });
                applyButtonHoverEffect(twoPlayerButton);

                // 局域网按钮
                Button lanModeButton = new Button("局域网对战");
                lanModeButton.setFont(Font.font(20));
                lanModeButton.setTextFill(Color.WHITE);
                lanModeButton.setBackground(Background.EMPTY);
                lanModeButton.setLayoutX(100);
                lanModeButton.setLayoutY(400);
                lanModeButton.setOnAction(event -> {
                    playSound(clickSoundPath);
                    showLanMenu();
                });
                applyButtonHoverEffect(lanModeButton);

                // 操作说明按钮
                Button instructionsButton = new Button("操作说明");
                instructionsButton.setFont(Font.font(20));
                instructionsButton.setTextFill(Color.WHITE);
                instructionsButton.setBackground(EMPTY);
                instructionsButton.setLayoutX(100);
                instructionsButton.setLayoutY(450);
                instructionsButton.setOnAction(event -> {
                    showInstructions();
                    playSound(clickSoundPath);
                }); // 显示操作说明界面
                applyButtonHoverEffect(instructionsButton);

                // 开发者按钮
                Button developerButton = new Button("开发者信息");
                developerButton.setFont(Font.font(20));
                developerButton.setTextFill(Color.WHITE);
                developerButton.setBackground(EMPTY);
                developerButton.setLayoutX(100);
                developerButton.setLayoutY(500);
                developerButton.setOnAction(event -> {
                    showDeveloperInfo();
                    playSound(clickSoundPath);
                }); // 显示开发者信息界面
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

                    Tooltip difficultyTip = new Tooltip(difficultyTooltip[i]);
                    difficultyTip.setShowDelay(Duration.ZERO);
                    difficultyButton.setTooltip(difficultyTip);

                    difficultyButton.setOnAction(event -> {
                        difficulty = diff; // 设置难度
                        PongApp.dif = difficulty; // 全局变量
                        PongApp.isTwoP = false;
                        getController().startNewGame(); // 直接开始游戏
                    });
                    applyButtonHoverEffect(difficultyButton);
                    getContentRoot().getChildren().add(difficultyButton);
                }

                // 返回按钮
                backButton(difficultyLabel);
            }

            private void backButton(Label difficultyLabel) {
                Button backButton = new Button("返回");
                backButton.setFont(Font.font(20));
                backButton.setTextFill(Color.WHITE);
                backButton.setBackground(EMPTY);
                backButton.setLayoutX(100);
                backButton.setLayoutY(500);
                backButton.setOnAction(event -> {
                    showMainMenu();
                    playSound(clickSoundPath);
                }); // 返回一级菜单
                applyButtonHoverEffect(backButton);

                getContentRoot().getChildren().addAll(difficultyLabel, backButton);
            }



        // 局域网菜单：创建房间或加入房间
            private void showLanMenu() {
                getContentRoot().getChildren().clear(); // 清空之前的内容

                // 设置背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

                // 局域网标题
                Label lanLabel = new Label("局域网对战");
                lanLabel.setFont(Font.font(40));
                lanLabel.setTextFill(Color.WHITE);
                lanLabel.setLayoutX(100);
                lanLabel.setLayoutY(40);

                // 创建房间按钮
                Button createRoomButton = new Button("创建房间");
                createRoomButton.setFont(Font.font(20));
                createRoomButton.setTextFill(Color.WHITE);
                createRoomButton.setBackground(EMPTY);
                createRoomButton.setLayoutX(370);
                createRoomButton.setLayoutY(60);
                createRoomButton.setOnAction(event -> createRoom());
                applyButtonHoverEffect(createRoomButton);

                // 加入房间按钮
                Button joinRoomButton = new Button("查找房间");
                joinRoomButton.setFont(Font.font(20));
                joinRoomButton.setTextFill(Color.WHITE);
                joinRoomButton.setBackground(EMPTY);
                joinRoomButton.setLayoutX(500);
                joinRoomButton.setLayoutY(60);
                joinRoomButton.setOnAction(event -> scanRooms());
                applyButtonHoverEffect(joinRoomButton);

                // 房间扫描区域
                VBox roomListContainer = new VBox(10);
                roomListContainer.setLayoutX(150);
                roomListContainer.setLayoutY(100);
                roomListContainer.setPrefWidth(350);
                roomListContainer.setPrefHeight(350);
                roomListContainer.setStyle("-fx-background-color: #333; -fx-border-color: white; -fx-border-width: 2px;");

                // 扫描房间并显示
                scanRooms();

                getContentRoot().getChildren().addAll(lanLabel, createRoomButton, joinRoomButton, roomListContainer);

                // 返回按钮
                Label backButton = new Label("返回");
                backButton(backButton);
            }

            // 创建房间
            private void createRoom() {
                System.out.println("创建房间");

                // 启动服务器，监听客户端的连接
                new Thread(() -> {
                    GameServer server = new GameServer();
                    server.startServer();  // 启动服务器
                }).start();

                // 房主等待界面
                Label countdownLabel = new Label("等待玩家加入...");
                countdownLabel.setFont(Font.font(15));
                countdownLabel.setTextFill(Color.WHITE);
                countdownLabel.setLayoutX(550);
                countdownLabel.setLayoutY(450);
                getContentRoot().getChildren().add(countdownLabel);

                // 倒计时 3 秒
                PauseTransition countdown = new PauseTransition(Duration.seconds(3));
                countdown.setOnFinished(event -> startGame());
                countdown.play();
            }

            // 扫描局域网房间
            private void scanRooms() {
                System.out.println("扫描局域网房间...");

                // 假设通过 LanScanner 扫描房间
                LanScanner lanScanner = new LanScanner();
                String[] rooms = lanScanner.scan(); // 获取房间列表

                VBox roomListContainer = new VBox(10);
                roomListContainer.setLayoutX(150);
                roomListContainer.setLayoutY(100);
                roomListContainer.setPrefWidth(350);
                roomListContainer.setPrefHeight(350);
                roomListContainer.setStyle("-fx-background-color: #333; -fx-border-color: white; -fx-border-width: 2px;");

                for (String room : rooms) {
                    Button roomButton = new Button(room);
                    roomButton.setFont(Font.font(18));
                    roomButton.setTextFill(Color.WHITE);
                    roomButton.setBackground(EMPTY);
                    roomButton.setOnAction(event -> joinRoom(room));
                    roomListContainer.getChildren().add(roomButton);
                }

                getContentRoot().getChildren().add(roomListContainer);
            }

            // 加入房间
            private void joinRoom(String roomName) {
                System.out.println("加入房间: " + roomName);

                // 倒计时
                Label countdownLabel = new Label("准备开始...");
                countdownLabel.setFont(Font.font(30));
                countdownLabel.setTextFill(Color.WHITE);
                countdownLabel.setLayoutX(150);
                countdownLabel.setLayoutY(550);
                getContentRoot().getChildren().add(countdownLabel);

                // 3秒倒计时
                PauseTransition countdown = new PauseTransition(Duration.seconds(3));
                countdown.setOnFinished(event -> startGame());
                countdown.play();
            }

            // 游戏开始
            private void startGame() {
                System.out.println("游戏开始");

                // 切换到游戏界面
                // 你可以根据实际情况来启动游戏界面
            }



            // 操作说明界面
            private void showInstructions() {
                getContentRoot().getChildren().clear();

                // 背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

                // 说明内容
                Label instructionsLabel = new Label("操作说明：\n目标：将球打到对方区域,并避免球打到己方边界\n\n玩家1：'W'/'S'键控制上/下\n玩家2：:'↑'/'↓' 键控制上/下");
                instructionsLabel.setFont(Font.font(20));
                instructionsLabel.setTextFill(Color.WHITE);
                instructionsLabel.setLayoutX(50);
                instructionsLabel.setLayoutY(100);

                // 返回按钮
                backButton(instructionsLabel);
            }

            // 开发者信息界面
            private void showDeveloperInfo() {
                getContentRoot().getChildren().clear();

                // 背景
                getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

                // 开发者信息
                Label developerLabel = new Label("开发者：孙健柏\n版本：v1.0\n源代码:https://github.com/SunJianBai/PingPongGame\n\n赞美欧姆弥赛亚");
                developerLabel.setFont(Font.font(20));
                developerLabel.setTextFill(Color.WHITE);
                developerLabel.setLayoutX(50);
                developerLabel.setLayoutY(100);

                // 返回按钮
                backButton(developerLabel);
            }

            @Override
            public void onCreate() {
                showMainMenu(); // 启动时显示一级菜单
            }
        };
    }
}
