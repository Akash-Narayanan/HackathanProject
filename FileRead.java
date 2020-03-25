import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.json.simple.JSONValue;
public class FileRead {
	static BufferedReader br = null;
	static int itr = 0;
	static Map<String, Object> data4 = new HashMap<String, Object>();
	public static void main(String[] args) {
		double sum=0.0;
		String name="";
		double avg=0.0;
		double max=0.0;
		Connection myConn = null;
    	Statement myStmt = null;
		try {
    		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root" , "akash21");	
    		myStmt = myConn.createStatement();
    		PreparedStatement pStmt = myConn.prepareStatement("INSERT into cpuvalues (transname,maximum,average) values(?,?,?)");
			String line;
			br = new BufferedReader(new FileReader("C:\\Users\\AKASH\\Desktop\\sample input.txt"));
			while ((line = br.readLine()) != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
				while (stringTokenizer.hasMoreElements()) {
					int x=0;
					while(x<8) {
						stringTokenizer.nextElement().toString();
						x++;
					}
					Double reqCPU = Double.parseDouble(stringTokenizer.nextElement().toString());
					data4.put(itr+" s", reqCPU);	
					itr++;
					sum=sum+reqCPU;
					if(reqCPU>max)
					{
						max=reqCPU;
					}
					while(x<11) {
						stringTokenizer.nextElement().toString();
						x++;
					}
				}	
			}
			pStmt.setString(1,"SampleTransaction");
			pStmt.setDouble(2,max);
			avg=sum/itr;
			pStmt.setDouble(3,avg);
			pStmt.execute();
			ResultSet rs=myStmt.executeQuery("select * from cpuvalues");  
			while(rs.next()) 
			{
				name=rs.getString(1);
				max=rs.getDouble(2);  
				avg=rs.getDouble(3);
			}
			double roundOffAvg = (double) Math.round(avg * 100) / 100;
			double roundOffMax = (double) Math.round(max * 100) / 100;
			data4.put("Transaction", name);
			data4.put("average",roundOffAvg);
			data4.put("maximum",roundOffMax);
			String jsonStr = JSONValue.toJSONString(data4);
			System.out.println("JSON Output");
			System.out.println(" ");
			System.out.println(jsonStr);
			PrintWriter pw = new PrintWriter(new FileWriter("C:\\Users\\AKASH\\Desktop\\output.html"));
	        pw.println("<TABLE BORDER><TR><TH>TRANSACTION NAME<TH>MAXIMUM CPU TIME<TH>AVERAGE CPU TIME</TR>");
	        pw.println("<TR><TD>" +name+ "<TD>" + roundOffMax+"<TD>"+roundOffAvg);
	        pw.println("</TABLE>");
	        pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
