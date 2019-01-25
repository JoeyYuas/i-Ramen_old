package com.ramenticket.ramenticket

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class db_Adapter (Context : Context){

    //初期化
    private val userDB : db_HelperClass = db_HelperClass(Context)
    private val db : SQLiteDatabase = userDB.writableDatabase

    fun insertDB(eatDay :Long) {
        val values = ContentValues()
        values.put("eatday", eatDay)
        //values.put("date", day )
        //values.put("type", type)

        // insertOrThrow()
        // 第1引数はテーブル名
        // 第2引数はデータを挿入する際にnull値が許可されていないカラムに代わりに利用される値を指定(?)
        // 第3引数は ContentValue(データ)
        db.insertOrThrow("RAMEN_EAT_TABLE", null, values)
    }

    fun selectDB() :ArrayList<Long> {
        val selectQql : String = "select " + "eatday " + "from "  + "RAMEN_EAT_TABLE "
        // 第一引数にある?の部分に
        // 第二引数が入る（複数可能、先に指定した順)
        val cursor: Cursor = db.rawQuery(selectQql, null)

        cursor.use { cursor ->
            val list: ArrayList<Long> = arrayListOf<Long>()
            while (cursor.moveToNext()) {
                list.add(cursor.getLong(cursor.getColumnIndex("eatday")))
            }
            return  list
        }
        /*try {
            if (cursor.moveToNext()) {
                Result = cursor.getLong(cursor.getColumnIndex("memo"))
            }
        } finally {
            cursor.close()
        }
        return Result*/
    }




}