package cn.nju.edu.domain.activity.service.quota.rule.factory;

import cn.nju.edu.domain.activity.service.quota.rule.IActionChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：DefaultActivityChainFactory
 * 作者：tkj
 * 日期：2024/12/2
 * 描述：
 */
@Service

public class DefaultActivityChainFactory {

    private final Map<String,IActionChain> actionChainsGroup;
    private final IActionChain actionChain;

    public DefaultActivityChainFactory(Map<String, IActionChain> actionChainsGroup) {
        this.actionChainsGroup = actionChainsGroup;
        actionChain = actionChainsGroup.get(ActionModel.activity_base_action.code);
        actionChain.appendNext(actionChainsGroup.get(ActionModel.activity_sku_stock_action.code));
    }

    public IActionChain openActionChain() {
        return this.actionChain;
    }



    //责任链枚举类，用于标识每种责任链
    @Getter
    @AllArgsConstructor
    public enum ActionModel {

        activity_base_action("activity_base_action", "活动的库存、时间以及状态校验"),
        activity_sku_stock_action("activity_sku_stock_action", "活动sku库存"),
        ;

        private final String code;
        private final String info;

    }
    //可以把责任链的生成结果封装在工厂类里


}
