package com.flink.streaming.web.controller.web;

import com.flink.streaming.web.model.dto.JobConfigDTO;
import com.flink.streaming.web.model.dto.PageModel;
import com.flink.streaming.web.model.param.JobConfigParam;
import com.flink.streaming.web.model.vo.DetailJobConfigVO;
import com.flink.streaming.web.model.vo.JobConfigVO;
import com.flink.streaming.web.model.vo.PageVO;
import com.flink.streaming.web.service.JobConfigService;
import com.flink.streaming.web.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

/**
 * @author zhuhuipei
 * @Description:
 * @date 2020-08-16
 * @time 23:24
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class JobConfigController {

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private SystemConfigService systemConfigService;


    @RequestMapping(value = "/listPage")
    public String listPage(ModelMap modelMap, JobConfigParam jobConfigParam) {

        PageModel<JobConfigDTO> pageModel = jobConfigService.queryJobConfig(jobConfigParam);
        PageVO pageVO = new PageVO();
        pageVO.setPageNum(pageModel.getPageNum());
        pageVO.setPages(pageModel.getPages());
        pageVO.setPageSize(pageModel.getPageSize());
        pageVO.setTotal(pageModel.getTotal());
        modelMap.put("pageVO", pageVO);
        modelMap.put("jobConfigParam", jobConfigParam);

        List<JobConfigVO> jobConfigVOList=null;
        if (CollectionUtils.isEmpty(pageModel.getResult())){
            jobConfigVOList=Collections.emptyList();
        }else{
            jobConfigVOList=JobConfigVO.toListVO(pageModel.getResult(),systemConfigService.getYarnRmHttpAddress());
        }
        modelMap.put("jobConfigList",jobConfigVOList );


        modelMap.put("active", "list");
        return "screen/job_config/listPage";
    }

    @RequestMapping("/addPage")
    public String addPage(ModelMap modelMap) {
        return "screen/job_config/addPage";
    }


    @RequestMapping("/editPage")
    public String editPage(ModelMap modelMap, Long id) {
        JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
        modelMap.put("jobConfig", DetailJobConfigVO.toVO(jobConfigDTO));
        return "screen/job_config/editPage";
    }


    @RequestMapping("/detailPage")
    public String detailPage(ModelMap modelMap, Long id) {
        JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
        if (jobConfigDTO == null) {
            modelMap.put("message", "数据不存在");
        } else {
            modelMap.put("jobConfig", DetailJobConfigVO.toVO(jobConfigDTO));
        }
        return "screen/job_config/detailPage";
    }


}