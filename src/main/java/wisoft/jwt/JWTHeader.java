package wisoft.jwt;

public class JWTHeader {
    String alg;
    String typ;

    public JWTHeader() {
        this.alg = "HS256";
        this.typ = "JWT";
    }

    public String getAlg() {
        return alg;
    }

    public String getTyp() {
        return typ;
    }

    @Override
    public String toString() {
        return "JWTHeader [alg=" + alg + ", typ=" + typ + "]";
    }
}
