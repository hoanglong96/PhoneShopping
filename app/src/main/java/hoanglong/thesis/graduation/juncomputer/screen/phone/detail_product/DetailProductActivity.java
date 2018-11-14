package hoanglong.thesis.graduation.juncomputer.screen.phone.detail_product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoanglong.thesis.graduation.juncomputer.R;
import hoanglong.thesis.graduation.juncomputer.data.model.phone_product.DetailContent;
import hoanglong.thesis.graduation.juncomputer.data.model.phone_product.ItemPhoneProduct;
import hoanglong.thesis.graduation.juncomputer.data.model.phone_product.ListParameter;
import hoanglong.thesis.graduation.juncomputer.screen.home.adapter.SamplePagerAdapter;
import hoanglong.thesis.graduation.juncomputer.screen.phone.adapter.ContentAdapter;
import hoanglong.thesis.graduation.juncomputer.screen.phone.adapter.ExtraProductAdapter;
import hoanglong.thesis.graduation.juncomputer.screen.phone.adapter.InfoAdapter;
import hoanglong.thesis.graduation.juncomputer.screen.phone.adapter.SaleAdapter;
import hoanglong.thesis.graduation.juncomputer.utils.FragmentTransactionUtils;
import hoanglong.thesis.graduation.juncomputer.utils.customView.LoopViewPager;
import me.relex.circleindicator.CircleIndicator;


public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DetailProductActivity.class.getName();
    @BindView(R.id.text_sale)
    TextView mTextSale;
    @BindView(R.id.text_title_product)
    TextView mTextNameProduct;
    @BindView(R.id.text_price_product)
    TextView mTextPrice;
    @BindView(R.id.rating_bar_product)
    RatingBar mRatingBar;
    @BindView(R.id.text_number_rating_product)
    TextView mTextNumRating;
    @BindView(R.id.recycler_sale)
    RecyclerView mRecyclerSale;
    @BindView(R.id.recycler_extra_product)
    RecyclerView mRecyclerExtraProduct;
    @BindView(R.id.recycler_info_product)
    RecyclerView mRecyclerInfoProduct;
    @BindView(R.id.text_title_content)
    TextView mTextTitleContent;
    @BindView(R.id.text_h2)
    TextView mTextH2;
    @BindView(R.id.recycler_des)
    RecyclerView mRecyclerDes;
    @BindView(R.id.viewpager)
    LoopViewPager mViewPager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    @BindView(R.id.relative_all_content)
    RelativeLayout mRLAllContent;
    @BindView(R.id.text_see_detail)
    TextView textSeeDetail;
    @BindView(R.id.relative_comment)
    RelativeLayout mRelativeComment;
    @BindView(R.id.ic_shopping)
    ImageView mImageShopping;
    private ItemPhoneProduct itemPhoneProduct;
    private List<DetailContent> mContentListHide;
    private List<ListParameter> mInfoProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        ButterKnife.bind(this);
        mRLAllContent.setOnClickListener(this);
        textSeeDetail.setOnClickListener(this);
        mRelativeComment.setOnClickListener(this);
        mContentListHide = new ArrayList<>();
        mInfoProducts = new ArrayList<>();
        //setData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemPhoneProduct = bundle.getParcelable("BUNDLE_ITEM_PRODUCT");
            setupView(itemPhoneProduct);
        }
        setSlide();
    }

    private void setSlide() {
        mViewPager.setAdapter(new SamplePagerAdapter(
                itemPhoneProduct.getSlider().size(),
                itemPhoneProduct.getSlider()));
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageShopping.setVisibility(View.VISIBLE);
    }

    private void setupView(ItemPhoneProduct itemPhoneProduct) {
        if (itemPhoneProduct == null) {
            return;
        }
        mTextSale.setText(itemPhoneProduct.getDeal());
        mTextNameProduct.setText(itemPhoneProduct.getTitle());
        mTextPrice.setText(itemPhoneProduct.getPrice());
        mRecyclerSale.setAdapter(
                new SaleAdapter(itemPhoneProduct.getListSale()));
        mRecyclerSale.setNestedScrollingEnabled(false);
        mRecyclerExtraProduct.setAdapter(
                new ExtraProductAdapter(itemPhoneProduct.getListExtraProduct()));
        mRecyclerExtraProduct.setNestedScrollingEnabled(false);

        //InFo
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerInfoProduct.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerInfoProduct.addItemDecoration(dividerItemDecoration);
        for (int i = 0; i < 3; i++) {
            mInfoProducts.add(itemPhoneProduct.getListParameter().get(i));
        }
        mRecyclerInfoProduct.setAdapter(
                new InfoAdapter(mInfoProducts));
        mRecyclerInfoProduct.setNestedScrollingEnabled(false);

        //Content
        mTextTitleContent.setText(itemPhoneProduct.getTitleContent());
        mTextH2.setText(itemPhoneProduct.getTitleH2());
        for (int i = 0; i < 2; i++) {
            mContentListHide.add(itemPhoneProduct.getDetailContent().get(i));
        }

        mRecyclerDes.setAdapter(
                new ContentAdapter(mContentListHide));
        mRecyclerDes.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_all_content:
                Intent intent = new Intent(this, ContentDetailActivity.class);
                intent.putExtra(ContentDetailActivity.BUNDLE_TITLE_CONTENT, itemPhoneProduct.getTitleContent());
                intent.putExtra(ContentDetailActivity.BUNDLE_H2_CONTENT, itemPhoneProduct.getTitleH2());
                intent.putParcelableArrayListExtra(ContentDetailActivity.BUNDLE_DETAIL_CONTENT,
                        (ArrayList<? extends Parcelable>) itemPhoneProduct.getDetailContent());
                startActivity(intent);
                break;
            case R.id.text_see_detail:
                Intent intentInfo = new Intent(this, InfoDetailActivity.class);
                intentInfo.putParcelableArrayListExtra(InfoDetailActivity.BUNDLE_INFO,
                        (ArrayList<? extends Parcelable>) itemPhoneProduct.getListParameter());
                startActivity(intentInfo);
                break;
            case R.id.relative_comment:
                mImageShopping.setVisibility(View.GONE);
                FragmentTransactionUtils.addFragment(
                        getSupportFragmentManager(),
                        CommentFragment.newInstance(itemPhoneProduct),
                        R.id.frame_full,
                        CommentFragment.TAG,
                        true,
                        -1, -1
                );

                break;
        }
    }
}
