package hoanglong.thesis.graduation.juncomputer.screen.payment.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoanglong.thesis.graduation.juncomputer.R;
import hoanglong.thesis.graduation.juncomputer.data.model.cart.CartItem;
import hoanglong.thesis.graduation.juncomputer.data.source.local.realm.RealmCart;

public class CartConfirmAdapter extends RecyclerView.Adapter<CartConfirmAdapter.CartViewHolder> {

    private List<CartItem> mCartItems;
    private LayoutInflater mInflater;
    private OnUpdatePrice mOnUpdatePrice;

    public CartConfirmAdapter(List<CartItem> cartItems, OnUpdatePrice onUpdatePrice) {
        mCartItems = cartItems;
        mOnUpdatePrice = onUpdatePrice;
    }

    @NonNull
    @Override
    public CartConfirmAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = mInflater.inflate(R.layout.item_cart_confirm, viewGroup, false);
        return new CartViewHolder(view, viewGroup.getContext(), mOnUpdatePrice);
    }

    @Override
    public void onBindViewHolder(@NonNull CartConfirmAdapter.CartViewHolder cartViewHolder, int i) {
        CartItem cartItem = mCartItems.get(i);
        cartViewHolder.bindData(cartItem);
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    public interface OnUpdatePrice {
        void onUpdatePrice();

        void updateCart();
    }

    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mContext;
        @BindView(R.id.image_item_cart)
        ImageView mImageItemCart;
        @BindView(R.id.text_name_item)
        TextView mTextNameItem;
        @BindView(R.id.text_price_item)
        TextView mTextPriceItem;
        @BindView(R.id.text_quantity_item)
        TextView mTextQuantity;
        private int number;
        private CartItem mCartItem;
        private OnUpdatePrice mOnUpdatePrice;

        CartViewHolder(@NonNull View itemView, Context context, OnUpdatePrice onUpdatePrice) {
            super(itemView);
            mContext = context;
            mOnUpdatePrice = onUpdatePrice;
            ButterKnife.bind(this, itemView);
        }

        public void bindData(CartItem cartItem) {
            if (cartItem == null) {
                return;
            }
            mCartItem = cartItem;
            Glide.with(mContext).load(cartItem.getImage()).into(mImageItemCart);
            mTextNameItem.setText(cartItem.getName());
            mTextPriceItem.setText(cartItem.getPrice());
            mTextQuantity.setText("x"+String.valueOf(cartItem.getQuantity()));
            number = cartItem.getQuantity();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    }
}
