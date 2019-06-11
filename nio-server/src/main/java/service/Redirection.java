package service;

public class Redirection {
    public static final String PERMANENTLY = "perm";
    public static final String TEMPORATILY = "temp";
    private String type;
    private String source;
    private String target;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
