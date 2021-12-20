package si.fri.rso.uniborrow.requests.lib;

import com.sun.jdi.FloatType;

import java.time.Instant;

public class Request {

    private Integer requestId;
    private Integer userId;
    private String message;
    private Float price;
    private Instant timestampStart;
    private Instant timestampEnd;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Instant getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(Instant timestampStart) {
        this.timestampStart = timestampStart;
    }

    public Instant getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(Instant timestampEnd) {
        this.timestampEnd = timestampEnd;
    }
}
