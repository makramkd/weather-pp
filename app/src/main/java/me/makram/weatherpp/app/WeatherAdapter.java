package me.makram.weatherpp.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.makram.weatherpp.app.backend.Day;

/**
 * A custom adapter to use to populate the list view in the
 * main activity.
 */
public class WeatherAdapter extends BaseAdapter{

    private static final String TAG = WeatherAdapter.class.getName();

    private int itemLayoutId;
    private Context context;
    private List<Day> days;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    private class ViewHolder {
        NetworkImageView forecastImageView;
        TextView temperatureTextView;
        TextView cityTextView;
        TextView descriptionTextView;
        TextView dateTextView;
    }

    public WeatherAdapter(Context context, int itemLayoutId, Collection<Day> days, ImageLoader loader) {
        Log.d(TAG, "Constructing weather adapter");

        this.context = context;
        this.itemLayoutId = itemLayoutId;
        this.days = new ArrayList<>();
        this.days.addAll(days);
        layoutInflater = LayoutInflater.from(context);
        imageLoader = loader;

        Log.d(TAG, "Finished constructing weather adapter");
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        Log.d(TAG, "In getView for position " + position + ", convertView is " +
                ((convertView == null) ? "null" : "being recycled."));

        if (convertView == null) {
            convertView = layoutInflater.inflate(itemLayoutId, null);
            holder = new ViewHolder();
            holder.forecastImageView = (NetworkImageView) convertView.findViewById(R.id.weather_icon_imageview);
            holder.cityTextView = (TextView) convertView.findViewById(R.id.location_textview);
            holder.temperatureTextView = (TextView) convertView.findViewById(R.id.temperature_textview);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.description_textview);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = days.get(position);

        holder.descriptionTextView.setText(day.description);
        holder.temperatureTextView.setText(context.getResources().getString(R.string.temperature_textview,
                (int) day.minTemp, (int) day.maxTemp));
        holder.cityTextView.setText(context.getResources().getString(R.string.city_textview,
                day.cityName, day.countryName));
        holder.forecastImageView.setImageUrl(day.getImageUrl(), imageLoader);
        holder.dateTextView.setText(dateRelativeToPosition(position));

        return convertView;
    }

    private String dateRelativeToPosition(int position) {
        DateFormat format = DateFormat.getDateInstance();
        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        switch (position) {
            case 0:
                return "Today, " + format.format(date);
            case 1:
                return "Tomorrow, " + format.format(dateTime.plusDays(1));
            default:
                return format.format(dateTime.plusDays(position));
        }
    }
}
