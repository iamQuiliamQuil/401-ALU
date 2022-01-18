import java.util.BitSet;
import java.lang.Math;

class LongWord{
    private BitSet bitset;
    private int bitSetSize;

    public int getBitSetSize(){
        return bitSetSize;
    }

    //returns trues iff every bit in bitset is false
    public Boolean isZero(){
        for (int i = 0; i < 32;  i ++){
            if(getBit(i)==true){
                return false;
            }
        }
        return true;
    }

    //returns a LongWord that is the bit-per-bit opposite of this one
    public LongWord not(){
        LongWord not = new LongWord();
        for (int i = 0; i < bitSetSize; i++){
            if (getBit(i)!=true){
                not.setBit(i);
            }
        }
        return not;
    }

    //takes another LongWord as argument, returns a LongWord that is a bitwise and of it and this 
    public LongWord and(LongWord other){
        LongWord and = new LongWord();
        for (int i = 0; i < bitSetSize; i++){
            if (getBit(i)==true && other.getBit(i)==true){
                and.setBit(i);
            }
        }
        return and;
    }

    //takes another LongWord as argument, returns a LongWord that is a bitwise or of it and this 
    public LongWord or(LongWord other){
        LongWord or = new LongWord();
        for (int i = 0; i < bitSetSize; i++){
            if (getBit(i)==true || other.getBit(i)==true){
                or.setBit(i);
            }
        }
        return or;
    }
    //takes another LongWord as argument, returns a LongWord that is a bitwise xor of it and this 
    public LongWord xor(LongWord other){
        LongWord xor = new LongWord();
        for (int i = 0; i < bitSetSize; i++){
            if (getBit(i) != other.getBit(i)){
                xor.setBit(i);
            }
        }
        return xor;
    }

    //Shifts all bits to the right "amount" times, filling in with ones on the left if this is negative
    public LongWord shiftRightArithmetic (int amount){
        if (amount < 0){
            throw new ArgumentIsBadException("Cannot shift in the negative direction");
        }
        LongWord other = shiftRightLogical(amount);
        //checking to see if value is negative and recquires leading 1's
        if (getBit(bitSetSize-1)==true){
            for (int i = 0; i < amount; i ++){
                other.setBit(bitSetSize-1-i);
            }
        }
        return other;
    }

    //shifts all bits to right "amount" times 
    public LongWord shiftRightLogical(int amount){
        if (amount < 0){
            throw new ArgumentIsBadException("Cannot shift in the negative direction");
        }
        LongWord other = new LongWord();
        for (int i = 0; i < bitSetSize; i++){
            if(i - amount > -1 && getBit(i)==true){
                other.setBit(i-amount);
            }
        }
        return other;
    }

    //shifts all bits to left "amount" times 
    public LongWord shiftLeftLogical(int amount){
        if (amount < 0){
            throw new ArgumentIsBadException("Cannot shift in the negative direction");
        }
        LongWord other = new LongWord();
        for (int i =0; i < bitSetSize - amount; i++){
            if(getBit(i)==true){
                other.setBit(i+amount);
            }
        }
        return other;
    }

    //takes another LongWord as argument, copies its contents into this
    public void copy (LongWord other){
        for (int i = 0; i < bitSetSize; i++){
            if(other.getBit(i)!=getBit(i)){
                toggleBit(i);
            }
        }
    }

    //method to help converting numbers to negative in set
    //adds 1 or 0 to a bit within bitset, resolves addition with remainders  
    private void addToBit(int index,char toAdd){
        if (toAdd == '1'){
            if (getBit(index)==true){
                bitset.clear(index);
                if(index < bitSetSize -1){
                    addToBit(index+1, '1');
                }
            } else {
                bitset.set(index);
            }
        } 
    }

    //takes int as argument, turns bitset into bit-per-bit equivalent
    public void set(int value){
        //validating input is pointless becasue any integer value is valid
        Boolean valueIsNegative = false;
        if (value < 0){
            //this will allow us to use the same arithmetic on positive and negative numbers
            valueIsNegative = true;
            value *= -1;
        }
        for(int i = bitSetSize-1; i > -1; i--){
            clearBit(i);
            if(value - Math.pow(2, i)>=0){
                setBit(i);
                value -= Math.pow(2, i);
            }
        }
        //algorithm for creating the two's compliment of a number: 
        //take the positive version, flip the bits, add 1.
        if(valueIsNegative){
            for(int i = 0; i < bitSetSize; i++){
                bitset.flip(i);
            }
            addToBit(0, '1');
        }  
    }
    
