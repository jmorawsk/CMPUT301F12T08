package tasktrackertests.trivial;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class TrivialTests {

	@Test
	public void comma_at_end() {
		testCommaDelimiter("hello, world,", new String[] { "hello", "world" });
	}

	@Test
	public void white_space() {
		testCommaDelimiter("hello    \t\n,\t world", new String[] { "hello",
				"world" });
	}

	@Test
	public void commas_only() {
		testCommaDelimiter(",,,", new String[0]);
	}

	private void testCommaDelimiter(String in, String[] expected) {
		String[] actual = in.split("(\\s+)?,(\\s+)?");
		assertEquals(expected.length, actual.length);

		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
	

	@Test
	public void test_replaceAll() {
		String string = "That's a wrap!";
		assertEquals("That''s a wrap!", string.replaceAll("'", "''"));
		
		string = "\n\tyay".replace("\\", "\\\\");
		System.out.println(string);
		System.out.println(string.replace("\\", "\\\\"));
		System.out.println(string.replaceAll("\\\\", "\\\\\\\\"));
		

	}
	
	@Test
	public void test_timeInMillis() {
		long time = Calendar.getInstance().getTimeInMillis();

		System.out.println(time);
		System.out.println((int) time);
		
		String hex = Long.toHexString(time);
		Date date = new Date(Long.decode("0x" + hex));

		String dateString = new SimpleDateFormat("MMM dd, yyyy | HH:mm")
				.format(date);
		System.out.println(dateString);

	}

}
