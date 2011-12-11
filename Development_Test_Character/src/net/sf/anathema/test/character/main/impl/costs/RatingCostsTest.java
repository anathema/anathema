package net.sf.anathema.test.character.main.impl.costs;

import static org.junit.Assert.assertEquals;
import net.sf.anathema.character.generic.impl.template.points.MultiplyRatingCosts;

import org.junit.Test;

public class RatingCostsTest {

	@Test(expected=UnsupportedOperationException.class)
  public void testMulitplyRatingCostsWithoutInitalValue() throws Exception {
    final MultiplyRatingCosts costs = new MultiplyRatingCosts(4);
        costs.getRatingCosts(0);
  }
	@Test
  public void testMulitplyRatingCostsWithInitalValue() throws Exception {
    MultiplyRatingCosts costs = new MultiplyRatingCosts(4, 3);
    assertEquals(3, costs.getRatingCosts(0));
    assertEquals(4, costs.getRatingCosts(1));
    assertEquals(8, costs.getRatingCosts(2));
    assertEquals(12, costs.getRatingCosts(3));
    assertEquals(16, costs.getRatingCosts(4));
  }
}
