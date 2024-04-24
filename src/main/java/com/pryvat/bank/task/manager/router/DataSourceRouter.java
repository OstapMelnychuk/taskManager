package com.pryvat.bank.task.manager.router;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DataSourceRouter that is used to change data source with dataSourceKey
 */
@Log4j2
public class DataSourceRouter extends AbstractRoutingDataSource {
    private static ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();

    /**
     * Method that tracks which datasource to use
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }

    /**
     * Method to set a datasource
     * @param dataSource h2 or postgres
     */
    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    public static String getDataSourceKey() {
        return dataSourceKey.get();
    }
}
