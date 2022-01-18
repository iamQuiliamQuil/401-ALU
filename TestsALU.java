import java.util.Random;

class TestALU{
    //for readability, to be used in alu.operate() calls
    public static final int AND = 0;
    public static final int OR = 1;
    public static final int XOR = 2;
    public static final int ADD = 3;
    public static final int SUB = 4;
    public static final int SLL = 5;
    public static final int SRL = 6;
    public static final int SRA = 7;

    //for use in manyAdditionTest and manySubtractionTest
    //helps generate numbers of more varying magnitude for tests
    private static final int[] magnitudes = new int[]{(int)Math.pow(2, 4),(int)Math.pow(2, 8),(int)Math.pow(2, 16),(int)(long)Math.pow(2, 31)};



    static Boolean additionTest(LongWord op1, LongWord op2, ALU alu){
        LongWord result = alu.operate(ADD, op1, op2);
        return (result.getSigned() == (op1.getSigned() + op2.getSigned()));
        //true if rippleAdd gives the same answer as java addition
    }


    static Boolean subtractionTest(LongWord op1, LongWord op2, ALU alu){
        LongWord result = alu.operate(SUB, op1, op2);
        return (result.getSigned() == (op1.getSigned() - op2.getSigned()));
         //true if rippleAdd gives the same answer as java subtraction
    }

    //test hundreds of different addition equations, false if any of them fail
    static Boolean manyAdditionTest(LongWord op1, LongWord op2, ALU alu){
        Random random = new Random();
        int temp1 = 0;
        int temp2 = 0;

        //magnitudes defined at top of file
        for(int mag : magnitudes){
            for (int i = 0; i < 50; i++){
                temp1 = random.nextInt(mag-1);
                temp2 = random.nextInt(mag-1);
                temp1 = random.nextBoolean() ? temp1 : temp1*-1; // on coinflips, decide if
                temp2 = random.nextBoolean() ? temp2 : temp2*-1; // temp1 and/or temp2 should be negative

                op1.set(temp1);
                op2.set(temp2);

                if (!additionTest(op1, op2, alu)){
                    System.out.println(temp1+"+"+temp2+"!="+(temp1+temp2));
                    return false;
                }
            }
        }
        return true; // if all tests have passed
    }

    //test hundreds of different subtraction equations, false if any of them fail
    static Boolean manySubtractionTest(LongWord op1, LongWord op2, ALU alu){
        Random random = new Random();
        int temp1 = 0;
        int temp2 = 0;

        //magnitudes defined at top of file 
        for(int mag : magnitudes){
            for (int i = 0; i < 50; i++){
                temp1 = random.nextInt(mag-1);
                temp2 = random.nextInt(mag-1);
                temp1 = random.nextBoolean() ? temp1 : temp1*-1; // on coinflips, decide if
                temp2 = random.nextBoolean() ? temp2 : temp2*-1; // temp1 and/or temp2 should be negative

                op1.set(temp1);
                op2.set(temp2);

                if (!subtractionTest(op1, op2, alu)){
                    System.out.println(temp1+"-"+temp2+"!="+(temp1+temp2));
                    return false;
                }
            }
        }
        return true; // if all tests have passed
    }


    //exists to compress code later on 
    static void setAndDisplayOps(LongWord op1, LongWord op2, int op1Val, int op2Val){
        op1.set(op1Val);
        op2.set(op2Val);
        System.out.println("op1: "+op1+"\t="+op1.getSigned()+"\nop2: "+op2+"\t="+op2.getSigned());
    }

    //exists for clarity
    static void displayALUFlags(ALU alu){
        System.out.println("ZF: "+alu.getZF()+" -NF: "+alu.getNF()+" -CF: "+alu.getCF()+" -OF: "+alu.getOF());
    }

    //one method to call to run and display any unit test, regardless of ALU operation and with custom input
    static void genericUnitTest(int code, LongWord op1, LongWord op2, ALU alu, int op1Val, int op2Val){
        setAndDisplayOps(op1, op2, op1Val, op2Val);

        LongWord result = alu.operate(code, op1, op2);

        System.out.println("res: "+result+"\t="+result.getSigned());
        displayALUFlags(alu);
    } 
    
    static void andTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming tests of AND: \n");
        System.out.println("Showing basic AND works, and showing negative flag works:");
        genericUnitTest(AND, op1, op2, alu, -2147483647, -1); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(AND, op1, op2, alu, 64, 6); 
    }

    static void orTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming tests of OR: \n");
        System.out.println("Showing basic OR works, and showing negative flag works:");
        genericUnitTest(OR, op1, op2, alu, -2147483647, 68); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(OR, op1, op2, alu, 0, 0); 
    }

    static void xorTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming tests of XOR: \n");
        System.out.println("Showing basic XOR works, and showing negative flag works:");
        genericUnitTest(XOR, op1, op2, alu, -2147483647, 69); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(XOR, op1, op2, alu, -1, -1); 
    }

    static void addTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming visual tests of ADD: \n");
        System.out.println("Showing basic ADD works, and showing negative flag works, Showing CF and OF work:");
        genericUnitTest(ADD, op1, op2, alu, -2147483647, 69); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(ADD, op1, op2, alu, 0, 0); 
        System.out.println("\nPreforming non-visual tests of ADD, adding hunderds of randomly generated numbers and verifying the results:");
        System.out.println("Result: "+(manyAdditionTest(op1, op2, alu)?"Sucess":"Failure"));
    }

    static void subTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming visual tests of SUB: \n");
        System.out.println("Showing basic SUB works, Showing CF and OF work:");
        genericUnitTest(SUB, op1, op2, alu, -2147483647, 69); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(SUB, op1, op2, alu, 0, 0); 
        System.out.println("Showing negative flag works");
        genericUnitTest(SUB, op1, op2, alu, 3, 4);
        System.out.println("\nPreforming non-visual tests of SUB, subtracting hunderds of randomly generated numbers and verifying the results:");
        System.out.println("Result: "+(manySubtractionTest(op1, op2, alu)?"Sucess":"Failure"));
    }

    static void sllTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming tests of SLL: \n");
        System.out.println("Showing basic SLL works, and showing OF works:");
        genericUnitTest(SLL, op1, op2, alu, -2147483647, 1); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(SLL, op1, op2, alu, -1, 32); 
    }

    static void srlTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming tests of SRL: \n");
        System.out.println("Showing basic SRL works:");
        genericUnitTest(SRL, op1, op2, alu, -2147483647, 1); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(SRL, op1, op2, alu, -1, 32); 
    }

    static void sraTests(LongWord op1, LongWord op2, ALU alu){
        System.out.println("\nPreforming tests of SRA: \n");
        System.out.println("Showing basic SRA works, negative flag works:");
        genericUnitTest(SRA, op1, op2, alu, -2147483647, 8); 
        System.out.println("Showing zero flag works:");
        genericUnitTest(SRA, op1, op2, alu, 458285, 32); 
    }

    static void runTests(){
        ALU alu = new ALU();
        LongWord op1 = new LongWord();
        LongWord op2 = new LongWord();

        System.out.println("For the Following Tests, op1 and op2 are the arguments of the method ALU.operate(),");
        System.out.println("and res is the return value. ");
        andTests(op1, op2, alu);
        orTests(op1, op2, alu);
        xorTests(op1, op2, alu);
        addTests(op1, op2, alu);
        subTests(op1, op2, alu);
        sllTests(op1, op2, alu);
        srlTests(op1, op2, alu);
        sraTests(op1, op2, alu);
    }

    public static void main(String[] args) {
        runTests();
    }
}