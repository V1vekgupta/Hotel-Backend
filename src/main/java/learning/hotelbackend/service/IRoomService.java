package learning.hotelbackend.service;

import learning.hotelbackend.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    // 1. Removed "throws SQLException, IOException". The implementation should handle these.
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);

    List<String> getAllRoomTypes();

    List<Room> getAllRooms();

    // 1. Removed "throws SQLException".
    byte[] getRoomPhotoByRoomId(Long roomId);

    void deleteRoom(Long roomId);

    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);

    Optional<Room> getRoomById(Long roomId);

    List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}