package edu.illinois.cs465.github_portfolio;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class to store the Repository information in.
 * @author sahil1105
 */

public class Repo extends RealmObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @PrimaryKey
    private String full_name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    private String html_url;

    private String description;

    /**
     * Overriding the equals function to enable comparison of Repo objects.
     * @param other The other Repo object to compare with.
     * @return True if equal, else False.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Repo) {
            return this.getFull_name().equals(((Repo) other).getFull_name());
        }
        return false;
    }

}
