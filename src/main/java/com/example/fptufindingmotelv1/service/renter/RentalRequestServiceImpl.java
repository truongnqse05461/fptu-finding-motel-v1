package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import com.restfb.json.JsonObject;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @Autowired
    UserRepository userRepository;

    public boolean checkExitRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RenterModel renterModel = (RenterModel) userRepository.findByUsername(rentalRequestDTO.getRenterUsername());
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            RentalRequestModel rentalRequestModel = rentalRequestRepository
                    .findByRentalRenterAndRentalRoom(renterModel, roomModel);
            return rentalRequestModel != null;
        } catch (Exception e) {
            e.printStackTrace();
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
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            RenterModel renterModel = renterRepository.findByUsername(rentalRequestDTO.getRenterUsername());
            StatusModel statusModel = statusRepository.findById(rentalRequestDTO.getStatusId()).get();
            rentalRequestModel.setId(rentalRequestDTO.getId());
            rentalRequestModel.setRentalRoom(roomModel);
            rentalRequestModel.setRentalRenter(renterModel);
            rentalRequestModel.setRentalStatus(statusModel);

            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_ONLY_DATE);
            if(createdDate.after(rentalRequestDTO.getStartDate())){
                return responseMsg("001", "Vui lòng chọn ngày bắt đầu sau ngày " + sdf.format(createdDate), null);
            }
            //set start date
            rentalRequestModel.setStartDate(rentalRequestDTO.getStartDate());
            rentalRequestModel.setRequestDate(createdDate);

            rentalRequestRepository.save(rentalRequestModel);
            return responseMsg("000", "Cập nhật thành công!", null);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return responseMsg("999", "Cập nhật không thành công!", null);
        }
    }

    @Override
    public JSONObject searchRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            ArrayList<RentalRequestModel> renterModels = rentalRequestRepository.searchRentalRequest(
                    rentalRequestDTO.getId(), rentalRequestDTO.getRenterUsername(), rentalRequestDTO.getRoomId()
                    ,rentalRequestDTO.getStatusId());

            ArrayList<RentalRequestDTO> requestDTOS = new ArrayList<>();
            for (RentalRequestModel requestModel : renterModels) {
                RentalRequestDTO requestDTO = new RentalRequestDTO(requestModel);
                requestDTOS.add(requestDTO);
            }
            return renterModels.size() > 0 ? responseMsg("000", "OK", requestDTOS   )
                                            : responseMsg("111", "No Data!", null);
        } catch (Exception e) {
            return responseMsg("999", "System Error!", null);
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
