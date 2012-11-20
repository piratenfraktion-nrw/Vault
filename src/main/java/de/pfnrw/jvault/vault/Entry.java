package de.pfnrw.jvault.vault;

import de.pfnrw.jvault.util.PasswordGenerator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * User: vileda
 * Date: 11/16/12
 * Time: 12:35 AM
 */
public class Entry {
    private UUID uuid;
    private String name;
    private String description;
    private String url;
    private String username;
    private String password;

    public Entry() {
        uuid = UUID.randomUUID();
    }

    public Entry(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null || password.matches("gen\\d+")) {
            if(password == null) {
                this.password = new PasswordGenerator().generate(20);
            }
            else {
                this.password = new PasswordGenerator().generate(Integer.parseInt(password.substring(3)));
            }
        }
        else {
            this.password = password;
        }
    }

    @Override
    public String toString() {
        try {
            return new JSONObject(this).toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}
