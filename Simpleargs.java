import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Niket
 */
public class Simpleargs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            ServerSocket ss=new ServerSocket(8801);
            System.out.println("Argument server is acrtive on 127.0.0.1:8801");
        while(true)
        {            
            try{
                Socket s=ss.accept();
                BufferedReader is=new BufferedReader(new InputStreamReader(s.getInputStream()));
                String output=is.readLine();
                System.out.println(output);
                PrintStream pw=new PrintStream(s.getOutputStream());
                pw.println("Hello "+output+"...!!");
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
    
}
