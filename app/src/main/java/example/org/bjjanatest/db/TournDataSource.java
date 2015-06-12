package example.org.bjjanatest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.org.bjjanatest.Tourn;

public class TournDataSource {
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    private static int tournLen = 0;
    private static double tdSucPerc=0.0;
    private static double passSucPerc=0.0;
    private static double sweepSucPerc=0.0;
    private static double subSucPerc=0.0;
    private static double avgMatchTime=0.0;
    private static int totalPts = 0;
    private static int totalPtsAllowed = 0;
    private static int totalPassAtt = 0;
    private static int totalPassSuc = 0;
    private static int totalSweepAtt = 0;
    private static int totalSweepSuc = 0;
    private static int totalTdAtt = 0;
    private static int totalTdSuc = 0;
    private static int totalSubAtt = 0;
    private static int totalSubSuc = 0;
    private static double totalMatchTime = 0.0;

    private static final String LOGTAG = "BJJTRAINING";
    private static final String[] allColumns = {
            trainingDBOpenHelper.COLUMN_ID,
            trainingDBOpenHelper.COLUMN_WEIGHT,
            trainingDBOpenHelper.COLUMN_TOURN_NAME,
            trainingDBOpenHelper.COLUMN_BELT,
            trainingDBOpenHelper.COLUMN_DATE,
            trainingDBOpenHelper.COLUMN_PTS_ALLOWED,
            trainingDBOpenHelper.COLUMN_PTS_SCORED,
            trainingDBOpenHelper.COLUMN_SUB_ATTEMPT,
            trainingDBOpenHelper.COLUMN_SUB_SUCCESS,
            trainingDBOpenHelper.COLUMN_PASS_ATTEMPTED,
            trainingDBOpenHelper.COLUMN_PASS_SUCCESS,
            trainingDBOpenHelper.COLUMN_SWEEP_ATTEMPTED,
            trainingDBOpenHelper.COLUMN_SWEEP_SUCCESS,
            trainingDBOpenHelper.COLUMN_TD_ATTEMPTED,
            trainingDBOpenHelper.COLUMN_TD_SUCCESS,
            trainingDBOpenHelper.COLUMN_BACK_TAKES,
            trainingDBOpenHelper.COLUMN_MOUNTS,
            trainingDBOpenHelper.COLUMN_MATCH_TIME};

    public TournDataSource (Context context) {
        dbhelper = new trainingDBOpenHelper(context);
        database = dbhelper.getWritableDatabase(); //calls oncreate method, creates table structure and allows for database connection
    }

