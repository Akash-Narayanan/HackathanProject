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
public class FileParse {
	static BufferedReader br = null;
	static int itr = 0;
	static Map<String, Object> data3 = new HashMap<String, Object>();
	public static void main(String[] args) {
		double avg=0.0;
		double max=0.0;
		Connection myConn = null;
    	Statement myStmt = null;
		try {
    		myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root" , "akash21");	
    		myStmt = myConn.createStatement();
    		PreparedStatement pStmt = myConn.prepareStatement("INSERT into cpuvalue (value) values(?)");
			String line;
			br = new BufferedReader(new FileReader("C:\\Users\\AKASH\\Desktop\\sample input.txt"));
			while ((line = br.readLine()) != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
				data3.put("Transaction", "SampleTrans");
				while (stringTokenizer.hasMoreElements()) {
					int x=0;
					while(x<8) {
						stringTokenizer.nextElement().toString();
						x++;
					}
					Double reqCPU = Double.parseDouble(stringTokenizer.nextElement().toString());
					data3.put(itr+" s", reqCPU);	
					itr++;
					pStmt.setDouble(1,reqCPU);
					//pStmt.execute();
					while(x<11) {
						stringTokenizer.nextElement().toString();
						x++;
					}
				}	
			}
			ResultSet rs=myStmt.executeQuery("select max(value) from cpuvalue");  
			while(rs.next())  
				max=(rs.getDouble(1));
			ResultSet rs2=myStmt.executeQuery("select avg(value) from cpuvalue");  
			while(rs2.next())  
				avg=(rs2.getDouble(1));
			double roundOffAvg = (double) Math.round(avg * 100) / 100;
			double roundOffMax = (double) Math.round(max * 100) / 100;
			data3.put("average",roundOffAvg);
			data3.put("maximum",roundOffMax);
			String jsonStr = JSONValue.toJSONString(data3);
			System.out.println("JSON Output");
			System.out.println(" ");
			System.out.println(jsonStr);
			PrintWriter pw = new PrintWriter(new FileWriter("C:\\Users\\AKASH\\Desktop\\output.html"));
	        pw.println("<TABLE BORDER><TR><TH>TRANSACTION NAME<TH>MAXIMUM CPU TIME<TH>AVERAGE CPU TIME</TR>");
	        pw.println("<TR><TD>" +"SampleTrans"+ "<TD>" + roundOffMax+"<TD>"+roundOffAvg);
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
