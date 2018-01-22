package it.desimone.gsheets.dto;

import java.util.List;

public abstract class AbstractSheetRow implements SheetRow {

	protected Integer sheetRow;
	protected List<Object> data;
	
	public Integer getSheetRow() {
		return sheetRow;
	}
	public void setSheetRow(Integer sheetRow) {
		this.sheetRow = sheetRow;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
}
