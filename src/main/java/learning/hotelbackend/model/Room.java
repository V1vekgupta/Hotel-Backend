package learning.hotelbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;

    // 3. Removed the flawed 'isBooked' flag. Availability is not a simple boolean.

    @Lob
    private Blob photo;

    // 1. CRITICAL FIX: Removed CascadeType.ALL to prevent accidental deletion of booking history.
    // The lifecycle of bookings should be managed by the BookingService.
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    // 2. The addBooking method should only handle the relationship, not business logic.
    // The logic for confirmation codes and status will be moved to the BookingService.
    public void addBooking(BookedRoom booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
    }
}