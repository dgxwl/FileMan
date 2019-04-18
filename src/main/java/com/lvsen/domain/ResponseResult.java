package com.lvsen.domain;

public class ResponseResult {
	public static Integer SUCCESS = Integer.valueOf(1);

	private Integer result = Integer.valueOf(1);
	private String message;
	private Object data;
	private Integer total;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	private Object extraData;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getExtraData() {
		return this.extraData;
	}

	public void setExtraData(Object extraData) {
		this.extraData = extraData;
	}

	public ResponseResult() {
	}

	public ResponseResult(String message) {
		this.message = message;
	}

	public ResponseResult(int result, String message) {
		this.result = Integer.valueOf(result);
		this.message = message;
	}
}
