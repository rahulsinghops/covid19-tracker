package com.singhrepo.covid19tracker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(path = "/alldata", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getallData() {
		log.info("get all data called");
		List<CovidStats> result = dataService.getCovidlist();
		log.info("get all data executed ,data:" + result);
		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}

	@RequestMapping(path = "/data", method = RequestMethod.GET)
	 public ModelAndView getData() {   
	//public ModelAndView getData(@RequestParam("page") Optional<Integer> page, 
		//      @RequestParam("size") Optional<Integer> size) { changes for pagination
		log.info("get all data called");
		

		List<CovidStats> result = dataService.getCovidlist();
		long count = result.stream().mapToLong(i -> i.getLatestCases()).sum();
		long newcases = result.stream().mapToLong(i -> i.getNewcases()).sum();
		ModelAndView mv = new ModelAndView();
		/*
		 * // for pagination int currentPage = page.orElse(1); int pageSize =
		 * size.orElse(5);
		 * 
		 * Page<CovidStats> bookPage =
		 * dataService.findPaginated(PageRequest.of(currentPage - 1, pageSize),result);
		 * 
		 * mv.addObject("bookPage", bookPage);
		 * 
		 * int totalPages = bookPage.getTotalPages(); if (totalPages > 0) {
		 * List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages) .boxed()
		 * .collect(Collectors.toList()); mv.addObject("pageNumbers", pageNumbers); }
		 * 
		 * 
		 * // pagnitn
		 */		
		mv.setViewName("home");
		mv.addObject("listdata", result);
		mv.addObject("data", result);
		mv.addObject("totalcount", count);
		mv.addObject("newcount", newcases);
		mv.addObject("newcountIndia", dataService.getNewcasesIndia());
		log.info("get all data executed ,data:" + result);
		return mv;

	}

	// search functionality
	// @RequestMapping(path = "/data/{country}",method = RequestMethod.GET)
	@RequestMapping(path = "/data", method = RequestMethod.POST)
	// public ModelAndView getSearchData(@PathVariable("country") String country)
//	public ModelAndView getSearchData(@RequestParam("cname") String country) {        --signautre chnged due to pagination
	public ModelAndView getSearchData(@RequestParam("cname") String country) {
	log.info("getSearchData executed with input :" + country);
	
	  if (country.equals("") || country == null || country.equals("Choose...")) {return getData(); }
		 /* * pagination getData(new optio,0); }---changed due to
	 */

		List<CovidStats> result = dataService.getCovidlist();
		long count = result.stream().mapToLong(i -> i.getLatestCases()).sum();
		long newcases = result.stream().mapToLong(i -> i.getNewcases()).sum();
		List<CovidStats> filteredResult = result.stream().filter(i -> i.getCountry().equalsIgnoreCase(country))
				.collect(Collectors.toList());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("listdata", result);
		if (filteredResult != null && filteredResult.size() > 0)

			mv.addObject("data", filteredResult);
		else
			mv.addObject("data", new ArrayList<CovidStats>());
		mv.addObject("totalcount", count);
		mv.addObject("newcount", newcases);
		mv.addObject("newcountIndia", dataService.getNewcasesIndia());
		log.info("get getSearchData executed ,data:" + filteredResult);
		return mv;

	}

}
