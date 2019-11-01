package com.chanlun.yx.data.model;

import java.io.Serializable;

public class MiData implements Serializable {
    private Long index;

    private Double open;

    private Double high;

    private Double low;

    private Double close;

    private Long volume;

    private Double amount;

    private Integer adjustflag;

    private static final long serialVersionUID = 1L;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getAdjustflag() {
        return adjustflag;
    }

    public void setAdjustflag(Integer adjustflag) {
        this.adjustflag = adjustflag;
    }
}