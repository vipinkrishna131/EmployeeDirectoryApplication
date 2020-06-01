package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeedirectoryapplication.R;

import java.util.ArrayList;
import java.util.List;

import model.EmployeeModel;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {

    public ArrayList<EmployeeModel> employeeList;
    public  Context context;

    public EmployeeListAdapter(Context context,ArrayList<EmployeeModel> employeeList) {
         this.employeeList = employeeList;
         this.context = context;
    }

    @NonNull
    @Override
    public EmployeeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_item, parent, false);
        return new EmployeeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListAdapter.ViewHolder holder, int position) {
        holder.image.setImageBitmap(employeeList.get(position).getUrlImage());
        holder.name.setText(employeeList.get(position).getName());
        holder.userName.setText(employeeList.get(position).getUserName());
        holder.email.setText(employeeList.get(position).getEmail());
        holder.street.setText(employeeList.get(position).getStreet());
        holder.suite.setText(employeeList.get(position).getSuite());
        holder.zipcode.setText(employeeList.get(position).getZipcode());
        holder.city.setText(employeeList.get(position).getCity());
        holder.phone.setText(employeeList.get(position).getPhone());
        holder.website.setText(employeeList.get(position).getWebsite());
        holder.companyName.setText(employeeList.get(position).getCompanyName());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name,userName,email,street,city,zipcode,suite,phone,website,companyName;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textName);
            name = itemView.findViewById(R.id.textUserName);
            name = itemView.findViewById(R.id.textEmail);
            name = itemView.findViewById(R.id.textStreet);
            name = itemView.findViewById(R.id.textSuite);
            name = itemView.findViewById(R.id.textZip);
            name = itemView.findViewById(R.id.textCity);
            name = itemView.findViewById(R.id.textPhone);
            name = itemView.findViewById(R.id.textSite);
            name = itemView.findViewById(R.id.textCompanyName);

        }
    }
}
