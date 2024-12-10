package com.soprano.francesco.services;

import com.soprano.francesco.entities.Room;
import com.soprano.francesco.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> updateRoom(Long roomId, Room updatedRoom) {
        if (roomRepository.existsById(roomId)) {
            updatedRoom.setId(roomId);
            return Optional.of(roomRepository.save(updatedRoom));
        }
        return Optional.empty();
    }

    public boolean deleteRoom(Long roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
            return true;
        }
        return false;
    }
}

