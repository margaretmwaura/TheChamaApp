package com.example.admin.chamaapp;



import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
