package example.digitallife;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DL_Adapter extends RecyclerView.Adapter<DL_Adapter.DLViewHolder> {
    private String[] Dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class DLViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView textView;

        private DLViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DL_Adapter(String[] myDataset) {
        Dataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DL_Adapter.DLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.account_view, parent, false);
        return new DLViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DLViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(Dataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Dataset.length;
    }

}
