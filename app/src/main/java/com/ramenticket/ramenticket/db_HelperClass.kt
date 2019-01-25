package com.ramenticket.ramenticket

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class db_HelperClass (Context : Context) : SQLiteOpenHelper(Context, "RAMEN_POINT", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        // テーブルがなかったときに が呼ばれる
        // execSQL で　クエリSQL文を実行 これでDBの構造が決定
        //以下デフォルト
        /*db?.execSQL(
            "CREATE TABLE " + DB_TABLE_NAME + " ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "type integer not null, "+
                    "date text not null, " +
                    "memo text not null " +
                    ");")*/

        //DB名：RAMEN_POINT
        //DBテーブル名：RAMEN_EAT_TABLE
        //DBのカラム：_id, eatday


        db?.execSQL(
            "CREATE TABLE " + "RAMEN_EAT_TABLE" + " ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "eatday INTEGER "+
                    ");")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // バージョンが変わった時に実行される
        db?.execSQL("DROP TABLE IF EXISTS " + "RAMEN_EAT_DAY" + ";")
        onCreate(db)
        // 今回は,一度消して、作り直ししてます　
    }


}