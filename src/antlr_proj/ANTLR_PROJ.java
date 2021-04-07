/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antlr_proj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;

public class ANTLR_PROJ extends JFrame {
     public static void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    
    public static ArrayList<String> GetFiles(final File folder) {
        ArrayList<String> FileNames = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            FileNames.add(fileEntry.getName());
        }
        return FileNames;
    }

    //recursive tree
    public static void CreateAncestorsTree(HashMap<String, Set<String>> Ancestors, String CurrentClass, String A) {
        if (Ancestors.containsKey(A)) {
            Ancestors.get(CurrentClass).add(Ancestors.get(A).stream().findFirst().get());
            CreateAncestorsTree(Ancestors, A, Ancestors.get(A).stream().findFirst().get());
        }
    }

    public static boolean FileExists(String Filename) {
        File tempFile = new File(Filename);
        return (tempFile.exists() && !tempFile.isDirectory());
    }

    public static boolean ReadPreviousVersion(String opfilepath, String cur_class, int method, int mem_flag, HashSet<String> Classname, HashMap<String, String> data_members, HashMap<String, String> method_members) {
        //**********************************
        try {
            File fp = new File(opfilepath);
            if (!fp.exists()) {
                fp.createNewFile();
                return false;
            } else {
                FileReader fr = new FileReader(fp);
                BufferedReader br = new BufferedReader(fr);
                String line;

                while ((line = br.readLine()) != null) {
                    String[] arr = line.split(" ");
                    if (mem_flag == 1) {
                        Iterator itr = Classname.iterator();
                        while (itr.hasNext()) {
                            cur_class = (String) itr.next();
                        }
                        Matcher m = Pattern.compile("\\(*\\)").matcher(line);
                        if (m.find()) {
                            method = 1;
                            method_members.put(line, cur_class);
                        }
                        /*
                        if (method == 0) {
                            data_members.put(line, cur_class);
                        }*/
                        
                        //KEDARNATH's data member finder
                        //*********************************************************************************************
                        //*********************************************************************************************
                        if (method == 0) {
                           // System.out.println("I'm here where not method");
                          //  Matcher m1 = Pattern.compile("\\'int'\\)").matcher(line);
                            if(line.contains("private"))
                            {
                                
                              System.out.println("I contain the private..\n"+line);
                              String Data_Members_Array1 = line.replaceAll("\\s+", " ");
                              String sample[] = Data_Members_Array1.split(" ");
                                   
                              if(sample.length<=1)
                              {
                                  System.out.println("I contain length 1"+line);
                              }
                              else
                              { 
                                  System.out.println("My lenght is not 1"+line);
                                  if(line.contains("int"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                   
                                               Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private int "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                            }//if(for int)
                                  else if(line.contains("float"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private float "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)
                                  else if(line.contains("double"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private double "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                  
                                            else if(line.contains("char"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private char "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                                            
                              
                              
                              }                          
                            
                            }
                            else if(line.contains("protected"))
                            {
                                
                              System.out.println("I contain the protected..\n"+line);
                              String Data_Members_Array1 = line.replaceAll("\\s+", " ");
                              String sample[] = Data_Members_Array1.split(" ");
                                   
                              if(sample.length<=1)
                              {
                                  System.out.println("I contain length 1"+line);
                              }
                              else
                              { 
                                  System.out.println("My lenght is not 1"+line);
                                  if(line.contains("int"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                   
                                               Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected int "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                            }//if(for int)
                                  else if(line.contains("float"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected float "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)
                                  else if(line.contains("double"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected double "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                  
                                            else if(line.contains("char"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected char "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                                            
                              
                              
                              }                          
                            
                            }
                            else if(line.contains("public"))
                            {
                              System.out.println("I contain the public..\n"+line);
                              String Data_Members_Array1 = line.replaceAll("\\s+", " ");
                              String sample[] = Data_Members_Array1.split(" ");
                                   
                              if(sample.length<=1)
                              {
                                  System.out.println("I contain length 1"+line);
                              }
                              else
                              { 
                                  System.out.println("My lenght is not 1"+line);
                                  if(line.contains("int"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public int "+word;
                                                         System.out.println("Value of class is cur_class");
                                                         System.out.println(" WORD IS FOR PRE and temp is"+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                            }//if(for int)
                                  else if(line.contains("float"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public float "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)
                                  else if(line.contains("double"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public double "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                  
                                            else if(line.contains("char"))
                                            {
                                              String Data_Members_Array = line.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public char "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_members.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                                            
                              
                              
                              }    
                            }
                         
                            //separate int checking 
                            else if(line.contains("int"))
                            {
                                System.out.println("Contains int and the line is ++ " + line);
                                String Data_Members_Array = line.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                               // System.out.println(" INT NEW "+word);
                                                String temp = "int "+word;
                                                System.out.println(" temp NEW HERE >>>" + temp);
                                                data_members.put(temp, cur_class);
                                            }

                                    }
                            }
                            //separate float checking
                            else if(line.contains("float"))
                            {
                                String Data_Members_Array = line.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                               // System.out.println(" INT NEW "+word);
                                                String temp = "float "+word;
                                                System.out.println(" temp NEW "+temp);
                                                data_members.put(temp, cur_class);
                                            }

                                    }
                            }
                            
                            else if(line.contains("double"))
                            {
                                String Data_Members_Array = line.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                               // System.out.println(" double NEW "+word);
                                                String temp = "double "+word;
                                                System.out.println(" temp NEW "+temp);
                                                data_members.put(temp, cur_class);
                                            }

                                    }
                            }
                            
                            else if(line.contains("char"))
                            {
                                String Data_Members_Array = line.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                              //  System.out.println(" char NEW "+word);
                                                String temp = "char "+word;
                                                System.out.println(" temp NEW "+temp);
                                                data_members.put(temp, cur_class);
                                            }

                                    }
                            }
                            
                            
                          // System.out.println("HERE FOR  THE DATA MEMBSERS...!!! and the line is::\n"+line);
                          //  data_members.put(line, cur_class);
                        
                        }
                        
                        
                        
                        
                        
                        
                        //*********************************************************************************************
                        //*********************************************************************************************
                        
                        
                        
                    }
                    if (arr[0].equals("<Associates>")) {
                        method = 0;
                        mem_flag = 0;
                    }
                    if (arr[0].equals("<class>:")) {
                        method = 0;
                        Classname.add(arr[1]);
                    }
                    if (arr[0].equals("<Data"));
                    {
                        //System.out.println("Gf");
                        mem_flag = 1;
                    }
                }
                fr.close();
                //System.out.println(Classname);
                //System.out.println(data_members);
                for (String key : method_members.keySet()) {
                    {
                        System.out.println(key + "," + method_members.get(key));
                    }
                }

                for (String key : data_members.keySet()) {
                    //if(method_members.get(key)=="Class2")
                    {
                        //System.out.println(key + "," + data_members.get(key));
                    }
                }
                fp.delete();
                fp = new File(opfilepath);
                return true;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while trying to open input file");
            e.printStackTrace();
        }
        return false;
    }

    public static void VersionComparator(String opfilename, String cur_class, int method, int mem_flag, HashSet<String> Classname, HashMap<String, String> data_members, HashMap<String, String> method_members) {
        try {
            File fpp = new File(opfilename);
            if (!fpp.exists()) {
                System.err.println("No current version file exists!!");
            } else {
                FileReader frr = new FileReader(fpp);
                BufferedReader brr = new BufferedReader(frr);
                ArrayList<String> liness = new ArrayList<>();
                String linee;
                List<metdata> listt = new ArrayList<metdata>();
                cur_class = null;
                method = 0;
                mem_flag = 0;
                HashSet<String> Classnamee = new HashSet<String>();
                HashMap<String, String> data_memberss = new HashMap<String, String>();
                HashMap<String, String> method_memberss = new HashMap<String, String>();
                HashMap<String, String> change_method = new HashMap<String, String>();
                HashMap<String, String> change_method1 = new HashMap<String, String>();
                HashMap<String, String> change_data = new HashMap<String, String>();
                HashMap<String, String> change_data1 = new HashMap<String, String>();
                
                while ((linee = brr.readLine()) != null) {
                    String[] arr = linee.split(" ");
                    if (mem_flag == 1) {
                        Iterator itr = Classnamee.iterator();
                        while (itr.hasNext()) {
                            cur_class = (String) itr.next();
                        }
                        Matcher m = Pattern.compile("\\(*\\)").matcher(linee);
                        if (m.find()) {
                            method = 1;
                            method_memberss.put(linee, cur_class);
                        }
                        /*if (method == 0) {
                            data_memberss.put(linee, cur_class);
                        }*/
                        
                        //*********************************************************************************************
                        //Kedarnath's post ver data member identifier
                        //*********************************************************************************************
                        if (method == 0) 
{
                        if(linee.contains("private"))
                            {
                                
                              System.out.println("I contain the private..\n"+linee);
                              String Data_Members_Array1 = linee.replaceAll("\\s+", " ");
                              String sample[] = Data_Members_Array1.split(" ");
                                   
                              if(sample.length<=1)
                              {
                                  System.out.println("I contain length 1"+linee);
                              }
                              else
                              { 
                                  System.out.println("My lenght is not 1"+linee);
                                  if(linee.contains("int"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                   
                                               Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private int "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put(temp, cur_class);
                                                           }

                                                    }
                                            }//if(for int)
                                  else if(linee.contains("float"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private float "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)
                                  else if(linee.contains("double"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private double "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put(temp, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                  
                                            else if(linee.contains("char"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
  
                                               Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("private", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "private char "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("private char "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                                            
                              
                              
                              }                          
                            
                            }
                            else if(linee.contains("protected"))
                            {
                                
                              System.out.println("I contain the protected..\n"+linee);
                              String Data_Members_Array1 = linee.replaceAll("\\s+", " ");
                              String sample[] = Data_Members_Array1.split(" ");
                                   
                              if(sample.length<=1)
                              {
                                  System.out.println("I contain length 1"+linee);
                              }
                              else
                              { 
                                  System.out.println("My lenght is not 1"+linee);
                                  if(linee.contains("int"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                   
                                               Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected int "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("protected int "+word, cur_class);
                                                           }

                                                    }
                                            }//if(for int)
                                  else if(linee.contains("float"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected float "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("protected float "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)
                                  else if(linee.contains("double"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected double "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("protected double "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                  
                                            else if(linee.contains("char"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("protected", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "protected char "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("protected char "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                                            
                              
                              
                              }                          
                            
                            }
                            else if(linee.contains("public"))
                            {
                              System.out.println("I contain the public..\n"+linee);
                              String Data_Members_Array1 = linee.replaceAll("\\s+", " ");
                              String sample[] = Data_Members_Array1.split(" ");
                                   
                              if(sample.length<=1)
                              {
                                  System.out.println("I contain length 1"+linee);
                              }
                              else
                              { 
                                  System.out.println("My lenght is not 1"+linee);
                                  if(linee.contains("int"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public int "+word;
                                                         System.out.println(" WORD IS FOR INT "+temp);
                                                         data_memberss.put(temp, cur_class);
                                                           }

                                                    }
                                            }//if(for int)
                                  else if(linee.contains("float"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public float "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("public float "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)
                                  else if(linee.contains("double"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public double "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("public double "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                  
                                            else if(linee.contains("char"))
                                            {
                                              String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                                 // Data_Members_Array = Data_Members_Array.replaceAll("\\s+", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                               Data_Members_Array = Data_Members_Array.replaceAll("public", "");
      
                                               Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                                 for(String word : Data_Members_Array.split("\\s+"))
                                                   {
                                                      if(word.isEmpty())
                                                            {

                                                            }
                                                                   // System.out.println(" YES "+word);
                                                      else{
                                                                   // System.out.println(" INT NEW "+word);
                                                         String temp = "public char "+word;
                                                         System.out.println(" WORD IS "+temp);
                                                         data_memberss.put("public char "+word, cur_class);
                                                           }

                                                    }
                                                }//else if (float)                                                            
                              
                              
                              }    
                            }
                         
                            //separate int checking 
                            else if(linee.contains("int"))
                            {
                                System.out.println("Contains int and the line is ++ " + linee);
                                String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("int", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                               // System.out.println(" INT NEW "+word);
                                                String temp = "int "+word;
                                                System.out.println(" temp NEW HERE >>>" + temp);
                                                data_memberss.put("int "+word, cur_class);
                                            }

                                    }
                            }
                            //separate float checking
                            else if(linee.contains("float"))
                            {
                                String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("float", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                               // System.out.println(" INT NEW "+word);
                                                String temp = "float "+word;
                                                System.out.println(" temp NEW "+temp);
                                                data_memberss.put("float "+word, cur_class);
                                            }

                                    }
                            }
                            
                            else if(linee.contains("double"))
                            {
                                String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("double", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                               // System.out.println(" double NEW "+word);
                                                String temp = "double "+word;
                                                System.out.println(" temp NEW "+temp);
                                                data_memberss.put("double "+word, cur_class);
                                            }

                                    }
                            }
                            
                            else if(linee.contains("char"))
                            {
                                String Data_Members_Array = linee.replaceAll("\\s+", " ");
                                Data_Members_Array = Data_Members_Array.replaceAll("char", "");
                                Data_Members_Array = Data_Members_Array.replaceAll(",", "");
                                for(String word : Data_Members_Array.split("\\s+"))
                                    {
                                        if(word.isEmpty())
                                        {
                                        
                                        }
                                               // System.out.println(" YES "+word);
                                        else{
                                              //  System.out.println(" char NEW "+word);
                                                String temp = "char "+word;
                                                System.out.println(" temp NEW "+temp);
                                                data_memberss.put("char "+word, cur_class);
                                            }

                                    }
                            }
		  
		  
		  
                    }
                        
                        
                        //*********************************************************************************************
                        //*********************************************************************************************
                        
                        
                        
                    }
                    if (arr[0].equals("<Associates>")) {
                        method = 0;
                        mem_flag = 0;
                    }
                    if (arr[0].equals("<class>:")) {
                        method = 0;
                        Classnamee.add(arr[1]);
                    }
                    if (arr[0].equals("<Data"));
                    {
                        //System.out.println("Gf");
                        mem_flag = 1;
                    }
                }

                brr.close();

                //Logging pre and post data members
                for(String key : data_members.keySet())
            {
                System.out.println(key + "," + data_members.get(key));
            }
            System.out.println("Post Version Data::\n\n");
            for(String key : data_memberss.keySet())
            {
                System.out.println(key + "," + data_memberss.get(key));
            }
             //*************
             
             //Changes to data members
             
             int flag_data;
            for (String key : data_members.keySet()) {
                flag_data = 0;
                //String keytemp=method_memberss.get(key);

                for (String key_s : data_memberss.keySet()) {

                    String key_temp = data_memberss.get(key_s);
                    if (key_s.equals(key)) {

                        flag_data = 1;
                        break;
                    }
                }
                if (flag_data == 0) {

                    change_data.put(key, data_members.get(key));
                }
            }
            System.out.println("<deleted Data item>" + "\n" + change_data);
            
            for (String key : data_memberss.keySet()) {
                flag_data = 0;
                for (String key_s : data_members.keySet()) {

                    String key_temp = data_members.get(key_s);
                    if (key_s.equals(key)) {
                        flag_data = 1;
                        break;
                    }
                }
                if (flag_data == 0) {
                    change_data1.put(key, data_memberss.get(key));

                }
            }
            System.out.println("<Added Data item>" + "\n" + change_data1);
             //*******************************
             //Data mebers access specifier changed code
             String s11="";
		  String s22="";
		  StringBuilder sb2=new StringBuilder("");
		  for (String key : change_data.keySet())
		  {
                      //public int a
                      // private int a
                      //int a
//				flag=0;
				key = key.replaceAll("\\s{2,}", " ").trim(); 
                                String[] func = key.split(" ");
                                for (String key_s : change_data1.keySet()) 
                                {
		    		key_s = key_s.replaceAll("\\s{2,}", " ").trim(); 
		      		String[] func_post = key_s.split(" ");
                                if(func[0].equals("private")||func[0].equals("protected")||func[0].equals("public"))
                                {
                                if(func_post[1].equals(func[1]) && func_post[2].equals(func[2])) 
		      		{
		      			if(!func_post[0].equals(func[0]))
		      			{
		      				System.out.println(key+"\n"+key_s);
		      				s11=key;
		      				s22=key_s;
		      				//System.out.println(key.toString());
		      				// change_data1.remove(key_s.toString());
		      				// change_data.remove(key.toString());
		      				sb2.append(func[1]+" "+ func[2]+" changed "+func[0]+" to "+func_post[0] + "  ");
                                                System.out.println("Data Member "+func[1]+" "+ func[2]+" change access specifier "+func[0]+" to "+func_post[0]);
                                        }
		      		}
                                }
		      		
		        }
		   }
             
             
             //**************
                for (String key : method_memberss.keySet()) {
                    {
                        System.out.println(key + "," + method_memberss.get(key));
                    }
                }
                int flag;
                for (String key : method_members.keySet()) {
                    flag = 0;
                    //String keytemp=method_memberss.get(key);

                    for (String key_s : method_memberss.keySet()) {

                        String key_temp = method_memberss.get(key_s);
                        if (key_s.equals(key)) {

                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {

                        change_method.put(key, method_members.get(key));
                    }
                }
                //System.out.println("<deleted item>" + "\n" + change_method);
                //change_method.clear();
                for (String key : method_memberss.keySet()) {
                    flag = 0;
                    for (String key_s : method_members.keySet()) {

                        String key_temp = method_members.get(key_s);
                        if (key_s.equals(key)) {

                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        change_method1.put(key, method_memberss.get(key));

                    }
                }

                String s1 = "";
                String s2 = "";
                StringBuilder sb = new StringBuilder("");
                for (String key : change_method.keySet()) {
                    flag = 0;
                    key = key.replaceAll("\\s{2,}", " ").trim();
                    String[] func = key.split(" ");
                    for (String key_s : change_method1.keySet()) {
                        key_s = key_s.replaceAll("\\s{2,}", " ").trim();
                        String[] func_post = key_s.split(" ");
                        if (func_post[1].equals(func[1]) && func_post[2].equals(func[2])) {
                            if (!func_post[0].equals(func[0])) {
                                System.out.println(key + "\n" + key_s);
                                s1 = key;
                                s2 = key_s;
                                //System.out.println(key.toString());
                                change_method1.remove(key_s.toString());
                                change_method.remove(key.toString());
                                sb.append("Method " + func[1] + " " + func[2] + " change access specifier " + func[0] + " to " + func_post[0]);
                            }
                        }
                    }
                }
                 
                
                /*
                //Add delete Classes
                HashSet<String> del1 = new HashSet<String>();
                HashSet<String> add1 = new HashSet<String>();
                for (String i : Classname) {
                    flag = 0;
                    for (String ii : Classnamee) {
                        if (i.equals(ii)) {
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        del1.add(i);

                    }
                }

                for (String i : Classnamee) {
                    flag = 0;
                    for (String ii : Classname) {
                        if (i.equals(ii)) {
                            flag = 1;
                        }
                    }

                    if (flag == 0) {
                        add1.add(i);
                    }
                }
                */
                System.out.println("<deleted item>" + "\n" + change_method);
                System.out.println("<Add item>" + "\n" + change_method1);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.println();

                BufferedWriter writer = new BufferedWriter(new FileWriter("Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\scan.txt"));
                /*
                System.err.println(add1);
                for(String i: add1) 
                {
                    writer.write("\n"+i+" class was added from previous scan");
                }
                for(String i: del1) 
                {
                        writer.write("\n"+i+" class was deleted from previous scan");
                }
                */
                
                //Added By Kedarnath
                //Store data member changes to scan
                for (String key : change_data.keySet()) {
                    writer.write("\n[-] " + key + " was deleted from previous scan");
                }
                for (String key : change_data1.keySet()) {
                    writer.write("\n[+] " + key + " was added from previous scan");
                }
                //*************
                
                for (String key : change_method.keySet()) {
                    writer.write("\n[-] " + key + "was deleted from previous scan");
                }
                for (String key : change_method1.keySet()) {
                    writer.write("\n[+] " + key + "was added from previous scan");
                }
                writer.write("\n[*] " + sb2.toString());
                writer.write("\n");
                writer.write("\n[*] " + sb.toString());
                writer.write(sb.toString());
                
                writer.flush();
                writer.close();

                BufferedWriter AutoDocwriter = new BufferedWriter(new FileWriter("Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\autodoc.txt"));
                AutoDocwriter.write("//*****AUTODOC Commentary*****");
                AutoDocwriter.write("\nOn " + dtf.format(now) + " following changes were automatically noted by AutoDoc: \n");

                BufferedReader scanReader = new BufferedReader(new FileReader("Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\scan.txt"));
                String ScanFileLine;
                scanReader.readLine();
                while ((ScanFileLine = scanReader.readLine()) != null) {
                    AutoDocwriter.write("\n" + ScanFileLine);
                }
                scanReader.close();

                AutoDocwriter.write("\n\nClass Model:");
                BufferedReader outputReader = new BufferedReader(new FileReader(opfilename));
                //Skip comments and timestamp lines
                for (int i = 1; i <= 2; i++) {
                    outputReader.readLine();
                }
                String OpFileLine;
                while ((OpFileLine = outputReader.readLine()) != null) {
                    AutoDocwriter.write("\n" + OpFileLine);
                }
                outputReader.close();

                AutoDocwriter.write("\n//*****END AUTODOC Commentary\n\n\n");

                //Read and write source code
                final File folder = new File("Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\inputs");
                ArrayList<String> FileNames = GetFiles(folder);
                for (String fileclass : FileNames) {
                    BufferedReader inputReader = new BufferedReader(new FileReader("inputs\\" + fileclass));
                    String IpFileLine;
                    while ((IpFileLine = inputReader.readLine()) != null) {
                        AutoDocwriter.write("\n" + IpFileLine);
                    }
                }
                AutoDocwriter.close();
            }
        } catch (IOException e) {
            System.err.println("C   annot open current version to compare!");
        }
    }

    public static void main(String[] args) throws IOException, RecognitionException {
        final File folder = new File("Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\inputs");
        String opfilepath = "Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\output.txt";

        //ReadPreviousVersion data
        String cur_class = null;
        int method = 0, mem_flag = 0;
        HashSet<String> Classname = new HashSet<String>();
        HashMap<String, String> data_members = new HashMap<String, String>();
        HashMap<String, String> method_members = new HashMap<String, String>();

        //if previous output doesn't exists, create a blank output file | else read and clear the previous version of output file
        boolean PrevRead = ReadPreviousVersion(opfilepath, cur_class, method, mem_flag, Classname, data_members, method_members);

        ArrayList<String> FileNames = GetFiles(folder);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        HashMap<String, ArrayList<String>> Descendants = new HashMap<String, ArrayList<String>>();
        HashMap<String, Set<String>> Ancestors = new HashMap<String, Set<String>>();

        for (String filename : FileNames) {
            //Parses java input files and writes the parsed output to output.txt
            antlr_projLexer lexer = new antlr_projLexer(new ANTLRFileStream("inputs\\" + filename));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            antlr_projParser parser = new antlr_projParser(tokens);
            parser.compilationUnit();

            // If previous version exists, reads and compares it with current version
            if (PrevRead) {
                VersionComparator(opfilepath, cur_class, method, mem_flag, Classname, data_members, method_members);
            }

            //Create tree
            final ArrayList<String> DataMembers = parser.DataMembers;
            final ArrayList<String> MethodMembers = parser.MethodMembers;
            final Map<String, Set<String>> mc_map = parser.mc_map;
            final ArrayList<String> desc = parser.Class_Descedents;
            final ArrayList<String> ancestor = parser.Class_Ancestors;
            final Set<String> Aggregation = parser.Aggregation;
            final Set<String> Association = parser.Assocation;

            if (!desc.isEmpty() && Descendants.containsKey(desc.get(0))) {
                Descendants.get(desc.get(0)).add(desc.get(1));
            } else {
                if (!desc.isEmpty()) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(desc.get(1));
                    Descendants.put(desc.get(0), temp);
                }
            }

            //Ancestors
            if (!ancestor.isEmpty() && Ancestors.containsKey(ancestor.get(0))) //would never be true, since we have single key value pair of a class and it's parent, there won't be a case where it is already addded to Ancestors hashmap
            {
                Ancestors.get(ancestor.get(0)).add(ancestor.get(1));
            } else {
                if (!ancestor.isEmpty()) {
                    Set<String> temp = new HashSet<String>();
                    temp.add(ancestor.get(1));
                    Ancestors.put(ancestor.get(0), temp);
                    Ancestors.put(ancestor.get(0), temp);
                }
            }

            // System.err.println("\n\n\nAncestors :" + Ancestors);
            //recursive tree
            for (Map.Entry element : Ancestors.entrySet()) {
                String CurrentClass = (String) element.getKey();
                String A = "";
                if (Ancestors.containsKey(CurrentClass)) {
                    A = Ancestors.get(CurrentClass).stream().findFirst().get();
                    CreateAncestorsTree(Ancestors, CurrentClass, A);
                }
            }
            Aggregation.remove("String");
            //System.out.println("Data Members:: " + parser.DataMembers);
            //System.out.println("Method Members:: " + parser.MethodMembers);
            //System.out.println("Method Calls " + parser.mc_map);
            //System.out.println("Hm" + parser.Class_Descedents);

            JTextPane tPane = new JTextPane();
            EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
            tPane.setBorder(eb);
            tPane.setMargin(new Insets(5, 5, 5, 5));
            //FONT_CHANGER
            tPane.setFont(new Font("Lucida Console", Font.PLAIN, 15));
            JPanel topPanel = new JPanel();
            topPanel.add(tPane);
            try 
            {
                String autodocReader;
                BufferedReader autoinput = new BufferedReader(new InputStreamReader(
                        new FileInputStream("Z:\\MS CS\\SEM 3\\OOAD CECS 575\\Assignment 4\\ANTLR_PROJ\\autodoc.txt")));
                
                while ((autodocReader = autoinput.readLine()) != null) 
                {
                    String[] parts = autodocReader.split(" ");
                    if(parts[0].equals("[+]"))
                    {
                        appendToPane(tPane, autodocReader + "\n", Color.GREEN);
                    }
                    else if(parts[0].equals("[-]"))
                    {
                        appendToPane(tPane, autodocReader + "\n", Color.RED);
                    }
                    else if(parts[0].equals("[*]"))
                    {
                              appendToPane(tPane, autodocReader + "\n", Color.BLUE);
                    }
                    else
                    {
                          appendToPane(tPane, autodocReader + "\n", Color.BLACK);
                    }
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            
            

            //************************
            //
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ANTLR_PROJ app = new ANTLR_PROJ();
                    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    app.setTitle("OOPIES");
                    app.setLayout(new BorderLayout());
                    app.setPreferredSize(new Dimension(2000, 1000));
                    JTreePanel mytree = new JTreePanel(root, DataMembers, MethodMembers, mc_map, Descendants, Ancestors, Aggregation, Association, filename);
                    app.add(mytree.getJpanel(), BorderLayout.LINE_START);
                    app.add(topPanel, BorderLayout.CENTER);

                    app.setVisible(true);
                    app.pack();
                }
            });

        }
        /* Delete input files
        
        for (String filename : FileNames) 
        {
            Files.deleteIfExists(Paths.get("inputs\\"+filename));
        }
         */
    }
}
