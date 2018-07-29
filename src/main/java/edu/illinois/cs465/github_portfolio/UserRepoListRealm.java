package edu.illinois.cs465.github_portfolio;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class to store the repos of a user in a format that can be inserted into a
 * Realm DB.
 * @author sahil1105
 */

public class UserRepoListRealm extends RealmObject {

    public UserRepoListRealm() {
        //default constructor
    }

    public UserRepoListRealm(String username, RealmList<Repo> list) {
        this.username = username;
        this.list = list;
    }

    @PrimaryKey
    private String username; //username of the repos owner
    private RealmList<Repo> list; //list of user's repos

    public String getUsername() {
        return username;
    }

    public void setUsername(String usernameType) {
        this.username = usernameType;
    }

    public RealmList<Repo> getList() {
        return list;
    }

    public void setList(RealmList<Repo> list) {
        this.list = list;
    }
}
