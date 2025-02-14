package wisoft.jwt;

public class JWTPayload {
    private String userId;
    private String username;
    private long exp;
    private long iat;

    public JWTPayload() {}

    public JWTPayload(String userId, String username, long exp, long iat) {
        this.userId = userId;
        this.username = username;
        this.exp = exp;
        this.iat = iat;
    }

    public JWTPayload(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public long getExp() {
        return exp;
    }

    public long getIat() {
        return iat;
    }

    @Override
    public String toString() {
        return "{ userId=" + userId + ", username=" + username + ", exp=" + exp + ", iat=" + iat + " }";
    }
}
