package OO_PIES;

/**
 * Summary description for Class1.
 */
public class Class1 extends Class2
{
    private boolean myBool;
    public int myInt;
    protected double myDouble;
    
    public Class1(boolean mb)
    {
        myBool = mb;
        myInt = 0;
        myDouble = 0.0;
    }
    public void myFunc1(int A, int B){
        myInt = A + B + getmyInt();
    }
    public int getmyInt() {
        return myInt;
    }
    public double getmyDouble()
    {
        return myDouble;
    }
    public boolean getmyBool(){
        return myBool;
    }
}
