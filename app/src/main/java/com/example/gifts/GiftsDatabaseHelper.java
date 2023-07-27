package com.example.gifts;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GiftsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gifts.db";
    private static final int DATABASE_VERSION = 1;

    public GiftsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableGifts = "CREATE TABLE IF NOT EXISTS gifts (id INTEGER PRIMARY KEY, name TEXT, gift TEXT, gender TEXT)";
        db.execSQL(createTableGifts);

        String createTableGiftAges = "CREATE TABLE IF NOT EXISTS gift_ages (id INTEGER PRIMARY KEY, gift_id INTEGER, age_id INTEGER, FOREIGN KEY(gift_id) REFERENCES gifts(id))";
        db.execSQL(createTableGiftAges);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS gift_ages");
        db.execSQL("DROP TABLE IF EXISTS gifts");
        onCreate(db);
    }

    public void addGifts(List<Gift> gifts) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            for (Gift gift : gifts) {
                ContentValues values = new ContentValues();
                values.put("name", gift.getName());
                values.put("gift", gift.getGift());
                values.put("gender", gift.getGender());

                db.insert("gifts", null, values);

                // Получаем идентификатор последнего вставленного подарка
                Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
                if (cursor != null && cursor.moveToFirst()) {
                    int giftId = cursor.getInt(0);
                    cursor.close();

                    for (int ageId : gift.getAgeIds()) {
                        ContentValues ageValues = new ContentValues();
                        ageValues.put("gift_id", giftId);
                        ageValues.put("age_id", ageId);
                        db.insert("gift_ages", null, ageValues);
                    }
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Gift> getAllGifts() {
        SQLiteDatabase db = getReadableDatabase();
        List<Gift> giftList = new ArrayList<>();

        String selectQuery = "SELECT * FROM gifts";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexId = cursor.getColumnIndex("id");
            int columnIndexName = cursor.getColumnIndex("name");
            int columnIndexGift = cursor.getColumnIndex("gift");
            int columnIndexGender = cursor.getColumnIndex("gender");

            do {
                int giftId = cursor.getInt(columnIndexId);
                String giftName = cursor.getString(columnIndexName);
                String giftGift = cursor.getString(columnIndexGift);
                String giftGender = cursor.getString(columnIndexGender);

                List<Integer> ageIds = getAgeIdsForGift(db, giftId);

                Gift gift = new Gift(giftId, giftName, giftGift, ageIds, giftGender);
                giftList.add(gift);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return giftList;
    }

    private List<Integer> getAgeIdsForGift(SQLiteDatabase db, int giftId) {
        List<Integer> ageIds = new ArrayList<>();

        String selectQuery = "SELECT age_id FROM gift_ages WHERE gift_id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(giftId)});

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexAgeId = cursor.getColumnIndex("age_id");
            do {
                int ageId = cursor.getInt(columnIndexAgeId);
                ageIds.add(ageId);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return ageIds;
    }

    public List<Gift> getGiftsByAgeAndGender(int age, String gender) {
        SQLiteDatabase db = getReadableDatabase();
        List<Gift> filteredGifts = new ArrayList<>();

        String selectQuery = "SELECT * FROM gifts " +
                "INNER JOIN gift_ages ON gifts.id = gift_ages.gift_id " +
                "WHERE age_id = ? AND gender = ?";
        String[] selectionArgs = {String.valueOf(age), gender};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexId = cursor.getColumnIndex("id");
            int columnIndexName = cursor.getColumnIndex("name");
            int columnIndexGift = cursor.getColumnIndex("gift");
            int columnIndexGender = cursor.getColumnIndex("gender");

            do {
                int giftId = cursor.getInt(columnIndexId);
                String giftName = cursor.getString(columnIndexName);
                String giftGift = cursor.getString(columnIndexGift);
                String giftGender = cursor.getString(columnIndexGender);

                List<Integer> ageIds = getAgeIdsForGift(db, giftId);

                Gift gift = new Gift(giftId, giftName, giftGift, ageIds, giftGender);
                filteredGifts.add(gift);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return filteredGifts;
    }

    public void updateGift(Gift gift) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", gift.getName());
        contentValues.put("gift", gift.getGift());
        contentValues.put("gender", gift.getGender());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(gift.getId())};

        db.update("gifts", contentValues, whereClause, whereArgs);

        db.close();
    }
}