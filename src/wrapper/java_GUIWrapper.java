package wrapper;

import java.util.ArrayList;

import javax.swing.JList;

public class java_GUIWrapper 
{
	public java_GUIWrapper() 
	{

	}

	public JList<String> updateJListData(JList<String> list, ArrayList<String> arrList) 
	{
		String[] javaArr;
		
		javaArr=arrList.toArray(new String[arrList.size()]);
		list.setListData(javaArr);
		
		return list;
	}
}