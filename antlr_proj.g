grammar antlr_proj;

options {backtrack=true; memoize=true;}

@header{
package antlr_proj;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
}

@members{

boolean Has_Modifier = false;

boolean HasComma = false;
boolean Has_Par = false;
boolean Data_Tag = false;

boolean Decl_Has_Params =false;
boolean variable_decl_flag=false;
String Mod_Temp="";
String Prim_Temp="";
boolean isExtends=false, isImp=false;
int intCount=0;
boolean isFormalPara=false;
boolean isFun=false;
boolean isNew=false;
boolean isSuperSuffix=false;
String funName = "";
String CurrentClass;
//  track methods' call

Map<String, Set<String>> mc_map = new LinkedHashMap<>();
// key

String method = "";

ArrayList<String> Class_Descedents = new ArrayList<String>();
ArrayList<String> Class_Ancestors = new ArrayList<String>();

// values

ArrayList<String> Classnames = new ArrayList();

ArrayList<String> DataMembers = new ArrayList();

ArrayList<String> MethodMembers = new ArrayList();

String Void_Method_Before_Decl;
String Current_Modifier="";
String Current_Primitive="";

boolean void_method_flag = false;
boolean test_method = false;

boolean Method_Flag=false;
String Method_Return_Type;

Set<String> methodCall = new HashSet<>();
Set<String> Assocation = new HashSet<>();

boolean isAggregation = false;
Set<String> Aggregation = new HashSet<>();

String methodName = "";
boolean inMethod = false;
boolean inMethodprint = false;

public class FileIO
{
	public void FileIOMethod(String write, boolean NewLine)
	{
		try{
			FileWriter FileWriter = new FileWriter("output.txt", true);
			PrintWriter Fout = new PrintWriter(FileWriter);
			if(NewLine)
			{
				Fout.append(System.lineSeparator());
			}
			Fout.append(write + " ");
			Fout.close();
		}
		catch(IOException e)
		{
		System.out.println("An error occurred in FileIO.");
		e.printStackTrace();
		}
	}
	
	public void FileIOMethod(List<String> write, boolean NewLine)
	{
		try{
			FileWriter FileWriter = new FileWriter("output.txt", true);
			PrintWriter Fout = new PrintWriter(FileWriter);
			if(NewLine)
			{
				Fout.append(System.lineSeparator());
			}
			for(String w : write)
			{
				Fout.append(w);
				Fout.append(System.lineSeparator());
			}	
			Fout.close();
		}
		catch(IOException e)
		{
		System.out.println("An error occurred in FileIO.");
		e.printStackTrace();
		}
	}

	public void FileIOMethod(Set<String> write, boolean NewLine)
	{
		try{
			FileWriter FileWriter = new FileWriter("output.txt", true);
			PrintWriter Fout = new PrintWriter(FileWriter);
			if(NewLine)
			{
				Fout.append(System.lineSeparator());
			}
			for(String w : write)
			{
				Fout.append(w);
				Fout.append(System.lineSeparator());
			}	
			Fout.close();
		}
		catch(IOException e)
		{
		System.out.println("An error occurred in FileIO.");
		e.printStackTrace();
		}
	}

	
}
FileIO fio = new FileIO();
}
@lexer::header{
package antlr_proj;
}

@lexer::members {
  protected boolean enumIsKeyword = true;
  protected boolean assertIsKeyword = true;
}


// starting point for parsing a java file
/* The annotations are separated out to make parsing faster, but must be associated with
   a packageDeclaration or a typeDeclaration (and not an empty one). */
compilationUnit
    :   annotations
        (   packageDeclaration importDeclaration* typeDeclaration*
        |   classOrInterfaceDeclaration typeDeclaration*
        )
    |   packageDeclaration? importDeclaration* typeDeclaration*
    {
	System.err.println("\n Method Calls: " + mc_map);
	Assocation.remove("String");
     	System.err.println("Association " +Assocation);  fio.FileIOMethod("Associates : " + Assocation, true);
     	System.err.println("Aggregation "+ Aggregation); fio.FileIOMethod("Aggregates : " + Aggregation, true);
    }
    ;

packageDeclaration
    :   'package' qualifiedName ';'
    ;
    
importDeclaration
    :   'import' 'static'? qualifiedName ('.' '*')? ';'
    ;
    
typeDeclaration
    :   classOrInterfaceDeclaration
    |   ';'
    ;
    
