import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SalesItemTest
{
    public SalesItemTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
    }

    @AfterEach
    public void tearDown()
    {
    }


    @Test
    public void testAddComment()
    {
        SalesItem salesIte1 = new SalesItem("A", 1000);
        assertEquals(true, salesIte1.addComment("Andie", "Halo", 2));
    }
    
    @Test
    public void testIlegalRating()
    {
        SalesItem salesIte1 = new SalesItem("A", 1000);
        assertEquals(false, salesIte1.addComment("Andie", "Halo", 0));
    }

    @Test
    public void testDupe()
    {
        SalesItem salesIte1 = new SalesItem("A", 1000);
        assertEquals(true, salesIte1.addComment("Andie", "Halo", 4));
        assertEquals(false, salesIte1.addComment("Andie", "Hai", 2));
    }
}

