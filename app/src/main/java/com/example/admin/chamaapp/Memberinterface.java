package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface Memberinterface
{
    @Insert
    void insertMember(Member object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMembers(List<Member> members);

    @Delete
    void deleteMember(Member object);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMember(Member object);

//    The name should be the name of the entity
    @Query("SELECT * FROM Member")
    LiveData<Member> loadMember();

    @Delete
    void deleteMembers(List<Member> members);
}