classOrInterfaceDeclaration
    :   classOrInterfaceModifiers (classDeclaration | interfaceDeclaration)
    ;
    
classOrInterfaceModifiers
    :   classOrInterfaceModifier*
    ;

classOrInterfaceModifier
    :   annotation   // class or interface
    |   'public'     {System.out.print("public "); fio.FileIOMethod("public ", true);}// class or interface
    |   'protected'   {System.out.println("protected "); fio.FileIOMethod("protected ", true);}// class or interface
    |   'private'     {System.out.println("private "); fio.FileIOMethod("private ", true);}// class or interface
    |   'abstract'   {System.out.println("abstract "); fio.FileIOMethod("abstract ", false);} // class or interface
    |   'static'      {System.out.println("static "); fio.FileIOMethod("static ", false);}// class or interface
    |   'final'      // class only -- does not apply to interfaces
    |   'strictfp'   // class or interface
    ;

modifiers
    :   modifier*
    ;

classDeclaration
    :   normalClassDeclaration
    |   enumDeclaration
    ;
    
normalClassDeclaration
    :   'class' Identifier { 
    	System.out.println("class:"+$Identifier.text);
    //Removed Class: By As asked by prathmesh
       // fio.FileIOMethod("<ClassName:>", true);
    	fio.FileIOMethod("<class>: "+$Identifier.text, true);
    	CurrentClass = $Identifier.text;
    	Classnames.add(CurrentClass);
    } typeParameters?
        ('extends'{isExtends=true;} type)?
        ('implements'{isImp= true;} typeList)?
        {System.out.println("Data members: "); fio.FileIOMethod("<Data Members>: ", true);} classBody 
    ;
    
typeParameters
    :   '<' typeParameter (',' typeParameter)* '>'
    ;

typeParameter
    :   Identifier ('extends' typeBound)?
    ;
        
typeBound
    :   type ('&' type)*
    ;

enumDeclaration
    :   ENUM Identifier ('implements' typeList)? enumBody
    ;

enumBody
    :   '{' enumConstants? ','? enumBodyDeclarations? '}'
    ;

enumConstants
    :   enumConstant (',' enumConstant)*
    ;
    
enumConstant
    :   annotations? Identifier arguments? classBody?
    ;
    
enumBodyDeclarations
    :   ';' (classBodyDeclaration)*
    ;
    
interfaceDeclaration
    :   normalInterfaceDeclaration
    |   annotationTypeDeclaration
    ;
    
normalInterfaceDeclaration
    :   'interface' Identifier typeParameters? ('extends' typeList)? interfaceBody
    ;
    
typeList
    :   type (',' type)*
    ;
    
classBody
    :   '{' classBodyDeclaration* '}' 
    ;
    
interfaceBody
    :   '{' interfaceBodyDeclaration* '}'
    ;

classBodyDeclaration
    :   ';'
    |   'static'? block
    |   modifiers memberDecl 
    ;

memberDecl
    :   genericMethodOrConstructorDecl 
    |   memberDeclaration 
    |    {inMethod = true;} 'void' Identifier {method = $Identifier.text;  System.out.print("void" + " " + method); fio.FileIOMethod("void" + " " + method, false); 
    	 MethodMembers.add(Current_Modifier+" "+"void"+ " " + method+"()");} voidMethodDeclaratorRest  
    |  {inMethod = true; }  Identifier {method = $Identifier.text; System.out.print(method); fio.FileIOMethod(method, false);	MethodMembers.add(Current_Modifier+" "+method+"()");}
    	 constructorDeclaratorRest  
    
    |   interfaceDeclaration
    |   classDeclaration 
    ;
    
memberDeclaration
    :    type  ( {inMethod = true;} methodDeclaration   | {inMethod = false;} fieldDeclaration ) 
    ;

genericMethodOrConstructorDecl
    :  typeParameters genericMethodOrConstructorRest
    ;
    
genericMethodOrConstructorRest
    :   (type | 'void') Identifier methodDeclaratorRest
    |   Identifier constructorDeclaratorRest 
    ;

methodDeclaration
    :   Identifier { 	method = $Identifier.text; 
	    		System.out.print(method+"("); 
	    		fio.FileIOMethod(method + "(", false); 
	    		
	    		Has_Par = true; 
	    	       //	fio.FileIOMethod("HERE I'M SETTING THE VALUE", false);
	    		MethodMembers.add(Current_Modifier+" "+method + "()");
    		} methodDeclaratorRest {
    	System.out.println(")"); fio.FileIOMethod(")", false);
    }
    ;

