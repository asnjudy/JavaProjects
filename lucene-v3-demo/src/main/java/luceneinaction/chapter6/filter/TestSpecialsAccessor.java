package luceneinaction.chapter6.filter;

/**
 * Created by asnju on 2017/1/11.
 */
public class TestSpecialsAccessor implements SpecialsAccessor {


    private String[] isbns;

    public TestSpecialsAccessor(String[] isbns) {
        this.isbns = isbns;
    }

    @Override
    public String[] isbns() {
        return isbns;
    }
}
