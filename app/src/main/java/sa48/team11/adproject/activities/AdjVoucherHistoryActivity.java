package sa48.team11.adproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import sa48.team11.adproject.R;
import sa48.team11.adproject.adapters.AdjustmentListAdapter;
import sa48.team11.adproject.listeners.IDatePickerListener;
import sa48.team11.adproject.models.AdjVoucher;
import sa48.team11.adproject.models.Employee;
import sa48.team11.adproject.retrofit.MyRetrofit;
import sa48.team11.adproject.retrofit.ResponseList;
import sa48.team11.adproject.retrofit.ApiClient;
import sa48.team11.adproject.retrofit.ApiService;
import sa48.team11.adproject.utils.App;
import sa48.team11.adproject.utils.Utils;

public class AdjVoucherHistoryActivity extends AppCompatActivity implements View.OnClickListener, IDatePickerListener {
    private EditText edtStartDate, edtEndDate;
    private List<AdjVoucher> dataList = new ArrayList<>();
    private AdjustmentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_requests);
        loadUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Employee user = ((App)getApplicationContext()).getUser();
        ApiService service = ApiClient.getAPIService();
             Call<ResponseList<AdjVoucher>> call = service.getAdjVoucherHistory(user.getId());
                call.enqueue(new MyRetrofit<>(this, response -> {
                    if (response.isSuccess()) {
                        dataList.clear();
                        dataList.addAll(response.getResultList());
                        renderRecyclerView();
                    }
                }));

    }

    private void loadUI() {
        edtStartDate = findViewById(R.id.edt_start_date);
        edtEndDate = findViewById(R.id.edt_end_date);
        edtStartDate.setOnClickListener(this);
        edtEndDate.setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);
    }

    private void renderRecyclerView() {
        RecyclerView rc_req_list = findViewById(R.id.rc_adj_list);
        rc_req_list.setHasFixedSize(true);
        rc_req_list.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.recycler_layout_anim));
        rc_req_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdjustmentListAdapter(this);
        rc_req_list.setAdapter(adapter);
        adapter.updateList(dataList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_start_date:
                Utils.showDatePicker(getFragmentManager(), (EditText) v, this);
                break;
            case R.id.edt_end_date:
                Utils.showDatePicker(getFragmentManager(), (EditText) v, this);
                break;
            case R.id.fab:
                Utils.goNext(this,AdjVoucherCreateActivity.class);
                break;
        }
    }
    private void filterByDate() {
        if (edtStartDate.getText().toString().isEmpty() || edtEndDate.getText().toString().isEmpty())
            return;
        Date startDate = Utils.dateString(edtStartDate.getText().toString());
        Date endDate = Utils.dateString(edtEndDate.getText().toString());
        if (!validateDates(startDate, endDate)) return;
    }

    private boolean validateDates(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            Utils.showAlert(R.string.alert_error, R.string.msg_date_error,this);
            return false;
        }
        List<AdjVoucher> tempList = new ArrayList<>();
        for (AdjVoucher req : dataList) {
            if (!(req.getDate().after(endDate) || req.getDate().before(startDate))) {
                tempList.add(req);
            }
        }
        adapter.updateList(tempList);
        return true;
    }


    @Override
    public void dateChange() {
        filterByDate();
    }
}