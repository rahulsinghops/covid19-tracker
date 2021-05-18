package com.singhrepo.covid19tracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.singhrepo.covid19tracker.model.CovidStats;
import com.singhrepo.covid19tracker.service.CovidDataService;

@Controller
@RequestMapping("/covid")
public class CovidStatsService {
	@Autowired
	CovidDataService dataService;
	Logger log = LoggerFactory.getLogger(CovidStatsService.class);
	
	@RequestMapping(path = "/alldata",method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getallData()
	{
		log.info("get all data called");
		List<CovidStats> result =dataService.getCovidlist();
		log.info("get all data executed ,data:"+result);
		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(path = "/data",method = RequestMethod.GET)
	public ModelAndView getData()
	{
		log.info("get all data called");
		List<CovidStats> result =dataService.getCovidlist();
		long count =result.stream().mapToLong(i->i.getLatestCases()).sum();
		long newcases =result.stream().mapToLong(i->i.getNewcases()).sum();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("data", result);
		mv.addObject("totalcount", count);
		mv.addObject("newcount", newcases);
		mv.addObject("newcountIndia", dataService.getNewcasesIndia());
		log.info("get all data executed ,data:"+result);
		return mv;
		
	}

}
