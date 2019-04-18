package com.lvsen.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyQuery {
	private String orderField;
	private String orderType;
	private int limit = 10;
	private int page = 1;
	private List<SearchFilter> filters = new ArrayList<>();

	public MyQuery() {
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderfield) {
		if (orderfield.length() > 20) {
			return ;
		}
		this.orderField = orderfield;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		if (!orderType.equalsIgnoreCase("ASC") || !orderType.equalsIgnoreCase("DESC")) {
			return ;
		}
		this.orderType = orderType;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public List<SearchFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<SearchFilter> filters) {
		this.filters = filters;
	}

	public int getOffset() {
		return (page - 1) * limit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + limit;
		result = prime * result + ((orderField == null) ? 0 : orderField.hashCode());
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + page;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyQuery other = (MyQuery) obj;
		if (limit != other.limit)
			return false;
		if (orderField == null) {
			if (other.orderField != null)
				return false;
		} else if (!orderField.equals(other.orderField))
			return false;
		if (orderType == null) {
			if (other.orderType != null)
				return false;
		} else if (!orderType.equals(other.orderType))
			return false;
		if (page != other.page)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MyQuery [orderField=" + orderField + ", orderType=" + orderType + ", limit=" + limit + ", page=" + page
				+ ", filters=" + filters + "]";
	}

}
