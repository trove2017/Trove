package fileObject;

import java.io.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dataType.stringArray;

import java.io.FileWriter;
import java.util.ArrayList;

public class excel2CSV {  
	ArrayList<stringArray> targetStringArray=new ArrayList<stringArray>();
	ArrayList<stringArray> sourceStringArray=new ArrayList<stringArray>();
	ArrayList<stringArray> networkStringArray=new ArrayList<stringArray>();
	
	stringArray targetHeader=new stringArray(11);
	stringArray sourceHeader=new stringArray(2);
	stringArray networkHeader=new stringArray(9);
	
	public excel2CSV() { }

	private void getTargetStringArray(XSSFSheet my_worksheet) {
		int rowNum=0;
		XSSFRow thisRow;
		
		do
		{
			thisRow=my_worksheet.getRow(rowNum);
			int numCol=thisRow.getLastCellNum();
			if(rowNum==0)
			{
				for(int c=0; c<numCol; c++)
				{
					if(thisRow.getCell(c)==null)
						targetHeader.setStringArrayAtIndex(c, "");
					else
					{
						thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
						targetHeader.setStringArrayAtIndex(c, thisRow.getCell(c).toString());
					}
				}
			}
			else
			{
				stringArray arr=new stringArray(targetHeader.size());
				for(int c=0; c<numCol; c++)
				{
					if(thisRow.getCell(c)==null)
						arr.setStringArrayAtIndex(c, "");
					else
					{
						thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
						arr.setStringArrayAtIndex(c, thisRow.getCell(c).toString());
					}
				}
				if(targetStringArray.contains(arr)==false)
					targetStringArray.add(arr);
			}
			rowNum++;
		}while(my_worksheet.getRow(rowNum)!=null);
	}
	
	private void getSourceStringArray(XSSFSheet my_worksheet) {
		int numRow=my_worksheet.getLastRowNum();
		
		for(int r=0; r<=numRow; r++)
		{
			XSSFRow thisRow=my_worksheet.getRow(r);
			int numCol=thisRow.getLastCellNum();
			if(r==0)
			{
				for(int c=0; c<numCol; c++)
				{
					if(thisRow.getCell(c)==null)
						sourceHeader.setStringArrayAtIndex(c, "");
					else
					{
						thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
						sourceHeader.setStringArrayAtIndex(c, thisRow.getCell(c).toString());
					}
				}
			}
			else
			{
				stringArray arr=new stringArray(sourceHeader.size());
				for(int c=0; c<numCol; c++)
				{
					if(thisRow.getCell(c)==null)
						arr.setStringArrayAtIndex(c, "");
					else
					{
						thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
						arr.setStringArrayAtIndex(c, thisRow.getCell(c).toString());
					}
				}
				if(sourceStringArray.contains(arr)==false)
					sourceStringArray.add(arr);
			}
		}
	}
	
	private void getNetworkStringArray(XSSFSheet my_worksheet) {
		int numRow=my_worksheet.getLastRowNum();
		
		for(int r=0; r<=numRow; r++)
		{
			XSSFRow thisRow=my_worksheet.getRow(r);
			int numCol=thisRow.getLastCellNum();
			if(r==0)
			{
				for(int c=0; c<numCol; c++)
				{
					if(thisRow.getCell(c)==null)
						networkHeader.setStringArrayAtIndex(c, "");
					else
					{
						thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
						networkHeader.setStringArrayAtIndex(c, thisRow.getCell(c).toString());
					}
				}
			}
			else
			{
				stringArray arr=new stringArray(networkHeader.size());
				for(int c=0; c<numCol; c++)
				{
					if(thisRow.getCell(c)==null)
						arr.setStringArrayAtIndex(c, "");
					else
					{
						thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
						arr.setStringArrayAtIndex(c, thisRow.getCell(c).toString());
					}
				}
				if(networkStringArray.contains(arr)==false)
					networkStringArray.add(arr);
			}
		}
	}
	
	public void getAllStringArray(String directory, String fName) {
		targetStringArray=new ArrayList<stringArray>();
		sourceStringArray=new ArrayList<stringArray>();
		networkStringArray=new ArrayList<stringArray>();
		
		targetHeader=new stringArray(11);
		sourceHeader=new stringArray(2);
		networkHeader=new stringArray(9);
		
		try {
			//First we read the Excel file in binary format into FileInputStream
			FileInputStream input_document = new FileInputStream(new File(directory+fName));
			// Read workbook into HSSFWorkbook
			XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document); 
			int numSheet=my_xls_workbook.getNumberOfSheets();

			for(int s=0; s<numSheet; s++)
			{
				//Read worksheet into HSSFSheet
				XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(s); 
				
				if(s==0)
					getTargetStringArray(my_worksheet);
				else if(s==1)
					getSourceStringArray(my_worksheet);
				else if(s==2)
					getNetworkStringArray(my_worksheet);					
				else
				{
					System.out.println("excel2CSV.java error: numSheet>2. s="+s);
					return;
				}
			}
			//we created our file..!!
			input_document.close(); //close xls
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<stringArray> getTargetStringArray() {
		return targetStringArray;
	}
	
	public ArrayList<stringArray> getSourceStringArray() {
		return sourceStringArray;
	}
	
	public ArrayList<stringArray> getNetworkStringArray() {
		return networkStringArray;
	}
	
	public stringArray getTargetHeader() {
		return targetHeader;
	}
	
	public stringArray getSourceHeader() {
		return sourceHeader;
	}
	
	public stringArray getNetworkHeader() {
		return networkHeader;
	}
	
	public void convert(String directory, String fName) {
		try {
			//First we read the Excel file in binary format into FileInputStream
			FileInputStream input_document = new FileInputStream(new File(directory+fName));
			// Read workbook into HSSFWorkbook
			XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document); 
			int numSheet=my_xls_workbook.getNumberOfSheets();

			for(int s=0; s<numSheet; s++)
			{
				//Read worksheet into HSSFSheet
				XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(s); 
				int numRow=my_worksheet.getLastRowNum();

				// OpenCSV writer object to create CSV file
				File f=new File(directory+my_worksheet.getSheetName()+".csv");
				if(f.exists())
				{
					if(f.delete())
						System.out.println(directory+my_worksheet.getSheetName()+".csv deleted successfully");
					else
						System.out.println("WARNING: Fail to delete "+directory+my_worksheet.getSheetName()+".csv");
				}

				BufferedWriter my_csv=new BufferedWriter(new FileWriter(directory+my_worksheet.getSheetName()+".csv"));
				for(int r=0; r<=numRow; r++)
				{
					XSSFRow thisRow=my_worksheet.getRow(r);
					int numCol=thisRow.getLastCellNum();

					for(int c=0; c<numCol; c++)
					{
						if(thisRow.getCell(c)==null)
							my_csv.append("");
						else
						{
							thisRow.getCell(c).setCellType(Cell.CELL_TYPE_STRING);
							my_csv.append(thisRow.getCell(c).toString());
						}
						if(c<numCol-1)
							my_csv.append(",");
					}
					my_csv.newLine();
				}

				my_csv.close(); //close the CSV file
			}
			//we created our file..!!
			input_document.close(); //close xls
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void printTarget(){
		for(int i=0; i<targetStringArray.size(); i++)
		{
			stringArray arr=new stringArray(11);
			arr=targetStringArray.get(i);
			arr.print();
		}
	}
}