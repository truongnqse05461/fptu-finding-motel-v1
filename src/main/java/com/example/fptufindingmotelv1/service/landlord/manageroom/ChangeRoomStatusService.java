package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RoomModel;

public interface ChangeRoomStatusService {

    RoomModel changeRoomStatus(RentalRequestDTO rentalRequestDTO);

}