    //returns what the bitset would respresent if it were an int
    public int getSigned(){
        //Java's long to int cast creates a perfect two's compliment of any negative number,
        //and leaves positive numbers alone
        return (int) getUnsigned();
    }

    //returns what the bitset would respresent if it were a long
    public long getUnsigned(){
        long result = 0;
        for (int i = 0; i < bitSetSize; i++){
            if(getBit(i)==true){
                result += Math.pow(2, i);
            }
        }
        return result;
    }

    //returns bit at position
    public Boolean getBit(int i){
        if (i < 0 || i > bitSetSize -1){
            throw new ArgumentIsBadException("Index out of bounds");
        }
        
        return bitset.get(i);
    }

    //sets bit at position to 1
    public void setBit(int i){
        if (i < 0 || i > bitSetSize -1){
            throw new ArgumentIsBadException("Index out of bounds");
        }
        bitset.set(i);
    }

    //sets bit at position to 0
    public void clearBit (int i){
        if (i < 0 || i > bitSetSize -1){
            throw new ArgumentIsBadException("Index out of bounds");
        }
        bitset.clear(i);
    }

    //changes bit at position to be the oposite of what it was. 
    public void toggleBit (int i){
        if (i < 0 || i > bitSetSize -1){
            throw new ArgumentIsBadException("Index out of bounds");
        }
        bitset.flip(i);
    }



    //This object will codify numbers in a little-endian manner, but sometimes it
    //will be much easier to conceptualize these problems in a big-endian way.
    //this functions will allow us to do this
    private int littleEndianAlias(int bigEndian){
        return bitSetSize - bigEndian -1;
    }

    //assumes big endian representation
    //converts a string of 1's and 0's into an integer 
    private int binaryToInt(String binaryString){
        char[] binaryArray = binaryString.toCharArray();
        int result = 0;
        for (int i = 0; i < binaryArray.length; i++){
            //validating input 
            if (binaryArray[i] != '1' && binaryArray[i] != '0'){
                throw new ArgumentIsBadException("Binary string contained invalid characters");
            }
            if (binaryArray[i] == '1'){
                result += Math.pow(2, binaryArray.length-i-1);
            }
        }
        return result;
    }
    
    //takes a number between 0 an 15, returns the char associated with its hex representation
    private char intToHex(int num){
        if (num < 0 || num > 15){
            throw new ArgumentIsBadException("A number outside of the range between 0 and 15 was found");
        }
        if(num < 10){
            //converting int to ASCII value of equivalent char
            return (char)(num+48); 
        } else { // if num > 9
            return (char)(num+87); 
        }
    }

    //creates a string representing the bitset, formatted in a big-endian manner, followed by a hex representation
    @Override
    public String toString(){
        //We use an array of strings to partition bits into groups of 4, 
        //making it easier to convert them to hex later 
        String[] substrings = new String[bitSetSize/4];
        char temp = 'a';

        for(int i = 0; i < bitSetSize; i++){
            if (getBit(littleEndianAlias(i))==true){
                temp='1';
            } else {
                temp='0';
            }
            //if i is the beginning of a new substring, make the substring i
            //otherwise, append it to the substring.
            if (i%4 == 0){
                substrings[littleEndianAlias(i)/4] = ""+temp;
            } else {
                substrings[littleEndianAlias(i)/4] += temp;
            }
            //for debugging 
            //System.out.println(littleEndianAlias(i)+" "+temp+" "+substrings[littleEndianAlias(i)/4]);
        }

        String result = ""; 
        String hexRepresentation = "";
        
        for (int i = 0; i < substrings.length; i++){
            result += substrings[substrings.length-i-1]+" "; //not confident this is right
            hexRepresentation += intToHex(binaryToInt(substrings[substrings.length-i-1]));
        }
        result += "\t0x"+hexRepresentation;

        return result;
    }
    
    //constructor
    //generates a 32 bit bitset, sets all bits to 0
    LongWord(){
        bitSetSize = 32;
        bitset = new BitSet(bitSetSize);
        //setting all bits to 0
        for (int i = 0; i < bitSetSize; i++){
            bitset.set(i,false);
        }
    }
}