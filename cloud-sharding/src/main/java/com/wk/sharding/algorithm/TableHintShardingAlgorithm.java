package com.wk.sharding.algorithm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TableHintShardingAlgorithm implements HintShardingAlgorithm {

    @Override
    public Collection<String> doSharding(Collection collection, HintShardingValue hintShardingValue) {
        List<String> actualTable = Lists.newArrayList();
        List<String> list = Lists.newArrayList(hintShardingValue.getValues());
        String tableName=hintShardingValue.getLogicTableName();
        String json = list.get(0);
        Map<String, Object> map = JSONUtil.toBean(String.valueOf(json), Map.class);

        String startPayTime = null;
        String endPayTime = null;
        if (Objects.nonNull(map.get("startCreateTime"))) {
            startPayTime = map.get("startCreateTime").toString();
        }
        if (Objects.nonNull(map.get("endCreateTime"))) {
            endPayTime = map.get("endCreateTime").toString();
        }
        List<String> months = getMonths(startPayTime, endPayTime);
        if (!months.isEmpty()) {
            for (String month : months) {
                actualTable.add(tableName + "_" + month);
            }
        }
        if (!actualTable.isEmpty()) {
            return actualTable;
        } else {
            return collection;
        }
    }

    /**
     * 获取开始及结束日期间隔月份列表
     * start格式 "2015-01-01"
     * end格式 "2016-01-01"
     * @param start
     * @param end
     * @return
     */
    private static List<String> getMonths(String start, String end) {
        List<String> dateList = new ArrayList<String>();
        try {
            Date dBegin;
            Date dEnd;
            if (StrUtil.isBlank(start) || DateUtil.parse(start).before(DateUtil.parse("2021-01-01"))) {
                //数据库最早是2021年01月
                dBegin = DateUtil.parse("2021-01-01");
            } else {
                dBegin = DateUtil.parse(start);
            }

            if (StrUtil.isBlank(end) || DateUtil.parse(end).after(DateUtil.date())) {
                //查询最晚的数据为当月
                dEnd = DateUtil.date();
            } else {
                dEnd = DateUtil.parse(end);
            }

            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.setTime(dBegin);
            int yearMonth1 = (calBegin.get(Calendar.YEAR)) * 100 + calBegin.get(Calendar.MONTH) + 1;
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.setTime(dEnd);
            int yearMonth2 = (calEnd.get(Calendar.YEAR)) * 100 + calEnd.get(Calendar.MONTH) + 1;
            // 测试此日期是否在指定日期之后
            while (yearMonth2 >= yearMonth1) {
                dateList.add(DateUtil.format(calBegin.getTime(), "yyyyMM"));
                calBegin.add(Calendar.MONTH, 1);
                yearMonth1 = (calBegin.get(Calendar.YEAR)) * 100 + calBegin.get(Calendar.MONTH) + 1;
            }
        } catch (Exception e) {
            log.error("表路由出错，错误信息", e);
        }

        return dateList;
    }


}
