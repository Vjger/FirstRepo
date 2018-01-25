package it.desimone.gsheets.dto;

import java.util.List;

public interface SheetRow extends Cloneable{

	public Integer getSheetRow();
	public void setSheetRow(Integer sheetRow);
	
	public List<Object> getData();
	public void setData(List<Object> data);
	
	public List<Integer> keyCols();
	
	public Object clone() throws CloneNotSupportedException;
}
