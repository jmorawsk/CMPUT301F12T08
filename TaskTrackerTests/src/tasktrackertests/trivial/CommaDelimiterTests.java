package tasktrackertests.trivial;

import org.junit.*;
import static org.junit.Assert.*;

public class CommaDelimiterTests {
	
	@Test
	public void comma_at_end(){	
		testCommaDelimiter("hello, world,", new String[]{"hello", "world"});
	}

	@Test
	public void white_space(){
		testCommaDelimiter("hello    \t\n,\t world", new String[]{"hello", "world"});
	}

	@Test
	public void commas_only(){
		testCommaDelimiter(",,,", new String[0]);
	}
	
	private void testCommaDelimiter(String in, String[] expected){
		String[] actual = in.split("(\\s+)?,(\\s+)?");
		assertEquals(expected.length, actual.length);
		
		for (int i = 0; i < expected.length; i++){
			assertEquals(expected[i], actual[i]);
		}
	}
	
}
