/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antlr_proj;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author prathamesh
 */
public class JTreePanel 
{
    private JPanel jpanel= new JPanel();
    private JTree tree;
    private ArrayList<String> DataMembers;
    private ArrayList<String> MethodMembers;
    private Map<String, Set<String>> mc;
    private Set<String> Aggr;
    private Set<String> Assoc;
    
    HashMap<String, ArrayList<String>> Descendants;
    HashMap<String, Set<String>> Ancestors;
    public JTreePanel(DefaultMutableTreeNode r, ArrayList<String> DM, ArrayList<String> MM, Map<String, Set<String>> mc,HashMap<String, ArrayList<String>> desc, HashMap<String, Set<String>> anc, Set<String> Aggregation, Set<String> Association,String ClassName)
    {
        super();
        DataMembers = DM;
        MethodMembers = MM;
        mc = mc;
        Descendants = desc;
        Ancestors = anc;
        DefaultMutableTreeNode root = r;
        Aggr = Aggregation;
        Assoc = Association;
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode Class = new DefaultMutableTreeNode(new DefaultMutableTreeNode(ClassName));
        DefaultMutableTreeNode dm = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Data Members"));
        DefaultMutableTreeNode mm = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Method Members"));
        DefaultMutableTreeNode methc = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Method Calls"));
        DefaultMutableTreeNode descendants = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Descendants"));
        DefaultMutableTreeNode ancestors = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Ancestors"));
        DefaultMutableTreeNode agg = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Aggregation"));
        DefaultMutableTreeNode assoc = new DefaultMutableTreeNode(new DefaultMutableTreeNode("Association"));
        
        
        for(String datamem: DataMembers)
        {
            dm.add(new DefaultMutableTreeNode(datamem));
        }
        
        for(String methmem: MethodMembers)
        {
            mm.add(new DefaultMutableTreeNode(methmem));
        }
        
        for (Map.Entry<String, Set<String>> entry : mc.entrySet()) 
        {
            String key = entry.getKey();
            DefaultMutableTreeNode methodname = new DefaultMutableTreeNode(new DefaultMutableTreeNode(key));
            for(String calls: mc.get(key))
            {
                methodname.add(new DefaultMutableTreeNode(calls));
            }
            methc.add(methodname);
        }
        String classname = ClassName.substring(0, ClassName.indexOf("."));
         if(desc.containsKey(classname))
         {
             for(String d : desc.get(classname))
             {
                 descendants.add(new DefaultMutableTreeNode(d));
             }
         }
         
         if(Ancestors.containsKey(classname))
         {
             for(String d : Ancestors.get(classname))
             {
                 ancestors.add(new DefaultMutableTreeNode(d));
             }
         }
         
         for(String a : Aggr)
         {
             agg.add(new DefaultMutableTreeNode(a));
         }
         for(String a : Assoc)
         {
             assoc.add(new DefaultMutableTreeNode(a));
         }
         
        Class.add(dm);
        Class.add(mm);
        Class.add(methc);
        Class.add(descendants);
        Class.add(ancestors);
        Class.add(agg);
        Class.add(assoc);
        root.add(Class);
        tree = new JTree(root);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
       
        //Set font size
        final Font currentFont = tree.getFont();
        final Font bigFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 15);
        tree.setFont(bigFont);
     
        jpanel.add(tree);
        
        //Auto Expand nodes recursively
        //expandAllNodes(tree, 0, tree.getRowCount());
    }
    
    public JPanel getJpanel()
    {
        return jpanel;
    }
    
    //Function to auto expand all tree nodes recursively
    /*
    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
    for(int i=startingIndex;i<rowCount;++i){
        tree.expandRow(i);
    }

    if(tree.getRowCount()!=rowCount){
        expandAllNodes(tree, rowCount, tree.getRowCount());
    }
}*/
}
