package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Konstantin on 30.12.2015.
 */
public class TodayWidgetIntentService extends IntentService {

    public final String LOG_TAG = FootballRemoteViewService.class.getSimpleName();
    private static final String[] MATCH_COLUMNS = {

            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL

    };
    // these indices must match the projection
    private static final int INDEX_MATCH_ID = 0;
    private static final int INDEX_LEAGUE_COL = 1;
    private static final int INDEX_HOME_COL = 2;
    private static final int INDEX_AWAY_COL = 3;
    private static final int INDEX_DATE_COL = 4;
    private static final int INDEX_TIME_COL = 5;
    public static final int  INDEX_HOME_GOALS = 6;
    public static final int  INDEX_AWAY_GOALS = 7;


    public TodayWidgetIntentService() {
        super("TodayWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                TodayWidgetProvider.class));


        Uri dateuri=  DatabaseContract.scores_table.buildScoreWithDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();

        Cursor data = getContentResolver().query(dateuri, MATCH_COLUMNS, null,
                new String[]{df.format(dateobj )}, DatabaseContract.scores_table.DATE_COL + " ASC");

        int layoutId,layoutId_empty;
        layoutId = R.layout.widget_today;
        layoutId_empty =R.layout.empty_today_widget;




        if (data == null) {
            RemoteViews views = new RemoteViews(getPackageName(), layoutId_empty);
            return;
        }
        if (!data.moveToFirst()) {
            RemoteViews views = new RemoteViews(getPackageName(), layoutId_empty);
            data.close();

            return;
        }

        for (int appWidgetId : appWidgetIds) {
            Log.e(LOG_TAG, "datacount" + data.getCount());

            RemoteViews views = new RemoteViews(getPackageName(), layoutId);


            int matchId = data.getInt(INDEX_MATCH_ID);
            int matchHomeIcon = (Utilies.getTeamCrestByTeamName(
                    data.getString(INDEX_HOME_COL)));
            String descriptionHome = data.getString(INDEX_HOME_COL);

            int matchAwayIcon = (Utilies.getTeamCrestByTeamName(
                    data.getString(INDEX_AWAY_COL)));
            String descriptionAway = data.getString(INDEX_AWAY_COL);
            String matchTime = data.getString(INDEX_TIME_COL);
            String matchScore = Utilies.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS));



            views.setTextViewText(R.id.today_widget_home_name, descriptionHome);
            views.setTextViewText(R.id.today_widget_away_name, descriptionAway);

            views.setTextViewText(R.id.today_widget_data_textview, matchTime);
            views.setTextViewText(R.id.today_widget_score_textview, matchScore);

            views.setImageViewResource(R.id.today_widget_home_crest, matchHomeIcon);
            views.setImageViewResource(R.id.today_widget_away_crest, matchAwayIcon);

            String description = descriptionHome + " " + matchScore + " " + descriptionAway;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views,descriptionHome,descriptionAway);
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            launchIntent.putExtra(MainActivity.DETAIL_MATCH_ID, matchId);
            launchIntent.putExtra(MainActivity.TODAY_VIEW_OPEN,2);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.today_widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description_home, String description_away) {
        views.setContentDescription(R.id.today_widget_home_crest, description_home);
        views.setContentDescription(R.id.today_widget_away_crest, description_away);
    }
}
