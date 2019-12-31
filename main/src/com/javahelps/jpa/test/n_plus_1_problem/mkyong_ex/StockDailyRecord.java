package com.javahelps.jpa.test.n_plus_1_problem.mkyong_ex;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stock_daily_record")
public class StockDailyRecord {
    @Id
    @GeneratedValue
    private long id;
}
