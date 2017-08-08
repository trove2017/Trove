package node;

import java.util.ArrayList;

public class annotationList 
{
	private ArrayList<annotation> list;
	
	public annotationList(){
		list=new ArrayList<annotation>();
	}

	public void newRecord() {
		list.add(new annotation());
	}
	public void addUniProt(int i, String v) {
		list.get(i).addUniProt(v);
	}
	public int getUniProtSize(int i) {
		return list.get(i).getUniProtSize();
	}
	public String getUniProt(int i, int j) {
		return list.get(i).getUniProt(j);
	}
	public void addInterPro(int i, String v) {
		list.get(i).addInterPro(v);
	}
	public int getInterProSize(int i) {
		return list.get(i).getInterProSize();
	}
	public String getInterPro(int i, int j) {
		return list.get(i).getInterPro(j);
	}	
	public void addKEGG(int i, String v) {
		list.get(i).addKEGG(v);
	}
	public int getKEGGSize(int i) {
		return list.get(i).getKEGGSize();
	}
	public String getKEGG(int i, int j) {
		return list.get(i).getKEGG(j);
	}
	public void addChEBI(int i, String v) {
		list.get(i).addChEBI(v);
	}
	public int getChEBISize(int i) {
		return list.get(i).getChEBISize();
	}
	public String getChEBI(int i, int j) {
		return list.get(i).getChEBI(j);
	}
	public void removeAnnotation(int index){
		if(index>=0 && index<list.size())
			list.remove(index);
	}
	public void printAll() {
		for(int i=0; i<list.size(); i++)
			list.get(i).printAll();
	}
}