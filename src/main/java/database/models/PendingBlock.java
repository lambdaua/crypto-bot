package database.models;

import org.apache.http.util.TextUtils;

public class PendingBlock {

    private boolean isPending;
    private Callback callback;

    public PendingBlock(boolean isPending, String callback) {
        this.isPending = isPending;
        this.callback = Callback.from(callback);
    }

    public boolean isPending() {
        return isPending;
    }

    public Callback getCallback() {
        return callback;
    }

    public static class Callback {
        private static final Callback EMPTY = new Callback("", "");

        private String name;
        private String value;

        Callback(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean isEmpty() {
            return name.length() == 0 && value.length() == 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Callback callback = (Callback) o;

            if (!name.equals(callback.name)) return false;
            return value.equals(callback.value);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        public static Callback from(String callback) {
            if (TextUtils.isEmpty(callback)) {
                return EMPTY;
            } else {
                String[] split = callback.split("\\.");
                return new Callback(split[0], split[1]);
            }
        }
    }
}