    public void open() {
        Log.i(LOGTAG, "Database opened.");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG,"Database closed.");
        dbhelper.close();
    }

    public Tourn create(Tourn tourn) {
        ContentValues values = new ContentValues();
        //values.put(trainingDBOpenHelper.COLUMN_ID,tourn.getId()); // if the id is set to autoincrement from the db generation string, remove this line
        values.put(trainingDBOpenHelper.COLUMN_WEIGHT,tourn.getWeightClass());
        values.put(trainingDBOpenHelper.COLUMN_TOURN_NAME,tourn.getTournName());
        values.put(trainingDBOpenHelper.COLUMN_BELT,tourn.getBelt());
        values.put(trainingDBOpenHelper.COLUMN_DATE,tourn.getDate());
        values.put(trainingDBOpenHelper.COLUMN_PTS_ALLOWED,tourn.getPointsAllowed());
        values.put(trainingDBOpenHelper.COLUMN_PTS_SCORED,tourn.getPointsScored());
        values.put(trainingDBOpenHelper.COLUMN_SUB_ATTEMPT,tourn.getSubAttempted());
        values.put(trainingDBOpenHelper.COLUMN_SUB_SUCCESS,tourn.getSubSuccessful());
        values.put(trainingDBOpenHelper.COLUMN_PASS_ATTEMPTED,tourn.getPassAttempted());
        values.put(trainingDBOpenHelper.COLUMN_PASS_SUCCESS,tourn.getPassSuccessful());
        values.put(trainingDBOpenHelper.COLUMN_SWEEP_ATTEMPTED,tourn.getSweepAttempted());
        values.put(trainingDBOpenHelper.COLUMN_SWEEP_SUCCESS,tourn.getSweepSuccessful());
        values.put(trainingDBOpenHelper.COLUMN_TD_ATTEMPTED,tourn.getTdAttempted());
        values.put(trainingDBOpenHelper.COLUMN_TD_SUCCESS,tourn.getTdSuccessful());
        values.put(trainingDBOpenHelper.COLUMN_BACK_TAKES,tourn.getNumBackTakes());
        values.put(trainingDBOpenHelper.COLUMN_MOUNTS,tourn.getNumMounts());
        values.put(trainingDBOpenHelper.COLUMN_MATCH_TIME,tourn.getMatchTime());
        long insertId = database.insert(trainingDBOpenHelper.TABLE_TOURN,null,values);
        Log.i(LOGTAG,"Tourn ID: " + insertId);
        tourn.setId(insertId);
        return tourn;
    }

    public List<Tourn> findAll() {
        List<Tourn> tourns = new ArrayList<Tourn>();
        Cursor cursor = database.query(trainingDBOpenHelper.TABLE_TOURN,allColumns,null,null,null,null,null);
        Log.i(LOGTAG,"returned " + cursor.getCount() + " rows.");
        tournLen = cursor.getCount();
        if (tournLen>0 && cursor != null) {
            totalPts = 0;
            totalPtsAllowed = 0;
            totalPassAtt = 0;
            totalPassSuc = 0;
            totalSweepAtt = 0;
            totalSweepSuc = 0;
            totalTdAtt = 0;
            totalTdSuc = 0;
            totalSubAtt = 0;
            totalSubSuc = 0;
            totalMatchTime = 0;
            tdSucPerc = 0.0;
            passSucPerc = 0.0;
            sweepSucPerc = 0.0;
            subSucPerc = 0.0;
            avgMatchTime = 0.0;
            while (cursor.moveToNext()) {
                Tourn tourn = new Tourn();
                Log.i(LOGTAG,"findall: tournid " + cursor.getLong(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_ID)));
                tourn.setId(cursor.getLong(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_ID)));
                tourn.setTournName(cursor.getString(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_TOURN_NAME)));
                tourn.setMatchTime(cursor.getDouble(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_MATCH_TIME)));
                tourn.setWeightClass(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_WEIGHT         )));
                tourn.setBelt(cursor.getString(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_BELT           )));
                tourn.setDate(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_DATE           )));
                tourn.setPointsAllowed(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_PTS_ALLOWED    )));
                tourn.setPointsScored(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_PTS_SCORED     )));
                tourn.setSubAttempted(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_SUB_ATTEMPT    )));
                tourn.setSubSuccessful(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_SUB_SUCCESS    )));
                tourn.setPassAttempted(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_PASS_ATTEMPTED )));
                tourn.setPassSuccessful(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_PASS_SUCCESS   )));
                tourn.setSweepAttempted(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_SWEEP_ATTEMPTED)));
                tourn.setSweepSuccessful(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_SWEEP_SUCCESS  )));
                tourn.setTdAttempted(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_TD_ATTEMPTED   )));
                tourn.setTdSuccessful(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_TD_SUCCESS   )));
                tourn.setNumBackTakes(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_BACK_TAKES   )));
                tourn.setNumMounts(cursor.getInt(cursor.getColumnIndex(trainingDBOpenHelper.COLUMN_MOUNTS   )));


                totalPts        = totalPts + tourn.getPointsScored();
                totalPtsAllowed = totalPtsAllowed + tourn.getPointsAllowed();
                totalPassAtt    = totalPassAtt + tourn.getPassAttempted();
                totalPassSuc    = totalPassSuc + tourn.getPassSuccessful();
                totalSweepAtt   = totalSweepAtt + tourn.getSweepAttempted();
                totalSweepSuc   = totalSweepSuc + tourn.getSweepSuccessful();
                totalTdAtt      = totalTdAtt + tourn.getTdAttempted();
                totalTdSuc      = totalTdSuc + tourn.getTdSuccessful();
                totalSubAtt     = totalSubAtt + tourn.getSubAttempted();
                totalSubSuc     = totalSubSuc + tourn.getSubSuccessful();
                totalMatchTime  = totalMatchTime + tourn.getMatchTime();

                tourns.add(tourn);
            }
            if (totalTdAtt !=0) {
                tdSucPerc = (double) totalTdSuc/totalTdAtt;
            }
            if (totalPassAtt !=0) {
                passSucPerc = (double) totalPassSuc/totalPassAtt;
            }
            if (totalSweepAtt !=0) {
                sweepSucPerc = (double) totalSweepSuc/totalSweepAtt;
            }
            if (totalSubAtt !=0) {
                subSucPerc = (double) totalSubSuc/totalSubAtt;
            }
            if (tournLen !=0) {
                avgMatchTime = (double) totalMatchTime/tournLen;
            }
        }


        return tourns;
    }

    public int getTotalPts() {
        return totalPts;
    }

    public int getTotalPtsAllowed() {
        return totalPtsAllowed;
    }

    public int getTotalPassAtt() {
        return totalPassAtt;
    }

    public int getTotalPassSuc() {
        return totalPassSuc;
    }

    public int getTotalSweepAtt() {
        return totalSweepAtt;
    }

    public int getTotalSweepSuc() {
        return totalSweepSuc;
    }

    public int getTotalTdAtt() {
        return totalTdAtt;
    }

    public int getTotalTdSuc() {
        return totalTdSuc;
    }

    public int getTotalSubAtt() {
        return totalSubAtt;
    }

    public int getTotalSubSuc() {
        return totalSubSuc;
    }

    public double getTdSucPerc() {
        return tdSucPerc;
    }

    public double getPassSucPerc() {
        return passSucPerc;
    }

    public double getSweepSucPerc() {
        return sweepSucPerc;
    }

    public double getSubSucPerc() {
        return subSucPerc;
    }

    public double getAvgMatchTime() {
        return avgMatchTime;
    }

    public int getTournLen() {
        return tournLen;
    }
}
