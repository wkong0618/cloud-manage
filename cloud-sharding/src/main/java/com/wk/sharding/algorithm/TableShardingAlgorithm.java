package com.wk.sharding.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
public class TableShardingAlgorithm implements PreciseShardingAlgorithm<LocalDateTime> {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
        //精确分片
        String targetTable = null;
        if (Objects.isNull(shardingValue.getValue())) {
            //精确分片
            targetTable = shardingValue.getLogicTableName() + "_" + LocalDate.now().format(FORMATTER);
            if (log.isDebugEnabled()) {
                log.debug("精确分片：targetTable={}", targetTable);
            }
        } else {
            // 直接按sql分片字段路由
            targetTable =
                    shardingValue.getLogicTableName() + "_" + shardingValue.getValue().format(FORMATTER);
            if (log.isDebugEnabled()) {
                log.debug("按SQL字段分片：targetTable={}", targetTable);
            }
        }
        if (availableTargetNames.contains(targetTable)) {
            return targetTable;
        }
        throw new IllegalArgumentException();
    }


}
