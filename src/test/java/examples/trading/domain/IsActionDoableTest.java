package examples.trading.domain;

import junit.framework.TestCase;
import org.junit.Assert;

public class IsActionDoableTest extends TestCase {

    public static final double TOLERANCE = Math.pow(10, -7);

    public void testCalculateFeature() throws Exception {
        IsActionDoable isActionDoable = new IsActionDoable();

        TradingState state = new TradingState(0.0, 1, new double[] {-1}, 1);
        TradingAction action = new TradingAction(1, 1, TradingAction.Type.BUY);
        Assert.assertEquals(-1, isActionDoable.calculateFeature(state, action), TOLERANCE);

        state = new TradingState(0.0, 1, new double[] {-1}, 0.5);
        Assert.assertEquals(-1, isActionDoable.calculateFeature(state, action), TOLERANCE);

        state = new TradingState(0.0, 1, new double[] {-1}, 0.00001);
        Assert.assertEquals(-1, isActionDoable.calculateFeature(state, action), TOLERANCE);






    }
}