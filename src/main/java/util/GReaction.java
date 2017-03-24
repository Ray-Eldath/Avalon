package util;

/**
 * Created by Eldath Ray on 2017/3/20.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GReaction {
    private final GItem A;
    private final GItem B;
    private final GItem product;
    private final boolean reversible;

    public GReaction(GItem a, GItem b, GItem product, boolean reversible) {
        A = a;
        B = b;
        this.product = product;
        this.reversible = reversible;
    }

    public GReaction(GItem a, GItem b, GItem product) {
        this(a, b, product, false);
    }

    public GItem getA() {
        return A;
    }

    public GItem getB() {
        return B;
    }

    public GItem getProduct() {
        return product;
    }

    public boolean isReversible() {
        return reversible;
    }
}
