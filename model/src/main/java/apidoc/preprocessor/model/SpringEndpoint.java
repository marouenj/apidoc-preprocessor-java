package apidoc.preprocessor.model;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

public class SpringEndpoint implements Endpoint, Comparable<SpringEndpoint> {

    // TODO add id
    private RequestMethod[] methods;
    private String[] prefix;
    private String[] suffix;

    public SpringEndpoint(RequestMethod[] methods, String[] prefix, String[] suffix) {
        this.methods = methods;
        this.prefix = prefix;
        this.suffix = suffix;
        Arrays.sort(this.methods);
        Arrays.sort(this.prefix);
        Arrays.sort(this.suffix);
    }

    public RequestMethod[] getMethods() {
        return methods;
    }

    public String[] getPrefix() {
        return prefix;
    }

    public String[] getSuffix() {
        return suffix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof SpringEndpoint)) {
            return false;
        }
        SpringEndpoint that = (SpringEndpoint) obj;

        if (this.methods.length != that.methods.length) {
            return false;
        }
        if (this.prefix.length != that.prefix.length) {
            return false;
        }
        if (this.suffix.length != that.suffix.length) {
            return false;
        }

        int i = -1;
        for (RequestMethod method : methods) {
            if (method != that.methods[++i]) {
                return false;
            }
        }

        i = -1;
        for (String pre : prefix) {
            if (!pre.equals(that.prefix[++i])) {
                return false;
            }
        }

        i = -1;
        for (String suf : suffix) {
            if (!suf.equals(that.suffix[++i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int compareTo(SpringEndpoint that) {
        int i = this.methods.length - 1;
        int j = that.methods.length - 1;
        while (i > -1 && j > -1) {
            if (this.methods[i].compareTo(that.methods[j]) == -1) {
                return -1;
            } else if (this.methods[i].compareTo(that.methods[j]) == 1) {
                return 1;
            }
            i--;
            j--;
        }

        if (j > -1) {
            return -1;
        } else if (i > -1) {
            return 1;
        }

        i = this.prefix.length - 1;
        j = that.prefix.length - 1;
        while (i > -1 && j > -1) {
            if (this.prefix[i].compareTo(that.prefix[j]) == -1) {
                return -1;
            } else if (this.prefix[i].compareTo(that.prefix[j]) == 1) {
                return 1;
            }
            i--;
            j--;
        }

        if (j > -1) {
            return -1;
        } else if (i > -1) {
            return 1;
        }

        i = this.suffix.length - 1;
        j = that.suffix.length - 1;
        while (i > -1 && j > -1) {
            if (this.suffix[i].compareTo(that.suffix[j]) == -1) {
                return -1;
            } else if (this.suffix[i].compareTo(that.suffix[j]) == 1) {
                return 1;
            }
            i--;
            j--;
        }

        if (j > -1) {
            return -1;
        } else if (i > -1) {
            return 1;
        }

        return 0;
    }

    @Override
    public String toString() {
        String str = "[";

        str += "methods = [";
        for (RequestMethod method : methods) {
            str += method + ", ";
        }
        str += "]";

        str += " prefix = [";
        for (String pre : prefix) {
            str += pre + ", ";
        }
        str += "]";

        str += " suffix = [";
        for (String suf : suffix) {
            str += suf + ", ";
        }
        str += "]";

        return str + "]";
    }
}
