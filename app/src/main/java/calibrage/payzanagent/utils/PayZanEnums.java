package calibrage.payzanagent.utils;

/**
 * Created by Calibrage19 on 02-11-2017.
 */

public class PayZanEnums {
    public enum StringseNUM {
        STRING_ONE("ONE"),
        STRING_TWO("TWO");
        private final String text;
        private StringseNUM(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
