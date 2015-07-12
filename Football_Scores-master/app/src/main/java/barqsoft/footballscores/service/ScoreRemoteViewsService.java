package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;

import static barqsoft.footballscores.scoresAdapter.*;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoreRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoreRemoteViewsFactory();
    }

    private class ScoreRemoteViewsFactory implements RemoteViewsFactory {

        private Cursor mCursor;

        @Override
        public void onCreate() {

        }

        private Cursor createScoreCursor() {
            return getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null)
                mCursor.close();
            mCursor = createScoreCursor();
        }

        @Override
        public void onDestroy() {
            if (mCursor != null)
                mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor != null)
                return mCursor.getCount();
            else
                return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            String homeTeamName = "";
            String awayTeamName = "";
            String scores = "";
            String time = "";
            if (mCursor.moveToPosition(position)) {
                homeTeamName = mCursor.getString(COL_HOME);
                awayTeamName = mCursor.getString(COL_AWAY);
                scores = Utilies.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS));
                time = mCursor.getString(COL_MATCHTIME);
            }

            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.widget_scores_list_item);

            remoteViews.setTextViewText(R.id.home_name, homeTeamName);
            remoteViews.setTextViewText(R.id.away_name, awayTeamName);
            remoteViews.setTextViewText(R.id.score_textview, scores);
            remoteViews.setTextViewText(R.id.data_textview, time);

            remoteViews.setOnClickFillInIntent(R.id.list_item, new Intent());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (mCursor != null)
                return mCursor.getLong(mCursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));
            else
                return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
