package com;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PongApp extends GameApplication {
    // 定义游戏类型
    static boolean isTwoP = true;
    // 主界面传值的游戏难度
    static int dif = 1;
    // 玩家胜利的提示
    final String[] playerWinStrings = new String[]{
            "打赢这种程度的敌人有什么好骄傲的",
            "恭喜玩家获得胜利!",
            "不错不错,挺有实力的嘛",
            "高!实在是高!",
            "啊?"
    };
    // 玩家失败的提示
    final String[] enemyWinStrings = new String[]{
            "怎么连入机版的入机都打不过? 你才是真入机",
            "菜就多练",
            "杂鱼~ 杂鱼~",
            "再接再厉!",
            "有实力,但不多"
    };

    @Override
    protected void initSettings(GameSettings settings) {
        // 名字
        settings.setTitle("Ping Pong");
        settings.setVersion("1.1");
        // 窗口大小
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setManualResizeEnabled(true); // 允许手动调整窗口大小
        // 启用开始菜单
        settings.setMainMenuEnabled(true);
        //settings.setGameMenuEnabled(false);
        // 启用场景切换动画
        settings.setSceneFactory(new PongSceneFactory());

        // 添加模式选择
        //settings.setMenuEnabled(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("isTwoPlayerMode", isTwoP); // 默认为单人模式

        vars.put("player1score", 0);
        vars.put("player2score", 0);

        // 用于控制玩家移动
        vars.put("key_w", false);
        vars.put("key_s", false);
        vars.put("mousePressed", false);
        vars.put("key_up", false);
        vars.put("key_down", false);


        vars.put("difficulty", dif);
    }

    @Override
    protected void initInput() {
        // 玩家1控制
        getInput().addAction(new UserAction("Player 1 Up") {
            @Override
            protected void onAction() {
                set("key_w", true);
                System.out.println("Player 1 Up pressed"); // 调试用
            }

            @Override
            protected void onActionEnd() {
                set("key_w", false);
                System.out.println("Player 1 Up released");
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Player 1 Down") {
            @Override
            protected void onAction() {
                set("key_s", true);
                System.out.println("Player 1 Down pressed");
            }

            @Override
            protected void onActionEnd() {
                set("key_s", false);
                System.out.println("Player 1 Down released");
            }
        }, KeyCode.S);

        // 如果是双人模式，添加玩家2控制

        if (isTwoP) {
            getInput().addAction(new UserAction("Player 2 Up") {
                @Override
                protected void onAction() {
                    set("key_up", true);
                    System.out.println("Player 2 Up pressed");
                }

                @Override
                protected void onActionEnd() {
                    set("key_up", false);
                    System.out.println("Player 2 Up released");
                }
            }, KeyCode.UP);

            getInput().addAction(new UserAction("Player 2 Down") {
                @Override
                protected void onAction() {
                    set("key_down", true);
                    System.out.println("Player 2 Down pressed");
                }

                @Override
                protected void onActionEnd() {
                    set("key_down", false);
                    System.out.println("Player 2 Down released");
                }
            }, KeyCode.DOWN);
        }

    }


    @Override
    protected void initUI() {
        // 得分显示
        Label player1ScoreLabel = new Label();
        Label player2ScoreLabel = new Label();
        // 设置样式
        player1ScoreLabel.textProperty().bind(getip("player1score").asString());
        player1ScoreLabel.setFont(Font.font(80));
        player1ScoreLabel.setLayoutX(120);
        player1ScoreLabel.setLayoutY(100);
        player1ScoreLabel.setTextFill(Color.WHITE);

        player2ScoreLabel.textProperty().bind(getip("player2score").asString());
        player2ScoreLabel.setFont(Font.font(80));
        player2ScoreLabel.setLayoutX(getAppWidth() - 50 - 120);
        player2ScoreLabel.setLayoutY(100);
        player2ScoreLabel.setTextFill(Color.WHITE);

        // 添加Node
        addUINode(player1ScoreLabel);
        addUINode(player2ScoreLabel);

        // 添加动画
        player1ScoreLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            animationBuilder()
                    .autoReverse(true)
                    .repeat(2)
                    .duration(Duration.seconds(0.05))
                    .translate(player1ScoreLabel)
                    .from(new Point2D(0, 0))
                    .to(new Point2D(0, 40))
                    .buildAndPlay();
            animationBuilder()
                    .autoReverse(true).repeat(2)
                    .duration(Duration.seconds(0.1))
                    .fade(player1ScoreLabel)
                    .from(1).to(0.2)
                    .buildAndPlay();
        });

        player2ScoreLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            animationBuilder()
                    .autoReverse(true)
                    .repeat(2)
                    .duration(Duration.seconds(0.05))
                    .translate(player2ScoreLabel)
                    .from(new Point2D(0, 0))
                    .to(new Point2D(0, 40))
                    .buildAndPlay();
            animationBuilder()
                    .autoReverse(true).repeat(2)
                    .duration(Duration.seconds(0.05))
                    .fade(player2ScoreLabel)
                    .from(1).to(0.2)
                    .buildAndPlay();
        });
    }

    @Override
    protected void initPhysics() {
        // 球与墙壁的碰撞
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PongType.ball, PongType.wall) {
            @Override
            protected void onHitBoxTrigger(Entity a, Entity b, HitBox boxA, HitBox boxB) {
                if (boxB.getName().equals("LEFT")) {
                    inc("player2score", +1);
                } else if (boxB.getName().equals("RIGHT")) {
                    inc("player1score", +1);
                }
                play("hit_wall.wav");
                getGameScene().getViewport().shakeTranslational(5);
            }
        });
        // 球与拍子的碰撞
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PongType.ball, PongType.bat) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
                // 播放声音
                play("hit_bat.wav");
            }
        });
    }

    // 自定义背景（更改RGB值）
    private void initCustomBackground() {
        // 自定义背景颜色 (使用深红色和深蓝色作为默认值)
        Color leftColor = Color.rgb(50, 0, 0);    // 深红色
        Color rightColor = Color.rgb(0, 0, 80);  // 深蓝色

        // 你可以根据需要调整 RGB 值
        entityBuilder()
                .at(0, 0) // 从 (0, 0) 开始绘制
                .view(new CustomBackgroundView(leftColor, rightColor))
                .zIndex(-1) // 确保背景在最底层
                .buildAndAttach();
    }


    @Override
    protected void initGame() {
        // 缓存音效
        getAssetLoader().loadSound("hit_bat.wav");
        getAssetLoader().loadSound("hit_wall.wav");
        //添加实体工厂
        getGameWorld().addEntityFactory(new PongFactory());

        // 设置世界重力
        getPhysicsWorld().setGravity(0, 0);

        // 添加自定义背景实体
        initCustomBackground();

        // 生成球
        spawn("ball", new SpawnData(getAppWidth() / 2d, getAppHeight() / 2d));


        // 生成玩家1的球拍
        spawn("bat", new SpawnData()
            .put("isPlayer", true) // 表示该球拍由玩家控制
            .put("playerID", 1)); // 指定玩家 ID 为 1

        // 如果是双人模式，生成玩家2的球拍
        if (isTwoP) {
            // 双人模式下，玩家 2 的球拍也由玩家控制
            spawn("bat", new SpawnData()
                .put("isPlayer", true) // 表示该球拍由玩家控制
                .put("playerID", 2)); // 指定玩家 ID 为 2
        } else {
            // 单人模式下，玩家 2 的球拍由电脑控制
            spawn("bat", new SpawnData()
                .put("isPlayer", false) // 表示该球拍由电脑控制
                .put("playerID", 2)); // 指定玩家 ID 为 2
        }

        // 生成边界
        entityBuilder().type(PongType.wall).collidable().with(new PhysicsComponent()).buildScreenBoundsAndAttach(100);

        // 当游戏结束时
        if (isTwoP) {
            getip("player1score").addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 10) {
                    getDialogService().showChoiceBox(
                         "恭喜玩家1获胜!\n右边的玩家还是逊啦\n是否重新开始?",
                        o -> {
                            if (o.equals("是")) {
                                getGameController().startNewGame();
                            } else {
                                getGameController().exit();
                            }
                        },
                        "是",
                        "否"
                    );
                }
            });
            getip("player2score").addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 10) {
                    getDialogService().showChoiceBox(
                          "恭喜玩家2获胜!\n左边的玩家有点菜哦\n是否重新开始?",
                        o -> {
                            if (o.equals("是")) {
                                getGameController().startNewGame();
                            } else {
                                getGameController().exit();
                            }
                        },
                        "是",
                        "否"
                    );
                }
            });
        }else{
            getip("player1score").addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 10) {
                    getDialogService().showChoiceBox(
                        playerWinStrings[dif] + "\n是否重新开始?",
                        o -> {
                            if (o.equals("是")) {
                                getGameController().startNewGame();
                            } else {
                                getGameController().exit();
                            }
                        },
                        "是",
                        "否"
                    );
                }
            });
            getip("player2score").addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 10) {
                    getDialogService().showChoiceBox(
                        enemyWinStrings[dif] + "\n是否重新开始?",
                        o -> {
                            if (o.equals("是")) {
                                getGameController().startNewGame();
                            } else {
                                getGameController().exit();
                            }
                        },
                        "是",
                        "否"
                    );
                }
            });
        }

    }

    public static void main(String[] args) {
        // 禁用系统缩放
        System.setProperty("prism.allowhidpi", "false"); // 其中，XXX 在实际的环境下应改为 true 或 false

        launch(args);
    }
}
