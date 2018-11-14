package com.example.npc.doannhung;

class BTDevice {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BTDevice(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public BTDevice() {
    }

    @Override
    public String toString() {
        return name;
    }
}
