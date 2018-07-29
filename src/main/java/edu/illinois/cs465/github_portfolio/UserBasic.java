package edu.illinois.cs465.github_portfolio;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Description of information of a user. Holds the basic information of a user.
 * Used for 'followers' and 'following' lists.
 * @author sahil1105
 */

public class UserBasic extends RealmObject {

    @PrimaryKey
    private String login;
    private String avatar_url;
    private String html_url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    public void setHtmlUrl(String html_url) {
        this.html_url = html_url;
    }

    /**
     * Overriding the equals function to enable comparison of UserBasic objects.
     * @param other The other UserBasic object to compare with.
     * @return True if equal, else False.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof UserBasic) {
            return this.getLogin().equals(((UserBasic)other).getLogin());
        }
        return false;
    }


}
