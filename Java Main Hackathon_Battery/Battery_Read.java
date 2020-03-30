import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.sql.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Battery_Read {
	static BufferedReader br = null;
	static JSONObject jsonObject = new JSONObject();
	public static Connection getConnection() throws SQLException {
		Connection con=null;
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root" , "akash21");
		return con;
	}
	
	public static void getDetails(Connection con) throws SQLException, JSONException {
		
		PreparedStatement preparedStatement2=con.prepareStatement("SELECT * FROM batteryvalues");
		ResultSet results=preparedStatement2.executeQuery();
		while(results.next()) {
			String Foreground=results.getString(1);
			double Battery_drain=results.getDouble(3);
			double Battery_percentage=results.getDouble(2);
			jsonObject.put("Foreground_time",Foreground);
			jsonObject.put("Battery_drain" ,Battery_drain);
			jsonObject.put("Battery_percentage" ,Battery_percentage);
		}
	}
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Connection con=getConnection();
		PreparedStatement pStmt =con.prepareStatement("INSERT into batteryvalues (Foreground_time,Battery_percentage,Battery_drain) values(?,?,?)");
		try{
			String Foreground="";
			double Battery_percentage=0,Battery_drain=0;			
			String line;
			String compare1="Uid u0a202";
			String compare2="Foreground activities";
			br = new BufferedReader(new FileReader("C:\\Users\\AKASH\\Desktop\\Battery.txt"));
			while ((line = br.readLine()) != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(line, ":");
				while (stringTokenizer.hasMoreElements()) {
					String str=stringTokenizer.nextElement().toString();
					if((str.trim()).equals(compare1))
					{
						String str2=stringTokenizer.nextElement().toString().trim();
						String[] array1 = str2.split(" ");
						Battery_drain=Double.parseDouble(array1[0]);
						pStmt.setDouble(3,Battery_drain);
					}
					if((str.trim()).equals(compare2))
					{
						Foreground=stringTokenizer.nextToken(":").trim();
						pStmt.setString(1,Foreground);
					}
			}
			}
			Battery_percentage=(Battery_drain/1000);
			double roundOff_Battery_percentage = (double) Math.round(Battery_percentage * 1000) / 1000;
			pStmt.setDouble(2,roundOff_Battery_percentage);
			pStmt.execute();
			getDetails(con);
			FileWriter file1=new FileWriter("C:\\Users\\AKASH\\Desktop\\jsonFile.json");
			file1.write(jsonObject.toString());
			file1.flush();
			file1.close();
			System.out.println(jsonObject);
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