fieldDeclaration
    :  { variable_decl_flag=true;} variableDeclarators  ';' 
    ;
        
interfaceBodyDeclaration
    :   modifiers interfaceMemberDecl
    |   ';'
    ;

interfaceMemberDecl
    :   interfaceMethodOrFieldDecl
    |   interfaceGenericMethodDecl
    |   'void' Identifier voidInterfaceMethodDeclaratorRest
    |   interfaceDeclaration
    |   classDeclaration
    ;
    
interfaceMethodOrFieldDecl
    :   type Identifier interfaceMethodOrFieldRest
    ;
    
interfaceMethodOrFieldRest
    :   constantDeclaratorsRest ';'
    |   interfaceMethodDeclaratorRest
    ;
    
methodDeclaratorRest
    :   formalParameters ('[' ']')*
        ('throws' qualifiedNameList)?
        (   methodBody
        |   ';'
        ) 
    ;
    
voidMethodDeclaratorRest
    :   {System.out.print("("); fio.FileIOMethod("(", false); Has_Par = true; }  formalParameters {System.out.println(")"); fio.FileIOMethod(")", false);} ('throws' qualifiedNameList)?
        (   {isAggregation = true;}  methodBody 
        |   ';'
        )
    ;
    
interfaceMethodDeclaratorRest
    :   formalParameters ('[' ']')* ('throws' qualifiedNameList)? ';'
    ;
    
interfaceGenericMethodDecl
    :   typeParameters (type | 'void') Identifier
        interfaceMethodDeclaratorRest
    ;
    
voidInterfaceMethodDeclaratorRest
    :   formalParameters ('throws' qualifiedNameList)? ';'
    ;
    
constructorDeclaratorRest
    :   {System.out.print("("); fio.FileIOMethod("(", false); Has_Par = true;} formalParameters {System.out.println(")"); fio.FileIOMethod(")", false);} ('throws' qualifiedNameList)? constructorBody
    ;

constantDeclarator
    :   Identifier constantDeclaratorRest
    ;
    
variableDeclarators
    :   variableDeclarator (',' {System.out.print(", "); fio.FileIOMethod(", ", false);variable_decl_flag=true; Decl_Has_Params=true;
    			Current_Primitive=Prim_Temp; Current_Modifier = Mod_Temp;} variableDeclarator)*
    ;

variableDeclarator
    :   variableDeclaratorId ('=' variableInitializer)?
    ;
    
constantDeclaratorsRest
    :   constantDeclaratorRest (',' constantDeclarator)*
    ;

constantDeclaratorRest
    :   ('[' ']')* '=' variableInitializer
    ;
    
variableDeclaratorId
    :   Identifier {System.out.print($Identifier.text); fio.FileIOMethod($Identifier.text, false);
    if(variable_decl_flag==true){
    	
	    	if(Decl_Has_Params==false)
	    	{    		
	    		 DataMembers.add(Current_Modifier+" "+Current_Primitive+" "+$Identifier.text);    		
	    		 Mod_Temp = Current_Modifier;
	    		 Prim_Temp = Current_Primitive;
	    		  variable_decl_flag=false;
	    	}
	    
        if(Decl_Has_Params==true)
    	{
    		DataMembers.add(Current_Modifier+" "+Current_Primitive+" "+ $Identifier.text);    		
    		Decl_Has_Params=false;
    		variable_decl_flag=false;

    	}
    	}} ('[' ']')*
    ;

variableInitializer
    :   arrayInitializer
    |   expression
    ;
        
arrayInitializer
    :   '{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
    ;
