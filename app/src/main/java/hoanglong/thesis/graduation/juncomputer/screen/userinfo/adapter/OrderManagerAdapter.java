package hoanglong.thesis.graduation.juncomputer.screen.userinfo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoanglong.thesis.graduation.juncomputer.R;
import hoanglong.thesis.graduation.juncomputer.data.model.order.Order;

import static hoanglong.thesis.graduation.juncomputer.data.TypeConfirmOrder.ORDER_CONFIRMED;
import static hoanglong.thesis.graduation.juncomputer.data.TypeConfirmOrder.ORDER_DELIVERY;
import static hoanglong.thesis.graduation.juncomputer.data.TypeConfirmOrder.ORDER_DONE;
import static hoanglong.thesis.graduation.juncomputer.data.TypeConfirmOrder.ORDER_WAITING_CONFIRM;

public class OrderManagerAdapter extends RecyclerView.Adapter<OrderManagerAdapter.OrderViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Order> mOrders;
    private OnClickOrderListener mOnClickOrderListener;

    public OrderManagerAdapter(List<Order> orders, OnClickOrderListener onClickOrderListener) {
        mOrders = orders;
        mOnClickOrderListener = onClickOrderListener;
    }

    public interface OnClickOrderListener {
        void onClickOrder(Order order);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = mLayoutInflater.inflate(R.layout.item_manager_order, viewGroup, false);
        return new OrderViewHolder(view, mOnClickOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {
        Order order = mOrders.get(i);
        orderViewHolder.bindData(order);
    }

    @Override
    public int getItemCount() {
        return mOrders != null ? mOrders.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Order mOrder;
        private OnClickOrderListener mOnClickOrderListener;
        @BindView(R.id.text_title_order)
        TextView mTextTileOrder;
        @BindView(R.id.text_code_order)
        TextView mTextCodeOrder;
        @BindView(R.id.text_date_order)
        TextView mTextDateOrder;
        @BindView(R.id.text_status_order)
        TextView mTextStatusOrder;
        @BindView(R.id.ic_status)
        ImageView mImageStatus;

        OrderViewHolder(@NonNull View itemView, OnClickOrderListener onClickOrderListener) {
            super(itemView);
            mOnClickOrderListener = onClickOrderListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindData(Order order) {
            if (order == null) {
                return;
            }
            mOrder = order;
            String codeOrder = order.getIdOrder()
                    .substring(order.getIdOrder().length()-7,order.getIdOrder().length()-1);
            mTextTileOrder.setText(String.format("Mã đơn hàng: %s", codeOrder));
            mTextCodeOrder.setText(String.format("Người đặt hàng: %s", order.getNameUser()));
            mTextDateOrder.setText(String.format("Ngày đặt hàng: %s", order.getDateOrder()));
            mTextStatusOrder.setText(String.format("Trạng thái: %s", order.getStatusOrder()));
            switch (order.getStatusOrder()) {
                case ORDER_WAITING_CONFIRM:
                    mImageStatus.setImageResource(R.drawable.ic_filter_tilt_shift_black_24dp);
                    break;
                case ORDER_CONFIRMED:
                    mImageStatus.setImageResource(R.drawable.ic_cofirm);
                    break;
                case ORDER_DELIVERY:
                    mImageStatus.setImageResource(R.drawable.ic_delivery);
                    break;
                case ORDER_DONE:
                    mImageStatus.setImageResource(R.drawable.ic_done);
            }
        }

        @Override
        public void onClick(View v) {
            mOnClickOrderListener.onClickOrder(mOrder);
        }
    }
}
