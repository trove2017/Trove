package node;

import java.util.ArrayList;

public class annotation 
{
	private ArrayList<String> uniProt;
	private ArrayList<String> interPro;
	private ArrayList<String> chEBI;
	private ArrayList<String> KEGG;

	public annotation(){
		uniProt=new ArrayList<String>();
		interPro=new ArrayList<String>();
		chEBI=new ArrayList<String>();
		KEGG=new ArrayList<String>();
	}

	public int getUniProtSize()
	{
		return uniProt.size();
	}

	public int getInterProSize()
	{
		return interPro.size();
	}

	public int getChEBISize()
	{
		return chEBI.size();
	}

	public int getKEGGSize()
	{
		return KEGG.size();
	}

	public void addUniProt(String v)
	{
		uniProt.add(v);
	}

	public void addInterPro(String v)
	{
		interPro.add(v);
	}

	public void addChEBI(String v)
	{
		chEBI.add(v);
	}

	public void addKEGG(String v)
	{
		KEGG.add(v);
	}

	public String getUniProt(int i)
	{
		if(i<0 || i>getUniProtSize())
			return null;
		else
			return uniProt.get(i);
	}

	public String getInterPro(int i)
	{
		if(i<0 || i>getInterProSize())
			return null;
		else
			return interPro.get(i);
	}

	public String getChEBI(int i)
	{
		if(i<0 || i>getChEBISize())
			return null;
		else
			return chEBI.get(i);
	}

	public String getKEGG(int i)
	{
		if(i<0 || i>getKEGGSize())
			return null;
		else
			return KEGG.get(i);
	}

	public void printAll(){
		System.out.println("-------------- Annotations ------------------");
		System.out.println("UniProt");
		if(uniProt.size()==0)
			System.out.println("No records");
		else
		{
			for(int i=0; i<uniProt.size(); i++)
				System.out.println(i+": "+uniProt.get(i));
		}
		System.out.println("interPro");
		if(interPro.size()==0)
			System.out.println("No records");
		else
		{
			for(int i=0; i<interPro.size(); i++)
				System.out.println(i+": "+interPro.get(i));
		}
		System.out.println("chEBI");
		if(chEBI.size()==0)
			System.out.println("No records");
		else
		{
			for(int i=0; i<chEBI.size(); i++)
				System.out.println(i+": "+chEBI.get(i));
		}
		System.out.println("KEGG");
		if(KEGG.size()==0)
			System.out.println("No records");
		else
		{
			for(int i=0; i<KEGG.size(); i++)
				System.out.println(i+": "+KEGG.get(i));
		}
	}
}