// 'class' Identifier {System.out.println("Class:"+$Identifier.text);} typeParameters?
modifier
    :   annotation    
    |   'public' {System.out.print("public "); fio.FileIOMethod("public", true); Current_Modifier = "public"; Has_Modifier =true;}
    |   'protected' {System.out.print("protected "); fio.FileIOMethod("protected", true); Current_Modifier = "protected";Has_Modifier =true;}
    |   'private' {System.out.print("private " ); fio.FileIOMethod("private", true); Current_Modifier = "private";Has_Modifier =true;}
    //removed static from new line by kedarnath
    |   'static' {System.out.print("static "); fio.FileIOMethod("static", false); Current_Modifier = "static";Has_Modifier =true;}
    |   'abstract' {System.out.print("abstract "); fio.FileIOMethod("abstract", false); Current_Modifier = "abstract";Has_Modifier =true;}
    |   'final' {System.out.print("final "); fio.FileIOMethod("final", true); Current_Modifier = "final";Has_Modifier =true;}
    |   'native' {System.out.print("native "); fio.FileIOMethod("native ", false); Current_Modifier = "native";Has_Modifier =true;}
    |   'synchronized' {System.out.print("synchronized "); fio.FileIOMethod("synchronized ", false); Current_Modifier = "synchronized";}
    |   'transient' {System.out.print("transient "); fio.FileIOMethod("transient ", false); Current_Modifier = "transient";}
    |   'volatile' {System.out.print("volatile "); fio.FileIOMethod("volatile ", false); Current_Modifier = "volatile";}
    |   'strictfp' {System.out.print("strictfp "); fio.FileIOMethod("strictfp ", false); Current_Modifier = "strictfp";}
    
    ;

packageOrTypeName
    :   qualifiedName
    ;

enumConstantName
    :   Identifier
    ;

typeName
    :   qualifiedName
    ;

type
	:	{isAggregation = true;} classOrInterfaceType  ('[' ']')*
	|	{isAggregation = false;} primitiveType ('[' ']')*
	;

classOrInterfaceType								
	:	I1=Identifier {
				System.out.print(CurrentClass+" "); fio.FileIOMethod(CurrentClass+" ", true);
				if (isFun) {
					if ($I1.text != null && !$I1.text.trim().isEmpty())
						methodCall.add($I1.text);
					isFun = false;
				}
				funName = "";
				isFun = false;
				if(isExtends){ 
	                          System.out.println("extends "+$I1.text); fio.FileIOMethod("extends "+$I1.text, false); Class_Descedents.add($I1.text); Class_Descedents.add(CurrentClass); Class_Ancestors.add(CurrentClass);  Class_Ancestors.add($I1.text); isExtends=false;} 
	                       else 
	                       if(isImp){
	                       	  System.out.println("implements "+$I1.text); fio.FileIOMethod("implements "+$I1.text, false); isExtends=false;
	                       	  isImp=false;
	                       } 
	                       else {
	                                
	                             //  System.out.println("Adding Associates and the isNew value is "+ isNew +"for the associates"+$I1.text);
	                     		
	                     		if(!isNew)
	                     		{
	                     		Assocation.add($I1.text);
	                     		}
	                     		else
	                     		{
	                     			isNew=false;
	                     		}
	                     		if (isAggregation && !inMethod) {
	                     			Aggregation.add($I1.text);
	                     			// Association
	                     			isAggregation = false;
	                     		}
	                       }
	         }

	         typeArguments? ('.' I2 = Identifier  typeArguments? )*
	;
// private boolean myBool
primitiveType
    :   'boolean' {	    	
    				    System.out.print("boolean ");
     
				    if(Has_Par || HasComma || Has_Modifier){
				    	fio.FileIOMethod("boolean ", false);
				    	Has_Par = false;
				    	HasComma = false;
				    	Has_Modifier = false;
				    }
				   /* else if(Data_Tag)
				    {
					fio.FileIOMethod("boolean ", true);    
				    }*/
				    else
				    {
				    fio.FileIOMethod("boolean ", true);
				    }
    		   }
    |   'char' {
    		System.out.print("char "); 
		    		if(Has_Par || HasComma || Has_Modifier)
		    		{
		    			fio.FileIOMethod("char ", false);    	
		    			Has_Par = false;
		    			HasComma = false;
		    			Has_Modifier = false;
		    		}
		    		else
		    		{
		    		        fio.FileIOMethod("char ", true);    	
		    		}
    		}
    |   'byte'
    |   'short'
    |   'int' {System.out.print("int ");
    			if(Has_Par || HasComma || Has_Modifier)
		    		{
		    		       // fio.FileIOMethod("I'm here foo Par",false);		    			
		    			fio.FileIOMethod("int ", false);intCount++;    	
		    			Has_Par = false;
		    			HasComma = false;
		    			Has_Modifier = false;
		    		}
		    		else
		    		{
		    		        fio.FileIOMethod("int", true);intCount++;    	
    				} 
    		}
    |   'long'
    |   'float'
    |   'double' {System.out.print("double ");
    			if(Has_Par || HasComma || Has_Modifier)
		    		{
					fio.FileIOMethod("double ", false);
		    			Has_Par = false;
		    			HasComma = false;
		    			Has_Modifier = false;
		    		}
		    		else
		    		{
		    		        fio.FileIOMethod("double ", true);    	
		    		}  
    		}
    ;

