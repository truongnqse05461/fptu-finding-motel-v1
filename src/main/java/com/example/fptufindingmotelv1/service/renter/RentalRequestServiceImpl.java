package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import com.restfb.json.JsonObject;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@Service
public class RentalRequestServiceImpl implements RentalRequestService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    StatusRepository statusRepository;

    public boolean checkExitRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RenterModel renterModel = renterRepository.findByUsername(rentalRequestDTO.getRenterUsername());
            RoomModel roomModel = roomRepository.getOne(rentalRequestDTO.getRoomId());
            RentalRequestModel rentalRequestModel = rentalRequestRepository
                    .findByRentalRenterAndRentalRoom(renterModel, roomModel);
            return rentalRequestModel != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public JSONObject sentRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            if (checkExitRentalRequest(rentalRequestDTO)) {
                return responseMsg("111", "Bạn đã yêu cầu thuê phòng này!", null);
            }
            RentalRequestModel rentalRequestModel = new RentalRequestModel();
            RoomModel roomModel = roomRepository.getOne(rentalRequestDTO.getRoomId());
            RenterModel renterModel = renterRepository.getOne(rentalRequestDTO.getRenterUsername());
            StatusModel statusModel = statusRepository.getOne(rentalRequestDTO.getStatusId());
            rentalRequestModel.setId(rentalRequestDTO.getId());
            rentalRequestModel.setRentalRoom(roomModel);
            rentalRequestModel.setRentalRenter(renterModel);
            rentalRequestModel.setRentalStatus(statusModel);
            //get current date
            if (rentalRequestDTO.getRequestDate() == null) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));

                String currentDate = dateFormat.format(new Date());
                Date date = dateFormat.parse(currentDate);
                rentalRequestModel.setRequestDate(date);
            } else {
                rentalRequestModel.setRequestDate(rentalRequestDTO.getRequestDate());
            }

            rentalRequestRepository.save(rentalRequestModel);
            return responseMsg("000", "Cập nhật thành công!", null);
        } catch (Exception e) {
            return responseMsg("999", "Cập nhật không thành công!", null);
        }
    }

    @Override
    public JSONObject changeStatus(String renterRqId, Long statusId) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.getOne(renterRqId);
            StatusModel statusModel = statusRepository.getOne(statusId);
            rentalRequestModel.setRentalStatus(statusModel);
            rentalRequestRepository.save(rentalRequestModel);
            return responseMsg("000", "Cập nhật thành công!", null);
        } catch (Exception e) {
            return responseMsg("999", "Cập nhật không thành công!", null);
        }

    }

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
