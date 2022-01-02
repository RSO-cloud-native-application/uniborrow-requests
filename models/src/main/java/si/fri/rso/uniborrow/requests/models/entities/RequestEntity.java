package si.fri.rso.uniborrow.requests.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "requests")
@NamedQueries(value =
        {
                @NamedQuery(name = "RequestEntity.getAll",
                        query = "SELECT req FROM RequestEntity req")
        })
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "price")
    private Float price;

    @Column(name = "timestampStart")
    private Instant timestampStart;

    @Column(name = "timestampEnd")
    private Instant timestampEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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