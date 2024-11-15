package cn.nju.edu.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public static class RedisKey {
        //redis键前缀
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static String STRATEGY_KEY = "big_market_strategy_key_";
        //alias算法重构键
        public static String STRATEGY_LOTTERY_AWARDS_KEY = "big_market_strategy_lottery_awards_key_";
        public static String STRATEGY_LOTTERY_ALIAS_KEY = "big_market_strategy_lottery_alias_key_";
        public static String STRATEGY_LOTTERY_SCALEDRATE_KEY = "big_market_strategy_lottery_scaledrate_key_";

        //rule_models分隔符
        public static String SPLIT = ",";
    }


}
