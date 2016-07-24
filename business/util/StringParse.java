package business.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import business.exceptions.ParseException;



public class StringParse {
	public final static Integer ZERO = new Integer(0);
	public final static Integer ONE = new Integer(1);
	
	public static final void swap(List<Object> l, int pos1, int pos2) {
		if(pos1 < 0 || pos2 < 0 || pos1 >= l.size() || pos2 >= l.size()){
			throw new IllegalArgumentException(
			"Attempt to swap values in list using a position that is outside of list range.");
		}
		Object string1 = l.get(pos1);
		Object string2 = l.get(pos2);
		l.remove(pos1);
		l.add(pos1, string2);
		l.remove(pos2);
		l.add(pos2, string1);
		
	}
	
	/** 
	 * Returns a String of spaces. The number of spaces
	 * is determined by the <code>int</code>
	 * argument <code>numSp</code>.
	 */
	public static String sp(int numSp) {
		StringBuffer sb = new StringBuffer();
		if(numSp <=0) return "";
		for(int i = 0; i < numSp; ++i) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	
	/**
	 *  Returns true if the argument 
	 * <code>targetStr</code> contains
	 * the argument <code>testStr</code>, false otherwise. Case ignored if 
	 * <code>ignoreCase</code> argument has value <code>true</code>.
	 *  If this is a case-insensitive search, it is more efficient to use
	 *  the String method <code>indexOf</code>.<br>
	 *  Handling nulls: If both strings are null, the method returns true. If just
	 *  one of the strings is null, the method returns false. 
	 */
	public static boolean stringContains(String targetStr,String testStr,boolean ignoreCase) {
		
		int i; //Used in iteration.
		if(targetStr == null && testStr == null) {
			return true;
		}
		//now that the 'both null' case is taken care of, we can do this:
		if(targetStr == null || testStr == null) {
			return false;
		}
		//now we know that both strings are not null
		int targetLen = targetStr.length();
		int testLen = testStr.length();
		for (i = 0; i <= (targetLen - testLen); i++)
			if (targetStr.regionMatches(ignoreCase, i, testStr, 0, testLen)) {
				return true;
			}
		return false;
	}
	/**
	 * The method checks to see if <code>testStr</code> is one of the elements of the 
	 * array argument <code>arr</code>.
	 * If <code>arr</code> is null or of length 0, the return value is false.
	 * If <code>testStr</code> is null, the return value is true only if one of the elements of 
	 * <code>arr</code>
	 * is null.
	 */
	public static boolean stringArrayContains(String[] arr, String testStr) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		
		for (int i = 0; i < arr.length; ++i) {
			//test whether testStr is null
			if(testStr == null) {
				if(arr[i]==null) return true;
			}
			else if (testStr.equals(arr[i])) {
				return true;
			}
		}
		return false;
	}
	/**
	 *  This method tests to see if the String argument <code>s</code> 
	 *  is a String-encoded long.
	 *  If s is null, the method returns false.
	 * @param <code>s</code> String. The String to test
	 * @return true if <code>s</code> is a String-encoded long; false otherwise
	 */
	public static boolean isNonnegLong(String s) {
		if(s == null) {
			return false;
		}
		try {
			long val = (new BigInteger(s)).longValue();
			if (val < 0)
				return false;
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	/**
	 * Same as replace method, except just substitutes <code>newValue</code> for first occurrence
	 * of <code>oldValue</code>.
	 * 
	 * Note: This is the same as the jdk library function replaceFirst(oldValue, newValue).
	 */
	public static String replaceFirst(String newValue, String oldValue, String s) {
		
		if (s == null || newValue ==null || oldValue == null || oldValue.equals("")) {
			return s;
		}
		String t = s;
		if (t.indexOf(oldValue) == -1) {
			return t;
		}
		int startIndex = t.indexOf(oldValue);
		int nextIndex = startIndex + oldValue.length();
		String firstPart = t.substring(0, startIndex);
		String middlePart = newValue;
		String lastPart = t.substring(nextIndex);
		t = firstPart + middlePart + lastPart;
		
		return t;		
	}
	
	/**
	* This method replaces <code>oldValue</code> with <code>newValue</code> everywhere <code>oldValue</code> is
	* found in String s. If any of the parameters is null, the original String s is returned unmodified.
	* If <code>oldValue</code> is "", the original String is returned unmodified.
	* @param newValue String. The new substring to insert
	* @param oldValue String. The substring to be replaced
	* @param s String. The String in which the replacement is to occur.
	* @return A String in which all occurrences of the substring <code>oldValue</code>
	* have been replaced with <code>newValue</code>
	*/
	public static String replace(String newValue, String oldValue, String s) {
		if (s == null || newValue ==null || oldValue == null || oldValue.equals("")) {
			return s;
		}
		String t = s;
		boolean someLeft = true;
		String firstPart = "";
		String middlePart = "";
		String lastPart = t;
		while (someLeft) {
			if (t.indexOf(oldValue) == -1) {
				someLeft = false;
				return t;
			}
			int startIndex = t.indexOf(oldValue);
			int nextIndex = startIndex + oldValue.length();
			firstPart = t.substring(0, startIndex);
			middlePart = newValue;
			lastPart = t.substring(nextIndex);
			t = firstPart + middlePart + lastPart;
		}
		return t;
	}

	/**
	* This method replaces whatever character is at position <code>index</code> 
	* in String <code>s</code>
	* with the new char <code>c</code>
	*/
	public static String replace(char c, int index, String s) {
		if(s==null || index < 0 || index >= s.length()) return s;
		char [] chars = s.toCharArray();
		chars[index] = c;
		return String.valueOf(chars);
	}	

    /**
     *  Removes all occurrences of the specified character <code>c</code> from the 
     *  String argument <code>s</code>.
     */
    public static String removeCharacter(char c, String s) {
        if(s==null) return s;
        StringBuffer ret = new StringBuffer();
        
        for(int i = 0; i < s.length(); ++i) {
            if(s.charAt(i) != c) {
                ret.append(s.charAt(i));
            }
        }
        return ret.toString();
    }
    
    /**
     * Removes all leading and trailing occurrences of the
     * specified character <code>c</code> in the String argument <code>s</code>.
     */
    public static String removeLeadingTrailingChar(char c, String s) {
    	if(s==null) return s;
    	boolean hasLeading = true;
    	boolean hasTrailing=true;
    	String retVal = s;
    	while(hasLeading || hasTrailing) {
	    	if(hasLeadingChar(c,retVal)) {
	    		retVal = retVal.substring(1);
	    	}
	    	else {
	    		hasLeading = false;
	    	}
	    	if(hasTrailingChar(c,retVal)) {
	    		retVal = retVal.substring(0,retVal.length()-1);
	    	}
	    	else {
	    		hasTrailing = false;
	    	}
	    	
    	}
    	return retVal;
    }
    /**
     * Returns true if the char argument <code>c</code> is the first character
     * in the String argument <code>s</code>
     */
    public static boolean hasLeadingChar(char c,String s) {
    	if(s == null || s.length()==0) {
    		return false;
    	}
    	
    	return (s.charAt(0) == c);
    }
     /**
     * Returns true if the char argument <code>c</code> is the last character
     * in the String argument <code>s</code>
     */   
    public static boolean hasTrailingChar(char c,String s) {
    	if(s == null || s.length()==0) {
    		return false;
    	}
    	return (s.charAt(s.length()-1) == c);
    }
 
    /**
     * Returns true if the String argument <code>s</code> 
     * is null or equal to ""
     */
    public static boolean isEmptyString(String s) {
        return (s==null || s.equals(""));
    }
    /**
     * Returns true if the Object argument has runtime type String
     * and is null or empty, false otherwise.
     */
    public static boolean isEmptyString(Object o) {
    	if (o instanceof java.lang.String) {
    		return isEmptyString((String)o);
    	}
    	return false;
    	
    }
    /**
     * Returns true if argument <code>s</code> is null or if, after trimming
     * whitespace, <code>s</code> equals ""
     */
    public static boolean isEmptyStringAfterTrim(String s) {
    	if(s == null) {
    		return true;
    	}
    	return isEmptyString(s.trim());	
    }
    /**
     * Returns the class name without the package name
     * of the fully qualified Java class stored in the argument
     * <code>aClass</code>. For instance, passing in 
     * <code>com.mutualofomaha.esv.RulesManager</code>
     * the method will return <code>RulesManager</code>. 
     * If no package prefix is
     * included as part of the <code>aClass</code> argument, 
     * <code>aClass</code> is returned unmodified.
     * 
     * If the Class param is a local or anonymous class, the method fails.
     */
    public static String getClassNameNoPackage(Class<?> aClass) {
    	
        String fullClassName = aClass.getName();
        int index = fullClassName.lastIndexOf('.');
        String className = null;
        
        //in this case, there is no package name
        if(index==-1) {
        	return fullClassName;
        }
        else {
            className = fullClassName.substring(index+1);
            return className;
        }    
    }
	/**
	 * This method retuns the number of times the argument <code>ch</code> 
	 * occurs
	 * in the String argument <code>testString</code>
	 */
    public static int numOccurrences(String testString, char ch) {
    	if(testString==null) return 0;
		int numOccurrences = 0;
		char [] chars = testString.toCharArray();
		int len = chars.length;
		for(int i = 0; i < len; ++i) {
			if(chars[i] == ch) {
				++numOccurrences;
			}
			
		}
		return numOccurrences;    	
    }
    
    /**
     * Returns the ASCII value of the char argument <code>c</code>
     */
    public static int ascii(char c) {
    	return (int)c;
    }
    
    /**
     * Returns the first index of the String argument <code>testStr</code> 
     * found in the
     * String[] argument <code>strArray</code>; returns -1 if not found.
     */
    public static int indexOf(String testStr, String [] strArray) {
    	if(strArray==null || strArray.length==0) return -1;
    	int len = strArray.length;
    	for(int i =0; i < len; ++i) {
    		if(strArray[i]==null) {
    			if(testStr == null) return i;
    		}
    		else {
    			if(strArray[i].equals(testStr)) return i;
    		}
    	}
    	return -1;
    } 
    
    /**
     * Returns "" if the argument <code>s</code> is null. 
     * Otherwise, returns argument
     * <code>s</code> unmodified.
     */
    public static String convertNullToBlank(String s) {
    	if(s==null) return "";
    	return s;
    }
    public static String multiplyDoubles(String val1, String val2){
        Double d1 = new Double(val1);
        Double d2 = new Double(val2);
        double prod = d1.doubleValue() * d2.doubleValue();
        return (new Double(prod)).toString();
    }    
    public static String addDoubles(String val1, String val2){
        Double d1 = new Double(val1);
        Double d2 = new Double(val2);
        double sum = d1.doubleValue() + d2.doubleValue();
        return (new Double(sum)).toString();
    }   
    public static String divideDoubles(String val1, String val2){
        Double d1 = new Double(val1);
        Double d2 = new Double(val2);
        double quot = d1.doubleValue() / d2.doubleValue();
        return (new Double(quot)).toString();
    } 
    
    public static String extractTextFromFile(String fileLocation) throws ParseException {
        
            File f = new File(fileLocation);
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = null;
                StringBuffer buf = new StringBuffer();
                while((line=reader.readLine()) != null){
                    buf.append(line+" ");
                }
                reader.close();
                return buf.toString();
            }

            catch(IOException e){
                throw new ParseException("Unable to read text file -- unable to complete transaction.");
            }
               
    }
	public static List<String[]> convertToStringArrays(List<String> list) {
		if(list == null) return null;
		List<String[]> retVal = new LinkedList<String[]>();
		String[] nextArray = null;
		for(String s : list){
			nextArray = new String[]{s};
			retVal.add(nextArray);
		}
		return retVal;
	}
	public static String makeString(int integ ){
	    return (new Integer(integ)).toString();
	}
	public static String makeString(double dbl){
	    return (new Double(dbl)).toString();
	}	
		
	
	public static void main(String[] args) {
		String s = "helloumlloum";
		System.out.println(StringParse.replaceFirst("", "llo", s));
		
	}
        
}
