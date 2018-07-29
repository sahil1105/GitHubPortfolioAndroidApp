package edu.illinois.cs465.github_portfolio;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class to store the followers/following of a user in a format that can be inserted into a
 * Realm DB.
 * References: https://stackoverflow.com/questions/30115021/
 * how-to-convert-realmresults-object-to-realmlist/34435169
 * @author sahil1105
 */

public class UserFollowListRealm extends RealmObject {

    @PrimaryKey
    private String usernameAndListType; // of the form "{user}/{list_type}"
    private RealmList<UserBasic> list;

    public UserFollowListRealm(String usernameAndListType, RealmList<UserBasic> list) {
        this.usernameAndListType = usernameAndListType;
        this.list = list;
    }

    public UserFollowListRealm() {
        //default constructor
    }


    public String getUsernameAndListType() {
        return usernameAndListType;
    }

    public void setUsernameAndListType(String usernameAndListType) {
        this.usernameAndListType = usernameAndListType;
    }

    public RealmList<UserBasic> getList() {
        return list;
    }

    public void setList(RealmList<UserBasic> list) {
        this.list = list;
    }
}
