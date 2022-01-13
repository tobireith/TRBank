package de.othr.sw.TRBank.controller.rest;

import java.util.Date;
import java.util.Random;

public class TempDelivery {
    private int deliveryId;
    private Date estimatedDeliveryDate;
    private Date sendDate;
    private double preis;
    private String status;

    public TempDelivery() {
        this.deliveryId = new Random().nextInt(99999999);
    }

    public TempDelivery(Date estimatedDeliveryDate, Date sendDate, double preis, String status) {
        this.deliveryId = new Random().nextInt(99999999);
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.sendDate = sendDate;
        this.preis = preis;
        this.status = status;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Date getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
