package com.example.quanlynhapxuat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.example.quanlynhapxuat.model.Statistics;
import com.example.quanlynhapxuat.model.TopProduct;
import com.example.quanlynhapxuat.service.StatisticService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private View mView;
    private MainActivity mainActivity;
    private TextView tvslton,tvGiaSLTon, tvSLPX,tvSLPN,tvGiaTriPN,tvGiaTriPX;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity= (MainActivity) getActivity();
        mView= inflater.inflate(R.layout.fragment_home, container, false);
        AnyChartView chartView=mView.findViewById(R.id.chart_home);
        tvslton=mView.findViewById(R.id.tv_slton);
        tvGiaSLTon=mView.findViewById(R.id.tv_giatonkho);
        tvSLPN=mView.findViewById(R.id.tv_slphieunhap);
        tvSLPX=mView.findViewById(R.id.tv_slphieuxuat);
        tvGiaTriPN=mView.findViewById(R.id.tv_gia_tk_pn);
        tvGiaTriPX=mView.findViewById(R.id.tv_tk_giapx);
        StatisticService.statisticService.getStatistics().enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if (response.isSuccessful()){
                    Statistics statistics=response.body();
                    List<TopProduct> topProducts=statistics.getListProducts();
                    Cartesian cartesian = AnyChart.column();
                    List<DataEntry> data = new ArrayList<>();
                    for (TopProduct item :
                            topProducts) {
                        data.add(new ValueDataEntry(item.getName(), item.getValue()));
                    }
                    Column column = cartesian.column(data);

                    column.tooltip()
                            .titleFormat("{%X}")
                            .position(Position.CENTER_BOTTOM)
                            .anchor(Anchor.CENTER_BOTTOM)
                            .offsetX(0d)
                            .offsetY(5d)
                            .format("{%Value}{groupsSeparator: }");

                    cartesian.animation(true);
                    cartesian.title("Thống kê những sản phẩm bán chạy nhất");

                    cartesian.yScale().minimum(0d);

                    cartesian.yAxis(0).labels().format("${%(Value)}");

                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                    cartesian.interactivity().hoverMode(HoverMode.BY_X);
                    chartView.setChart(cartesian);
                    DecimalFormat formatter = new DecimalFormat("###,####,###,###,###");
                    tvslton.setText(statistics.getInventory()+"");
                    tvGiaSLTon.setText(formatter.format(statistics.getValueInventory()));
                    tvSLPX.setText(statistics.getNumDelivery()+"");
                    tvGiaTriPX.setText(formatter.format(statistics.getValueDelivery()));
                    tvSLPN.setText(statistics.getNumReceived()+"");
                    tvGiaTriPN.setText(formatter.format(statistics.getValueReceived()));

                }else{
                    Log.e("statistics", "lỗi api");
                    Toast.makeText(mainActivity,"Lỗi! Không thể lấy được dữ liệu!",
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {
                Log.e("statistics", t.getMessage() );
            }
        });


//        Cartesian cartesian = AnyChart.column();
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("Rouge", 80540));
//        data.add(new ValueDataEntry("Foundation", 94190));
//        data.add(new ValueDataEntry("Mascara", 102610));
//        data.add(new ValueDataEntry("Lip gloss", 110430));
//        data.add(new ValueDataEntry("Lipstick", 128000));
//
//
//        Column column = cartesian.column(data);
//
//        column.tooltip()
//                .titleFormat("{%X}")
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(5d)
//                .format("${%Value}{groupsSeparator: }");
//
//        cartesian.animation(true);
//        cartesian.title("Thống kê 10 sản phẩm bán chạy nhất");
//
//        cartesian.yScale().minimum(0d);
//
//        cartesian.yAxis(0).labels().format("${%(Value)}");
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//        cartesian.interactivity().hoverMode(HoverMode.BY_X);
//
////        cartesian.xAxis(0).title("Product");
////        cartesian.yAxis(0).title("Revenue");
//
//
//        chartView.setChart(cartesian);
        return mView;
    }


}