variableModifier
    :   'final'
    |   annotation
    ;

typeArguments
    :   '<' typeArgument (',' typeArgument)* '>'
    ;
    
typeArgument
    :   type
    |   '?' (('extends' | 'super') type)?
    ;
    
qualifiedNameList
    :   qualifiedName (',' qualifiedName)* 
    ;

formalParameters
    :   '(' formalParameterDecls? ')' 
    ;
    
formalParameterDecls
    :   variableModifiers type formalParameterDeclsRest
    ;
    
formalParameterDeclsRest
    :   variableDeclaratorId (',' {System.out.print(","); fio.FileIOMethod(" , ", false); HasComma = true;} formalParameterDecls)?
    |   '...' variableDeclaratorId 
    ;
    
methodBody
    :   block
    {
//    	System.err.println(method);
//    	System.err.println(methodCall);
       	if (!methodCall.isEmpty() || methodCall == null)
        	mc_map.put(method, methodCall);	
        	// reset
        method = "";
        funName = "";
        methodCall = new HashSet<>();
        isFun = false;
    }
    ;

constructorBody
    :   '{' explicitConstructorInvocation? blockStatement* '}' {
       	if (!methodCall.isEmpty() || methodCall == null)
        	mc_map.put(method, methodCall);	
        	// reset
        method = "";
        methodCall = new HashSet<>();
    }
    ;

explicitConstructorInvocation
    :   nonWildcardTypeArguments? ('this' | 'super') arguments ';'
    |   primary '.' nonWildcardTypeArguments? 'super' {System.err.println(method); fio.FileIOMethod(method, true); System.err.println(methodCall); fio.FileIOMethod(methodCall, true);} arguments ';'
    ;


qualifiedName
    :   Identifier ('.' Identifier)* 
    ;
    
literal 
    :   integerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   booleanLiteral
    |   'null'
    ;

integerLiteral
    :   HexLiteral
    |   OctalLiteral
    |   DecimalLiteral
    ;

booleanLiteral
    :   'true'
    |   'false'
    ;

// ANNOTATIONS

annotations
    :   annotation+
    ;

annotation
    :   '@' annotationName ( '('  ( elementValuePairs | elementValue )? ')' )?
    ;
    
annotationName
    : Identifier ('.' Identifier)*
    ;

elementValuePairs
    :   elementValuePair (',' elementValuePair)*
    ;

elementValuePair
    :   Identifier '=' elementValue
    ;
    
elementValue
    :   conditionalExpression
    |   annotation
    |   elementValueArrayInitializer
    ;
    
elementValueArrayInitializer
    :   '{' (elementValue (',' elementValue)*)? (',')? '}'
    ;
    
annotationTypeDeclaration
    :   '@' 'interface' Identifier annotationTypeBody
    ;
    
annotationTypeBody
    :   '{' (annotationTypeElementDeclaration)* '}'
    ;
    
annotationTypeElementDeclaration
    :   modifiers annotationTypeElementRest
    ;
    
annotationTypeElementRest
    :   type annotationMethodOrConstantRest ';'
    |   normalClassDeclaration ';'?
    |   normalInterfaceDeclaration ';'?
    |   enumDeclaration ';'?
    |   annotationTypeDeclaration ';'?
    ;
    
annotationMethodOrConstantRest
    :   annotationMethodRest
    |   annotationConstantRest
    ;
    
annotationMethodRest
    :   Identifier '(' ')'  defaultValue?
    ;
    
annotationConstantRest
    :   variableDeclarators
    ;
    
defaultValue
    :   'default' elementValue
    ;

// STATEMENTS / BLOCKS

block
    :   '{' blockStatement* '}'
    ;
    
blockStatement
    :   localVariableDeclarationStatement 
    |   classOrInterfaceDeclaration 
    |   statement 
    ;
    
localVariableDeclarationStatement
    :    localVariableDeclaration ';'
    ;

localVariableDeclaration
    :   variableModifiers type variableDeclarators
    ;
    
