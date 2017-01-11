package luceneinaction.chapter5.termvector;

import luceneinaction.common.TestUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by asnju on 2017/1/11.
 */
public class GetCategory {

    private Map categoryMap = new TreeMap();



    private void buildCategoryVectors() throws IOException {
        IndexReader reader = IndexReader.open(TestUtil.getBookIndexDirectory());
        int maxDoc = reader.maxDoc();

        for (int i = 0; i < maxDoc; i++) {
            if (!reader.isDeleted(i)) {
                Document doc = reader.document(i);
                System.out.println(doc);
                String category = doc.get("category");

                Map vectorMap = (Map) categoryMap.get(category);

                if (vectorMap == null) {
                    vectorMap = new TreeMap();
                    categoryMap.put(category, vectorMap);
                }

                TermFreqVector termFreqVector = reader.getTermFreqVector(i, "subject");
                addTermFreqToMap(vectorMap, termFreqVector);
            }
        }
    }


    private void addTermFreqToMap(Map vectorMap, TermFreqVector termFreqVector) {
        String[] terms = termFreqVector.getTerms();
        int[] freqs = termFreqVector.getTermFrequencies();

        for (int i = 0; i < terms.length; i++) {
            String term = terms[i];

            if (vectorMap.containsKey(term)) {
                Integer value = (Integer) vectorMap.get(term);
                vectorMap.put(term, new Integer(value.intValue() + freqs[i]));
            } else {
                vectorMap.put(term, new Integer(freqs[i]));
            }
        }
    }



    private String getCategory(String subject) {
        String[] words = subject.split(" ");

        Iterator categoryItr = categoryMap.keySet().iterator();
        double bestAngle = Double.MAX_VALUE;
        String bestCategory = null;

        while (categoryItr.hasNext()) {
            String category = (String) categoryItr.next();
            double angle = computeAngle(words, category);
            if (angle < bestAngle) {
                bestAngle = angle;
                bestCategory = category;
            }
        }

        return bestCategory;
    }



    private double computeAngle(String[] words, String category) {
        Map vectorMap = (Map) categoryMap.get(category);
        int dotProduct = 0;
        int sumOfSquares = 0;
        for (String word : words) {
            int categoryWordFreq = 0;
            if (vectorMap.containsKey(word)) {
                categoryWordFreq = ((Integer) vectorMap.get(word)).intValue();
            }

            dotProduct += categoryWordFreq;
            sumOfSquares += categoryWordFreq * categoryWordFreq;
        }

        double denominator;
        if (sumOfSquares == words.length) {
            denominator = sumOfSquares;
        } else {
            denominator = Math.sqrt(sumOfSquares) * Math.sqrt(words.length);
        }
        double ratio = dotProduct / denominator;
        return Math.acos(ratio);
    }


    public static void main(String[] args) throws IOException {

        GetCategory get = new GetCategory();
        get.buildCategoryVectors();

        System.out.println(get.getCategory("extreme agile methodology"));
        System.out.println(get.getCategory("montessori education philosophy"));
        System.out.println(get.getCategory("lucene in xxxx"));
    }
}
