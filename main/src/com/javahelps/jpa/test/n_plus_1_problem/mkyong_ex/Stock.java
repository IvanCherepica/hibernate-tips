package com.javahelps.jpa.test.n_plus_1_problem.mkyong_ex;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stock")
public class Stock implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	private Set<StockDailyRecord> stockDailyRecords = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
//	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
        @BatchSize(size = 10)
	public Set<StockDailyRecord> getStockDailyRecords() {
		return this.stockDailyRecords;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setStockDailyRecords(Set<StockDailyRecord> stockDailyRecords) {
		this.stockDailyRecords = stockDailyRecords;
	}
}