variableModifiers
    :   variableModifier*
    ;

statement
    : block
    |   ASSERT expression (':' expression)? ';'
    |   'if' parExpression statement (options {k=1;}:'else' statement)?
    |   'for' '(' forControl ')' statement
    |   'while' parExpression statement
    |   'do' statement 'while' parExpression ';'
    |   'try' block
        ( catches 'finally' block
        | catches
        |   'finally' block
        )
    |   'switch' parExpression '{' switchBlockStatementGroups '}'
    |   'synchronized' parExpression block
    |   'return' expression? ';'
    |   'throw' expression ';'
    |   'break' Identifier? ';'
    |   'continue' Identifier? ';'
    |   ';' 
    |   statementExpression ';'
    |   Identifier ':' statement
    ;
    
catches
    :   catchClause (catchClause)*
    ;
    
catchClause
    :   'catch' '(' formalParameter ')' block
    ;

formalParameter
    :   variableModifiers type variableDeclaratorId 
    ;
        
switchBlockStatementGroups
    :   (switchBlockStatementGroup)*
    ;
    
/* The change here (switchLabel -> switchLabel+) technically makes this grammar
   ambiguous; but with appropriately greedy parsing it yields the most
   appropriate AST, one in which each group, except possibly the last one, has
   labels and statements. */
switchBlockStatementGroup
    :   switchLabel+ blockStatement*
    ;
    
switchLabel
    :   'case' constantExpression ':'
    |   'case' enumConstantName ':'
    |   'default' ':'
    ;
    
forControl
options {k=3;} // be efficient for common case: for (ID ID : ID) ...
    :   enhancedForControl
    |   forInit? ';' expression? ';' forUpdate?
    ;

forInit
    :   localVariableDeclaration
    |   expressionList
    ;
    
enhancedForControl
    :   variableModifiers type Identifier ':' expression
    ;

forUpdate
    :   expressionList
    ;

// EXPRESSIONS

parExpression
    :   '(' expression ')' 
    ;
    
expressionList
    :   expression (',' expression)*
    ;

statementExpression
    :   expression
    ;
    
constantExpression
    :   expression
    ;
    
expression
    :   conditionalExpression (assignmentOperator expression)?
    ;
    
assignmentOperator
    :   '='
    |   '+='
    |   '-='
    |   '*='
    |   '/='
    |   '&='
    |   '|='
    |   '^='
    |   '%='
    |   ('<' '<' '=')=> t1='<' t2='<' t3='=' 
        { $t1.getLine() == $t2.getLine() &&
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    |   ('>' '>' '>' '=')=> t1='>' t2='>' t3='>' t4='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&
          $t3.getLine() == $t4.getLine() && 
          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() }?
    |   ('>' '>' '=')=> t1='>' t2='>' t3='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    ;

conditionalExpression
    :   conditionalOrExpression ( '?' expression ':' expression )?
    ;

conditionalOrExpression
    :   conditionalAndExpression ( '||' conditionalAndExpression )*
    ;

conditionalAndExpression
    :   inclusiveOrExpression ( '&&' inclusiveOrExpression )*
    ;

inclusiveOrExpression
    :   exclusiveOrExpression ( '|' exclusiveOrExpression )*
    ;

exclusiveOrExpression
    :   andExpression ( '^' andExpression )*
    ;

andExpression
    :   equalityExpression ( '&' equalityExpression )*
    ;

equalityExpression
    :   instanceOfExpression ( ('==' | '!=') instanceOfExpression )*
    ;

instanceOfExpression
    :   relationalExpression ('instanceof' type)?
    ;

relationalExpression
    :   shiftExpression ( relationalOp shiftExpression )*
    ;
    
relationalOp
    :   ('<' '=')=> t1='<' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   ('>' '=')=> t1='>' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   '<' 
    |   '>' 
    ;

shiftExpression
    :   additiveExpression ( shiftOp additiveExpression )*
    ;

shiftOp
    :   ('<' '<')=> t1='<' t2='<' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   ('>' '>' '>')=> t1='>' t2='>' t3='>' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    |   ('>' '>')=> t1='>' t2='>'
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    ;


additiveExpression
    :   multiplicativeExpression ( ('+' | '-') multiplicativeExpression )*
    ;

multiplicativeExpression
    :   unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
    ;
    
