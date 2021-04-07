package OO_PIES1;

/**
 * Summary description for Class2.
 */
public class Class2 extends Class3
{
    private int myInt2;
    public int AddMine2()
    {
        return myInt2 + super.getmyInt();
    }
    public Class2()
    {
        super(true);
        myInt2 = 5;
    }
}
