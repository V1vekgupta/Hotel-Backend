package learning.hotelbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_in")
    private LocalDate checkInDate;

    @Column(name = "check_out")
    private LocalDate checkOutDate;

    @Column(name = "guest_fullName")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    // 1. Corrected to standard Java camelCase
    @Column(name = "adults")
    private int numOfAdults;

    // 1. Corrected to standard Java camelCase
    @Column(name = "children")
    private int numOfChildren;

    // 1. Corrected to standard Java camelCase
    @Column(name = "total_guest")
    private int totalNumOfGuests;

    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    // 2. Use a JPA lifecycle callback for more reliable calculations.
    // This method will be called automatically before the entity is saved or updated.
    @PrePersist
    @PreUpdate
    public void calculateTotalNumberOfGuests() {
        this.totalNumOfGuests = this.numOfAdults + this.numOfChildren;
    }

    // 3. The explicit setters are no longer needed; Lombok's @Setter handles them.
    // Lombok will now correctly generate setNumOfAdults(int) and setNumOfChildren(int).
}