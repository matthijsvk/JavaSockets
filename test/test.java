package test;

public class test {

	public static void main(String[] args) {
		String test = "http://test.com/test/test";
		for (int i=0; i<10; i++)
			System.out.println(nthOccurrence(test, "/", i));

	}
	
	public static int nthOccurrence(String str, String c, int n) {
		    int pos = str.indexOf(c, 0);
		    while (n-- > 0 && pos != -1)
		        pos = str.indexOf(c, pos+1);
		    return pos;
		}

}
