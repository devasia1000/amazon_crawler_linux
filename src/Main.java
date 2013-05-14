
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class Main {
    
    static double fail=0;
    static double sucsess=0;
    static double total;

    public static void main(String args[]){
        //File f=new File("C:/Users/Devasia Manuel/AmazonProducts");
        File f=new File(".");
        String[] s=f.list();
        total=s.length;
        for(String s1:s){
            if(s1.endsWith(".xml")){
                parse(s1);
            }
        }
        
        System.out.println("\n\nPercentage Fail: "+fail/(fail+sucsess)*100);
    }
    
        public static void parse(String filename){
        try{
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //Document dom = dBuilder.parse(new File("C:/Users/Devasia Manuel/AmazonProducts/"+filename));
            Document dom = dBuilder.parse(new File(filename));
            
            //System.out.println(review+"\n\n");
            //System.out.println(gold);
            
            NodeList child3=dom.getElementsByTagName("Title");
            String title=null;
            try{
                title=child3.item(0).getTextContent();
                System.out.println();
            } catch (Exception e){
                System.err.println("Could not fetch title: "+filename);
            }
            //System.out.println(title);
             //Point of Error!
            //System.out.println(child3.item(0).getTextContent()); //Title
            
            NodeList child4=dom.getElementsByTagName("BrowseNode");
            String temp=child4.item(0).getTextContent();
            
            if(temp.length()>250){
                throw new Exception();
            }
            
            temp=temp.replaceAll("Categories", "");
            temp=temp.replaceAll("Products", "");
            temp=temp.replaceAll("[0-9]+", "/");
            temp=temp.trim();
            //System.out.println(temp);
            
            String t[]=temp.split("/");
            List<String> list = Arrays.asList(t);
            Collections.reverse(list);
            t = (String[]) list.toArray();
            
            String cat="/";
            for(int i=0;i<t.length-1;i++){
                cat=cat+t[i].trim()+"/";
                if(t[i].length()<3){
                    System.err.println(t[i]);
                    throw new Exception(); 
                }
            }
            
            //System.out.println(cat);
            
            NodeList child5=dom.getElementsByTagName("Brand");
            
            String brand="NULL";
            try{
                brand=child5.item(0).getTextContent();
                //System.out.println(brand);
            } catch (NullPointerException ed){
                brand="NULL";
            }
            
            NodeList child1=dom.getElementsByTagName("EditorialReviews");
            NodeList child2=child1.item(0).getChildNodes(); 
            NodeList n2=child2.item(0).getChildNodes();
            int j=1;
            for(j=1;j<n2.getLength();j++){
                if(n2.item(j).getNodeName().equals("Content")){
                    
                    if(j==2){
                        System.out.println("Gold is 2");
                    }
                    
                    String gold=n2.item(j).getTextContent()+"\n\n";
                    
                    //File catDirec=new File("C:/AmazonProducts"+cat);
                    //File goldFile=new File("C:/AmazonProducts"+cat+"GOLD."+j+".("+brand+")."+filename.replace("xml", "html"));
                    File catDirec=new File("./"+cat);
                    File goldFile=new File("./"+cat+"GOLD."+j+".("+brand+")."+filename.replace("xml", "html"));
                    
                    catDirec.mkdirs();
                    //System.out.println(xmlFile.getPath());
                    if(!goldFile.exists()){
                        goldFile.createNewFile();
                    }
            
            
                    BufferedWriter wt=new BufferedWriter(new FileWriter(goldFile));
                    wt.write(gold);
                    wt.flush();
                    wt.close();
                    
                    //String htmlFilename="C:/Users/Devasia Manuel/AmazonProducts/"+filename.replaceAll(".xml", ".html");
                    String htmlFilename=filename.replaceAll(".xml", ".html");
                    BufferedReader rd=new BufferedReader(new FileReader(htmlFilename));
                    String mess="", temp1=null;
                    while((temp1=rd.readLine())!=null){
                        mess=mess+temp1;
                    }
                    
                    Pattern p1 = Pattern.compile("(id=\"productDescription\")[\\s\\S]+(<div class=\"emptyClear\">)"); //FIX REGEX CODE!
                    Matcher m1 = p1.matcher(mess);
                    if(m1.find()){
                        String des=m1.group();
                        
                        des=des.replaceAll("id=\"productDescription\"> ", "");
                        des=des.replaceAll("\\s{2,}", "\n");
                        des=des.replaceAll("Product Description", "");
                        des=des.trim();
                        
                        des="<html>"+"<font size=\"6\"><b>"+title+"</b></font>"+des+"</html>";
                        
                        //BufferedWriter wt1=new BufferedWriter(new FileWriter("C:/AmazonProducts/"+cat+"("+brand+")."+filename.replaceAll(".xml", ".html")));
                        BufferedWriter wt1=new BufferedWriter(new FileWriter("./"+cat+"("+brand+")."+filename.replaceAll(".xml", ".html")));
                        wt1.write(des);
                        wt1.flush();
                        wt1.close();
                    }
                    
                    sucsess++;
                }
            }
            
            j=j-2;
            
            NodeList featureList=dom.getElementsByTagName("Feature");
            String feature="";
            for(int i=0;i<(featureList.getLength()/5);i++){
                feature=feature+featureList.item(0).getTextContent()+"\n";
                
            }   
            
            File catDirec=new File("./"+cat);
            File goldFile=new File("./"+cat+"GOLD."+(j+1)+".("+brand+")."+filename.replace("xml", "html"));
                    
            catDirec.mkdirs();
            if(!goldFile.exists()){
                goldFile.createNewFile();
            }
            
            BufferedWriter wt=new BufferedWriter(new FileWriter(goldFile));
            wt.write(feature);
            wt.flush();
            wt.close();
            
            System.out.println("Done: "+((sucsess+fail)/total)*100+"%");
            
        } catch (Exception e){
            System.err.println("Failed to Parse for "+filename);
            e.printStackTrace();
            fail++;
        }
        
    }
}
