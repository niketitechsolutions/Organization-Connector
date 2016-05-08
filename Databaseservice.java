
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 *
 * @author Niket
 */
public class Databaseservice {

    static Statement stmt;
    static Connection con;
    static ResultSet rs;
    static ResultSetMetaData rsmd;
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            stmt=con.createStatement();
            ServerSocket ss=new ServerSocket(8802);
            System.out.println("Database server is acrtive on 127.0.0.1:8802");
        while(true)
        {            
            try{
                
                Socket s=ss.accept();
                BufferedReader is=new BufferedReader(new InputStreamReader(s.getInputStream()));
                String output=is.readLine();
                output=resolveQuery(output);
                PrintStream pw=new PrintStream(s.getOutputStream());
                pw.println(output);
                pw.flush();
                pw.close();
                is.close();
                s.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        }catch(Exception e){System.out.println(e.getMessage());}
    }
    public static String resolveQuery(String q)
    {
        String output="";
        try{
                 if(q.startsWith("select"))
            {
                rs=stmt.executeQuery(q);
                rsmd=rs.getMetaData();
                int colcnt=rsmd.getColumnCount();
                try{
                    while(rs.next())
                    {
                        output+="{";
                        for(int i=1;i<=colcnt;i++)
                        {
                            output+="["+rsmd.getColumnName(i)+":"+rs.getString(i)+"],";
                        }
                        output=output.substring(0,output.length()-1);
                    }output+="}\n";
                }catch(Exception e)
                {
                    output=e.getMessage();
                }
            }
            else if(q.startsWith("insert") || q.startsWith("update") || q.startsWith("delete"))
            {
                int res=stmt.executeUpdate(q);
                output="Response Code : "+res;
            }
            else
            {
                output="Operation is not Allowed...!!";
            }
        }catch(Exception e)
        {
            output=e.getMessage();
        }
        return output;
    }
    
}
