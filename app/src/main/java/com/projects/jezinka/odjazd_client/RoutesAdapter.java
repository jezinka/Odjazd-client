package com.projects.jezinka.odjazd_client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteViewHolder> {

    private static final String TAG = RoutesAdapter.class.getSimpleName();

    private JSONArray mItems;

    RoutesAdapter(JSONArray routes) {
        mItems = routes;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.route_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mItems.length();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView busTextView = null;
        TextView leaveTextView = null;
        TextView onTheSpotTextView = null;
        TextView transferTextView = null;

        RouteViewHolder(View itemView) {
            super(itemView);

            busTextView = itemView.findViewById(R.id.bus_tv);
            leaveTextView = itemView.findViewById(R.id.leave_tv);
            onTheSpotTextView = itemView.findViewById(R.id.on_the_spot_tv);
            transferTextView = itemView.findViewById(R.id.transfer_tv);
        }

        void bind(int listIndex) {
            try {
                JSONObject routeObject = (JSONObject) mItems.get(listIndex);
                busTextView.setText(routeObject.getJSONArray("bus").join(" | ").replaceAll("\"", ""));
                leaveTextView.setText(routeObject.get("leave").toString());
                onTheSpotTextView.setText(routeObject.get("on_the_spot").toString());

                if (routeObject.has("transfer")) {
                    transferTextView.setText(routeObject.get("transfer").toString());
                } else {
                    transferTextView.setText("");
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());

            }
        }
    }
}
