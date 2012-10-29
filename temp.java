package dbmanager;

import static org.junit.Assert.*;

import org.junit.Test;


public class temp
{

    @Test
    public void testReset()
    {
        dbmanager.nukeAll();
        String emptyList = dbmanager.listTasks();
        assertTrue(emptyList.equals("[]"));
    }

}
