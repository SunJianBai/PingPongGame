package com;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PongFactory implements EntityFactory {


    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        // 物理组件
        PhysicsComponent physics = new PhysicsComponent();
        // 设置初速度
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(200, 200));
        // 设置物理属性
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef()
                .density(0.3f)// 密度
                .restitution(1.0f));// 弹性

        // 监控得分情况
        var endGame = getip("player1score").greaterThanOrEqualTo(9)
                .or(getip("player2score").isEqualTo(9));



        // 初始化粒子发射器
        ParticleEmitter particleEmitter = new ParticleEmitter();

        // 设置粒子颜色
        particleEmitter.startColorProperty().bind(// 粒子最开始的颜色
                Bindings.when(endGame)// 当游戏结束时
                        .then(Color.LIGHTYELLOW) // 尾气最开始是亮黄色
                        .otherwise(Color.WHITE) // 否则为白色
        );
        // 以下同上
        particleEmitter.endColorProperty().bind(
                Bindings.when(endGame)
                        .then(Color.RED)
                        .otherwise(Color.LIGHTBLUE)
        );


        // 设置粒子持续时间
        particleEmitter.setExpireFunction(i -> Duration.seconds(FXGLMath.random(0.3, 1.0)));
        // 设置粒子大小
        particleEmitter.setSize(5, 10);
        // 设置为每一帧都发射粒子(具体用法看文档)
        particleEmitter.setEmissionRate(1);
        // 设置混合模式
        particleEmitter.setBlendMode(BlendMode.SRC_OVER);


        return entityBuilder(data)
                .type(PongType.ball)
                .bbox(new HitBox(BoundingShape.circle(5)))
                .collidable()
                .with(physics, new ParticleComponent(particleEmitter), new BallComponent())
                .build();
    }

    @Spawns("bat")
    public Entity newBat(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        boolean isPlayer = data.get("isPlayer"); // 判断是否为玩家
        int playerID = data.get("playerID"); // 获取玩家 ID（1 或 2）

        return entityBuilder(data)
                .type(PongType.bat)
                .viewWithBBox(new Rectangle(10, 80, Color.WHITE))
                .at(playerID == 1  ?
                        new Point2D(getAppWidth() / 4d - 5 - 80, getAppHeight() / 2d - 40) :// 如果是玩家的话就生成在屏幕左边
                        new Point2D(getAppWidth() / 4d * 3 - 5 + 80, getAppHeight() / 2d - 40))// 否则在屏幕右边
                .collidable()
                .with(physics)
                .with(isPlayer && playerID == 1 ? new PlayerBatComponent(1) : (isPlayer && playerID == 2 ? new PlayerBatComponent(2) : new EnemyBatComponent()))
                .zIndex(1)
                .build();
    }
}
