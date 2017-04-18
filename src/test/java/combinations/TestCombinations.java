package combinations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class TestCombinations {
   @Test
   public void threeFromFive() {
     Set<Integer> in = new HashSet<>(Arrays.asList(1,2,3,4,5));
     Assert.assertEquals("count should be 10", 10, Combinations.kFrom(3, in).count());
   }

   @Test
   public void fiveFromNine() {
     Set<Integer> in = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
     Assert.assertEquals("count should be 126", 126, Combinations.kFrom(5, in).count());
   }
}
