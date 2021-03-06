package com.example.fptufindingmotelv1.controller.admin.managereport;

import com.example.fptufindingmotelv1.service.admin.managereport.DeleteReportService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeleteReportController {

    @Autowired
    DeleteReportService deleteReportService;


    @ResponseBody
    @RequestMapping(value = "/delete-report")
    public JSONObject deleteReport(@RequestParam(value = "reportId") String reportId) {
        try {
            deleteReportService.deleteReport(reportId);
            return Constant.responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
