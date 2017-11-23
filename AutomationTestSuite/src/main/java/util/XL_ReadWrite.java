package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import core.Globals;


public class XL_ReadWrite { 
	private String path;
	private FileInputStream fin = null;
	public FileOutputStream fout = null;
	private Workbook workbook = null;
	private Sheet sheet = null;
	private Row row = null;
	public Cell cell = null;
	

	public XL_ReadWrite(String path) throws Exception{
	  this.path = path;
		try{ 
			fin = new FileInputStream(path);
			workbook = WorkbookFactory.create(fin);
			sheet = workbook.getSheetAt(0);
			fin.close();
	    } catch(FileNotFoundException e){
			throw new Exception("Exception occurred while finding the file : " + path + " while XLS initialize .Exception details : "+e.getMessage());
	    } catch(IOException e){
	    	throw new Exception("I/O interrupted exception occurred while XLS initialize .Exception details : " + e.getMessage());
	    } catch(Exception e){
	    	throw new Exception("Exception occurred while XLS initialize .Exception details : "+ e.getMessage());
	  }
	}

	public int getRowCount(String sheetName) throws Exception{
		int number = 0;
		try{ 
			int index = workbook.getSheetIndex(sheetName);
			if(index < 0)
			   return 0;
			else{
			   sheet = workbook.getSheetAt(index);
			   number = sheet.getLastRowNum()+1;
			   return number;
			}
	    } catch(Exception e){
	    	throw new Exception("Exception occurred while getting row count for sheetname :" + sheetName+" . Exception details : "+e.getMessage());
	    }
	}
	
/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			getColCount(int RowNum,String sheetName)	
	DESCRIPTION:	    gets the total used column count in a row in a sheet 
	PARAMETERS: 		int RowNum,String sheetName
	RETURNS:			integer Column number
	EXAMPLE:	        getColCount(int RowNum,String sheetName)	
	REVISION HISTORY: 
	------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :     
	------------------------------------------------------------------------------------------------------------------------------------------------------------
*/  public int getColCount(int RowNum,String sheetName) throws Exception{
	 	int iCol = 0;   
	 	try{
			int index = workbook.getSheetIndex(sheetName);
		    sheet = workbook.getSheetAt(index);
			row = sheet.getRow(RowNum);
			iCol = row.getLastCellNum();
			return iCol;
	    } catch(Exception e){		   
		    throw new Exception("Exception occurred while getting col count for row number : " + RowNum + " in sheet name : " +sheetName+" .Exception details : "+e.getMessage());
	    }
	}

/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			getCellData(String sheetName,int rowNum,String colName)	
	DESCRIPTION:	    gets the cell data from a specific sheet,specific row and specific column name 
	PARAMETERS: 		String sheetName,int rowNum,String colName
	RETURNS:			String Cell Data
	EXAMPLE:	        getCellData(String sheetName,int rowNum,String colName)		
	REVISION HISTORY: 
	------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :      
	------------------------------------------------------------------------------------------------------------------------------------------------------------
*/  public String getCellData(String sheetName,int rowNum,String colName) throws Exception{
	    try{
		  int index = workbook.getSheetIndex(sheetName);
		  if (index < 0) return Globals.GC_DATASHEET_ERR;
		  if (rowNum < 0) return Globals.GC_EMPTY;
		  int colNum = -1; 
		  sheet = workbook.getSheetAt(index);
		  row = sheet.getRow(0);
		  colNum = getColNum(sheetName,rowNum,colName);
				   
		  if(colNum == -1) return Globals.GC_EMPTY;
		  row = sheet.getRow(rowNum);
		  if(row == null) return Globals.GC_EMPTY;
		  cell = row.getCell(colNum);
		  if(cell == null) return Globals.GC_EMPTY;
		  
			switch (cell.getCellTypeEnum()) {
				case STRING:
						return cell.getRichStringCellValue().getString();
				case NUMERIC:   
					   if (DateUtil.isCellDateFormatted(cell)) {						 
						   String date = new SimpleDateFormat("MM/dd/yyyy").format(cell.getDateCellValue());
						   return date;						  
					   } else {   						   
						   double d = cell.getNumericCellValue();
						   if(d == Math.floor(d)){
							 cell.setCellType(CellType.STRING);
							 return cell.getRichStringCellValue().getString(); 
						   }else{
							 String num = new Double(d).toString();
							 return num;
						   }
					   }
				case BLANK:
					   return Globals.GC_EMPTY;
				case BOOLEAN:
					   return String.valueOf(cell.getBooleanCellValue());
				default:
					   return "Default";
		    }		    
	    } catch(Exception e){
	    	 throw new Exception("Exception occurred while getting cell data from Row: " + rowNum + "and Column Name : " + colName+". Exception details : "+e.getMessage());
	    }
	}

/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			getCellData(String sheetName,int rowNum,int colNum)	 OVERLOADED
	DESCRIPTION:	    gets the cell data from a specific sheet,specific row and specific col number
	PARAMETERS: 		String sheetName,int rowNum,int colNum
	RETURNS:			String Cell Data
	EXAMPLE:	        getCellData(String sheetName,int rowNum,int colNum)	
	REVISION HISTORY: 
	------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :      
	------------------------------------------------------------------------------------------------------------------------------------------------------------
*/  public String getCellData(String sheetName,int rowNum,int colNum) throws Exception{
	    try{
		   int index = workbook.getSheetIndex(sheetName);
		   if (index < 0) return Globals.GC_DATASHEET_ERR;
		   if(rowNum < 0) return Globals.GC_EMPTY;
		   sheet = workbook.getSheetAt(index);
		   row = sheet.getRow(0);

		   if(colNum == -1) return Globals.GC_EMPTY;
		   row = sheet.getRow(rowNum);
		   if(row == null) return Globals.GC_EMPTY;
		   cell = row.getCell(colNum);
		   if(cell == null) return Globals.GC_EMPTY;
		   cell.setCellType(CellType.STRING);
		
		    switch (cell.getCellTypeEnum()) {
				case STRING:
					   return cell.getRichStringCellValue().getString();
				case NUMERIC:   
					   if (DateUtil.isCellDateFormatted(cell)) {						
						   String date = new SimpleDateFormat("MM/DD/yyyy").format(cell.getDateCellValue());
						   return date;						
					   } else {   						 
						   double d = cell.getNumericCellValue();
						   if(d == Math.floor(d)){
							 cell.setCellType(CellType.STRING);
							 return cell.getRichStringCellValue().getString(); 
						   }else{
							 String num = new Double(d).toString();
							 return num;
						   }						   						   
					   }
				case BLANK:
					   return Globals.GC_EMPTY;
				case BOOLEAN:
					   return String.valueOf(cell.getBooleanCellValue());
				default:
					   return "Default";
	        }
   	    } catch(Exception e){
   	    	throw new Exception("Exception occurred while getting cell data from Row: " + rowNum + "and Column number : " + colNum+". Exception details : "+e.getMessage());
	    }
	}

/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			getColNum(String sheetName,int rowNum,String colName)
	DESCRIPTION:	    gets the column number from a specific sheet and specific column name 
	PARAMETERS: 		String sheetName,int rowNum,String colName
	RETURNS:			int Column number
	EXAMPLE:	        getColNum(String sheetName,int rowNum,String colName)	
	REVISION HISTORY: 
    ------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :      
    ------------------------------------------------------------------------------------------------------------------------------------------------------------
*/  public int getColNum(String sheetName,int rowNum,String colName) throws Exception{
		try{
		   int index = workbook.getSheetIndex(sheetName);
		   if (index < 0) return -1;
		   if (rowNum < 0) return -1;
		   int colNum = -1; 
		   sheet = workbook.getSheetAt(index);
		   row = sheet.getRow(0);
		   for(int iCol = 0;iCol<=row.getLastCellNum()-1;iCol++){
			   if(row.getCell(iCol)==null){
				   row.createCell(iCol,CellType.STRING);
				   setCellData(sheetName,0,iCol,"");
			   }
			   if(row.getCell(iCol).getStringCellValue().trim().equalsIgnoreCase(colName.trim())){
				colNum = iCol;break;     
			   }
		     } 
		   	   return colNum;
	    } catch(Exception e){
	    	throw new Exception("Exception occurred while getting Column number for Column name : " + colName+". Exception details : "+e.getMessage());
		}
    }

/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			setCellData(String sheetName,int rowNum,int colNum,String Data)  OVERLOADED
	DESCRIPTION:	    sets a cell value on a specific sheet,specific column column in a specific row
	PARAMETERS: 		String sheetName,int rowNum,int colNum,String Data
	RETURNS:			null
	EXAMPLE:	        setCellData(String sheetName,int rowNum,int colNum,String Data)	
	COMMENTS:           To save the changes made in Excel, use workbook and fout of XL_ReadWrite class
	                    (e.g. XL.workbook.write(XL.fout);XL.fout.close();)	
	REVISION HISTORY: 
	------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :      
	------------------------------------------------------------------------------------------------------------------------------------------------------------
*/	public void setCellData(String sheetName,int rowNum,int colNum,String Data) throws Exception{
	    try{
		   fout = new FileOutputStream(path);	
		   CellStyle style = workbook.createCellStyle();
		   int index = workbook.getSheetIndex(sheetName);
		   
		   sheet = workbook.getSheetAt(index);
		   row = sheet.getRow(rowNum);
		   if(row == null) row = sheet.createRow(rowNum);
		    
		   cell = row.createCell(colNum);   
		   setStyle(cell,rowNum);
		   cell.setCellType(CellType.STRING);
		   style.setAlignment(HorizontalAlignment.CENTER);
		   cell.setCellStyle(style);
		   cell.setCellValue(Data);		   
		} catch(Exception e){		 
		   throw new Exception("Exception occurred while setting data for Column: " + colNum + " and Row: "+ rowNum+". Exception details : "+e.getMessage());  			  
	    }
    }

/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			isSheetExist(String sheetName)
	DESCRIPTION:	    Confirms if the given sheet exist in a particular file
	PARAMETERS: 		String sheetName
	RETURNS:			null
	EXAMPLE:	        isSheetExist(String sheetName)	
	REVISION HISTORY: 
	------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :      Date : 03-10-2013       
	------------------------------------------------------------------------------------------------------------------------------------------------------------
*/	public boolean isSheetExist(String sheetName) throws Exception{
		try {
			int index = workbook.getSheetIndex(sheetName);
			if(index < 0) return Globals.GC_FALSE;
			return Globals.GC_TRUE;
		}catch (Exception e) {
			throw new Exception("Exception occurred checking if sheet exist : " + sheetName+". Exception details : "+e.getMessage());
		}
	}

/*  ------------------------------------------------------------------------------------------------------------------------------------------------------------
	FUNCTION:			setStyle(Cell cell,int rowNum)
	DESCRIPTION:	    Sets a Cell style  
	PARAMETERS: 		Cell cell,int rowNum
	RETURNS:			Nothing
	EXAMPLE:	        setStyle(Cell cell,int rowNum)	
	REVISION HISTORY: 
	------------------------------------------------------------------------------------------------------------------------------------------------------------
	Author :      
	------------------------------------------------------------------------------------------------------------------------------------------------------------
*/	public void setStyle(Cell cell,int rowNum) throws Exception{
			try{	
			   CellStyle style = workbook.createCellStyle();
			   Font font = workbook.createFont();
			   if(rowNum > 0){
				  style.setFillForegroundColor(IndexedColors.WHITE.getIndex());   
			   }else{
				   style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				   font.setBold(true);
				   style.setFont(font);
			   }			
			   style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   style.setBorderLeft(BorderStyle.THIN);
			   style.setBorderRight(BorderStyle.THIN);
			   style.setBorderTop(BorderStyle.THIN);
			   style.setBorderBottom(BorderStyle.THIN);
			   style.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			   cell.setCellStyle(style);
			}catch(Exception e) {
				throw new Exception("Exception occurred while setting cell style ");
			}
		}

	public void clearXL() throws Exception{	
		try{
			this.workbook.close();					
		}catch(Exception e){
			throw new Exception("Exception occurred while closing workbook. Exception details :" +e.getMessage());
		}
			
	}

	public void saveXL() throws Exception{		
		try{
			fout = new FileOutputStream(path);	
			workbook.write(fout);
			fout.close();
		}catch(Exception e){
			throw new Exception("Exception occurred while saving excel. Exception details :" +e.getMessage());
		}	
	}	
}

