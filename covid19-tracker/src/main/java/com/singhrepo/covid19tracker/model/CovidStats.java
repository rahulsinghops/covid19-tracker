package com.singhrepo.covid19tracker.model;

public class CovidStats {

	private String state;
	private String country;
	private long latestCases;
	private long newcases;
	
	
	public long getLatestCases() {
		return latestCases;
	}
	public void setLatestCases(long latestCases) {
		this.latestCases = latestCases;
	}
	public long getNewcases() {
		return newcases;
	}
	public void setNewcases(long newcases) {
		this.newcases = newcases;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	@Override
	public String toString() {
		return "CovidStats [state=" + state + ", country=" + country + ", latestCases=" + latestCases + ", newcases="
				+ newcases + "]";
	}
	public CovidStats(String state, String country, long latestCases, long newcases) {
		super();
		this.state = state;
		this.country = country;
		this.latestCases = latestCases;
		this.newcases = newcases;
	}
	public CovidStats() {
		super();
	}

	
}
