package cn.nju.edu.domain.strategy.service.annotation;

import cn.nju.edu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：big-market
 * 类名称：LogicStrategy
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：策略自定义枚举
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {

    //让策略工厂装载配有这个注解的类
    DefaultLogicFactory.LogicModel logicMode();

}

