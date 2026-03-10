package com.ericross.dealership.domain;

public class ShowroomState {
    private final HondaShowroom hondaShowroom = new HondaShowroom();
    private final ToyotaShowroom toyotaShowroom = new ToyotaShowroom();

    public HondaShowroom getHondaShowroom() { return hondaShowroom; }
    public ToyotaShowroom getToyotaShowroom() { return toyotaShowroom; }
}
