package com.example.fptufindingmotelv1.controller.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.service.renter.RentalRequestService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManageRentalRequestController {

    @Autowired
    RentalRequestService rentalRequestService;

    @ResponseBody
    @RequestMapping(value = "/sent-rental-request")
    public JSONObject sentRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
       return rentalRequestService.sentRentalRequest(rentalRequestDTO);
    }

    @ResponseBody
    @RequestMapping(value = "/change-rental-request-status", method = RequestMethod.POST)
    public JSONObject changeRentalRequestStatus(@RequestParam(value = "rentalRequestId") String rentalRequestId
            , @RequestParam(value = "StatusId") Long statusId) {
            return rentalRequestService.changeStatus(rentalRequestId, statusId);
    }

//    @ResponseBody
//    @RequestMapping(value = "/search-retal-request")
//    public JSONObject searchRentalRequest() {
//        try {
//            return rentalRequestService.changeStatus(rentalRequestId, statusId)
//                    ?  responseMsg("000", "Success!", null)
//                    :  responseMsg("999", "System error!", null);
//        } catch (Exception e) {
//            return responseMsg("001", "Something error!", null);
//        }
//    }

}
