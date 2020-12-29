package com.turing.b2c.model.dto;

import java.io.Serializable;

public class MsgBox implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean code;
	private String msg;

	public MsgBox() {
	}

	public MsgBox(Boolean code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Boolean getCode() {
		return code;
	}

	public void setCode(Boolean code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
