package ypsilon.app.cdn;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

public class CDTWidget extends AppWidgetProvider {

	RemoteViews widgetview;

	@Override
	public void onEnabled (Context cont) {
		super.onEnabled(cont);



	}

	@Override
	public void onUpdate (Context cont, AppWidgetManager manage, int[] widgetids) {

/*		for (int id: widgetids) {
			widgetview = new RemoteViews(cont.getPackageName(), R.layout.cdtw_layout);
			widgetview.setTextViewText(id, "55:55");
			manage.updateAppWidget(id, widgetview); 
		} */
	}


	@Override
	public void onDeleted (Context cont, int[] widgetIds) {
		super.onDeleted(cont, widgetIds);

	}

	@Override
	public void onReceive (Context cont, Intent intent) {


		if (intent.getAction().equals("YP_CDT_TIMECHANGE")) {

		}


		super.onReceive(cont, intent);
	}

}
