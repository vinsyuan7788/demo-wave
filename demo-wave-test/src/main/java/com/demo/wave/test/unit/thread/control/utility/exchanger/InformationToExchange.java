package com.demo.wave.test.unit.thread.control.utility.exchanger;

import java.math.BigDecimal;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class InformationToExchange {

    private Long id;

    private String name;

    private BigDecimal amount;

    public InformationToExchange(Long id, String name, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InformationToExchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
