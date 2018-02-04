package it.desimone.gheetsaccess.gsheets.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSheetRow implements SheetRow {

	protected Integer sheetRowNumber;
	protected List<Object> data;
	
	public Integer getSheetRowNumber() {
		return sheetRowNumber;
	}
	public void setSheetRowNumber(Integer sheetRowNumber) {
		this.sheetRowNumber = sheetRowNumber;
	}
	public List<Object> getData() {
		initializeData(getDataSize());
		data.set(data.size()-1, "=row()");
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
		sheetRowNumber = Integer.valueOf((String)data.get(data.size()-1));
	}
	
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
	protected void initializeData(Integer dimArray){
		data = Arrays.asList(new Object[dimArray]);
		Collections.fill(data, "");
	}
}
