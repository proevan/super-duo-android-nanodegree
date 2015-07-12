package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.ScoreRemoteViewsService;

public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = initViews(context, appWidgetManager, widgetId);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private RemoteViews initViews(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.simple_widget);
        Intent intent = new Intent(context, ScoreRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.list, intent);

        Intent appLaunchIntent = new Intent(context, MainActivity.class);
        PendingIntent appLaunchPendingIntent = PendingIntent.getActivity(context, 0, appLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.list, appLaunchPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.title_bar, appLaunchPendingIntent);

        return remoteViews;
    }
}

