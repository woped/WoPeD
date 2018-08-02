package org.woped.p2t.dataModel.intermediate;

import org.woped.p2t.textPlanning.recordClasses.ModifierRecord;
import org.woped.p2t.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractFragment {
    public static final int TYPE_JOIN = 1;
    public static final List<String> DEM_PRONOUNS;
    public static final List<String> PRONOUNS;

    static {
        DEM_PRONOUNS = new ArrayList<>();
        DEM_PRONOUNS.add("this");
        DEM_PRONOUNS.add("that");
        DEM_PRONOUNS.add("these");
        DEM_PRONOUNS.add("those");
    }

    static {
        PRONOUNS = new ArrayList<>();
        PRONOUNS.add("he");
        PRONOUNS.add("she");
        PRONOUNS.add("it");
    }

    private final ArrayList<Integer> associatedActivities = new ArrayList<>();
    public boolean bo_replaceWithPronoun = false;
    public boolean bo_isSubject = false;
    public boolean bo_isPlural = false;
    public boolean bo_hasArticle = true;
    public boolean bo_hasIndefArticle = false;
    public boolean verb_IsPassive = false;
    public boolean verb_isParticiple = false;
    public boolean verb_isPast = false;
    public boolean verb_isNegated = false;
    public boolean verb_isImperative = false;
    public boolean add_hasArticle = true;
    public boolean sen_isCoord = true;
    public boolean sen_hasConnective = false;
    public boolean sen_hasBullet = false;
    public int sen_level = 0;
    public boolean sen_hasComma = false;
    public boolean role_isImperative = false;
    private String action;
    private String bo;
    private String role;
    private String addition;
    private int fragmentType;
    private HashMap<String, ModifierRecord> modList;

    AbstractFragment(String action, String bo, String role, String addition) {
        this.action = action;
        this.bo = bo;
        this.role = role;
        this.addition = addition;
        modList = new HashMap<>();
        fragmentType = -1;
    }

    AbstractFragment(String action, String bo, String role, String addition, HashMap<String, ModifierRecord> modList) {
        this.action = action;
        this.bo = bo;
        this.role = role;
        this.addition = addition;
        this.modList = modList;
        fragmentType = -1;
    }

    public ArrayList<Integer> getAssociatedActivities() {
        return associatedActivities;
    }

    public void addAssociation(Integer id) {
        associatedActivities.add(id);
    }

    public boolean hasBO() {
        return !bo.equals("") || bo_replaceWithPronoun;
    }

    public int getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(int type) {
        this.fragmentType = type;
    }

    public ArrayList<String> getAllMods() {
        return new ArrayList(modList.keySet());
    }

    public void addMod(String mod, ModifierRecord modRecord) {
        modList.put(mod, modRecord);
    }

    public ArrayList<Pair<String, String>> getModOptions(String mod) {
        return modList.get(mod).getAttributes();
    }

    public int getModType(String mod) {
        return modList.get(mod).getType();
    }

    public int getModTarget(String mod) {
        return modList.get(mod).getTarget();
    }

    public String getAction() {
        return action;
    }

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }
}