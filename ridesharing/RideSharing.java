package ridesharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class RideSharing 
{

    public static ArrayList<Integer> points=new ArrayList<>();
    public static ArrayList<Integer> limits=new ArrayList<>();
    public static double basic_fare=2;
    
     public static double Distance(int source, int destination) throws SQLException, ClassNotFoundException
     {
     // Connection c = null;
      Statement stmt = null;
      Statement stmt1 = null;
      
         
        int iterator_value=0;
         Class.forName("org.postgresql.Driver");
            try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/India",
                    "postgres", "Asdfghjkl!23")) 
            {
                stmt = c.createStatement();
                stmt1 = c.createStatement();
                
                double km;
            try (ResultSet rs = stmt.executeQuery( "SELECT seq, id1 , id2 , cost FROM pgr_dijkstra('SELECT id, source, target, cost FROM hh_2po_4pgr',"+source+","+destination+", false, false);" )) 
            {
             ResultSet rs1;
              rs1 = null;
              km = 0;
              while ( rs.next() )
              {
                  int seq = rs.getInt("seq");
                  int id1 = rs.getInt("id1");
                  points.add(id1);
                  
                  int id2 = rs.getInt("id2");
                  double cost = rs.getDouble("cost");
                  rs1 = stmt1.executeQuery("select km from hh_2po_4pgr where id ="+ id2);
                  while(rs1.next())
                  {
                      km+=rs1.getDouble("km");
                      //System.out.println(km);
                  }
                  iterator_value++;
               }
              
              limits.add(iterator_value);
              
              rs1.close();
              rs.close();
          }
          stmt.close();
          return km;
         }           
      }
      
      
    public static int common_point()
    {
        int common=0;
        int size=points.size();
        int i = 0;
        int j=0;
        Integer[] temp=new Integer[size];
        Integer[] temp2=new Integer[limits.size()];
        Iterator it=points.iterator();
        Iterator it2=limits.iterator();
        while(it.hasNext())
        {
                temp[i]=(Integer) it.next();
                i++;
        }
        int total_points=0;
        while(it2.hasNext())
        { 
             temp2[j]=(Integer) it2.next();
             total_points+=temp2[j];
             j++;
        }
        for(int m=0;m<temp2[0];m++)
        {
            for(int k=temp2[0];k<total_points;k++)
            {
                if(temp[m].equals(temp[k]))
                {
                    common = temp[m];
                    return common;
                }
                
            }
        }
        
        return common;
    } 
      
    
    public static void check(int s1,int s2,int common,int d,Double s1_d,Double s2_d) throws SQLException, ClassNotFoundException
    {
       
        

        if(s1_d>s2_d)
        {       
            double s1_d_cost=s1_d*basic_fare;
            double s2_d_cost=s2_d*basic_fare; 
            double s1_s2_d=RideSharing.Distance(s1, s2);
            double s1_s2_cost=s1_s2_d*basic_fare;
            if(common == s2)
            {
                Double s1_c=RideSharing.Distance(s1, common);
                double s1_c_cost=s1_c*basic_fare;
                System.out.println("final cost is..."+ (s1_c_cost+s2_d_cost));
                System.out.println("Actual cost for A is "+ s1_d_cost);
                System.out.println("cost for A after ride sharing is... "+ (s1_c_cost+(s2_d_cost/2)));
                System.out.println("Actual cost for B is "+ s2_d_cost);
                System.out.println("Cost for B after ride sharing is..."+ (s2_d_cost/2));
                System.out.println("Final path is..S1->D  "+ s1_d);
            }
            else if(common == d)
            {
                
                if((s1_s2_cost+(s2_d_cost/2)) >= s1_d_cost){
                    System.out.println("The ride cannot be shared");
                }
                else{
                    System.out.println("Final cost is" + (s1_s2_cost+s2_d_cost));
                    System.out.println("Final Path is S1 -> S2 -> D ="+ s1_s2_d);
                    System.out.println("Actual cost for A is "+ s1_d_cost);
                    System.out.println("cost for after sharing A is "+ (s1_s2_cost+(s2_d_cost/2)));
                    System.out.println("Actual cost of B is "+ s2_d_cost);
                    System.out.println("Cost for B is "+(s2_d_cost/2) );
                }
            }
            else
            {
                
                Double s1_c=RideSharing.Distance(s1, common);
                Double s2_c=RideSharing.Distance(s2, common);
                
                if(s1_c > s2_c)
                {
                    double b=s1_c*basic_fare;
                    double c_d=RideSharing.Distance(common, d);
                    double c_d_cost1=c_d*basic_fare;
                    double c_d_cost=c_d_cost1/2;
                    System.out.println("Final path is..S1->D   "+ s1_d);
                    
                    if(s2_c<1){
                        System.out.println("Walking from S2 to common point");
                        System.out.println("final cost is..."+ b+c_d_cost1);
                        System.out.println("Actual Cost for A is "+ s1_d_cost);
                        System.out.println("cost for A after sharing is..."+ b+c_d_cost);
                        System.out.println("Actual Cost for B is "+ s2_d_cost);
                        System.out.println("cost for B is..."+ c_d_cost);
                    }
                    else{
                       double b1=s2_c*basic_fare; 
                       System.out.println("final cost is..."+ b+c_d_cost1);
                       System.out.println("Actual Cost for A is "+ s1_d_cost);
                       System.out.println("cost for A after sharing is..."+ b+c_d_cost);
                       System.out.println("Actual Cost for B is "+ s2_d_cost);
                       System.out.println("cost for B after sharing is..."+ (b1+c_d_cost));
                        
                       
                    }
                }
                else
                {
                    double b=s2_c*basic_fare;
                    double c_d=RideSharing.Distance(common, d);
                    double c_d_cost1=c_d*basic_fare;
                    double c_d_cost=c_d_cost1/2;
                    System.out.println("Final path is..S2->D   "+ s2_d);
                    if(s1_c<1){
                    
                        System.out.println("final cost is..."+ b+c_d_cost1);
                        System.out.println("Actual Cost for B is "+ s2_d_cost);
                        System.out.println("cost for B after sharing is..."+ b+c_d_cost);
                        System.out.println("Actual Cost for A is "+ s1_d_cost);
                        System.out.println("cost for A after sharing is..."+ c_d_cost);
                        
                    }
                    else{
                       double b1=s1_c*basic_fare; 
                        
                       System.out.println("final cost is..."+ b+c_d_cost1);
                       System.out.println("Actual Cost for B is "+ s2_d_cost);
                       System.out.println("cost for B after sharing is..."+ b+c_d_cost);
                       System.out.println("Actual Cost for A is "+ s1_d_cost);
                       System.out.println("cost for A after sharing is..."+ (b1+c_d_cost));
                        
                       
                    }
                }
            }
        }
        else if(s2_d>s1_d)
        {
            double s2_d_cost=s2_d*basic_fare;
               double s1_s2_d=RideSharing.Distance(s1, s2);
            double s1_s2_cost=s1_s2_d*basic_fare;     
            double s1_d_cost=s1_d*basic_fare;
            if(common == s1)
            {
                  Double s2_c=RideSharing.Distance(s2, common);
                double s2_c_cost=s2_c*basic_fare;
                System.out.println("final cost is..."+ (s2_c_cost+s1_d_cost));
                System.out.println("Actual Cost for B is "+ s2_d_cost);
                System.out.println("cost for B after sharing is..."+ (s2_c_cost+(s1_d_cost/2)));
                System.out.println("Actual Cost for A is "+ s1_d_cost);
                System.out.println("cost for A after sharing is..."+ (s1_d_cost/2));
                    
                System.out.println("Final path is..S2->D  "+ s2_d);
            }
            else if(common == d)
            {
            if((s1_s2_cost+(s1_d_cost/2)) >= s2_d_cost){
                    System.out.println("The ride cannot be shared");
                }
                else{
                    System.out.println("Final cost is" + (s1_s2_cost+s1_d_cost));
                    System.out.println("Final Path is S1 -> S2 -> D ="+ s1_s2_d);
                    System.out.println("Actual Cost for A is "+ s1_d_cost);
                    System.out.println("cost for A after sharing is "+ (s1_s2_cost+(s1_d_cost/2)));
                    System.out.println("Actual Cost for B is "+ s2_d_cost);
                    System.out.println("Cost for B after sharing is "+(s1_d_cost/2) );
                }
            }
            else
            {
                Double s1_c=RideSharing.Distance(s1, common);
                Double s2_c=RideSharing.Distance(s2, common);
                if(s1_c > s2_c)
                {
                     System.out.println("Final path is..S1->D   "+ s1_d);
                     double b=s1_c*basic_fare;
                    double c_d=RideSharing.Distance(common, d);
                    double c_d_cost1=c_d*basic_fare;
                    double c_d_cost=c_d_cost1/2;
                    System.out.println("Final path is..S1->D   "+ s1_d);
                    
                    if(s2_c<1){
                        System.out.println("Walking from S2 to common point");
                        System.out.println("final cost is..."+ b+c_d_cost1);
                        System.out.println("cost for A is..."+ b+c_d_cost);
                        System.out.println("cost for B is..."+ c_d_cost);
                    }
                    else{
                       double b1=s2_c*basic_fare; 
                       System.out.println("final cost is..."+ b+c_d_cost1);
                       System.out.println("cost for A is..."+ b+c_d_cost);
                       System.out.println("cost for B is..."+ (b1+c_d_cost));
                    }
                }
                else
                {
                    double b=s2_c*basic_fare;
                    double c_d=RideSharing.Distance(common, d);
                    double c_d_cost1=c_d*basic_fare;
                    double c_d_cost=c_d_cost1/2;
                    System.out.println("Final path is..S2->D   "+ s2_d);
                                            
                    if(s1_d<1){
                                               
                        System.out.println("shared distance is"+c_d);
                        System.out.println("final cost is..." );
                        System.out.println(b+c_d_cost1);
                        System.out.println("Actual Cost for B is "+ s2_d_cost);
                        System.out.println("cost for B after sharing is..." );
                        System.out.println(b+c_d_cost);
                        System.out.println("Actual Cost for A is "+ s1_d_cost);
                        System.out.println("cost for A after sharing is..." );
                        System.out.println(c_d_cost);
                        
                    }
                    else{
                       double b1=s1_c*basic_fare; 
                       double fin=b+c_d_cost1;
                       System.out.println("final cost is..$$$$."+ fin);
                       System.out.println("Actual Cost for B is "+ s2_d_cost);
                       System.out.println("cost for B after sharing is..."+ b+c_d_cost);
                       System.out.println("Actual Cost for A is "+ s1_d_cost);
                       System.out.println("cost for A after sharing is..."+ (b1+c_d_cost));
                        
                       
                    }
                }
            }
            
            
        }              
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException 
    {
      //RideSharing obj = new RideSharing();
      int s1=6622;
      int s2=4884;
      int d=6621;
      
      Double s1_d=RideSharing.Distance(s1,d);
      Double s2_d=RideSharing.Distance(s2,d);
      int common=common_point();
       // System.out.println(km+" "+km2+ "  "+ common);
      check(s1, s2, common, d,s1_d,s2_d);
    }    
}
