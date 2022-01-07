package com.singhrepo.covid19tracker.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.singhrepo.covid19tracker.model.CovidStats;

@Service
public class CovidDataService {
	Logger log = LoggerFactory.getLogger(CovidDataService.class);
	List<CovidStats> covidlist = new ArrayList<>();

	long newcasesIndia;
	@Autowired
	RestTemplate restTemplate;

	@Value("${data.url}")
	private String DATAURL;
	// private String DATAURL =
	// "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	public List<CovidStats> getCovidlist() {
		return covidlist;
	}

	public long getNewcasesIndia() {
		return newcasesIndia;
	}

	public Logger getLog() {
		return log;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public String getDATAURL() {
		return DATAURL;
	}

	// post construct is used to execute method just after springboot is done
	// starting app
	@PostConstruct
	//@Scheduled(cron = " * * 1 * * *")
	@Scheduled(cron = " * * 6,14 * * *")
	public void fetchVirusData() {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(URI.create(DATAURL), String.class);
		if (responseEntity.getStatusCodeValue() == 200) {
			// System.out.println(responseEntity.getBody());
			this.covidlist = csvformatHandling(responseEntity.getBody());
			// return responseEntity.getBody();
		} else {
			System.out.println(responseEntity.getStatusCodeValue());
			// return responseEntity.getStatusCodeValue() + "";
		}
	}

	public List<CovidStats> csvformatHandling(String message) {

		List<CovidStats> covidlist = new ArrayList<>();
		Reader in = new StringReader(message);
		Iterable<CSVRecord> records;
		try {

			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {
				CovidStats stats = new CovidStats();

				/*
				 * String state; if(record.get("Province/State").equals("") ||
				 * record.get("Province/State") == null) { state="Combined";
				 * 
				 * } else { System.out.println(record.get("Province/State")+"*");
				 * state=record.get("Province/State"); }
				 */
				stats.setState(!record.get("Province/State").equals("") ? record.get("Province/State") : "Combined");
				stats.setCountry(record.get("Country/Region"));
				if (record.get("Country/Region").equals("India")) {
					if (record.get(record.size() - 1) != null && record.get(record.size() - 1) != ""
							&& record.get(record.size() - 2) != null && record.get(record.size() - 2) != "") {

						this.newcasesIndia = Long.parseLong(record.get(record.size() - 1).trim())
								- Long.parseLong(record.get(record.size() - 2).trim());
					}

				}
				long latestcases = 0;
				long secondlatestcases = 0;
				if (record.get(record.size() - 1) != null && record.get(record.size() - 1) != "") {
					latestcases = Long.parseLong(record.get(record.size() - 1).trim());
					stats.setLatestCases(Long.parseLong(record.get(record.size() - 1).trim()));
				}
				if (record.get(record.size() - 2) != null && record.get(record.size() - 2) != "") {
					secondlatestcases = Long.parseLong(record.get(record.size() - 2).trim());
					stats.setNewcases(latestcases - secondlatestcases);
				}
				// System.out.println(stats);
				covidlist.add(stats);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("error occured " + e.getMessage());
		}

		return covidlist;
	}
	
	
	
	/*
	 * //pagination public Page<CovidStats> findPaginated(Pageable
	 * pageable,List<CovidStats> cases) { int pageSize = pageable.getPageSize(); int
	 * currentPage = pageable.getPageNumber(); int startItem = currentPage *
	 * pageSize; List<CovidStats> list;
	 * 
	 * if (cases.size() < startItem) { list = Collections.emptyList(); } else { int
	 * toIndex = Math.min(startItem + pageSize, cases.size()); list =
	 * cases.subList(startItem, toIndex); }
	 * 
	 * Page<CovidStats> bookPage = new PageImpl<CovidStats>(list,
	 * PageRequest.of(currentPage, pageSize), cases.size());
	 * 
	 * return bookPage; }
	 */
}
