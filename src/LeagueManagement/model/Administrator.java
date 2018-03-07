package LeagueManagement.model;

public class Administrator {
    private final int adminId;
    private final String username;
    private String passwordHash;

    public Administrator(int adminId, String username, String password) {
        this.passwordHash = password.trim();
        this.adminId = adminId;
        this.username = username;
    }

    public int getAdminId() {
        return this.adminId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

}
