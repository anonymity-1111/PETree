package org.PETree;

class Event {
    String eventType;
    int lower;
    int id;
    int vlb;
    int vub;
    int upper;
    double otherAtt;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getVlb() {
        return vlb;
    }
    public void setVlb(int vlb) {
        this.vlb = vlb;
    }

    public int getVub() {
        return vub;
    }
    public void setVub(int vub) {
        this.vub = vub;
    }

    public int getLower() {
        return lower;
    }
    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }
    public void setUpper(int upper) {
        this.upper = upper;
    }

    public double getOtherAtt() {
        return otherAtt;
    }

    public void setOtherAtt(double otherAtt) {
        this.otherAtt = otherAtt;
    }

    public Event(int lower, int upper, String eventType) {
        this.lower = lower;
        this.eventType = eventType;
        this.upper = upper;
    }


    @Override
    public String toString() {
        return "Event{" +
                ", eventType, "+eventType +
                ", lower=" + lower  +
                ", upper=" + upper +
                '}';
    }


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