unaryExpression
    :   '+' unaryExpression
    |   '-' unaryExpression
    |   '++' unaryExpression
    |   '--' unaryExpression
    |   unaryExpressionNotPlusMinus
    ;

unaryExpressionNotPlusMinus
    :   '~' unaryExpression
    |   '!' unaryExpression
    |   castExpression
    |   primary selector* ('++'|'--')?
    ;

castExpression
    :  '('  primitiveType ')'  unaryExpression
    |  '(' (type | expression) ')'  unaryExpressionNotPlusMinus
    ;

primary
    :   parExpression
    |   'this' ('.' Identifier)* identifierSuffix?
    |   'super' superSuffix
    |   literal
    |   'new' {isFun = true; isNew = true;} creator {System.out.println("HERE CALLED THE NEW RULE");}
    |   I1= Identifier {
    	if (isFun) {
		if (funName != null && !funName.trim().isEmpty())
			methodCall.add(funName);
		isFun = false;
	}
	funName = $I1.text;
	} ('.' I2 = Identifier {
		funName = funName+"."+$I2.text;
		if (funName != null && !funName.trim().isEmpty())
			methodCall.add(funName);
	} )* identifierSuffix?
    |   primitiveType ('[' ']')* '.' 'class'
    |   'void' '.' 'class'
    ;

identifierSuffix
    :   ('[' ']')+ '.' 'class'
    |   ('[' expression ']')+ // can also be matched by selector, but do here
    |   arguments 
    |   '.' 'class'
    |   '.' explicitGenericInvocation
    |   '.' 'this'
    |   '.' 'super' arguments
    |   '.' 'new' innerCreator
    ;

creator
    :   nonWildcardTypeArguments createdName {isFun = true;} classCreatorRest 
    |   createdName {isFun = true;} (arrayCreatorRest | classCreatorRest ){System.out.println("Coming here here 2nd for this");}
    ;

createdName
    :   classOrInterfaceType 
    |   primitiveType
    ;
    
innerCreator
    :   nonWildcardTypeArguments? Identifier classCreatorRest
    ;

arrayCreatorRest
    :   '['
        (   ']' ('[' ']')* arrayInitializer
        |   expression ']' ('[' expression ']')* ('[' ']')*
        )
    ;

classCreatorRest
    :   arguments classBody?
    ;
    
explicitGenericInvocation
    :   nonWildcardTypeArguments Identifier arguments
    ;
    
nonWildcardTypeArguments
    :   '<' typeList '>'
    ;
    
selector
    :   '.' Identifier  arguments?
    |   '.' 'this'
    |   '.' 'super' superSuffix
    |   '.' 'new' innerCreator
    |   '[' expression ']'
    ;
    
superSuffix
    :   arguments 
    |   '.' Identifier { methodCall.add($Identifier.text); isSuperSuffix = true;}arguments?
    ;

arguments
    :   '(' expressionList? ')' {
    	isFun = true; 
    	if (isFun && !isSuperSuffix) {
    		if (funName != null && !funName.trim().isEmpty())
    			methodCall.add(funName);
    	}
    }
    ;

// LEXER

HexLiteral : '0' ('x'|'X') HexDigit+ IntegerTypeSuffix? ;

DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) IntegerTypeSuffix? ;

OctalLiteral : '0' ('0'..'7')+ IntegerTypeSuffix? ;

fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
IntegerTypeSuffix : ('l'|'L') ;

FloatingPointLiteral
    :   ('0'..'9')+ '.' ('0'..'9')* Exponent? FloatTypeSuffix?
    |   '.' ('0'..'9')+ Exponent? FloatTypeSuffix?
    |   ('0'..'9')+ Exponent FloatTypeSuffix?
    |   ('0'..'9')+ FloatTypeSuffix
    ;

fragment
Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D') ;

CharacterLiteral
    :   '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
    ;

StringLiteral
    :  '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;

fragment
EscapeSequence
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UnicodeEscape
    |   OctalEscape
    ;

fragment
OctalEscape
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

ENUM:   'enum' {if (!enumIsKeyword) $type=Identifier;}
    ;
    
ASSERT
    :   'assert' {if (!assertIsKeyword) $type=Identifier;}
    ;
    
Identifier 
    :   Letter (Letter|JavaIDDigit)*
    ;

/**I found this char range in JavaCC's grammar, but Letter and Digit overlap.
   Still works, but...
 */
fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;
