package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViewsService;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

public class FootballRemoteViewService  extends RemoteViewsService {

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



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
               //  Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                //  String location = Utility.getPreferredLocation(DetailWidgetRemoteViewsService.this);
                Uri dateuri=  DatabaseContract.scores_table.buildScoreWithDate();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dateobj = new Date();

                data = getContentResolver().query(dateuri, MATCH_COLUMNS, null,
                                new String[]{df.format(dateobj )}, DatabaseContract.scores_table.DATE_COL + " ASC");

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_item);

                Log.e(LOG_TAG, "datacount" + data.getCount());
                int matchId = data.getInt(INDEX_MATCH_ID);
                int matchHomeIcon = (Utilies.getTeamCrestByTeamName(
                        data.getString(INDEX_HOME_COL)));
                String descriptionHome = data.getString(INDEX_HOME_COL);

                int matchAwayIcon = (Utilies.getTeamCrestByTeamName(
                        data.getString(INDEX_AWAY_COL)));
                String descriptionAway = data.getString(INDEX_AWAY_COL);
                String matchTime = data.getString(INDEX_TIME_COL);
                String matchScore = Utilies.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS));

                views.setTextViewText(R.id.widget_home_name, descriptionHome);
                views.setTextViewText(R.id.widget_away_name, descriptionAway);

                views.setTextViewText(R.id.widget_data_textview, matchTime);
                views.setTextViewText(R.id.widget_score_textview, matchScore);

                views.setImageViewResource(R.id.widget_home_crest, matchHomeIcon);
                views.setImageViewResource(R.id.widget_away_crest, matchAwayIcon);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, descriptionHome,descriptionAway);
                }

                final Intent fillInIntent = new Intent();
                Uri date_uri=  DatabaseContract.scores_table.buildScoreWithDate();
                //fillInIntent.setData(date_uri);
                // Send the matchId var to the MainActivity
                fillInIntent.putExtra(MainActivity.DETAIL_MATCH_ID, matchId);
                fillInIntent.putExtra(MainActivity.TODAY_VIEW_OPEN,2);
                views.setOnClickFillInIntent(R.id.widget_item_id, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description_home, String description_away) {
                views.setContentDescription(R.id.widget_home_crest, description_home);
                views.setContentDescription(R.id.widget_away_crest, description_away);
            }

            @Override
            public RemoteViews getLoadingView() {
               // return new RemoteViews(getPackageName(), R.layout.widget_detail_item);
                  return new RemoteViews(getPackageName(), R.layout.widget_detail_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
