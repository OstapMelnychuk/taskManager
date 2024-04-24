package com.pryvat.bank.task.manager.router;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Log4j2
public class DataSourceRouter extends AbstractRoutingDataSource {
    private static ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();

    public static void setDataSourceKey(String dataSource) {
        System.out.println();
        dataSourceKey.set(dataSource);
    }

    public static String getDataSourceKey() {
        return dataSourceKey.get();
    }
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}
