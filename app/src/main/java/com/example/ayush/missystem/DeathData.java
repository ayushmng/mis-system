package com.example.ayush.missystem;

public class DeathData {
    public String iid;
    public String family_id;
    public String death_one_year;
    public String no_of_deaths;
    public String cause;
    public String cause_other;
    public String ward_id;

    public DeathData(String iid, String family_id, String death_one_year, String no_of_death, String cause, String cause_other, String ward_id) {
        this.iid = iid;
        this.family_id = family_id;
        this.death_one_year = death_one_year;
        this.no_of_deaths = no_of_death;
        this.cause = cause;
        this.cause_other = cause_other;
        this.ward_id = ward_id;
    }
}
