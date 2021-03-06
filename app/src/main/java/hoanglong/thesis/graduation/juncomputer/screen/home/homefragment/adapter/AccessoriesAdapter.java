package hoanglong.thesis.graduation.juncomputer.screen.home.homefragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import hoanglong.thesis.graduation.juncomputer.data.model.home.Accessories;
import hoanglong.thesis.graduation.juncomputer.data.model.home.Km;
import hoanglong.thesis.graduation.juncomputer.data.model.home.Phone;

public class AccessoriesAdapter extends RecyclerView.Adapter<AccessoriesAdapter.PhoneViewHolder> {

    private List<Accessories> mPhones;
    private LayoutInflater mInflater;
    private OnClickAccessoriesListener mAccessoriesListener;

    public AccessoriesAdapter(List<Accessories> phones,OnClickAccessoriesListener onClickAccessoriesListener) {
        mPhones = phones;
        mAccessoriesListener = onClickAccessoriesListener;
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = mInflater.inflate(R.layout.item_accessories_home, viewGroup, false);
        return new PhoneViewHolder(view, viewGroup.getContext(),mAccessoriesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder phoneViewHolder, int i) {
        Accessories phone = mPhones.get(i);
        phoneViewHolder.bindData(phone);
    }

    @Override
    public int getItemCount() {
        return mPhones != null ? mPhones.size() : 0;
    }

    static class PhoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.image_phone_home)
        ImageView mImagePhone;
        @BindView(R.id.text_phone_home)
        TextView mTextPhone;
        @BindView(R.id.price_phone_home)
        TextView mTextPricePhone;
        @BindView(R.id.image_promo)
        ImageView mImagePromo;
        @BindView(R.id.text_promo_phone_home)
        TextView mTextPromo;
        @BindView(R.id.text_sale)
        TextView mTextSale;
        @BindView(R.id.relative_sale)
        RelativeLayout mRelativeSale;
        private Context mContext;
        private OnClickAccessoriesListener mAccessoriesListener;
        private Accessories mAccessories;

        PhoneViewHolder(@NonNull View itemView, Context context,OnClickAccessoriesListener onClickAccessoriesListener) {
            super(itemView);
            mContext = context;
            mAccessoriesListener = onClickAccessoriesListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindData(Accessories phone) {
            if (phone == null) {
                return;
            }
            mAccessories = phone;
            Glide.with(mContext).load(phone.getImage()).into(mImagePhone);
            if (!phone.getPer().equals("")) {
                mTextSale.setText(phone.getPer());
            }
            mTextPhone.setText(phone.getTitle());
            mTextPricePhone.setText(phone.getPrice());
        }

        @Override
        public void onClick(View v) {
            mAccessoriesListener.onClickAccessories(mAccessories);
        }
    }

    public interface OnClickAccessoriesListener {
        void onClickAccessories(Accessories accessories);
    }
}
