package com.example.quanlynhapxuat.fragment.phieuxuat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.main.LoginActivity;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.example.quanlynhapxuat.adapter.ChiTietPXAdapter;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.service.DeliveryDocketService;
import com.example.quanlynhapxuat.utils.Convert;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExportDetailFragment extends Fragment {

    public static final String TAG=ExportDetailFragment.class.getName();

    private RecyclerView recyclerView;
    private View mView;
    private MainActivity mainActivity;
    private TextView tvIdPX,tvTenKH,tvNgay,tvStatus,tvTongTien,tvGiamGia,tvCanTra;
    private ImageButton btnBack,btnMenu;
    private PopupMenu popupMenu;


    public ExportDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_export_detail, container, false);
        mainActivity= (MainActivity) getActivity();
        recyclerView=mView.findViewById(R.id.rcv_ctpx);
        tvIdPX=mView.findViewById(R.id.tv_idpx_ctpx);
        tvTenKH=mView.findViewById(R.id.tv_tenKH_ctpx);
        tvNgay=mView.findViewById(R.id.tv_ngayctpx);
        tvStatus=mView.findViewById(R.id.tv_trangthai_ctpx);
        btnBack=mView.findViewById(R.id.btn_backexportdetail);
        tvTongTien=mView.findViewById(R.id.tv_tongtienpx);
        tvGiamGia=mView.findViewById(R.id.tv_giamgiapx);
        tvCanTra=mView.findViewById(R.id.tv_khachcantrapx);
        btnMenu=mView.findViewById(R.id.btn_morectpx);

        Bundle bundleReceive=getArguments();

        List<DeliveryDocketDetail> deliveryDocketDetails=mainActivity.getmDeliveryDocketDetails() ;
        DeliveryDocket deliveryDocket=new DeliveryDocket();
        if (bundleReceive!=null){
            deliveryDocket = (DeliveryDocket) bundleReceive.get("deliveryDocket");
            if (deliveryDocket!=null){
                tvIdPX.setText(String.valueOf(deliveryDocket.getId()));
                tvNgay.setText(deliveryDocket.getCreatedAt());
                if(deliveryDocket.getStatus()==1){
                    tvStatus.setText("Đang xử lí");
                }else if(deliveryDocket.getStatus()==2){
                    tvStatus.setText("Hoàn thành");
                }else if(deliveryDocket.getStatus()==0) {
                    tvStatus.setText("Đã hủy");
                }
                int tong=0;
                for (DeliveryDocketDetail item :
                        deliveryDocketDetails) {
                    tong+=item.getPrice();
                }
                tvTongTien.setText(Convert.currencyFormat(tong));
                tvGiamGia.setText(String.valueOf(0));
                tvCanTra.setText(Convert.currencyFormat(tong));
                ApiUtils.getKhachHangService().getKHById(deliveryDocket.getCustomerId()).enqueue(new Callback<KhachHang>() {
                    @Override
                    public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                        KhachHang khachHang=response.body();
                        tvTenKH.setText(khachHang.getFullName());
                    }

                    @Override
                    public void onFailure(Call<KhachHang> call, Throwable t) {

                    }
                });
            }
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(mainActivity,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        ChiTietPXAdapter chiTietPXAdapter=new ChiTietPXAdapter();
        chiTietPXAdapter.setData(deliveryDocketDetails,mainActivity);
        recyclerView.setAdapter(chiTietPXAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        showPopUpMenu(deliveryDocket.getId());
        return mView;
    }
    public void showPopUpMenu(int docketId){
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu=new PopupMenu(mainActivity,v);
                popupMenu.inflate(R.menu.popup_menu_ctpx);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menupx_sua:
                                Toast.makeText(mainActivity,"Sửa",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menupx_xuatfile:
                                Toast.makeText(mainActivity,"Xuất file",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menupx_hoanthanh:
                                hoanThanhDonHang(docketId);
                                return true;
                            case R.id.menupx_huy:
                                huyDonHang(docketId);
                                return true;
                            default:return false;
                        }

                    }
                });
                popupMenu.show();
            }

        });

    }
    public void hoanThanhDonHang(int docketId){
        CustomAlertDialog alertDialog= new CustomAlertDialog(mainActivity);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((7* MainActivity.width)/8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        alertDialog.setMessage("Bạn muốn hoàn thành phiếu nhập này?");
        alertDialog.setBtnNegative("Hủy");
        alertDialog.setBtnPositive("Hoàn thành");
        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Object,Object> fields=new HashMap<>();
                fields.put("status",2);
                DeliveryDocketService.deliveryDocketService.upDateDeliveryDocketOnField(fields,docketId).enqueue(new Callback<DeliveryDocket>() {
                    @Override
                    public void onResponse(Call<DeliveryDocket> call, Response<DeliveryDocket> response) {
                        if (response.isSuccessful()){
                            tvStatus.setText("Hoàn thành");
                            Toast.makeText(mainActivity,"Đơn hàng được chuyển sang trạng thái hoàn thành!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeliveryDocket> call, Throwable t) {

                    }
                });
                alertDialog.cancel();
            }
        });
        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
    public void huyDonHang(int docketId){
        CustomAlertDialog alertDialog= new CustomAlertDialog(mainActivity);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((7* MainActivity.width)/8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        alertDialog.setMessage("Bạn chắc chắn muốn hủy phiếu nhập này?");
        alertDialog.setBtnNegative("Cancel");
        alertDialog.setBtnPositive("OK");
        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Object,Object> fields=new HashMap<>();
                fields.put("status",0);
                DeliveryDocketService.deliveryDocketService.upDateDeliveryDocketOnField(fields,docketId).enqueue(new Callback<DeliveryDocket>() {
                    @Override
                    public void onResponse(Call<DeliveryDocket> call, Response<DeliveryDocket> response) {
                        if (response.isSuccessful()){
                            tvStatus.setText("Đã hủy");
                            Toast.makeText(mainActivity,"Đơn hàng được chuyển sang trạng thái đã hủy!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeliveryDocket> call, Throwable t) {

                    }
                });
                alertDialog.cancel();
            }
        });
        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
}