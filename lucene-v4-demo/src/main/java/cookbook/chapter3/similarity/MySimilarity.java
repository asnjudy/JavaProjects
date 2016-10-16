package cookbook.chapter3.similarity;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

/**
 * Created by asnju on 2016/10/15.
 */
public class MySimilarity extends Similarity {

    private Similarity sim = null;

    public MySimilarity(Similarity sim) {
        this.sim = sim;
    }

    @Override
    public long computeNorm(FieldInvertState state) {
        return sim.computeNorm(state);
    }

    @Override
    public SimWeight computeWeight(float queryBoost, CollectionStatistics collectionStats, TermStatistics... termStats) {

        return sim.computeWeight(queryBoost, collectionStats, termStats);
    }

    @Override
    public SimScorer simScorer(SimWeight weight, AtomicReaderContext context) throws IOException {

        final Similarity.SimScorer scorer = sim.simScorer(weight, context);
        final NumericDocValues values = context.reader().getNumericDocValues("ranking");


        return new SimScorer() {
            @Override
            public float score(int doc, float freq) {
                return values.get(doc) * scorer.score(doc, freq);
            }

            @Override
            public float computeSlopFactor(int distance) {
                return scorer.computeSlopFactor(distance);
            }

            @Override
            public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
                return scorer.computePayloadFactor(doc, start, end, payload);
            }
        };
    }
}
