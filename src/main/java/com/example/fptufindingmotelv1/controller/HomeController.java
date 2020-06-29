package com.example.fptufindingmotelv1.controller;
import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.PagerModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.PostModelRepository;
import com.example.fptufindingmotelv1.service.displayall.PostService;
import com.example.fptufindingmotelv1.service.displayall.RenterService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @Autowired
    PostModelRepository postModelRepository;

    @Autowired
    RenterService renterService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHomepage(Model model,
                              @RequestParam(name = "page") Optional<Integer> page,
                              @RequestParam(name = "pageSize")  Optional<Integer> size,
                              @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){

        // sort post by date
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("createDate").ascending();
        }
        // Paging
        int evalPageSize = size.orElse(Constant.INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
        Pageable pageable = PageRequest.of(evalPage, evalPageSize,sortable);
        List<PostModel> postList =  postModelRepository.findAll();
        Page<PostModel> postPage =  postModelRepository.findAll(pageable);

        // Pass PostModel List to PostDTO
        List<PostDTO> postDTOs= new ArrayList<>();
        PostDTO postDTO= null;
        // check login
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            // get username
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            if(userDetails.getUserModel().getRole().getId()==2){
                for (int i = 0; i< postList.size(); i++){
                    postDTO=new PostDTO(postList.get(i));
                    postDTO.setIsLord("display:none");
                    postDTOs.add(postDTO);
                }
            }else{
                RenterModel renter =renterService.findOne(userDetails.getUserModel().getUsername());
                // Set color for DTO
                for(int j = 0; j < postList.size(); j++){
                    postDTO = new PostDTO(postList.get(j));
                    if(renter.getPosts().contains(postList.get(j))){
                        postDTO.setColor("color: red");
                    }else {
                        postDTO.setColor("color: white");
                    }
                    postDTOs.add(postDTO);
                }
            }
        } else{
            for (int i = 0; i< postList.size(); i++){
                postDTO=new PostDTO(postList.get(i));
                postDTOs.add(postDTO);
            }
        }
        int total = postDTOs.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);
        List<PostDTO> sublist = new ArrayList<>();
        if (start <= end) {
            sublist = postDTOs.subList(start, end);
        }
        Page<PostDTO> listDTO  = new PageImpl<>(sublist, pageable, postDTOs.size());
        // pass data and direct
        model.addAttribute("posts",listDTO);

        PagerModel pager = new PagerModel(listDTO.getTotalPages(),listDTO.getNumber(),Constant.BUTTONS_TO_SHOW);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", Constant.PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "index";
    }


    @GetMapping("/post-new-room")
    public String getPostNewRoom(){
        return "post-new-room";
    }


    @GetMapping("/post-detail")
    public String getPostDetail(Model model,@PathParam("id") String id){
        model.addAttribute("post",postService.findOne(Long.valueOf(id)));
        return "post-detail";
    }
    @ResponseBody
    @PostMapping(value = "/api-post-detail")
    public JSONObject viewDetail(@RequestParam(name = "id")String id){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("msg","Get id success");
        return jsonObject;
    }
}
