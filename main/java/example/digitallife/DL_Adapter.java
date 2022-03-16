package example.digitallife;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import example.digitallife.database.Account;

public class DL_Adapter extends RecyclerView.Adapter<DL_Adapter.DLViewHolder> implements View.OnClickListener {

    private List<Account> accounts;
    private View.OnClickListener listener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DL_Adapter(List<Account> accounts) {
        this.accounts = accounts;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        v.setOnClickListener(this);
        return new DLViewHolder(v);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DLViewHolder holder, int position) {
        holder.bindHolder(accounts.get(position));
    }

    // Return the size of dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(List<Account> filteredList) {
        accounts = filteredList;
        notifyDataSetChanged();
    }


    public static class DLViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_account;
        private final TextView tv_username;

        public DLViewHolder(View view) {
            super(view);
            tv_account = view.findViewById(R.id.tv_account);
            tv_username = view.findViewById(R.id.tv_username);
        }

        void bindHolder(Account account) {
            tv_account.setText(account.getName());
            tv_username.setText(account.getUser());
        }

    }

}
