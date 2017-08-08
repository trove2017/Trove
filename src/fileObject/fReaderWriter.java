package fileObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class fReaderWriter{
	private BufferedWriter writer;
	private BufferedReader reader;
	
	public fReaderWriter() {

	}

	public void writeToFileWithBufferedWriter(String directory, String filename, String dataStream, boolean destroyExistingFile)
	{
		try {
			File destination = new File(directory);
			if (!destination.exists()) {
				System.out.println("filename:" + filename);
				System.out.println("destination dir doesnt exists:" + directory);
				destination.mkdir();
			}
			if (destroyExistingFile)
			{
				File f = new File(directory+ filename);
				if(f.exists()) { f.delete(); }
			}
			writer = new BufferedWriter(new FileWriter(directory+ filename));
			writer.write(dataStream);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> readFromFileWithBufferedReaderArraySkipHeader(String directory_and_filename)
	{
		ArrayList<String> array=new ArrayList<String>();
		String nextLine;
		try {
			reader = new BufferedReader(new FileReader(directory_and_filename));
			nextLine=reader.readLine();
			while ((nextLine = reader.readLine()) != null) 
				array.add(nextLine);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public ArrayList<String> readFromFileWithBufferedReaderArray(String directory_and_filename)
	{
		ArrayList<String> array=new ArrayList<String>();
		String nextLine;
		try {
			reader = new BufferedReader(new FileReader(directory_and_filename));
			while ((nextLine = reader.readLine()) != null) 
				array.add(nextLine);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public ArrayList<String> readFromFileWithBufferedReaderArray(String directory, String filename)
	{
		ArrayList<String> array=new ArrayList<String>();
		String nextLine;
		try {
			reader = new BufferedReader(new FileReader(directory+ filename));
			while ((nextLine = reader.readLine()) != null) 
				array.add(nextLine);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public void writeToFileWithBufferedWriterArray(String directory_filename, ArrayList<String> dataStream, boolean destroyExistingFile, boolean concatenate)
	{
		try {
			if (destroyExistingFile)
			{
				File f = new File(directory_filename);
				if(f.exists()) { f.delete(); }
			}
			writer = new BufferedWriter(new FileWriter(directory_filename, concatenate));
			for(int i=0; i<dataStream.size(); i++)
			{
				writer.write(dataStream.get(i));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeToFileWithBufferedWriterArray(String directory, String filename, ArrayList<String> dataStream, boolean destroyExistingFile)
	{
		try {
			File destination = new File(directory);
			if (!destination.exists()) {
				System.out.println("filename:" + filename);
				System.out.println("destination dir doesnt exists:" + directory);
				destination.mkdir();
			}
			if (destroyExistingFile)
			{
				File f = new File(directory+ filename);
				if(f.exists()) { f.delete(); }
			}
			writer = new BufferedWriter(new FileWriter(directory+ filename));
			for(int i=0; i<dataStream.size(); i++)
			{
				writer.write(dataStream.get(i));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void concatenateToFileWithBufferedWriter(String directory, String filename, String dataStream)
	{
		try {
			File destination = new File(directory);
			if (!destination.exists()) {
				System.out.println("destination dir doesnt exists");
				destination.mkdir();
			}
			writer = new BufferedWriter(new FileWriter(directory+ filename, true));
			writer.write(dataStream);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyFile(String inputFileName, String outputFileName) 
	{
		String nextLine;

		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			writer = new BufferedWriter(new FileWriter(outputFileName));
			while ((nextLine = reader.readLine()) != null) 
			{
				writer.write(nextLine);
				writer.newLine();
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> concatenateFile(String inputFileName, ArrayList<Integer> lineIndexToLeaveOut, String outputFileName) 
	{
		String nextLine;
		int lineCounter=0 ;
		ArrayList<Integer> originalIndexList=new ArrayList<Integer>();
		
		//create a copy of the index list to be returned
		if(lineIndexToLeaveOut!=null && lineIndexToLeaveOut.size()>0)
		{
			for(int i=0; i<lineIndexToLeaveOut.size(); i++)
				originalIndexList.add(lineIndexToLeaveOut.get(i));
		}
		
		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			writer = new BufferedWriter(new FileWriter(outputFileName, true));
			while ((nextLine = reader.readLine()) != null) 
			{
				if(lineIndexToLeaveOut==null || lineIndexToLeaveOut.size()==0)
				{
					writer.write(nextLine);
					writer.newLine();
				}
				else
				{
					
					if(lineIndexToLeaveOut.get(0)==lineCounter)
						lineIndexToLeaveOut.remove(0);
					else
					{
						writer.write(nextLine);
						writer.newLine();
					}
				}
				lineCounter++;
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return originalIndexList;
	}
	
	public void deleteFile(String directory, String filename)
	{
		try{
		    String tempFile = directory+filename;
	        //Delete if tempFile exists
	        File fileTemp = new File(tempFile);
	          if (fileTemp.exists()){
	             fileTemp.delete();
	          }   
	      }catch(Exception e){
	         // if any error occurs
	         e.printStackTrace();
	      }
	}
}
