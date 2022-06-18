package com.example.quanlynhapxuat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.ReceivedDocket.ReceivedDocketDetailActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.model.Product2;
import com.example.quanlynhapxuat.model.ReceivedDocketDetail;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivedDocketDetailAdapter extends RecyclerView.Adapter<ReceivedDocketDetailAdapter.RDDViewHolder> {
    private Context context;
    private ArrayList<ReceivedDocketDetail> rddList;

    private ArrayList<ReceivedDocketDetail> rddListOld;

    public ArrayList<Product> productList;

    public SPSpinnerAdapter spSpinnerAdapter;
    public Spinner spinSP_dialogThemSP;
    public EditText etSL_dialogThemSP, etDonGia_dialogThemSP;
    public Button btnHuy_dialogThemSP, btnThem_dialogThemSP;

    int selectedProductID;

    private EditText etSL_dialogSuaSL;
    private Button btnHuy_dialogSuaSL, btnOK_dialogSuaSL;

    public ReceivedDocketDetailAdapter(Context context) {
        this.context = context;
        getProductList();
    }

    public ArrayList<Product> gotProductList() {
        return productList;
    }

    public ArrayList<ReceivedDocketDetail> getRddListOld() {
        return rddListOld;
    }

    public void setRddList(ArrayList<ReceivedDocketDetail> list) {
        this.rddList = list;
//        this.rddListOld = list;
        notifyDataSetChanged();
    }

    public void setRddListOld(ArrayList<ReceivedDocketDetail> list) {
        this.rddListOld = list;
    }

    public ArrayList<ReceivedDocketDetail> getRddList() {
        return rddList;
    }

    @NonNull
    @Override
    public RDDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_docket_detail,parent,false);
        return new ReceivedDocketDetailAdapter.RDDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RDDViewHolder holder, int position) {
        ReceivedDocketDetail rdd = rddList.get(position);
        if(rdd==null) {
            return;
        }

        Product product = getProduct(rdd.getProductId());

        if(product!=null) {
            holder.tvTenSP.setText(product.getName());
            holder.tvSL.setText(rdd.getQuantity()+"");
            holder.tvDonGia.setText(rdd.getPrice()+" VND");
            if(product.getImage()!=null) {
                Glide.with(context).load(product.getImage()).into(holder.ivAnhSP);
            }
        }
        else {
            CustomToast.makeText(context,"Không lấy được sản phẩm!!"
                    ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            return;
        }

        holder.ibEditQuantity.setOnClickListener(view -> {
            Dialog dialog = getDialogSuaSL();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            btnHuy_dialogSuaSL.setOnClickListener(view1 -> {
                dialog.cancel();
            });

            btnOK_dialogSuaSL.setOnClickListener(view1 -> {
                String sl = etSL_dialogSuaSL.getText().toString();
                if(sl.equals("")) {
                    CustomToast.makeText(context,"Vui lòng nhập số lượng mới!"
                            ,CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
                }
                else if(sl.equals("0")) {
                    CustomToast.makeText(context,"Vui lòng nhập số lượng lớn hơn 0!"
                            ,CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
                }
                else {
                    Log.e(" rddList.get(position)", rddList.get(position).getQuantity()+"");
                    Log.e(" rddListOld.get(position)", rddListOld.get(position).getQuantity()+"");
                    rddList.get(position).setQuantity(Integer.parseInt(etSL_dialogSuaSL.getText().toString()));
                    Log.e(" rddList.get(position)", rddList.get(position).getQuantity()+"");
                    Log.e(" rddListOld.get(position)", rddListOld.get(position).getQuantity()+"");
                    dialog.cancel();
                    ((ReceivedDocketDetailActivity)context).onResume();
                }
            });
        });

        holder.ibDelete.setOnClickListener(view -> {
            CustomAlertDialog alertDialog = new CustomAlertDialog(context);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            alertDialog.getWindow().setLayout((7*metrics.widthPixels)/8, WindowManager.LayoutParams.WRAP_CONTENT);
            alertDialog.show();

            alertDialog.setMessage("Xóa " + product.getName() + " khỏi phiếu nhập?");
            alertDialog.setBtnPositive("Xóa");
            alertDialog.setBtnNegative("Không");

            alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ibDelete","rddList.size "+rddList.size());
                    Log.e("ibDelete","rddListOld.size "+rddListOld.size());
                    Log.e("ibDelete","");
                    rddList.remove(rdd);
                    Log.e("ibDelete","rddList.size "+rddList.size());
                    Log.e("ibDelete","rddListOld.size "+rddListOld.size());
                    ((ReceivedDocketDetailActivity)context).onResume();
                    alertDialog.cancel();
                }
            });

            alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return rddList==null ? 0 : rddList.size();
    }

    public class RDDViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAnhSP;
        private TextView tvTenSP;
        private TextView tvSL;
        private TextView tvDonGia;
        private ImageButton ibEditQuantity;
        private ImageButton ibDelete;

        public RDDViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnhSP = itemView.findViewById(R.id.ivAnhSP_itemRDD);
            tvTenSP = itemView.findViewById(R.id.tvTenSP_itemRDD);
            tvSL = itemView.findViewById(R.id.tvSL_itemRDD);
            tvDonGia = itemView.findViewById(R.id.tvDonGia_itemRDD);
            ibEditQuantity = itemView.findViewById(R.id.ibEditQuantity_itemRDD);
            ibDelete = itemView.findViewById(R.id.ibDelete_itemRDD);
        }
    }

    public int getTotalList() {
        int sum = 0;
        if(rddList!=null) {
            for(ReceivedDocketDetail rdd : rddList) {
                sum += rdd.getQuantity()*rdd.getPrice();
            }
        }
        return sum;
    }

    private void getProductList() {
        ApiUtils.productRetrofit().getProducts().enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if(response.isSuccessful()) {
                    if (productList==null) {
                        productList = new ArrayList<>();
                    }
                    for(Product item : response.body()) {
                        if (item.getStatus()==1) {
                            productList.add(item);
                        }
                    }
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                        CustomToast.makeText(context,"TRY: " + errorResponse.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                    catch (Exception e) {
                        CustomToast.makeText(context,"CATCH: " + e.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                CustomToast.makeText(context,"CALL API FAIL!!!"
                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            }
        });
    }

    public Product2 getProduct2(int productId) {
        ArrayList<Product2> list = new ArrayList<>();
        for(Product item : productList) {
            if(item.getStatus()==1) {
                Product2 p = new Product2(item.getId(),item.getName(),item.getStatus(),item.getImage(),item.getCreated_at());
                Log.e("p",p.toString());
                list.add(p);
            }
        }
        for(Product2 item : list) {
            if(item.getId()==productId) {
                return item;
            }
        }
        return null;
    }

    public Product getProduct(int productId) {
        if(productList!=null) {
            for(Product item : productList) {
                if(item.getId()==productId) {
                    return item;
                }
            }
        }
        return null;
    }

    public Dialog getDialogThemSP(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_them_sp_rdd);

        spinSP_dialogThemSP = dialog.findViewById(R.id.spinSP_dialogThemSP);
        etSL_dialogThemSP = dialog.findViewById(R.id.etSL_dialogThemSP);
        etDonGia_dialogThemSP = dialog.findViewById(R.id.etDonGia_dialogThemSP);
        btnHuy_dialogThemSP = dialog.findViewById(R.id.btnHuy_dialogThemSP);
        btnThem_dialogThemSP = dialog.findViewById(R.id.btnThem_dialogThemSP);

        spSpinnerAdapter = new SPSpinnerAdapter(context,productList);
        spinSP_dialogThemSP.setAdapter(spSpinnerAdapter);
        spinSP_dialogThemSP.setDropDownVerticalOffset(150);

        spinSP_dialogThemSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProductID = productList.get(i).getId();
                Log.e("productList.get(i).getId()",productList.get(i).getId()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedProductID = 0;
            }
        });

        return dialog;
    }

    public int getSelectedProductID() {
        return selectedProductID;
    }

    public void addRDDList(ReceivedDocketDetail rdd) {
        if(rddList!=null) {
            for(ReceivedDocketDetail item : rddList) {
                if(item.getProductId()==rdd.getProductId()) {
                    item.setQuantity(item.getQuantity()+rdd.getQuantity());
                    item.setPrice(rdd.getPrice());
                    notifyDataSetChanged();
                    return;
                }
            }
        }
        else {
            rddList = new ArrayList<>();
        }
        rddList.add(rdd);
        notifyDataSetChanged();
    }

    private Dialog getDialogSuaSL() {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sua_sl_sp_rdd);

        etSL_dialogSuaSL = dialog.findViewById(R.id.etSL_dialogSuaSL);
        btnHuy_dialogSuaSL = dialog.findViewById(R.id.btnHuy_dialogSuaSL);
        btnOK_dialogSuaSL = dialog.findViewById(R.id.btnOK_dialogSuaSL);

        return dialog;
    }

    public void deleteOldRDDList() {
        if(rddListOld==null) {
            return;
        }
        for(ReceivedDocketDetail item : rddListOld) {
            ApiUtils.getReceivedDocketService().deleteReceivedDocketDetail(item.getId()).enqueue(new Callback<ReceivedDocketDetail>() {
                @Override
                public void onResponse(Call<ReceivedDocketDetail> call, Response<ReceivedDocketDetail> response) {
                    if(response.isSuccessful()) {
                        Log.e("deleteReceivedDocketDetail","success");
                        Log.e("deleteReceivedDocketDetail",response.toString());
                        Log.e("deleteReceivedDocketDetail","rddList.size() "+rddList.size());
                        Log.e("deleteReceivedDocketDetail","rddListOld.size() "+rddListOld.size());
                    }
                    else {
                        Log.e("deleteReceivedDocketDetail","successn't");
                    }
                }

                @Override
                public void onFailure(Call<ReceivedDocketDetail> call, Throwable t) {
                    Log.e("deleteReceivedDocketDetail","failed");
                }
            });
            ApiUtils.getProductService().getProduct(item.getProductId()).enqueue(new Callback<Product2>() {
                @Override
                public void onResponse(Call<Product2> call, Response<Product2> response) {
                    if(response.isSuccessful()) {
                        Log.e("getProduct","success");
                        Product2 p = response.body();
                        p.setInventory(p.getInventory()-item.getQuantity());
                        ApiUtils.getProductService().putProduct(p.getId(),p).enqueue(new Callback<Product2>() {
                            @Override
                            public void onResponse(Call<Product2> call, Response<Product2> response) {
                                if(response.isSuccessful()) {
                                    Log.e("putProduct","success");
                                }
                                else {
                                    Log.e("putProduct","successn't");
                                }
                            }

                            @Override
                            public void onFailure(Call<Product2> call, Throwable t) {
                                Log.e("putProduct","failed");
                            }
                        });
                    }
                    else {
                        Log.e("getProduct","successn't");
                    }
                }

                @Override
                public void onFailure(Call<Product2> call, Throwable t) {
                    Log.e("getProduct","failed");
                }
            });
        }
    }
}